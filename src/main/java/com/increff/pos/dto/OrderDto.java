package com.increff.pos.dto;

import com.google.common.io.ByteStreams;
import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;

    public OrderPojo add() {
        OrderPojo addedPojo = new OrderPojo();
        orderService.add(addedPojo);
        return addedPojo;
    }

    public List<OrderData> getAll() {
        List<OrderData> orderDataList = new ArrayList<>();
        Pair<List<OrderPojo>, Pair<List<List<OrderItemPojo>>, List<List<ProductPojo>>>> pairedPojos = orderService.getAll();
        int i = 0;
        for (OrderPojo orderPojo : pairedPojos.fst) {
            List<OrderItemPojo> orderItemPojoList = pairedPojos.snd.fst.get(i);
            List<ProductPojo> productPojoList = pairedPojos.snd.snd.get(i++);
            orderDataList.add(convertToData(orderPojo, orderItemPojoList, productPojoList));
        }
        return orderDataList;
    }

    public void delete(int id) throws ApiException {
        orderService.delete(id);
    }

    public void placeOrder(int id) throws ApiException {
        orderService.placeOrder(id);
    }

    public void generateInvoice(int orderId, HttpServletResponse response) throws ApiException {
        File document = orderService.getOrderInvoice(orderId);
        try {
            InputStream is = new FileInputStream(document);
            ByteStreams.copy(is, response.getOutputStream());
            response.setContentType("application/pdf");
            response.flushBuffer();
        } catch (IOException e) {
            throw new ApiException("Error while invoice generation");
        }
    }

    public OrderData convertToData(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList, List<ProductPojo> productPojoList) {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        if (orderPojo.getZonedDateTime() == null) {
            orderData.setDateTime("Not placed yet");
        } else {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm");
            orderData.setDateTime(orderPojo.getZonedDateTime().format(formatter));
        }
        orderData.setOrderItemDataList(OrderItemDtoHelper.convertPojoListToDataList(orderItemPojoList, productPojoList));
        return orderData;
    }

}
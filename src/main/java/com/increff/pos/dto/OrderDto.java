package com.increff.pos.dto;

import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;

    public OrderPojo add(OrderForm orderForm) throws ApiException {
        if (orderForm.getOrderItemIds().size() == 0) {
            throw new ApiException("There are no order items placed in this transaction ");
        }
        for (Integer orderItemId : orderForm.getOrderItemIds()) {
            if (orderItemId == null) {
                throw new ApiException("OrderItem id cannot be null ");
            }
        }
        OrderPojo addedPojo = new OrderPojo();
        addedPojo.setZonedDateTime(ZonedDateTime.now());
        return orderService.add(addedPojo, orderForm.getOrderItemIds());
    }

    public List<OrderData> getAll() {
        List<OrderData> orderDataList = new ArrayList<>();
        List<OrderPojo> orderPojoList = orderService.getAll();
        for (OrderPojo orderPojo : orderPojoList) {
            orderDataList.add(convertToData(orderPojo));
        }
        return orderDataList;
    }

    @Transactional(readOnly = true)
    public OrderData getById(int orderId) {
        return convertToData(orderService.getById(orderId));
    }

    public void generateInvoice(int orderId) throws ApiException {
        orderService.getOrderInvoice(orderId);
    }

    public OrderData convertToData(OrderPojo orderPojo) {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setZonedDateTime(orderPojo.getZonedDateTime());
        return orderData;
    }

}
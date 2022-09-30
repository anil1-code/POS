package com.increff.pos.dto;

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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;

    public OrderPojo add() throws ApiException {
//        if (orderForm.getOrderItemIds().size() == 0) {
//            throw new ApiException("There are no order items placed in this transaction ");
//        }
//        for (Integer orderItemId : orderForm.getOrderItemIds()) {
//            if (orderItemId == null) {
//                throw new ApiException("OrderItem id cannot be null ");
//            }
//            OrderItemPojo orderItemPojo = orderItemService.getByOrderItemId(orderItemId);
//            int updatedQuantity = inventoryService.getByProductId(orderItemPojo.getProductId()).getQuantity() - orderItemPojo.getQuantity();
//            if (updatedQuantity < 0) {
//                throw new ApiException("insufficient inventory"); // ask whether they need which order item(s) is giving this error
//            }
//            InventoryPojo inventoryPojo = new InventoryPojo();
//            inventoryPojo.setProductId(orderItemPojo.getProductId());
//            inventoryPojo.setQuantity(updatedQuantity);
//            inventoryService.update(orderItemPojo.getProductId(), inventoryPojo);
//        }
        OrderPojo addedPojo = new OrderPojo();
//        addedPojo.setZonedDateTime(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        orderService.add(addedPojo);
//        for (Integer id : orderForm.getOrderItemIds()) {
//            if (orderItemService.getByOrderItemId(id).getOrderId() != 0) {
//                throw new ApiException("Already placed order\n");
//            }
//            orderItemService.updateOrderId(id, addedPojo.getId());
//        }
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

    public void generateInvoice(int orderId) throws ApiException {
        orderService.getOrderInvoice(orderId);
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
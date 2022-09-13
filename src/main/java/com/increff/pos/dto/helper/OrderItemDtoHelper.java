package com.increff.pos.dto.helper;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;

import java.util.ArrayList;
import java.util.List;

public class OrderItemDtoHelper {
    public static List<OrderItemData> convertPojoListToDataList(List<OrderItemPojo> orderItemPojoList) {
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemDataList.add(convertPojoToData(orderItemPojo));
        }
        return orderItemDataList;
    }

    public static OrderItemData convertPojoToData(OrderItemPojo orderItemPojo) {
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setProductId(orderItemPojo.getProductId());
        return orderItemData;
    }

    public static OrderItemPojo convertFormToPojo(OrderItemForm orderItemForm) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderItemForm.getOrderId());
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setProductId(orderItemForm.getProductId());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        return orderItemPojo;
    }

    public static StringBuilder orderItemChecker(OrderItemForm orderItemForm) {
        StringBuilder errorMessageData = new StringBuilder();
        if (orderItemForm.getOrderId() == null) {
            errorMessageData.append("Missing OrderID. ");
        }
        if (orderItemForm.getProductId() == null) {
            errorMessageData.append("Missing ProductID. ");
        }
        if (orderItemForm.getQuantity() == null) {
            errorMessageData.append("Missing Quantity. ");
        } else if (orderItemForm.getQuantity() == 0) {
            errorMessageData.append("Quantity cannot be zero. ");
        }
        if (orderItemForm.getSellingPrice() == null) {
            errorMessageData.append("Missing Selling Price. ");
        } else if (orderItemForm.getSellingPrice() == 0) {
            errorMessageData.append("Price cannot be zero. ");
        }
        return errorMessageData;
    }
}

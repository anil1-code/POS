package com.increff.pos.dto.helper;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.BasicDataUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderItemDtoHelper {
    public static List<OrderItemData> convertPojoListToDataList(List<OrderItemPojo> orderItemPojoList, List<ProductPojo> productPojoList) {
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        int i = 0;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemDataList.add(convertPojoToData(orderItemPojo, productPojoList.get(i++)));
        }
        return orderItemDataList;
    }

    public static OrderItemData convertPojoToData(OrderItemPojo orderItemPojo, ProductPojo productPojo) {
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setOrderItemId(orderItemPojo.getId());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setProductId(orderItemPojo.getProductId());
        orderItemData.setProductName(productPojo.getName());
        orderItemData.setBarcode(productPojo.getBarcode());
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

    public static String validate(OrderItemForm orderItemForm) {
        StringBuilder errorMsg = new StringBuilder();
        if (orderItemForm.getQuantity() <= 0) {
            errorMsg.append("Quantity should not be less than 1, ");
        }
        orderItemForm.setSellingPrice(BasicDataUtil.roundOffDouble(orderItemForm.getSellingPrice()));
        if (orderItemForm.getSellingPrice() < 0) {
            errorMsg.append("Price should not be negative, ");
        }
        if (orderItemForm.getProductId() <= 0) {
            errorMsg.append("Product Id should not be negative, ");
        }
        if (orderItemForm.getOrderId() <= 0) {
            errorMsg.append("Order Id should not be negative, ");
        }
        if (errorMsg.length() != 0) {
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            errorMsg.append(".\n");
        }
        return errorMsg.toString();
    }
}

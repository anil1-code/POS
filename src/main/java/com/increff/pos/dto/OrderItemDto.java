package com.increff.pos.dto;

import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;

    public List<OrderItemData> getAll() {
        return OrderItemDtoHelper.convertPojoListToDataList(orderItemService.getAll());
    }

    public OrderItemPojo add(OrderItemForm orderItemForm) throws ApiException {
        StringBuilder errorMessageData = OrderItemDtoHelper.orderItemChecker(orderItemForm);
        if (errorMessageData.length() > 0) {
            errorMessageData.append("\n");
            throw new ApiException(errorMessageData.toString());
        }
        return orderItemService.add(OrderItemDtoHelper.convertFormToPojo(orderItemForm));
    }

    public OrderItemPojo update(int id, OrderItemForm orderItemForm) throws ApiException {
        StringBuilder errorMessageData = OrderItemDtoHelper.orderItemChecker(orderItemForm);
        if (errorMessageData.length() > 0) {
            errorMessageData.append("\n");
            throw new ApiException(errorMessageData.toString());
        }
        return orderItemService.update(id, OrderItemDtoHelper.convertFormToPojo(orderItemForm));
    }

    @Transactional(readOnly = true)
    public List<OrderItemData> getByOrderId(int orderId) {
        return orderItemService.getByOrderId(orderId);
    }

    public void delete(int id) {
        orderItemService.delete(id);
    }

    public OrderItemData getByOrderItemId(int orderItemId) {
        return OrderItemDtoHelper.convertPojoToData(orderItemService.getByOrderItemId(orderItemId));
    }
}

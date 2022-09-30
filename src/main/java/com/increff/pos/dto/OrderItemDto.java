package com.increff.pos.dto;

import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;

    public List<OrderItemData> getAll() {
        Pair<List<OrderItemPojo>, List<ProductPojo>> pairedPojoList = orderItemService.getAll();
        return OrderItemDtoHelper.convertPojoListToDataList(pairedPojoList.fst, pairedPojoList.snd);
    }

    public OrderItemPojo add(OrderItemForm orderItemForm) throws ApiException {
        String error = OrderItemDtoHelper.validate(orderItemForm);
        if (!error.isEmpty()) {
            throw new ApiException(error);
        }
        return orderItemService.add(OrderItemDtoHelper.convertFormToPojo(orderItemForm));
    }

    public OrderItemPojo update(int id, OrderItemForm orderItemForm) throws ApiException {
        String error = OrderItemDtoHelper.validate(orderItemForm);
        if (!error.isEmpty()) {
            throw new ApiException(error);
        }
        return orderItemService.update(id, OrderItemDtoHelper.convertFormToPojo(orderItemForm));
    }

    public List<OrderItemData> getByOrderId(int orderId) throws ApiException {
        Pair<List<OrderItemPojo>, List<ProductPojo>> pairedPojoList = orderItemService.getByOrderId(orderId);
        return OrderItemDtoHelper.convertPojoListToDataList(pairedPojoList.fst, pairedPojoList.snd);
    }

    public void delete(int id) {
        orderItemService.delete(id);
    }

    public OrderItemData getByOrderItemId(int orderItemId) {
        Pair<OrderItemPojo, ProductPojo> pairedPojos = orderItemService.getByOrderItemId(orderItemId);
        return OrderItemDtoHelper.convertPojoToData(pairedPojos.fst, pairedPojos.snd);
    }

    public void deleteByOrderId(int orderId) throws ApiException {
        orderItemService.deleteByOrderId(orderId);
    }
}

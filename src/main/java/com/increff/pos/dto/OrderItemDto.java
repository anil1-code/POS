package com.increff.pos.dto;

import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public List<OrderItemData> getAll() {
        List<ProductPojo> productPojoList = new ArrayList<>();
        List<OrderItemPojo> orderItemPojoList = orderItemService.getAll();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            productPojoList.add(productService.getById(orderItemPojo.getProductId()));
        }
        return OrderItemDtoHelper.convertPojoListToDataList(orderItemPojoList, productPojoList);
    }

    public OrderItemPojo add(OrderItemForm orderItemForm) throws ApiException {
        StringBuilder errorMessageData = OrderItemDtoHelper.orderItemChecker(orderItemForm);
        if (errorMessageData.length() > 0) {
            errorMessageData.append("\n");
            throw new ApiException(errorMessageData.toString());
        }
        if (productService.getById(orderItemForm.getProductId()) == null) {
            // product doesn't exists
            throw new ApiException("This product doesn't exist.\n");
        }
        InventoryPojo inventoryPojo = inventoryService.getByProductId(orderItemForm.getProductId());
        if (inventoryPojo == null || inventoryPojo.getQuantity() < orderItemForm.getQuantity()) {
            // inventory isn't sufficient
            throw new ApiException("Inventory is insufficient.\n");
        }
        return orderItemService.add(OrderItemDtoHelper.convertFormToPojo(orderItemForm));
    }

    public OrderItemPojo update(int id, OrderItemForm orderItemForm) throws ApiException {
        StringBuilder errorMessageData = OrderItemDtoHelper.orderItemChecker(orderItemForm);
        if (errorMessageData.length() > 0) {
            errorMessageData.append("\n");
            throw new ApiException(errorMessageData.toString());
        }
        if (productService.getById(orderItemForm.getProductId()) == null) {
            throw new ApiException("This product doesn't exist.\n");
        }
        InventoryPojo inventoryPojo = inventoryService.getByProductId(orderItemForm.getProductId());
        if (inventoryPojo == null || inventoryPojo.getQuantity() < orderItemForm.getQuantity()) {
            throw new ApiException("Inventory is insufficient.\n");
        }
        return orderItemService.update(id, OrderItemDtoHelper.convertFormToPojo(orderItemForm));
    }

    @Transactional(readOnly = true)
    public List<OrderItemData> getByOrderId(int orderId) {
        List<ProductPojo> productPojoList = new ArrayList<>();
        List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            productPojoList.add(productService.getById(orderItemPojo.getProductId()));
        }
        return OrderItemDtoHelper.convertPojoListToDataList(orderItemPojoList, productPojoList);
    }

    public void delete(int id) {
        orderItemService.delete(id);
    }

    public OrderItemData getByOrderItemId(int orderItemId) {
        OrderItemPojo orderItemPojo = orderItemService.getByOrderItemId(orderItemId);
        return OrderItemDtoHelper.convertPojoToData(orderItemPojo, productService.getById(orderItemPojo.getProductId()));
    }
}

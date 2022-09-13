package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(readOnly = true)
    public List<OrderItemPojo> getAll() {
        return orderItemDao.getAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderItemPojo add(OrderItemPojo orderItemPojo) throws ApiException {
        if (productService.getById(orderItemPojo.getProductId()) == null) {
            // product doesn't exists
            throw new ApiException("This product doesn't exist.\n");
        }
        if (inventoryService.getByProductId(orderItemPojo.getProductId()).getQuantity() < orderItemPojo.getQuantity()) {
            // inventory isn't sufficient
            throw new ApiException("Inventory is insufficient.\n");
        }
        return orderItemDao.add(orderItemPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderItemPojo update(int id, OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemPojo existingPojo = orderItemDao.getByOrderItemId(id);
        if (productService.getById(orderItemPojo.getProductId()) == null) {
            throw new ApiException("This product doesn't exist.\n");
        }
        if (inventoryService.getByProductId(orderItemPojo.getProductId()).getQuantity() < orderItemPojo.getQuantity()) {
            throw new ApiException("Inventory is insufficient.\n");
        }
        existingPojo.setOrderId(orderItemPojo.getOrderId());
        existingPojo.setQuantity(orderItemPojo.getQuantity());
        existingPojo.setProductId(orderItemPojo.getProductId());
        existingPojo.setSellingPrice(orderItemPojo.getSellingPrice());
        return orderItemDao.update(existingPojo);
    }

    public List<OrderItemData> getByOrderId(int orderId) {
        return OrderItemDtoHelper.convertPojoListToDataList(orderItemDao.getByOrderId(orderId));
    }

    @Transactional(readOnly = true)
    public OrderItemPojo getByOrderItemId(int orderItemId) {
        return orderItemDao.getByOrderItemId(orderItemId);
    }

    public OrderItemPojo updateOrderId(int id, int orderId)  throws ApiException {
        OrderItemPojo existingPojo = orderItemDao.getByOrderItemId(id);
        existingPojo.setOrderId(orderId);
        return existingPojo;
    }

    @Transactional(rollbackFor = ApiException.class)
    public void delete(int id) {
        orderItemDao.delete(id);
    }
}

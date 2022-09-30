package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    @Transactional(readOnly = true)
    public Pair<List<OrderItemPojo>, List<ProductPojo>> getAll() {
        List<OrderItemPojo> orderItemPojoList = orderItemDao.getAll();
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            // assuming the corresponding product will always exist
            productPojoList.add(productService.getById(orderItemPojo.getProductId()));
        }
        return new Pair<>(orderItemPojoList, productPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderItemPojo add(OrderItemPojo orderItemPojo) throws ApiException {
        if (productService.getById(orderItemPojo.getProductId()) == null) {
            // product doesn't exist
            throw new ApiException("Product doesn't exist.\n");
        }
        InventoryPojo inventoryPojo = inventoryService.getByProductId(orderItemPojo.getProductId());
        if (inventoryPojo == null || inventoryPojo.getQuantity() < orderItemPojo.getQuantity()) {
            // inventory isn't sufficient
            throw new ApiException("Inventory is insufficient.\n");
        }
        if (orderItemDao.getByOrderIdAndProductId(orderItemPojo.getOrderId(), orderItemPojo.getProductId()) != null) {
            throw new ApiException("Product already exist in this order.\n");
        }
        return orderItemDao.add(orderItemPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderItemPojo update(int id, OrderItemPojo orderItemPojo) throws ApiException {
        if (productService.getById(orderItemPojo.getProductId()) == null) {
            // product doesn't exist
            throw new ApiException("Product doesn't exist.\n");
        }
        InventoryPojo inventoryPojo = inventoryService.getByProductId(orderItemPojo.getProductId());
        if (inventoryPojo == null || inventoryPojo.getQuantity() < orderItemPojo.getQuantity()) {
            // inventory isn't sufficient
            throw new ApiException("Inventory is insufficient.\n");
        }
        OrderItemPojo existingPojo = orderItemDao.getByOrderItemId(id);
        existingPojo.setQuantity(orderItemPojo.getQuantity());
        existingPojo.setSellingPrice(orderItemPojo.getSellingPrice());
        return orderItemDao.update(existingPojo);
    }

    @Transactional(readOnly = true)
    public Pair<List<OrderItemPojo>, List<ProductPojo>> getByOrderId(int orderId) throws ApiException {
        OrderPojo orderPojo = orderService.getById(orderId);
        if(orderPojo == null) {
            throw new ApiException("Order doesn't exist");
        }
        List<OrderItemPojo> orderItemPojoList = orderItemDao.getByOrderId(orderId);
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            // assuming the corresponding product will always exist
            productPojoList.add(productService.getById(orderItemPojo.getProductId()));
        }
        return new Pair<>(orderItemPojoList, productPojoList);
    }

    @Transactional(readOnly = true)
    public Pair<OrderItemPojo, ProductPojo> getByOrderItemId(int orderItemId) {
        OrderItemPojo orderItemPojo = orderItemDao.getByOrderItemId(orderItemId);
        // assuming the corresponding product will always exist
        ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
        return new Pair<>(orderItemPojo, productPojo);
    }

//    @Transactional(rollbackFor = ApiException.class)
//    public OrderItemPojo updateOrderId(int id, int orderId) throws ApiException {
//        OrderItemPojo existingPojo = orderItemDao.getByOrderItemId(id);
//        existingPojo.setOrderId(orderId);
//        return existingPojo;
//    }

    @Transactional(rollbackFor = ApiException.class)
    public void delete(int id) {
        orderItemDao.delete(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void deleteByOrderId(int orderId) throws ApiException {
        if (orderService.getById(orderId).getZonedDateTime() == null) {
            orderItemDao.deleteByOrderId(orderId);
        } else {
            throw new ApiException("Order already placed, order item can't be deleted now.\n");
        }
    }

}

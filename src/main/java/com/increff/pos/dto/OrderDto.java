package com.increff.pos.dto;

import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.OrderForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo add(OrderForm orderForm) throws ApiException {
        if (orderForm.getOrderItemIds().size() == 0) {
            throw new ApiException("There are no order items placed in this transaction ");
        }
        for (Integer orderItemId : orderForm.getOrderItemIds()) {
            if (orderItemId == null) {
                throw new ApiException("OrderItem id cannot be null ");
            }
            OrderItemPojo orderItemPojo = orderItemService.getByOrderItemId(orderItemId);
            int updatedQuantity = inventoryService.getByProductId(orderItemPojo.getProductId()).getQuantity() - orderItemPojo.getQuantity();
            if (updatedQuantity < 0) {
                throw new ApiException("insufficient inventory"); // ask whether they need which order item(s) is giving this error
            }
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setProductId(orderItemPojo.getProductId());
            inventoryPojo.setQuantity(updatedQuantity);
            inventoryService.update(orderItemPojo.getProductId(), inventoryPojo);
        }
        OrderPojo addedPojo = new OrderPojo();
        addedPojo.setZonedDateTime(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        System.out.println(addedPojo.getZonedDateTime().toString());
        orderService.add(addedPojo);
        for (Integer id : orderForm.getOrderItemIds()) {
            if (orderItemService.getByOrderItemId(id).getOrderId() != 0) {
                throw new ApiException("Already placed order\n");
            }
            orderItemService.updateOrderId(id, addedPojo.getId());
        }
        System.out.println(addedPojo.getId());
        return addedPojo;
    }

    public List<OrderData> getAll() {
        List<OrderData> orderDataList = new ArrayList<>();
        List<OrderPojo> orderPojoList = orderService.getAll();
        System.out.println("order dto " + orderPojoList.size());
        for (OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderPojo.getId());
            List<ProductPojo> productPojoList = new ArrayList<>();
            for(OrderItemPojo orderItemPojo : orderItemPojoList) {
                productPojoList.add(productService.getById(orderItemPojo.getProductId()));
            }
            orderDataList.add(convertToData(orderPojo, orderItemPojoList, productPojoList));
        }
        System.out.println("order dto " + orderDataList.size());
        return orderDataList;
    }

    @Transactional(readOnly = true)
    public OrderPojo getById(int orderId) {
        return orderService.getById(orderId);
    }

    public void generateInvoice(int orderId) throws ApiException {
        ZonedDateTime time = getById(orderId).getZonedDateTime();
        double total = 0;
        List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            total += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
            productPojoList.add(productService.getById(orderItemPojo.getProductId()));
        }
        InvoiceData invoiceData = new InvoiceData(OrderItemDtoHelper.convertPojoListToDataList(orderItemPojoList, productPojoList), time, total, orderId);
        orderService.getOrderInvoice(orderId, invoiceData);
    }

    public OrderData convertToData(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList, List<ProductPojo> productPojoList) {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm");
        orderData.setDateTime(orderPojo.getZonedDateTime().format(formatter));
        orderData.setOrderItemDataList(OrderItemDtoHelper.convertPojoListToDataList(orderItemPojoList, productPojoList));
        return orderData;
    }

}
package com.increff.pos.controller;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/order-items")
public class OrderItemController {
    @Autowired
    private OrderItemDto orderItemDto;

    @ApiOperation(value = "get all order items (placed or not)")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrderItemData> getAll() {
        return orderItemDto.getAll();
    }

    @ApiOperation(value = "add an order item")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public OrderItemPojo add(@RequestBody OrderItemForm orderItemForm) throws ApiException {
        return orderItemDto.add(orderItemForm);
    }

    @ApiOperation(value = "update an order item by its id")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public OrderItemPojo update(@PathVariable int id, @RequestBody OrderItemForm orderItemForm) throws ApiException {
        return orderItemDto.update(id, orderItemForm);
    }

    @ApiOperation(value = "delete an order item by its id")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        System.out.println(id);orderItemDto.delete(id);
    }


    @ApiOperation(value = "delete a brand category pair by id")
    @RequestMapping(value = "/delete/order/{orderId}", method = RequestMethod.DELETE)
    public void deleteByOrderId(@PathVariable int orderId) {
        orderItemDto.delete(orderId);
    }

    @ApiOperation(value = "get a list of order items corresponding to the order id")
    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getByOrderId(@PathVariable int orderId) {
        return orderItemDto.getByOrderId(orderId);
    }

    @ApiOperation(value = "get an order item by its id")
    @RequestMapping(value = "/{orderItemId}", method = RequestMethod.GET)
    public OrderItemData getByOrderItemId(@PathVariable int orderItemId) {
        return orderItemDto.getByOrderItemId(orderItemId);
    }
}
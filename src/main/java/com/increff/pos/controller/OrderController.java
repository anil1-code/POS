package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "add/place the order")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public OrderPojo add(@RequestBody OrderForm orderForm) throws ApiException {
        return orderDto.add(orderForm);
    }

    @ApiOperation(value = "get all existing orders")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        List<OrderData> lst = orderDto.getAll();
        System.out.println(lst.size() + "sdfs");
        return lst;
    }

    @ApiOperation(value = "generate the invoice")
    @RequestMapping(value = "/invoice/{orderId}", method = RequestMethod.GET)
    public void generateInvoice(@PathVariable int orderId) throws ApiException {
        orderDto.generateInvoice(orderId);
    }
}
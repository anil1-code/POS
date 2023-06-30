package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.pojo.OrderPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "add/place the order")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public OrderPojo add() throws ApiException {
        return orderDto.add();
    }

    @ApiOperation(value = "get all existing orders")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        return orderDto.getAll();
    }

    @ApiOperation(value = "delete an existing order")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        orderDto.delete(id);
    }

    @ApiOperation(value = "place an unplaced order")
    @RequestMapping(value = "/place/{id}", method = RequestMethod.PUT)
    public void placeOrder(@PathVariable int id) throws ApiException {
        orderDto.placeOrder(id);
    }

    @ApiOperation(value = "generate the invoice")
    @RequestMapping(value = "/invoice/{orderId}", method = RequestMethod.GET)
    public void generateInvoice(@PathVariable int orderId, HttpServletResponse response) throws ApiException {
        orderDto.generateInvoice(orderId, response);
    }
}
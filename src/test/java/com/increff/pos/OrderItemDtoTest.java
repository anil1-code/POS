package com.increff.pos.dto;

import com.increff.pos.service.OrderItemService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class OrderItemDtoTest {
    @Autowired
    private OrderItemService orderItemService;

    @Test
    public void testAddInvalid() {
        OrderItemService orderItemService = new OrderItemService();
    }


}

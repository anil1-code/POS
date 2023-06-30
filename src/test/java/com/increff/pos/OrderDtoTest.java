package com.increff.pos;

import com.increff.pos.dto.*;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Transactional
public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto orderDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private BrandDto brandDto;

    @Test
    public void testAdd() {
        orderDto.add();
    }

    @Test
    public void testGetAll() {
        OrderPojo orderPojo = orderDto.add();
        try {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrandName("b");
            brandForm.setCategoryName("c");
            BrandPojo brandPojo = brandDto.add(Collections.singletonList(brandForm)).get(0);
            ProductForm productForm = new ProductForm();
            productForm.setBrandCategory(brandPojo.getId());
            productForm.setName("p");
            productForm.setBarcode("b");
            productForm.setMrp(100d);
            ProductPojo productPojo = productDto.add(Collections.singletonList(productForm)).get(0);
            InventoryForm inventoryForm = new InventoryForm();
            inventoryForm.setProductId(productPojo.getId());
            inventoryForm.setQuantity(10);
            inventoryDto.add(Collections.singletonList(inventoryForm));
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setQuantity(5);
            orderItemForm.setSellingPrice(100d);
            orderItemForm.setProductId(productPojo.getId());
            orderItemForm.setOrderId(orderPojo.getId());
            orderItemDto.add(orderItemForm);
        } catch (ApiException e) {
            fail();
        }
        orderDto.getAll();
        try {
            orderDto.placeOrder(orderPojo.getId());
        } catch (ApiException e) {
            fail();
        }
        orderDto.getAll();
    }

    @Test(expected = ApiException.class)
    public void testDelete() throws ApiException {
        OrderPojo orderPojo = orderDto.add();
        try {
            orderDto.delete(orderPojo.getId());
        } catch (ApiException e) {
            fail();
        }
        orderPojo = orderDto.add();
        BrandForm brandForm = new BrandForm();
        brandForm.setBrandName("b");
        brandForm.setCategoryName("c");
        BrandPojo brandPojo = brandDto.add(Collections.singletonList(brandForm)).get(0);
        ProductForm productForm = new ProductForm();
        productForm.setBrandCategory(brandPojo.getId());
        productForm.setName("p");
        productForm.setBarcode("b");
        productForm.setMrp(100d);
        ProductPojo productPojo = productDto.add(Collections.singletonList(productForm)).get(0);
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setProductId(productPojo.getId());
        inventoryForm.setQuantity(10);
        inventoryDto.add(Collections.singletonList(inventoryForm));
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(5);
        orderItemForm.setSellingPrice(100d);
        orderItemForm.setProductId(productPojo.getId());
        orderItemForm.setOrderId(orderPojo.getId());
        orderItemDto.add(orderItemForm);
        try {
            orderDto.placeOrder(orderPojo.getId());
        } catch (ApiException e) {
            fail();
        }
        try {
            orderDto.delete(orderPojo.getId());
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order already placed and can't be deleted.\n");
            throw e;
        }
    }

    @Test
    public void testGenerateInvoice() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        OrderPojo orderPojo = orderDto.add();
        int cnt = 0;
        try {
            orderDto.generateInvoice(21, response);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order doesn't exist.\n");
            cnt++;
        }
        try {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrandName("b");
            brandForm.setCategoryName("c");
            BrandPojo brandPojo = brandDto.add(Collections.singletonList(brandForm)).get(0);
            ProductForm productForm = new ProductForm();
            productForm.setBrandCategory(brandPojo.getId());
            productForm.setName("p");
            productForm.setBarcode("b");
            productForm.setMrp(100d);
            ProductPojo productPojo = productDto.add(Collections.singletonList(productForm)).get(0);
            InventoryForm inventoryForm = new InventoryForm();
            inventoryForm.setProductId(productPojo.getId());
            inventoryForm.setQuantity(10);
            inventoryDto.add(Collections.singletonList(inventoryForm));
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setQuantity(5);
            orderItemForm.setSellingPrice(100d);
            orderItemForm.setProductId(productPojo.getId());
            orderItemForm.setOrderId(orderPojo.getId());
            orderItemDto.add(orderItemForm);
        } catch (ApiException e) {
            fail();
        }
        try {
            orderDto.generateInvoice(orderPojo.getId(), response);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order isn't placed yet.\n");
            cnt++;
        }
        try {
            orderDto.placeOrder(orderPojo.getId());
        } catch (ApiException e) {
            fail();
        }
        try {
            orderDto.generateInvoice(orderPojo.getId(), response);
        } catch (ApiException e) {
            fail();
        }
        if (cnt != 2) {
            fail();
        }
    }

    @Test
    public void testPlaceOrder() {
        int cnt = 0;
        try {
            orderDto.placeOrder(1);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "No order exists for this ID: 1");
            cnt++;
        }
        OrderPojo orderPojo = orderDto.add();
        InventoryPojo inventoryPojo = null;
        InventoryForm inventoryForm = null;
        try {
            orderDto.placeOrder(orderPojo.getId());
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Empty order can't be placed");
            cnt++;
        }
        try {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrandName("b");
            brandForm.setCategoryName("c");
            BrandPojo brandPojo = brandDto.add(Collections.singletonList(brandForm)).get(0);
            ProductForm productForm = new ProductForm();
            productForm.setBrandCategory(brandPojo.getId());
            productForm.setName("p");
            productForm.setBarcode("b");
            productForm.setMrp(100d);
            ProductPojo productPojo = productDto.add(Collections.singletonList(productForm)).get(0);
            inventoryForm = new InventoryForm();
            inventoryForm.setProductId(productPojo.getId());
            inventoryForm.setQuantity(10);
            inventoryPojo = inventoryDto.add(Collections.singletonList(inventoryForm)).get(0);
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setQuantity(5);
            orderItemForm.setSellingPrice(100d);
            orderItemForm.setProductId(productPojo.getId());
            orderItemForm.setOrderId(orderPojo.getId());
            orderItemDto.add(orderItemForm);
            inventoryForm.setQuantity(3);
            inventoryDto.update(inventoryPojo.getProductId(), inventoryForm);
        } catch (ApiException e) {
            fail();
        }
        try {
            orderDto.placeOrder(orderPojo.getId());
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Insufficient Inventory for p.\n");
            cnt++;
        }
        inventoryForm.setQuantity(15);
        try {
            inventoryDto.update(inventoryPojo.getProductId(), inventoryForm);
            orderDto.placeOrder(orderPojo.getId());
        } catch (ApiException e) {
            fail();
        }

        try {
            orderDto.placeOrder(orderPojo.getId());
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order with given ID is already placed");
            cnt++;
        }
        if (cnt != 4) {
            fail();
        }
    }


}

package com.increff.pos;

import com.increff.pos.dto.*;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Transactional
public class OrderItemDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private OrderDto orderDto;

    @Test(expected = ApiException.class)
    public void testAddInvalidDto() throws ApiException {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(0);
        orderItemForm.setSellingPrice(-1d);
        orderItemForm.setProductId(0);
        orderItemForm.setOrderId(0);
        try {
            orderItemDto.add(orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Quantity should not be less than 1, Price should not be negative, Product Id should not be negative, Order Id should not be negative.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddInvalidProduct() throws ApiException {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(5);
        orderItemForm.setSellingPrice(100d);
        orderItemForm.setProductId(1);
        orderItemForm.setOrderId(1);
        try {
            orderItemDto.add(orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Product doesn't exist.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddInsufficientInventory() throws ApiException {
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
        orderItemForm.setQuantity(15);
        orderItemForm.setSellingPrice(100d);
        orderItemForm.setProductId(productPojo.getId());
        orderItemForm.setOrderId(1);
        try {
            orderItemDto.add(orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Inventory is insufficient.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddInvalidOrder() throws ApiException {
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
        orderItemForm.setOrderId(1);
        try {
            orderItemDto.add(orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order doesn't exist.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddDuplicateProduct() throws ApiException {
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
        OrderPojo orderPojo = orderDto.add();
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(5);
        orderItemForm.setSellingPrice(100d);
        orderItemForm.setProductId(productPojo.getId());
        orderItemForm.setOrderId(orderPojo.getId());
        try {
            orderItemDto.add(orderItemForm);
        } catch (ApiException e) {
            fail();
        }
        try {
            orderItemDto.add(orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Product already exist in this order.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalidDto() throws ApiException {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(0);
        orderItemForm.setSellingPrice(-1d);
        orderItemForm.setProductId(0);
        orderItemForm.setOrderId(0);
        try {
            orderItemDto.update(1, orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Quantity should not be less than 1, Price should not be negative, Product Id should not be negative, Order Id should not be negative.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalidProduct() throws ApiException {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(5);
        orderItemForm.setSellingPrice(100d);
        orderItemForm.setProductId(1);
        orderItemForm.setOrderId(1);
        try {
            orderItemDto.update(1, orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Product doesn't exist.\n");
            throw e;
        }
    }


    @Test(expected = ApiException.class)
    public void testUpdateInsufficientInventory() throws ApiException {
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
        orderItemForm.setQuantity(15);
        orderItemForm.setSellingPrice(100d);
        orderItemForm.setProductId(productPojo.getId());
        orderItemForm.setOrderId(1);
        try {
            orderItemDto.update(1, orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Inventory is insufficient.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalidOrder() throws ApiException {
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
        orderItemForm.setOrderId(1);
        try {
            orderItemDto.update(1, orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order doesn't exist.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalidOrderItem() throws ApiException {
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
        OrderPojo orderPojo = orderDto.add();
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(5);
        orderItemForm.setSellingPrice(100d);
        orderItemForm.setProductId(productPojo.getId());
        orderItemForm.setOrderId(orderPojo.getId());
        try {
            orderItemDto.update(1, orderItemForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order Item doesn't exist.\n");
            throw e;
        }
    }

    @Test
    public void testUpdateValid() {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrandName("b");
        brandForm.setCategoryName("c");
        BrandPojo brandPojo = null;
        try {
            brandPojo = brandDto.add(Collections.singletonList(brandForm)).get(0);
        } catch (ApiException e) {
            fail();
        }
        ProductForm productForm = new ProductForm();
        productForm.setBrandCategory(brandPojo.getId());
        productForm.setName("p");
        productForm.setBarcode("b");
        productForm.setMrp(100d);
        ProductPojo productPojo = null;
        try {
            productPojo = productDto.add(Collections.singletonList(productForm)).get(0);
        } catch (ApiException e) {
            fail();
        }
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setProductId(productPojo.getId());
        inventoryForm.setQuantity(10);
        OrderPojo orderPojo = null;
        try {
            inventoryDto.add(Collections.singletonList(inventoryForm));
            orderPojo = orderDto.add();
        } catch (ApiException e) {
            fail();
        }
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(5);
        orderItemForm.setSellingPrice(100d);
        orderItemForm.setProductId(productPojo.getId());
        orderItemForm.setOrderId(orderPojo.getId());
        OrderItemPojo orderItemPojo = null;
        try {
            orderItemPojo = orderItemDto.add(orderItemForm);
        } catch (ApiException e) {
            fail();
        }
        try {
            orderItemDto.update(orderItemPojo.getId(), orderItemForm);
        } catch (ApiException e) {
            fail();
        }
    }

    @Test(expected = ApiException.class)
    public void testGetByOrderIdInvalid() throws ApiException {
        try {
            orderItemDto.getByOrderId(1);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order doesn't exist");
            throw e;
        }
    }

    @Test
    public void testGetByOrderIdValid() {
        try {
            OrderPojo orderPojo = orderDto.add();
            orderItemDto.getByOrderId(orderPojo.getId());
        } catch (ApiException e) {
            fail();
        }
    }

    @Test
    public void testGetByOrderItemIdValid() {
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
            OrderPojo orderPojo = orderDto.add();
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setQuantity(5);
            orderItemForm.setSellingPrice(100d);
            orderItemForm.setProductId(productPojo.getId());
            orderItemForm.setOrderId(orderPojo.getId());
            OrderItemPojo orderItemPojo = orderItemDto.add(orderItemForm);
            orderItemDto.getByOrderItemId(orderItemPojo.getId());
        } catch (ApiException e) {
            fail();
        }
    }

    @Test
    public void testDeleteByOrderId() {
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
            OrderPojo orderPojo = orderDto.add();
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setQuantity(5);
            orderItemForm.setSellingPrice(100d);
            orderItemForm.setProductId(productPojo.getId());
            orderItemForm.setOrderId(orderPojo.getId());
            orderItemDto.add(orderItemForm);
            orderItemDto.deleteByOrderId(orderPojo.getId());
        } catch (ApiException e) {
            fail();
        }
    }

    @Test(expected = ApiException.class)
    public void testDelete() throws ApiException {
        OrderPojo orderPojo = null;
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
            orderPojo = orderDto.add();
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setQuantity(5);
            orderItemForm.setSellingPrice(100d);
            orderItemForm.setProductId(productPojo.getId());
            orderItemForm.setOrderId(orderPojo.getId());
            OrderItemPojo orderItemPojo = orderItemDto.add(orderItemForm);
            orderItemDto.delete(orderItemPojo.getId());
            orderItemDto.add(orderItemForm);
            orderDto.placeOrder(orderPojo.getId());
        } catch (ApiException e) {
            fail();
        }
        try {
            orderItemDto.deleteByOrderId(orderPojo.getId());
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Order already placed, order item can't be deleted now.\n");
            throw e;
        }
    }

    @Test
    public void testGetAll() {
        OrderPojo orderPojo = null;
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
            orderPojo = orderDto.add();
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setQuantity(5);
            orderItemForm.setSellingPrice(100d);
            orderItemForm.setProductId(productPojo.getId());
            orderItemForm.setOrderId(orderPojo.getId());
            orderItemDto.add(orderItemForm);
            orderItemDto.getByOrderId(orderPojo.getId());
            orderItemDto.getAll();
        } catch (ApiException e) {
            fail();
        }
    }
}

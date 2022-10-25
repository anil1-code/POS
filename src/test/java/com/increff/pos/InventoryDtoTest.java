package com.increff.pos;

import com.increff.pos.constants.Const;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Transactional
public class InventoryDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BrandDto brandDto;

    @Test(expected = ApiException.class)
    public void testAddMaxRows() throws ApiException {
        List<InventoryForm> rows = Arrays.asList(new InventoryForm[Const.MAX_ROWS + 1]);
        try {
            inventoryDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Number of rows exceeds the max limit.");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddDtoNull() throws ApiException {
        List<InventoryForm> rows = new ArrayList<>();
        rows.add(null);
        try {
            inventoryDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Input form should not be null.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddDtoInvalidFields() throws ApiException {
        List<InventoryForm> rows = new ArrayList<>();
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setProductId(0);
        inventoryForm.setQuantity(-1);
        rows.add(inventoryForm);
        try {
            inventoryDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Invalid Product Id, Invalid Quantity.\n");
            throw e;
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo productPojo = null;
        try {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrandName("b");
            brandForm.setCategoryName("c");
            BrandPojo brandPojo = brandDto.add(List.of(brandForm)).get(0);
            ProductForm productForm = new ProductForm();
            productForm.setName("p");
            productForm.setBarcode("bc");
            productForm.setBrandCategory(brandPojo.getId());
            productForm.setMrp(1.0755);
            productPojo = productDto.add(List.of(productForm)).get(0);
        } catch (ApiException e) {
            fail();
        }
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setQuantity(1);
        inventoryForm.setProductId(productPojo.getId());
        InventoryPojo inventoryPojo = inventoryDto.add(List.of(inventoryForm)).get(0);
        InventoryData inventoryData = inventoryDto.getAll().get(0);
        assertEquals(inventoryData.getQuantity(), Integer.valueOf(1));
        inventoryForm.setQuantity(2);
        inventoryDto.update(inventoryPojo.getProductId(), inventoryForm);
        inventoryData = inventoryDto.getAll().get(0);
        assertEquals(inventoryData.getQuantity(), Integer.valueOf(2));
    }
}

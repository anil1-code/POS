package com.increff.pos;

import com.increff.pos.constants.Const;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BrandDto brandDto;

    @Test(expected = ApiException.class)
    public void testAddMaxRows() throws ApiException {
        List<ProductForm> rows = new ArrayList<>();
        for (int i = 0; i < Const.MAX_ROWS + 1; i++) {
            try {
                BrandForm brandForm = new BrandForm();
                brandForm.setBrandName("b" + i);
                brandForm.setCategoryName("c");
                BrandPojo brandPojo = brandDto.add(List.of(brandForm)).get(0);
                ProductForm productForm = new ProductForm();
                productForm.setName("p");
                productForm.setMrp(100.0d);
                productForm.setBarcode("ba" + i);
                productForm.setBrandCategory(brandPojo.getId());
                rows.add(productForm);
            } catch (ApiException e) {
                fail();
            }
        }
        try {
            productDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Number of rows exceeds the max limit.");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddDtoNull() throws ApiException {
        List<ProductForm> rows = new ArrayList<>();
        rows.add(null);
        try {
            productDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Input form should not be null.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddInvalidBC() throws ApiException {
        ProductForm productForm = new ProductForm();
        productForm.setBrandCategory(10);
        productForm.setMrp(1.0755);
        productForm.setName("p");
        productForm.setBarcode("bc");
        try {
            productDto.add(List.of(productForm));
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Brand Category ID does not exists.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testUpdateNullIDDup() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setCategoryName("c");
        brandForm.setBrandName("b");
        BrandPojo brandPojo = brandDto.add(List.of(brandForm)).get(0);
        ProductForm productForm = new ProductForm();
        productForm.setBrandCategory(10);
        productForm.setMrp(1.0755);
        productForm.setName("p");
        productForm.setBarcode("bc");
        int cnt = 0;
        try {
            productDto.update(10, productForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Brand Category ID does not exists.\n");
            cnt++;
        }
        productForm.setBrandCategory(brandPojo.getId());
        try {
            productDto.update(10, productForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "No product exists for this ID: 10");
            cnt++;
        }
        if (cnt == 2) {
            throw new ApiException("");
        }
    }


    @Test(expected = ApiException.class)
    public void testAddDtoEmptyNames() throws ApiException {
        ProductForm productForm = new ProductForm();
        try {
            List<ProductForm> rows = new ArrayList<>();
            BrandForm brandForm = new BrandForm();
            brandForm.setBrandName("b");
            brandForm.setCategoryName("c");
            BrandPojo brandPojo = brandDto.add(List.of(brandForm)).get(0);
            productForm.setBarcode("");
            productForm.setName("");
            productForm.setMrp(-0.10999);
            productForm.setBrandCategory(brandPojo.getId());
        } catch (ApiException e) {
            fail();
        }
        try {
            productDto.add(List.of(productForm));
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Product name cannot be empty, Product barcode cannot be empty, Product MRP cannot be negative.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddDtoMaxNames() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrandName("b");
        brandForm.setCategoryName("c");
        ProductForm productForm = new ProductForm();
        try {
            BrandPojo brandPojo = brandDto.add(List.of(brandForm)).get(0);
            for (int i = -1; i < Const.MAX_LENGTH; i++) {
                productForm.setName(productForm.getName() + "p");
                productForm.setBarcode(productForm.getBarcode() + "b");
            }
            productForm.setBrandCategory(brandPojo.getId());
            productForm.setMrp(1d);
        } catch (ApiException e) {
            fail();
        }
        try {
            productDto.add(List.of(productForm));
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Product name should not exceed the maximum length(" + Const.MAX_LENGTH + "), Product barcode should not exceed the maximum length(" + Const.MAX_LENGTH + ").\n");
            throw e;
        }
    }

    @Test
    public void testTrimLC() {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrandName("b");
        brandForm.setCategoryName("c");
        ProductForm productForm = new ProductForm();
        try {
            BrandPojo brandPojo = brandDto.add(List.of(brandForm)).get(0);
            productForm.setMrp(1.07555d);
            productForm.setBarcode("  B  B  ");
            productForm.setBrandCategory(brandPojo.getId());
            productForm.setName("  P  P  ");
            List<ProductPojo> productPojoList = productDto.add(List.of(productForm));
            assertNotEquals(productPojoList, null);
            assertEquals(productPojoList.size(), 1);
            assertEquals(productPojoList.get(0).getName(), "p  p");
            assertEquals(productPojoList.get(0).getBarcode(), "b  b");
            assertEquals(productPojoList.get(0).getMrp(), Double.valueOf(1.08d));
        } catch (ApiException e) {
            fail();
        }
    }

    @Test
    public void testGetAll() {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrandName("b");
        brandForm.setCategoryName("c");
        try {
            BrandPojo brandPojo = brandDto.add(List.of(brandForm)).get(0);
            ProductForm productForm = new ProductForm();
            productForm.setMrp(1.07555d);
            productForm.setBarcode("b");
            productForm.setBrandCategory(brandPojo.getId());
            productForm.setName("p");
            productDto.add(List.of(productForm));
            List<ProductData> productDataList = productDto.getAll();
            assertNotEquals(productDataList, null);
            assertEquals(productDataList.size(), 1);
            assertEquals(productDataList.get(0).getBrandName(), "b");
            assertEquals(productDataList.get(0).getCategoryName(), "c");
            assertEquals(productDataList.get(0).getName(), "p");
            assertEquals(productDataList.get(0).getMrp(), Double.valueOf(1.08d));
            assertEquals(productDataList.get(0).getBarcode(), "b");
            assertEquals(productDataList.get(0).getBrandCategory(), brandPojo.getId());
        } catch (ApiException e) {
            fail();
        }
    }

    @Test
    public void testUpdate() {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrandName("b");
        brandForm.setCategoryName("c");
        try {
            BrandPojo brandPojo = brandDto.add(List.of(brandForm)).get(0);
            ProductForm productForm = new ProductForm();
            productForm.setName("p");
            productForm.setBarcode("b");
            productForm.setMrp(1.075);
            productForm.setBrandCategory(brandPojo.getId());
            ProductPojo productPojo = productDto.add(List.of(productForm)).get(0);
            productForm.setName("up");
            productForm.setMrp(1.0755d);
            productPojo = productDto.update(productPojo.getId(), productForm);
            assertNotEquals(productPojo, null);
            assertEquals(productPojo.getName(), "up");
            assertEquals(productPojo.getMrp(), Double.valueOf(1.08d));
        } catch (ApiException e) {
            fail();
        }
    }
}

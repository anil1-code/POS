package com.increff.pos;

import com.increff.pos.constants.Const;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;

    @Test(expected = ApiException.class)
    public void testAddMaxRows() throws ApiException {
        List<BrandForm> rows = new ArrayList<>();
        for (int i = 0; i < Const.MAX_ROWS + 1; i++) {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrandName("b" + i);
            brandForm.setCategoryName("c" + i);
            rows.add(brandForm);
        }
        try {
            brandDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Number of rows exceeds the max limit.");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddDtoNull() throws ApiException {
        List<BrandForm> rows = new ArrayList<>();
        rows.add(null);
        try {
            brandDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Input form should not be null.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddDtoEmptyNames() throws ApiException {
        List<BrandForm> rows = new ArrayList<>();
        BrandForm brandForm = new BrandForm();
        brandForm.setCategoryName("");
        brandForm.setBrandName("");
        rows.add(brandForm);
        try {
            brandDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Brand name should not be empty, Category name should not be empty.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddDtoMaxNames() throws ApiException {
        List<BrandForm> rows = new ArrayList<>();
        BrandForm brandForm = new BrandForm();
        StringBuilder sb = new StringBuilder(" "); // for trimming
        for (int i = 0; i < Const.MAX_LENGTH + 1; i++) sb.append('B'); // for lower case and max length
        brandForm.setBrandName(sb.toString());
        brandForm.setCategoryName(sb.toString());
        rows.add(brandForm);
        try {
            brandDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Brand name should not exceed maximum length(" + Const.MAX_LENGTH + "), Category name should not exceed maximum length(" + Const.MAX_LENGTH + ").\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testTrimLCDup() throws ApiException {
        List<BrandForm> rows = new ArrayList<>();
        BrandForm brandForm = new BrandForm();
        brandForm.setCategoryName("  C  C  ");
        brandForm.setBrandName("  B  B  ");
        rows.add(brandForm);
        try {
            List<BrandPojo> added = brandDto.add(rows);
            assertNotEquals(added, null);
            assertEquals(added.size(), 1);
            assertEquals(added.get(0).getBrandName(), "b  b");
            assertEquals(added.get(0).getCategoryName(), "c  c");
            brandDto.add(rows);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Brand Category combination already exists.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void testUpdateNullIDDup() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrandName("b");
        brandForm.setCategoryName("c");
        int cnt = 0;
        try {
            brandDto.update(10, brandForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "No Brand Category exists for this ID: 10");
            cnt++;
        }
        List<BrandForm> rows = new ArrayList<>();
        rows.add(brandForm);
        BrandPojo brandPojo = brandDto.add(rows).get(0);
        try {
            brandDto.update(brandPojo.getId(), brandForm);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Brand Category combination already exists");
            cnt++;
        }
        if (cnt == 2) {
            throw new ApiException("");
        }
    }

    @Test
    public void testGetAll() {
        List<BrandForm> row = new ArrayList<>();
        BrandForm brandForm = new BrandForm();
        brandForm.setBrandName("B");
        brandForm.setCategoryName("C");
        row.add(brandForm);
        try {
            brandDto.add(row);
        } catch (ApiException e) {
            fail();
        }
        List<BrandData> brandDataList = brandDto.getAll();
        assertNotEquals(brandDataList, null);
        assertEquals(brandDataList.size(), 1);
        assertEquals(brandDataList.get(0).getBrandName(), "b");
        assertEquals(brandDataList.get(0).getCategoryName(), "c");
    }

    @Test
    public void testUpdate() {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrandName("b");
        brandForm.setCategoryName("c");
        try {
            List<BrandPojo> brandPojoList = brandDto.add(Collections.singletonList(brandForm));
            assertNotEquals(brandPojoList, null);
            assertEquals(brandPojoList.size(), 1);
            BrandForm ub = new BrandForm();
            ub.setBrandName("d");
            ub.setCategoryName("e");
            BrandPojo brandPojo = brandDto.update(brandPojoList.get(0).getId(), ub);
            assertNotEquals(brandPojo, null);
            assertEquals(brandPojo.getBrandName(), "d");
            assertEquals(brandPojo.getCategoryName(), "e");
            List<BrandData> brandDataList = brandDto.getAll();
            assertNotEquals(brandDataList, null);
            assertEquals(brandDataList.size(), 1);
            assertEquals(brandDataList.get(0).getBrandName(), "d");
            assertEquals(brandDataList.get(0).getCategoryName(), "e");
        } catch (ApiException e) {
            fail();
        }
    }
}

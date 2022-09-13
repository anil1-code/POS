package com.increff.pos.dto;

import com.increff.pos.dto.helper.ProductDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Component
public class ProductDto {
    @Autowired
    private ProductService productService;

    public List<ProductData> getAll() {
        List<ProductPojo> productPojoList = productService.getAll();
        return ProductDtoHelper.convertToProductDataList(productPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ProductPojo> add(List<ProductForm> productFormList) throws ApiException {
        List<ProductPojo> addedPojoList = new ArrayList<>();
        StringBuilder errorMessageData = new StringBuilder();
        int row = 0;
        boolean isBulkAdd = productFormList.size() > 1;
        for (ProductForm productForm : productFormList) {
            try {
                ProductDtoHelper.normalize(productForm);
                addedPojoList.add(productService.add(ProductDtoHelper.convertToPojo(productForm)));
            } catch (ApiException e) {
                errorMessageData.append(isBulkAdd ? ("row " + row + ": ") : "").append(e.getMessage());
            }
            row++;
        }
        if (errorMessageData.length() != 0) {
            throw new ApiException(errorMessageData.toString());
        }
        return addedPojoList;
    }

    public void delete(int id) {
        productService.delete(id);
    }

    public ProductPojo update(int id, ProductForm productForm) throws ApiException {
        ProductDtoHelper.normalize(productForm);
        ProductPojo productPojo = ProductDtoHelper.convertToPojo(productForm);
        return productService.update(id, productPojo);
    }

    @Transactional(readOnly = true)
    public ProductData get(int id) {
        ProductPojo productPojo = productService.getById(id);
        return ProductDtoHelper.convertToData(productPojo);
    }
}
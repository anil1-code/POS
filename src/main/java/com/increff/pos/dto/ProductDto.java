package com.increff.pos.dto;

import com.increff.pos.dto.helper.ProductDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.BrandService;
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
    @Autowired
    private BrandService brandService;
    private final static int MAX_ROWS = 5000;

    public List<ProductData> getAll() {
        List<ProductPojo> productPojoList = productService.getAll();
        return ProductDtoHelper.convertToProductDataList(productPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ProductPojo> add(List<ProductForm> productFormList) throws ApiException {
        if (productFormList.size() > MAX_ROWS) {
            throw new ApiException("number of rows exceeds the limit");
        }
        List<ProductPojo> addedPojoList = new ArrayList<>();
        StringBuilder errorMessageData = new StringBuilder();
        int row = 0;
        boolean isBulkAdd = productFormList.size() > 1;
        for (ProductForm productForm : productFormList) {
            try {
                ProductDtoHelper.normalize(productForm);
                if (brandService.getById(productForm.getBrandCategory()) == null) {
                    throw new ApiException("This brand category doesn't exists");
                }
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
        if (brandService.getById(productForm.getBrandCategory()) == null) {
            throw new ApiException("This brand category doesn't exists");
        }
        ProductPojo productPojo = ProductDtoHelper.convertToPojo(productForm);
        return productService.update(id, productPojo);
    }

    @Transactional(readOnly = true)
    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.getById(id);
        if(productPojo == null) {
            throw new ApiException("Does not exist");
        }
        return ProductDtoHelper.convertToData(productPojo);
    }
}
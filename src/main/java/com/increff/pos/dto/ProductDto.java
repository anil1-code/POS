package com.increff.pos.dto;

import com.increff.pos.constants.Const;
import com.increff.pos.dto.helper.ProductDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProductDto {
    @Autowired
    private ProductService productService;

    public List<ProductData> getAll() {
        Pair<List<ProductPojo>, List<BrandPojo>> pairedPojoLists = productService.getAll();
        return ProductDtoHelper.convertToProductDataList(pairedPojoLists.fst, pairedPojoLists.snd);
    }

    public List<ProductPojo> add(List<ProductForm> productFormList) throws ApiException {
        if (productFormList.size() > Const.MAX_ROWS) {
            throw new ApiException("Number of rows exceeds the max limit.");
        }
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        boolean isBulkAdd = productFormList.size() > 1;
        for (ProductForm productForm : productFormList) {
            try {
                ProductDtoHelper.validateAndNormalize(productForm);
            } catch (ApiException e) {
                errorMsg.append(isBulkAdd ? ("row " + row + ": ") : "").append(e.getMessage());
            }
            row++;
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        return productService.add(ProductDtoHelper.convertToPojoList(productFormList));
    }

    public void delete(int id) {
        // productService.delete(id);
    }

    public ProductPojo update(int id, ProductForm productForm) throws ApiException {
        ProductDtoHelper.validateAndNormalize(productForm);
        ProductPojo productPojo = ProductDtoHelper.convertToPojo(productForm);
        return productService.update(id, productPojo);
    }

}
package com.increff.pos.dto;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.dto.helper.ProductDtoHelper.*;

@Component
public class ProductDto {
    @Autowired
    private ProductService productService;

    public List<ProductData> getAll() {
        List<ProductPojo> productPojoList = productService.getAll();
        return convertToProductDataList(productPojoList);
    }

    public List<ProductPojo> add(List<ProductForm> productFormList) {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for(ProductForm productForm : productFormList) {
            normalize(productForm);
            productPojoList.add(convertToPojo(productForm));
        }
        return productService.add(productPojoList);
    }

    public void delete(int id) {
        productService.delete(id);
    }

    public ProductPojo update(int id, ProductForm productForm) {
        normalize(productForm);
        ProductPojo productPojo = convertToPojo(productForm);
        return productService.update(id, productPojo);
    }
}
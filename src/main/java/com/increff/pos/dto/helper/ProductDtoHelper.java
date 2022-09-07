package com.increff.pos.dto.helper;

import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoHelper {
    public static List<ProductData> convertToProductDataList(List<ProductPojo> productPojoList){
        List<ProductData> productDataList = new ArrayList<>();
        for(ProductPojo productPojo : productPojoList) {
            ProductData productData = convertToData(productPojo);
            productDataList.add(productData);
        }
        return productDataList;
    }

    public static void normalize(ProductForm productForm) {
        if(StringUtil.isEmpty(productForm.getBarcode()) || StringUtil.isEmpty(productForm.getName())) {
            throw new ApiException("Barcode and Name cannot be empty");
        }
        productForm.setName(StringUtil.trimAndLowerCase(productForm.getName()));
        productForm.setBarcode(StringUtil.trimAndLowerCase(productForm.getBarcode()));
    }

    private static ProductData convertToData(ProductPojo productPojo) {
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setName(productPojo.getName());
        productData.setMrp(productPojo.getMrp());
        productData.setBrandCategory(productPojo.getBrandCategory());
        productData.setBarcode(productPojo.getBarcode());
        return productData;
    }

    public static ProductPojo convertToPojo(ProductForm productForm) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBrandCategory(productForm.getBrandCategory());
        return productPojo;
    }
}

package com.increff.pos.dto.helper;

import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoHelper {

    private static final int MAX_LENGTH = 50; // While changing this, also change the maxLength attribute of the input field in frontend

    public static List<ProductData> convertToProductDataList(List<ProductPojo> productPojoList, List<BrandPojo> brandPojoList) {
        List<ProductData> productDataList = new ArrayList<>();
        int i = 0;
        for (ProductPojo productPojo : productPojoList) {
            ProductData productData = convertToData(productPojo, brandPojoList.get(i));
            productDataList.add(productData);
            i++;
        }
        return productDataList;
    }

    public static void normalize(ProductForm productForm) throws ApiException {
        productForm.setName(StringUtil.trimAndLowerCase(productForm.getName()));
        productForm.setBarcode(StringUtil.trimAndLowerCase(productForm.getBarcode()));
        StringBuilder errorMsg = new StringBuilder("");
        if (StringUtil.isEmpty(productForm.getName())) {
            errorMsg.append("Product name cannot be empty. ");
        }
        if (StringUtil.isEmpty(productForm.getBarcode())) {
            errorMsg.append("Product barcode cannot be empty. ");
        }
        if (StringUtil.length(productForm.getName()) > MAX_LENGTH) {
            errorMsg.append("Product name should not exceed the maximum length(" + MAX_LENGTH + "). ");
        }
        if (StringUtil.length(productForm.getBarcode()) > MAX_LENGTH) {
            errorMsg.append("Product barcode should not exceed the maximum length(" + MAX_LENGTH + "). ");
        }
        if (productForm.getMrp() == null) {
            errorMsg.append("Product MRP should not be null");
        } else {
            productForm.setMrp(StringUtil.truncateDouble(productForm.getMrp()));
            if (productForm.getMrp() == 0) {
                errorMsg.append("Product MRP cannot be 0. ");
            }
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg + "\n");
        }
    }

    public static ProductData convertToData(ProductPojo productPojo, BrandPojo brandPojo) {
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setName(productPojo.getName());
        productData.setMrp(productPojo.getMrp());
        productData.setBrandCategory(productPojo.getBrandCategory());
        productData.setBarcode(productPojo.getBarcode());
        productData.setBrandName(brandPojo.getBrandName());
        productData.setCategoryName(brandPojo.getCategoryName());
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

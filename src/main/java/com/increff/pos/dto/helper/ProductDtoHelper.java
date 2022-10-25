package com.increff.pos.dto.helper;

import com.increff.pos.constants.Const;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.BasicDataUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoHelper {

    public static List<ProductData> convertToProductDataList(List<ProductPojo> productPojoList, List<BrandPojo> brandPojoList) {
        List<ProductData> productDataList = new ArrayList<>();
        int i = 0;
        for (ProductPojo productPojo : productPojoList) {
            ProductData productData = convertToData(productPojo, brandPojoList.get(i++));
            productDataList.add(productData);
        }
        return productDataList;
    }

    public static void validateAndNormalize(ProductForm productForm) throws ApiException {
        if (productForm == null) {
            throw new ApiException("Input form should not be null.\n");
        }
        StringBuilder errorMsg = new StringBuilder();
        if (BasicDataUtil.isEmpty(productForm.getName())) {
            errorMsg.append("Product name cannot be empty, ");
        }
        if (BasicDataUtil.isEmpty(productForm.getBarcode())) {
            errorMsg.append("Product barcode cannot be empty, ");
        }
        productForm.setName(BasicDataUtil.trimAndLowerCase(productForm.getName()));
        productForm.setBarcode(BasicDataUtil.trimAndLowerCase(productForm.getBarcode()));
        if (BasicDataUtil.length(productForm.getName()) > Const.MAX_LENGTH) {
            errorMsg.append("Product name should not exceed the maximum length(" + Const.MAX_LENGTH + "), ");
        }
        if (BasicDataUtil.length(productForm.getBarcode()) > Const.MAX_LENGTH) {
            errorMsg.append("Product barcode should not exceed the maximum length(" + Const.MAX_LENGTH + "), ");
        }
        productForm.setMrp(BasicDataUtil.roundOffDouble(productForm.getMrp()));
        if (productForm.getMrp() < 0) {
            errorMsg.append("Product MRP cannot be negative, ");
        }

        if (errorMsg.length() != 0) {
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            errorMsg.append(".\n");
            throw new ApiException(errorMsg.toString());
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

    public static List<ProductPojo> convertToPojoList(List<ProductForm> productFormList) {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (ProductForm productForm : productFormList) {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBrandCategory(productForm.getBrandCategory());
            productPojo.setBarcode(productForm.getBarcode());
            productPojo.setName(productForm.getName());
            productPojo.setMrp(productForm.getMrp());
            productPojoList.add(productPojo);
        }
        return productPojoList;
    }
}

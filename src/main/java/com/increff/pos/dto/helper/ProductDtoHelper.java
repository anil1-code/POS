package com.increff.pos.dto.helper;

import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoHelper {
    public static List<ProductData> convertToProductDataList(List<ProductPojo> productMasterPojoList){
        List<ProductData> productMasterDataList = new ArrayList<>();
        for(ProductPojo productMasterPojo : productMasterPojoList) {
            ProductData productMasterData = convertToData(productMasterPojo);
            productMasterDataList.add(productMasterData);
        }
        return productMasterDataList;
    }

    public static void normalize(ProductForm productMasterForm) {
        if(StringUtil.isEmpty(productMasterForm.getBarcode()) || StringUtil.isEmpty(productMasterForm.getName())) {
            throw new ApiException("Barcode and Name cannot be empty");
        }
        productMasterForm.setName(StringUtil.trimAndLowerCase(productMasterForm.getName()));
        productMasterForm.setBarcode(StringUtil.trimAndLowerCase(productMasterForm.getBarcode()));
    }

    public static ProductData convertToData(ProductPojo productMasterPojo) {
        ProductData productMasterData = new ProductData();
        productMasterData.setId(productMasterPojo.getId());
        productMasterData.setName(productMasterPojo.getName());
        productMasterData.setMrp(productMasterPojo.getMrp());
        productMasterData.setBrandCategory(productMasterPojo.getBrandCategory());
        productMasterData.setBarcode(productMasterPojo.getBarcode());
        return productMasterData;
    }

    public static ProductPojo convertToPojo(ProductForm productMasterForm) {
        ProductPojo productMasterPojo = new ProductPojo();
        productMasterPojo.setBarcode(productMasterForm.getBarcode());
        productMasterPojo.setName(productMasterForm.getName());
        productMasterPojo.setMrp(productMasterForm.getMrp());
        productMasterPojo.setBrandCategory(productMasterForm.getBrandCategory());
        return productMasterPojo;
    }
}

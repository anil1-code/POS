package com.increff.pos.dto.helper;

import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class BrandDtoHelper {

    private static final int MAX_LENGTH = 50; // While changing this, also change the maxLength attribute of the input field in frontend
    public static void normalize(BrandForm brandForm) throws ApiException {
        StringBuilder errorMsg = new StringBuilder("");
        if (StringUtil.isEmpty(brandForm.getBrandName())) {
            errorMsg.append("Brand cannot be empty. ");
        }
        if(StringUtil.isEmpty(brandForm.getCategoryName())) {
            errorMsg.append("Category cannot be empty. ");
        }
        if(brandForm.getBrandName().length() > MAX_LENGTH) {
            errorMsg.append("Brand should not exceed the maximum length(" + MAX_LENGTH+ "). ");
        }
        if(brandForm.getCategoryName().length() > MAX_LENGTH) {
            errorMsg.append("Category should not exceed the maximum length(" + MAX_LENGTH+ "). ");
        }
        if(errorMsg.length() != 0) {
            throw new ApiException(errorMsg + "\n");
        }
        brandForm.setBrandName(StringUtil.trimAndLowerCase(brandForm.getBrandName()));
        brandForm.setCategoryName(StringUtil.trimAndLowerCase(brandForm.getCategoryName()));
    }

    public static BrandPojo convertToPojo(BrandForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrandName(brandForm.getBrandName());
        brandPojo.setCategoryName(brandForm.getCategoryName());
        return brandPojo;
    }

    public static BrandData convertToData(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setBrandName(brandPojo.getBrandName());
        brandData.setCategoryName(brandPojo.getCategoryName());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

    public static List<BrandData> convertToDataList(List<BrandPojo> brandPojoList) {
        List<BrandData> brandDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojoList) {
            BrandData brandData = convertToData(brandPojo);
            brandDataList.add(brandData);
        }
        return brandDataList;
    }
}

package com.increff.pos.dto.helper;

import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;

public class BrandDtoHelper {

    private static final int MAX_LENGTH = 50; // While changing this, also change the maxLength attribute of the input field in frontend
    public static void normalize(BrandForm brandForm) {
        if (StringUtil.isEmpty(brandForm.getBrandName()) || StringUtil.isEmpty(brandForm.getCategoryName())) {
            throw new ApiException("Brand or Category cannot be empty");
        }
        if(brandForm.getBrandName().length() > MAX_LENGTH || brandForm.getCategoryName().length() > MAX_LENGTH) {
            throw new ApiException("Brand or Category exceeds the maximum length");
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
}

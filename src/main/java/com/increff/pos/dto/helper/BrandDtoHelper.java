package com.increff.pos.dto.helper;

import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;

public class BrandDtoHelper {
    public static void normalize(BrandForm brandMasterForm) {
        if (StringUtil.isEmpty(brandMasterForm.getBrandName()) || StringUtil.isEmpty(brandMasterForm.getCategoryName())) {
            throw new ApiException("Brand and Category cannot be empty");
        }
        brandMasterForm.setBrandName(StringUtil.trimAndLowerCase(brandMasterForm.getBrandName()));
        brandMasterForm.setCategoryName(StringUtil.trimAndLowerCase(brandMasterForm.getCategoryName()));
    }

    public static BrandPojo convertToPojo(BrandForm brandMasterForm) {
        BrandPojo brandMasterPojo = new BrandPojo();
        brandMasterPojo.setBrandName(brandMasterForm.getBrandName());
        brandMasterPojo.setCategoryName(brandMasterForm.getCategoryName());
        return brandMasterPojo;
    }

    public static BrandData convertToData(BrandPojo brandMasterPojo) {
        BrandData brandMasterData = new BrandData();
        brandMasterData.setBrandName(brandMasterPojo.getBrandName());
        brandMasterData.setCategoryName(brandMasterPojo.getCategoryName());
        brandMasterData.setId(brandMasterPojo.getId());
        return brandMasterData;
    }
}

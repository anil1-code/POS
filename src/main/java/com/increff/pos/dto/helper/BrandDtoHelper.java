package com.increff.pos.dto.helper;

import com.increff.pos.constants.Const;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.BasicDataUtil;

import java.util.ArrayList;
import java.util.List;

public class BrandDtoHelper {
    public static void validateAndNormalize(BrandForm brandForm) throws ApiException {
        if (brandForm == null) {
            throw new ApiException("Input form should not be null.\n");
        }
        StringBuilder errorMsg = new StringBuilder();
        if (BasicDataUtil.isEmpty(brandForm.getBrandName())) {
            errorMsg.append("Brand name should not be empty, ");
        }
        if (BasicDataUtil.isEmpty(brandForm.getCategoryName())) {
            errorMsg.append("Category name should not be empty, ");
        }
        brandForm.setBrandName(BasicDataUtil.trimAndLowerCase(brandForm.getBrandName()));
        brandForm.setCategoryName(BasicDataUtil.trimAndLowerCase(brandForm.getCategoryName()));
        if (brandForm.getBrandName().length() > Const.MAX_LENGTH) {
            errorMsg.append("Brand name should not exceed maximum length(" + Const.MAX_LENGTH + "), ");
        }
        if (brandForm.getCategoryName().length() > Const.MAX_LENGTH) {
            errorMsg.append("Category name should not exceed maximum length(" + Const.MAX_LENGTH + "), ");
        }
        if (errorMsg.length() != 0) {
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            errorMsg.append(".\n");
            throw new ApiException(errorMsg.toString());
        }
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

    public static List<BrandPojo> convertToPojoList(List<BrandForm> brandFormList) {
        List<BrandPojo> brandPojoList = new ArrayList<>();
        for (BrandForm brandForm : brandFormList) {
            brandPojoList.add(convertToPojo(brandForm));
        }
        return brandPojoList;
    }
}

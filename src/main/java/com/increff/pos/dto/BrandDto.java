package com.increff.pos.dto;

import com.increff.pos.Exception.ApiException;
import com.increff.pos.model.data.BrandMasterData;
import com.increff.pos.model.forms.BrandMasterForm;
import com.increff.pos.pojo.BrandMasterPojo;
import com.increff.pos.service.BrandMasterService;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class BrandDto {
    @Autowired
    public BrandMasterService brandMasterService;

    public BrandMasterPojo add(BrandMasterForm brandMasterForm) {
        normalize(brandMasterForm);
        return brandMasterService.add(convertToPojo(brandMasterForm));
    }

    public List<BrandMasterPojo> addMultiple(List<BrandMasterForm> brandMasterFormList) {
        List<BrandMasterPojo> brandMasterPojoList = new ArrayList<>();
        for (BrandMasterForm brandMasterForm : brandMasterFormList) {
            // call add method for each form, and it will handle everything itself
            BrandMasterPojo brandMasterPojo = add(brandMasterForm);
            brandMasterPojoList.add(brandMasterPojo);
        }
        return brandMasterPojoList;
    }

    public List<BrandMasterData> getAll() {
        List<BrandMasterPojo> brandMasterPojoList = brandMasterService.getAllBrandMasterPojos();
        List<BrandMasterData> brandMasterDataList = new ArrayList<>();
        for (BrandMasterPojo brandMasterPojo : brandMasterPojoList) {
            BrandMasterData brandMasterData = convertToData(brandMasterPojo);
            brandMasterDataList.add(brandMasterData);
        }
        return brandMasterDataList;
    }

    public void delete(int id) {
        brandMasterService.delete(id);
    }

    public BrandMasterPojo update(int id, BrandMasterForm brandMasterForm) {
        normalize(brandMasterForm);
        BrandMasterPojo brandMasterPojo = convertToPojo(brandMasterForm);
        return brandMasterService.update(id, brandMasterPojo);
    }

    private void normalize(BrandMasterForm brandMasterForm) {
        if (StringUtil.isEmpty(brandMasterForm.getBrandName()) || StringUtil.isEmpty(brandMasterForm.getCategoryName())) {
            throw new ApiException("Brand and Category cannot be empty");
        }
        brandMasterForm.setBrandName(StringUtil.toLowerCase(brandMasterForm.getBrandName()));
        brandMasterForm.setCategoryName(StringUtil.toLowerCase(brandMasterForm.getCategoryName()));
    }

    private BrandMasterPojo convertToPojo(BrandMasterForm brandMasterForm) {
        BrandMasterPojo brandMasterPojo = new BrandMasterPojo();
        brandMasterPojo.setBrandName(brandMasterForm.getBrandName());
        brandMasterPojo.setCategoryName(brandMasterForm.getCategoryName());
        return brandMasterPojo;
    }

    private BrandMasterData convertToData(BrandMasterPojo brandMasterPojo) {
        BrandMasterData brandMasterData = new BrandMasterData();
        brandMasterData.setBrandName(brandMasterPojo.getBrandName());
        brandMasterData.setCategoryName(brandMasterPojo.getCategoryName());
        brandMasterData.setId(brandMasterPojo.getId());
        return brandMasterData;
    }

}
/*
 * what should happen if the updated brand category combination is either empty or already exists, -throw an exception
 * what should happen if the id doesn't exist anymore, - throw an exception that id doesn't exist anymore
 * what should happen if the updated and the previous names are same, - throw an exception again
 * */
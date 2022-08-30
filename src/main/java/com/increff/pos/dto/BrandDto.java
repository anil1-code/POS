package com.increff.pos.dto;

import com.increff.pos.dto.helper.BrandDtoHelper;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class BrandDto {
    @Autowired
    public BrandService brandService;

    public List<BrandPojo> add(List<BrandForm> brandFormList) {
        List<BrandPojo> brandPojoList = new ArrayList<>();
        for (BrandForm brandForm : brandFormList) {
            BrandDtoHelper.normalize(brandForm);
            brandPojoList.add(BrandDtoHelper.convertToPojo(brandForm));
        }
        return brandService.add(brandPojoList);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> brandPojoList = brandService.getAll();
        List<BrandData> brandDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojoList) {
            BrandData brandData = BrandDtoHelper.convertToData(brandPojo);
            brandDataList.add(brandData);
        }
        return brandDataList;
    }

    public void delete(int id) {
        brandService.delete(id);
    }

    public BrandPojo update(int id, BrandForm brandForm) {
        BrandDtoHelper.normalize(brandForm);
        BrandPojo brandPojo = BrandDtoHelper.convertToPojo(brandForm);
        return brandService.update(id, brandPojo);
    }

}

/*
 * what should happen if the updated brand category combination is either empty or already exists, -throw an exception
 * what should happen if the id doesn't exist anymore, - throw an exception that id doesn't exist anymore
 * what should happen if the updated and the previous names are same, - throw an exception again
 * */
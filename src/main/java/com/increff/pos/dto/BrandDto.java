package com.increff.pos.dto;

import com.increff.pos.dto.helper.BrandDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Component
public class BrandDto {
    @Autowired
    public BrandService brandService;

    private final static int MAX_ROWS = 5000;

    @Transactional(rollbackFor = ApiException.class)
    public List<BrandPojo> add(List<BrandForm> brandFormList) throws ApiException {
        if(brandFormList.size() > MAX_ROWS) {
            throw new ApiException("number of rows exceeds the limit");
        }
        List<BrandPojo> addedPojoList = new ArrayList<>();
        StringBuilder errorMessageData = new StringBuilder();
        int row = 1;
        boolean isBulkAdd = brandFormList.size() > 1; // will be true if the data was sent via tsv
        for (BrandForm brandForm : brandFormList) {
            try {
                BrandDtoHelper.normalize(brandForm);
                BrandPojo addedPojo = brandService.add(BrandDtoHelper.convertToPojo(brandForm));
                addedPojoList.add(addedPojo);
            } catch (ApiException e) {
                errorMessageData.append(isBulkAdd ? ("row " + row + ": ") : "").append(e.getMessage()); // append this exception message
            }
            row++;
        }
        if (errorMessageData.length() != 0) {
            throw new ApiException(errorMessageData.toString());
        }
        return addedPojoList;
    }

    public List<BrandData> getAll() {
        List<BrandPojo> brandPojoList = brandService.getAll();
        return BrandDtoHelper.convertToDataList(brandPojoList);
    }

    @Transactional(readOnly = true)
    public BrandData get(int id) throws ApiException {
        BrandPojo brandPojo = brandService.getById(id);
        if(brandPojo == null) {
            throw new ApiException("Does not exist");
        }
        return BrandDtoHelper.convertToData(brandPojo);
    }

    public void delete(int id) {
        brandService.delete(id);
    }

    public BrandPojo update(int id, BrandForm brandForm) throws ApiException {
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
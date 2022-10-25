package com.increff.pos.dto;

import com.increff.pos.constants.Const;
import com.increff.pos.dto.helper.BrandDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BrandDto {
    @Autowired
    public BrandService brandService;

    /*
     * this method converts form to BrandPojo and add them one by one
     * in case of error it continues and eventually throws the well formatted full error string
     */
    public List<BrandPojo> add(List<BrandForm> brandFormList) throws ApiException {
        if (brandFormList.size() > Const.MAX_ROWS) {
            throw new ApiException("Number of rows exceeds the max limit.");
        }
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        boolean isBulkAdd = brandFormList.size() > 1; // will be true if the data was sent via tsv
        for (BrandForm brandForm : brandFormList) {
            try {
                BrandDtoHelper.validateAndNormalize(brandForm);
            } catch (ApiException e) {
                errorMsg.append(isBulkAdd ? ("row " + row + ": ") : "").append(e.getMessage()); // append this exception message
            }
            row++;
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        return brandService.add(BrandDtoHelper.convertToPojoList(brandFormList));
    }

    public List<BrandData> getAll() {
        List<BrandPojo> brandPojoList = brandService.getAll();
        return BrandDtoHelper.convertToDataList(brandPojoList);
    }

    /*
     * updates the BrandPojo with id given
     */
    public BrandPojo update(int id, BrandForm brandForm) throws ApiException {
        BrandDtoHelper.validateAndNormalize(brandForm);
        BrandPojo brandPojo = BrandDtoHelper.convertToPojo(brandForm);
        return brandService.update(id, brandPojo);
    }

}

/*
 * what should happen if the updated brand category combination is either empty or already exists, -throw an exception
 * what should happen if the id doesn't exist anymore, - throw an exception that id doesn't exist anymore
 * what should happen if the updated and the previous names are same, - throw an exception again
 * */
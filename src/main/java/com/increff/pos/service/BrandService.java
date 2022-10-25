package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandDao brandDao;

    @Transactional(readOnly = true)
    public List<BrandPojo> getAll() {
        return brandDao.getAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<BrandPojo> add(List<BrandPojo> brandPojoList) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        boolean isBulkAdd = brandPojoList.size() > 1; // will be true if the data was sent via tsv
        List<BrandPojo> addedPojoList = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojoList) {
            if (exists(brandPojo.getBrandName(), brandPojo.getCategoryName())) {
                errorMsg.append(isBulkAdd ? ("row " + row + ": ") : "").append("Brand Category combination already exists.\n"); // append this exception message
            } else {
                BrandPojo addedPojo = brandDao.add(brandPojo);
                addedPojoList.add(addedPojo);
            }
            row++;
        }
        if (errorMsg.length() > 0) {
            throw new ApiException(errorMsg.toString());
        }
        return addedPojoList;
    }

    @Transactional(rollbackFor = ApiException.class)
    public BrandPojo update(int id, BrandPojo brandPojo) throws ApiException {
        BrandPojo existingPojo = getById(id);
        if (existingPojo == null) {
            throw new ApiException("No Brand Category exists for this ID: " + id);
        }
        if (exists(brandPojo.getBrandName(), brandPojo.getCategoryName())) {
            throw new ApiException("Brand Category combination already exists"); // what if it(bc) was same
        }
        existingPojo.setBrandName(brandPojo.getBrandName());
        existingPojo.setCategoryName(brandPojo.getCategoryName());
        return brandDao.update(existingPojo);
    }


    // HELPER methods, these are supposed to be called from a transactional method
    private boolean exists(String brand, String category) {
        BrandPojo brandPojo = brandDao.getByBrandCategory(brand, category);
        return brandPojo != null;
    }

    public BrandPojo getById(int id) {
        return brandDao.getById(id);
    }

}

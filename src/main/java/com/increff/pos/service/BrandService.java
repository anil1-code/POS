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
    public List<BrandPojo> add(List<BrandPojo> brandPojoList) {
        List<BrandPojo> addedPojoList = new ArrayList<>();
        for(BrandPojo brandPojo : brandPojoList) {
            if (exists(brandPojo.getBrandName(), brandPojo.getCategoryName())) {
                throw new ApiException("This brand category combination already exists");
            }
            BrandPojo addedPojo = brandDao.add(brandPojo);
            addedPojoList.add(addedPojo);
        }
        return addedPojoList;
    }

    @Transactional(rollbackFor = ApiException.class)
    public void delete(int id) {
        brandDao.delete(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public BrandPojo update(int id, BrandPojo brandPojo) {
        BrandPojo existingPojo = brandDao.getById(id);
        if (existingPojo == null) {
            throw new ApiException("No entry exists for this ID :" + id);
        }
        if (exists(brandPojo.getBrandName(), brandPojo.getCategoryName())) {
            throw new ApiException("This brand category combination already exists");
        }
        existingPojo.setBrandName(brandPojo.getBrandName());
        existingPojo.setCategoryName(brandPojo.getCategoryName());
        return brandDao.update(existingPojo);
    }

    private boolean exists(String brand, String category) {
        BrandPojo brandPojo = brandDao.getByBrandCategory(brand, category);
        return brandPojo != null;
    }

    public boolean exists(int id) {
        BrandPojo brandPojo = brandDao.getById(id);
        return brandPojo != null;
    }
}

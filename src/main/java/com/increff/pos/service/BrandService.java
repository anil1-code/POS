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

    public BrandPojo add(BrandPojo brandPojo) throws ApiException {
        if (exists(brandPojo.getBrandName(), brandPojo.getCategoryName())) {
            throw new ApiException("This brand category combination already exists(If this was a bulk transaction, make sure this row is not coming twice in the same file).\n");
        }
        return brandDao.add(brandPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void delete(int id) {
        brandDao.delete(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public BrandPojo update(int id, BrandPojo brandPojo) throws ApiException {
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

    public BrandPojo getById(int id) {
        return brandDao.getById(id);
    }

}

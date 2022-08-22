package com.increff.pos.service;

import com.increff.pos.Exception.ApiException;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandMasterPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Service
public class BrandMasterService {
    @Autowired
    private BrandDao brandDao;

    @Transactional(readOnly = true)
    public List<BrandMasterPojo> getAllBrandMasterPojos() {
        return brandDao.getAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public BrandMasterPojo add(BrandMasterPojo brandMasterPojo) {
        if (exists(brandMasterPojo.getBrandName(), brandMasterPojo.getCategoryName()))
            throw new ApiException("This brand category combination already exists(or same as before)");
        return brandDao.add(brandMasterPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void delete(int id) {
        brandDao.delete(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public BrandMasterPojo update(int id, BrandMasterPojo brandMasterPojo) {
        if (exists(brandMasterPojo.getBrandName(), brandMasterPojo.getCategoryName())) {
            throw new ApiException("This brand category combination already exists");
        }
        try {
            BrandMasterPojo existingPojo = brandDao.getById(id);
            existingPojo.setBrandName(brandMasterPojo.getBrandName());
            existingPojo.setCategoryName(brandMasterPojo.getCategoryName());
            return brandDao.update(brandMasterPojo);
        } catch (NoResultException e) {
            throw new ApiException("This brand category doesn't exists.");
        }
    }

    @Transactional(readOnly = true)
    private boolean exists(String brand, String category) {
        try {
            brandDao.getByBrandCategory(brand, category);
        } catch (NoResultException e) {
            // no brand and category exists in the DB
            return false;
        }
        return true;
    }
}

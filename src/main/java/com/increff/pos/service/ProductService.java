package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private BrandService brandService;

    @Transactional(rollbackFor = ApiException.class)
    public List<ProductPojo> add(List<ProductPojo> productPojoList) {
        List<ProductPojo> addedProductPojoList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            if (productDao.getByBarcode(productPojo.getBarcode()) != null) {
                throw new ApiException("This barcode already exists for a product");
            }
            if (!brandService.exists(productPojo.getBrandCategory())) {
                throw new ApiException("This brand category doesn't exists");
            }
            addedProductPojoList.add(productDao.add(productPojo));
        }
        return addedProductPojoList;
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getAll() {
        return productDao.getAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void delete(int id) {
        productDao.delete(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public ProductPojo update(int id, ProductPojo productPojo) {
        ProductPojo existingPojo = productDao.getById(id);
        if (existingPojo == null) {
            throw new ApiException("No entry exists for this ID: " + id);
        }
        if (!brandService.exists(productPojo.getBrandCategory())) {
            throw new ApiException("This brand category doesn't exists");
        }
        existingPojo.setBrandCategory(productPojo.getBrandCategory());
        existingPojo.setName(productPojo.getName());
        existingPojo.setMrp(productPojo.getMrp());
        return productDao.update(existingPojo);
    }

    public boolean exists(int id) {
        return productDao.getById(id) != null;
    }
}
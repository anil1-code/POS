package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    public ProductPojo add(ProductPojo productPojo) throws ApiException {
        if (productDao.getByBarcode(productPojo.getBarcode()) != null) {
            throw new ApiException("This barcode already exists for a product");
        }
        return productDao.add(productPojo);
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
    public ProductPojo update(int id, ProductPojo productPojo) throws ApiException {
        ProductPojo existingPojo = productDao.getById(id);
        if (existingPojo == null) {
            throw new ApiException("No entry exists for this ID: " + id);
        }
        existingPojo.setBrandCategory(productPojo.getBrandCategory());
        existingPojo.setName(productPojo.getName());
        existingPojo.setMrp(productPojo.getMrp());
        return productDao.update(existingPojo);
    }

    public ProductPojo getById(int id) {
        return productDao.getById(id);
    }
}
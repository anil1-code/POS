package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.Pair;
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
    public List<ProductPojo> add(List<ProductPojo> productPojoList) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        boolean isBulkAdd = productPojoList.size() > 1;
        List<ProductPojo> addedPojoList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            String rowError = productChecker(productPojo, true);
            if (!rowError.isEmpty()) {
                errorMsg.append(isBulkAdd ? ("row " + row + ": ") : "").append(rowError);
            } else {
                ProductPojo addedPojo = productDao.add(productPojo);
                addedPojoList.add(addedPojo);
            }
            row++;
        }
        if (errorMsg.length() > 0) {
            throw new ApiException(errorMsg.toString());
        }
        return addedPojoList;
    }

    @Transactional(readOnly = true)
    public Pair<List<ProductPojo>, List<BrandPojo>> getAll() {
        List<ProductPojo> productPojoList = productDao.getAll();
        List<BrandPojo> brandPojoList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
            brandPojoList.add(brandPojo);
        }
        return new Pair<>(productPojoList, brandPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public ProductPojo update(int id, ProductPojo productPojo) throws ApiException {
        String error = productChecker(productPojo, false);
        if (!error.isEmpty()) {
            throw new ApiException(error);
        }
        ProductPojo existingPojo = getById(id);
        if (existingPojo == null) {
            throw new ApiException("No product exists for this ID: " + id);
        }
        existingPojo.setBrandCategory(productPojo.getBrandCategory());
        existingPojo.setName(productPojo.getName());
        existingPojo.setMrp(productPojo.getMrp());
        return productDao.update(existingPojo);
    }

    // HELPER methods, these are supposed to be called from a transactional method
    public ProductPojo getById(int id) {
        return productDao.getById(id);
    }

    private String productChecker(ProductPojo productPojo, boolean isAdd) {
        StringBuilder errorMsg = new StringBuilder();
        if (brandService.getById(productPojo.getBrandCategory()) == null) {
            errorMsg.append("Brand Category ID does not exists, ");
        }
        if (isAdd && productDao.getByBarcode(productPojo.getBarcode()) != null) {
            errorMsg.append("Barcode already exists, ");
        }
        if (errorMsg.length() > 0) {
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            errorMsg.deleteCharAt(errorMsg.length() - 1);
            errorMsg.append(".\n");
        }
        return errorMsg.toString();
    }
}
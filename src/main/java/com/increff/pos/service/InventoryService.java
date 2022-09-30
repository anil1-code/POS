package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private ProductService productService;


    @Transactional(readOnly = true)
    public Pair<List<InventoryPojo>, List<ProductPojo>> getAll() {
        List<InventoryPojo> inventoryPojoList = inventoryDao.getAll();
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
            productPojoList.add(productPojo);
        }
        return new Pair<>(inventoryPojoList, productPojoList);
    }

    public InventoryPojo getByProductId(int id) {
        return inventoryDao.getByProductId(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<InventoryPojo> add(List<InventoryPojo> inventoryPojoList) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        boolean isBulkAdd = inventoryPojoList.size() > 1;
        List<InventoryPojo> addedPojoList = new ArrayList<>();
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            if (inventoryDao.getByProductId(inventoryPojo.getProductId()) != null) {
                errorMsg.append(isBulkAdd ? ("row " + row + ": ") : "").append("Inventory already added.\n");
            } else {
                inventoryDao.add(inventoryPojo);
            }
            row++;
        }
        if (errorMsg.length() > 0) {
            throw new ApiException(errorMsg.toString());
        }
        return addedPojoList;
    }

    @Transactional(rollbackFor = ApiException.class)
    public void delete(int id) {
        inventoryDao.delete(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public InventoryPojo update(int productId, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo existingPojo = inventoryDao.getByProductId(productId);
        if (existingPojo == null) {
            throw new ApiException("Inventory not added yet.");
        }
        existingPojo.setQuantity(inventoryPojo.getQuantity());
        return inventoryDao.update(existingPojo);
    }
}
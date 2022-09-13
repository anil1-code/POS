package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private ProductService productService;

    @Transactional(readOnly = true)
    public List<InventoryPojo> getAll() {
        return inventoryDao.getAll();
    }

    public InventoryPojo getByProductId(int id) {
        return inventoryDao.getByProductId(id);
    }

    public InventoryPojo add(InventoryPojo inventoryPojo) throws ApiException {
        if (productService.getById(inventoryPojo.getProductId()) == null) {
            throw new ApiException("You haven't added this product yet, please add that first. ");
        }
        if (inventoryDao.getByProductId(inventoryPojo.getProductId()) != null) {
            throw new ApiException("You have already added this inventory, please update that instead. ");
        }
        return inventoryDao.add(inventoryPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void delete(int id) {
        inventoryDao.delete(id);
    }

    @Transactional(rollbackFor = ApiException.class)
    public InventoryPojo update(int productId, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo existingPojo = inventoryDao.getByProductId(productId);
        if (existingPojo == null) {
            throw new ApiException("This inventory has not been added, please add instead.");
        }
        existingPojo.setQuantity(inventoryPojo.getQuantity());
        return inventoryDao.update(existingPojo);
    }
}
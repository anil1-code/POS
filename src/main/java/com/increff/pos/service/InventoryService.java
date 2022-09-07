package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private ProductService productService;

    @Transactional
    public List<InventoryPojo> getAll() {
        return inventoryDao.getAll();
    }

    @Transactional
    public List<InventoryPojo> add(List<InventoryPojo> inventoryPojoList) {
        List<InventoryPojo> addedInventoryPojoList = new ArrayList<>();
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            if(inventoryDao.getByProductId(inventoryPojo.getProductId()) != null) {
                throw new ApiException("You have already added this inventory, please update that instead");
            }
            if(!productService.exists(inventoryPojo.getProductId())) {
                throw new ApiException("You haven't added this product yet, please add that first");
            }
            addedInventoryPojoList.add(inventoryDao.add(inventoryPojo));
        }
        return addedInventoryPojoList;
    }

    @Transactional
    public void delete(int id) {
        inventoryDao.delete(id);
    }
    @Transactional
    public InventoryPojo update(int productId, InventoryPojo inventoryPojo) {
        InventoryPojo existingPojo = inventoryDao.getByProductId(productId);
        if(existingPojo == null) {
            throw new ApiException("This inventory has not been added, please add instead.");
        }
        existingPojo.setQuantity(inventoryPojo.getQuantity());
        return inventoryDao.update(existingPojo);
    }
}
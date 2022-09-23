package com.increff.pos.dto;

import com.increff.pos.constants.consts;
import com.increff.pos.dto.helper.InventoryDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    public List<InventoryData> getAll() {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        List<ProductPojo> productPojoList = new ArrayList<>();
        for(InventoryPojo inventoryPojo : inventoryPojoList) {
            ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
            productPojoList.add(productPojo);
        }
        return InventoryDtoHelper.convertToInventoryDataList(inventoryPojoList, productPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public InventoryData getById(int id) {
        return InventoryDtoHelper.convertToInventoryData(inventoryService.getByProductId(id), productService.getById(id));
    }

    /*
    * this method converts form to InventoryPojo and add them one by one
    * in case of error it continues and eventually throws the well formatted full error string
    */
    @Transactional(rollbackFor = ApiException.class)
    public List<InventoryPojo> add(List<InventoryForm> inventoryFormList) throws ApiException {
        if (inventoryFormList.size() > consts.MAX_ROWS) {
            throw new ApiException("number of rows exceeds the limit");
        }
        List<InventoryPojo> addedPojoList = new ArrayList<>();
        StringBuilder errorMessageData = new StringBuilder();
        int row = 0;
        boolean isBulkAdd = inventoryFormList.size() > 1;
        for (InventoryForm inventoryForm : inventoryFormList) {
            try {
                if (productService.getById(inventoryForm.getProductId()) == null) {
                    throw new ApiException("You haven't added this product yet, please add that first. ");
                }
                InventoryPojo addedPojo = inventoryService.add(InventoryDtoHelper.convertToPojo(inventoryForm));
                addedPojoList.add(addedPojo);
            } catch (ApiException e) {
                errorMessageData.append(isBulkAdd ? ("row " + row + ": ") : "").append(e.getMessage());
            }
            row++;
        }
        if (errorMessageData.length() != 0) {
            throw new ApiException(errorMessageData.toString());
        }
        return addedPojoList;
    }

    public void delete(int id) {
        inventoryService.delete(id);
    }

    public InventoryPojo update(int id, InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = InventoryDtoHelper.convertToPojo(inventoryForm);
        return inventoryService.update(id, inventoryPojo);
    }
}

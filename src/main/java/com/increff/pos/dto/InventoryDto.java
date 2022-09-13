package com.increff.pos.dto;

import com.increff.pos.dto.helper.InventoryDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService inventoryService;

    public List<InventoryData> getAll() {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        return InventoryDtoHelper.convertToInventoryDataList(inventoryPojoList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public InventoryData getById(int id) {
        return InventoryDtoHelper.convertToInventoryData(inventoryService.getByProductId(id));
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<InventoryPojo> add(List<InventoryForm> inventoryFormList) throws ApiException {
        List<InventoryPojo> addedPojoList = new ArrayList<>();
        StringBuilder errorMessageData = new StringBuilder();
        int row = 0;
        boolean isBulkAdd = inventoryFormList.size() > 1;
        for(InventoryForm inventoryForm : inventoryFormList) {
            try {
                InventoryPojo addedPojo = inventoryService.add(InventoryDtoHelper.convertToPojo(inventoryForm));
                addedPojoList.add(addedPojo);
            } catch (ApiException e) {
                errorMessageData.append(isBulkAdd ? ("row " + row + ": ") : "").append(e.getMessage());
            }
            row++;
        }
        if(errorMessageData.length() != 0) {
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

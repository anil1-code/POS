package com.increff.pos.dto;

import com.increff.pos.dto.helper.InventoryDtoHelper;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public List<InventoryPojo> add(List<InventoryForm> inventoryFormList) {
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for (InventoryForm inventoryForm : inventoryFormList) {
            inventoryPojoList.add(InventoryDtoHelper.convertToPojo(inventoryForm));
        }
        return inventoryService.add(inventoryPojoList);
    }

    public void delete(int id) {
        inventoryService.delete(id);
    }

    public InventoryPojo update(int id, InventoryForm inventoryForm) {
        InventoryPojo inventoryPojo = InventoryDtoHelper.convertToPojo(inventoryForm);
        return inventoryService.update(id, inventoryPojo);
    }
}

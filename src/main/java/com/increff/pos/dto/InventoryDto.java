package com.increff.pos.dto;

import com.increff.pos.constants.Const;
import com.increff.pos.dto.helper.InventoryDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.InventoryService;
import com.increff.pos.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService inventoryService;

    public List<InventoryData> getAll() {
        Pair<List<InventoryPojo>, List<ProductPojo>> pairedPojoLists = inventoryService.getAll();
        return InventoryDtoHelper.convertToInventoryDataList(pairedPojoLists.fst, pairedPojoLists.snd);
    }

    /*
     * this method converts form to InventoryPojo and add them one by one
     * in case of error it continues and eventually throws the well formatted full error string
     */
    public List<InventoryPojo> add(List<InventoryForm> inventoryFormList) throws ApiException {
        if (inventoryFormList.size() > Const.MAX_ROWS) {
            throw new ApiException("Number of rows exceeds the max limit.");
        }
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        boolean isBulkAdd = inventoryFormList.size() > 1;
        for (InventoryForm inventoryForm : inventoryFormList) {
            try {
                InventoryDtoHelper.validate(inventoryForm);
            } catch (ApiException e) {
                errorMsg.append(isBulkAdd ? ("row " + row + ": ") : "").append(e.getMessage());
            }
            row++;
        }
        if (errorMsg.length()  !=0) {
            throw new ApiException(errorMsg.toString());
        }
        return inventoryService.add(InventoryDtoHelper.convertToPojoList(inventoryFormList));
    }

    public InventoryPojo update(int id, InventoryForm inventoryForm) throws ApiException {
        InventoryDtoHelper.validate(inventoryForm);
        InventoryPojo inventoryPojo = InventoryDtoHelper.convertToPojo(inventoryForm);
        return inventoryService.update(id, inventoryPojo);
    }
}

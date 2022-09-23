package com.increff.pos.dto.helper;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;

import java.util.ArrayList;
import java.util.List;

public class InventoryDtoHelper {
    public static List<InventoryData> convertToInventoryDataList(List<InventoryPojo> inventoryPojoList, List<ProductPojo> productPojoList) {
        List<InventoryData> inventoryDataList = new ArrayList<>();
        int i = 0;
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            inventoryDataList.add(convertToInventoryData(inventoryPojo, productPojoList.get(i++)));
        }
        return inventoryDataList;
    }

    public static InventoryData convertToInventoryData(InventoryPojo inventoryPojo, ProductPojo productPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setProductId(inventoryPojo.getProductId());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setProductName(productPojo.getName());
        return inventoryData;
    }

    public static InventoryPojo convertToPojo(InventoryForm inventoryForm) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(inventoryForm.getProductId());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        return inventoryPojo;
    }
}

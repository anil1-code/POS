package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryDto inventoryDto;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<InventoryData> getAll() {
        return inventoryDto.getAll();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<InventoryPojo> add(@RequestBody List<InventoryForm> inventoryFormList) {
        return inventoryDto.add(inventoryFormList);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        inventoryDto.delete(id);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public InventoryPojo update(@PathVariable int id, @RequestBody InventoryForm inventoryForm) {
        System.out.println("update started");
        return inventoryDto.update(id, inventoryForm);
    }
}

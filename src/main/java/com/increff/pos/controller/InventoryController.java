package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "get all inventory data")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<InventoryData> getAll() {
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "add a list of inventory to the database")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public List<InventoryPojo> add(@RequestBody List<InventoryForm> inventoryFormList) throws ApiException {
        return inventoryDto.add(inventoryFormList);
    }

    @ApiOperation(value = "update an inventory by its id")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public InventoryPojo update(@PathVariable int id, @RequestBody InventoryForm inventoryForm) throws ApiException {
        return inventoryDto.update(id, inventoryForm);
    }
}

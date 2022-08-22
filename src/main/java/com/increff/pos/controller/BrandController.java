package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandMasterData;
import com.increff.pos.model.forms.BrandMasterForm;
import com.increff.pos.pojo.BrandMasterPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/brands")
public class BrandController {
    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value = "getAll")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<BrandMasterData> getAllBrands() {
        // call DTO to get this
        return brandDto.getAll();
    }

    @ApiOperation(value = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BrandMasterPojo add(@RequestBody BrandMasterForm brandMasterForm) {
        return brandDto.add(brandMasterForm);
    }

    @ApiOperation(value = "add multiple")
    @RequestMapping(value = "/addMultiple", method = RequestMethod.POST)
    public List<BrandMasterPojo> addMultiple(@RequestBody List<BrandMasterForm> brandMasterFormList) {
        return brandDto.addMultiple(brandMasterFormList);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestBody int id) {
        brandDto.delete(id);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public BrandMasterPojo update(@PathVariable int id, @RequestBody BrandMasterForm brandMasterForm) {
        return brandDto.update(id, brandMasterForm);
    }

}
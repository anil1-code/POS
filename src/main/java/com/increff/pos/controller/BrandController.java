package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/brands")
public class BrandController {
    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value = "get all brand category pairs")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return brandDto.getAll();
    }

    @ApiOperation(value = "add a list of brand category pairs")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<BrandPojo> add(@RequestBody List<BrandForm> brandFormList) throws ApiException {
        return brandDto.add(brandFormList);
    }

    @ApiOperation(value = "get the brand category corresponding to the id given")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BrandData getById(@PathVariable int id) throws ApiException {
        return brandDto.get(id);
    }

    @ApiOperation(value = "delete a brand category pair by id")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
//        brandDto.delete(id);
    }

    @ApiOperation(value = "update a brand category pair by id")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public BrandPojo update(@PathVariable int id, @RequestBody BrandForm brandForm) throws ApiException {
        return brandDto.update(id, brandForm);
    }
}
package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api
@RestController
@RequestMapping(value = "/api/products")
public class ProductController {
    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "get all products")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductData> getAll() {
        return productDto.getAll();
    }

    @ApiOperation(value = "add a list of products")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public List<ProductPojo> add(@RequestBody List<ProductForm> productFormList) throws ApiException {
        return productDto.add(productFormList);
    }

    @ApiOperation(value = "update a product by its id")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ProductPojo update(@PathVariable int id, @RequestBody ProductForm productForm) throws ApiException {
        return productDto.update(id, productForm);
    }
}

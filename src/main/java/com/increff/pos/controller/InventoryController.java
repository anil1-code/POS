package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/inventory")
public class InventoryController {
    @Autowired
    ProductDto productDto;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductData> getAll() {
        return productDto.getAll();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<ProductPojo> add(@RequestBody List<ProductForm> productFormList) {
        return productDto.add(productFormList);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        productDto.delete(id);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ProductPojo update(@PathVariable int id, @RequestBody ProductForm productForm) {
        System.out.println("update started");
        return productDto.update(id, productForm);
    }
}

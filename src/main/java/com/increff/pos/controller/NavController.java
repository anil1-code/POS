package com.increff.pos.controller;

import com.increff.pos.model.data.InfoData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class NavController {

    @Value("${app.baseUrl")
    public String baseUrl;

    @RequestMapping(value = "")
    public ModelAndView index() {
        return mav("index.html");
    }

    @RequestMapping(value = "/brands")
    public ModelAndView brands() {
        return mav("brands.html");
    }

    @RequestMapping(value = "/products")
    public ModelAndView products() {
        return mav("products.html");
    }

    @RequestMapping(value = "/inventory")
    public ModelAndView inventory() {
        return mav("inventory.html");
    }

    @RequestMapping(value = "/orders")
    public ModelAndView orders() {
        return mav("orders.html");
    }

    private ModelAndView mav(String page) {
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("info", new InfoData());
        mav.addObject("baseUrl", baseUrl);
        return mav;
    }

}

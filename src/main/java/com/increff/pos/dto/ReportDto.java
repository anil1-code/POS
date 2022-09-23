package com.increff.pos.dto;

import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ReportDto {
    @Autowired
    private ReportService reportService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;

    public SalesReportData salesReport(String startDate, String endDate, String brand, String category) {
        System.out.println("DTo " + startDate + " " + endDate);
        List<OrderPojo> orderPojoList = orderService.getOrdersBetweenDates(startDate, endDate);
        System.out.println("between dates " + orderPojoList.size());
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderPojo.getId());
            for (OrderItemPojo orderItemPojo : orderItemPojoList) {
                BrandPojo brandPojo = brandService.getById(productService.getById(orderItemPojo.getProductId()).getBrandCategory());
                if ((Objects.equals(brand, "") || Objects.equals(brand, brandPojo.getBrandName())) && (Objects.equals(category, "") || Objects.equals(category, brandPojo.getCategoryName()))) {
                    categories.add(brandPojo.getCategoryName());
                    orderItemPojos.add(orderItemPojo);
                }
            }
        }
        return reportService.salesReport(orderItemPojos, categories);
    }

    public InventoryReportData inventoryReport() {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        List<BrandPojo> brandPojoList = new ArrayList<>();
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            BrandPojo brandPojo = brandService.getById(productService.getById(inventoryPojo.getProductId()).getBrandCategory());
            brandPojoList.add(brandPojo);
        }
        return reportService.inventoryReport(brandPojoList, inventoryPojoList);
    }
}
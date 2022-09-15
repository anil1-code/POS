package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.forms.SalesReportForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(value = "/api/reports")
public class ReportController {
    @Autowired
    private ReportDto reportDto;

    @ApiOperation(value = "gives the sales report encapsulated in SalesReportData")
    @RequestMapping(value = "/sales", method = RequestMethod.POST)
    public SalesReportData salesReport(@RequestBody SalesReportForm salesReportForm) {
        return reportDto.salesReport(salesReportForm.getStartDateTime(), salesReportForm.getEndDateTime(), salesReportForm.getBrand(), salesReportForm.getCategory());
    }

    @ApiOperation(value = "gives the inventory report")
    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    public InventoryReportData inventoryReport() {
        return reportDto.inventoryReport();
    }
}

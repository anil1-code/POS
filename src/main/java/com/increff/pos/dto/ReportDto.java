package com.increff.pos.dto;

import com.increff.pos.constants.Const;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReportDto {
    @Autowired
    private ReportService reportService;


    public SalesReportData salesReport(String startDate, String endDate, String brand, String category) throws ApiException {
        if (startDate == null || startDate.isEmpty()) {
            startDate = Const.MIN_ZONED_DATE_TIME; // assign the min value possible
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = Const.MAX_ZONED_DATE_TIME; // assign the max value possible
        }
        ZonedDateTime start = ZonedDateTime.parse(startDate);
        ZonedDateTime end = ZonedDateTime.parse(endDate);
        if (start.isAfter(end)) {
            throw new ApiException("Start date can't be after end date");
        }
        if (brand == null) {
            brand = "";
        }
        if (category == null) {
            category = "";
        }
        return reportService.salesReport(start, end, brand, category);
    }

    public InventoryReportData inventoryReport() {
        return reportService.inventoryReport();
    }
}
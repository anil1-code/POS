package com.increff.pos.service;

import com.increff.pos.dao.ReportDao;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.InventoryReportSingleRowData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.data.SalesReportSingleRowData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportDao reportDao;

    public SalesReportData salesReport(List<OrderItemPojo> orderItemPojoList, List<String> categories) {
        HashMap<String, SalesReportSingleRowData> hashMap = new HashMap<>();
        int i = 0;
        for (String category : categories) {
            SalesReportSingleRowData singleRowData = hashMap.get(category);
            if (singleRowData == null) {
                singleRowData = new SalesReportSingleRowData();
                singleRowData.setCategory(category);
                singleRowData.setQuantity(orderItemPojoList.get(i).getQuantity());
                singleRowData.setRevenue(orderItemPojoList.get(i).getQuantity() * orderItemPojoList.get(i).getSellingPrice());
                hashMap.put(category, singleRowData);
            } else {
                singleRowData.setQuantity(singleRowData.getQuantity() + orderItemPojoList.get(i).getQuantity());
                singleRowData.setRevenue(singleRowData.getRevenue() + orderItemPojoList.get(i).getQuantity() * orderItemPojoList.get(i).getSellingPrice());
            }
        }
        SalesReportData salesReportData = new SalesReportData();
        salesReportData.setSalesReportSingleRowDataList(new ArrayList<>(hashMap.values()));
        return salesReportData;
    }

    public InventoryReportData inventoryReport(List<BrandPojo> brandPojoList, List<InventoryPojo> inventoryPojoList) {
        HashMap<Integer, InventoryReportSingleRowData> hashMap = new HashMap<>();
        int i = 0;
        for (BrandPojo brandPojo : brandPojoList) {
            InventoryReportSingleRowData singleRowData = hashMap.get(brandPojo.getId());
            if (singleRowData == null) {
                singleRowData = new InventoryReportSingleRowData();
                singleRowData.setBrand(brandPojo.getBrandName());
                singleRowData.setCategory(brandPojo.getCategoryName());
                singleRowData.setQuantity(inventoryPojoList.get(i).getQuantity());
                hashMap.put(brandPojo.getId(), singleRowData);
            } else {
                singleRowData.setQuantity(singleRowData.getQuantity() + inventoryPojoList.get(i).getQuantity());
            }
            i++;
        }
        List<InventoryReportSingleRowData> inventoryReportSingleRowDataList = new ArrayList<>(hashMap.values());
        InventoryReportData inventoryReportData = new InventoryReportData();
        inventoryReportData.setInventoryReportSingleRowDataList(inventoryReportSingleRowDataList);
        return inventoryReportData;
    }

}

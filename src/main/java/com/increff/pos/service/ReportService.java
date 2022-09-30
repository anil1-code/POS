package com.increff.pos.service;

import com.increff.pos.dao.ReportDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.InventoryReportSingleRowData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.data.SalesReportSingleRowData;
import com.increff.pos.pojo.*;
import com.increff.pos.util.BasicDataUtil;
import com.increff.pos.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class ReportService {
    @Autowired
    private ReportDao reportDao;
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

    @Transactional(readOnly = true)
    public SalesReportData salesReport(ZonedDateTime start, ZonedDateTime end, String brand, String category) {
        HashMap<String, SalesReportSingleRowData> hashMap = new HashMap<>();
        int i = 0;
        for (OrderPojo orderPojo : orderService.getOrdersBetweenDates(start, end)) {
            Pair<List<OrderItemPojo>, List<ProductPojo>> pairedPojos = null;
            try {
                pairedPojos = orderItemService.getByOrderId(orderPojo.getId());
            } catch (ApiException e) {
                // can never happen
            }
            i = 0;
            for (ProductPojo productPojo : pairedPojos.snd) {
                BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                if ((Objects.equals(brand, "") || Objects.equals(brand, brandPojo.getBrandName())) &&
                        (Objects.equals(category, "") || Objects.equals(category, brandPojo.getCategoryName()))) {
                    SalesReportSingleRowData singleRowData = hashMap.get(brandPojo.getCategoryName());
                    if (singleRowData == null) {
                        singleRowData = new SalesReportSingleRowData();
                        singleRowData.setCategory(brandPojo.getCategoryName());
                        singleRowData.setQuantity(pairedPojos.fst.get(i).getQuantity());
                        singleRowData.setRevenue(pairedPojos.fst.get(i).getQuantity() * pairedPojos.fst.get(i).getSellingPrice());
                        hashMap.put(brandPojo.getCategoryName(), singleRowData);
                    } else {
                        singleRowData.setQuantity(singleRowData.getQuantity() + pairedPojos.fst.get(i).getQuantity());
                        singleRowData.setRevenue(singleRowData.getRevenue() + pairedPojos.fst.get(i).getQuantity() * pairedPojos.fst.get(i).getSellingPrice());
                    }
                }
                i++;
            }
        }
        List<SalesReportSingleRowData> salesReportSingleRowDataList = new ArrayList<>();
        for (HashMap.Entry<String, SalesReportSingleRowData> entry : hashMap.entrySet()) {
            entry.getValue().setRevenue(BasicDataUtil.roundOffDouble(entry.getValue().getRevenue()));
            salesReportSingleRowDataList.add(entry.getValue());
        }
        SalesReportData salesReportData = new SalesReportData();
        salesReportData.setSalesReportSingleRowDataList(salesReportSingleRowDataList);
        return salesReportData;
    }

    public InventoryReportData inventoryReport() {
        Pair<List<InventoryPojo>, List<ProductPojo>> pairedPojoList = inventoryService.getAll();
        int i = 0;
        HashMap<Integer, InventoryReportSingleRowData> hashMap = new HashMap<>();
        for (InventoryPojo inventoryPojo : pairedPojoList.fst) {
            BrandPojo brandPojo = brandService.getById(pairedPojoList.snd.get(i++).getBrandCategory());
            InventoryReportSingleRowData singleRowData = hashMap.get(brandPojo.getId());
            if (singleRowData == null) {
                singleRowData = new InventoryReportSingleRowData();
                singleRowData.setBrand(brandPojo.getBrandName());
                singleRowData.setCategory(brandPojo.getCategoryName());
                singleRowData.setQuantity(inventoryPojo.getQuantity());
                hashMap.put(brandPojo.getId(), singleRowData);
            } else {
                singleRowData.setQuantity(singleRowData.getQuantity() + inventoryPojo.getQuantity());
            }
        }
        List<InventoryReportSingleRowData> inventoryReportSingleRowDataList = new ArrayList<>(hashMap.values());
        InventoryReportData inventoryReportData = new InventoryReportData();
        inventoryReportData.setInventoryReportSingleRowDataList(inventoryReportSingleRowDataList);
        return inventoryReportData;
    }

}

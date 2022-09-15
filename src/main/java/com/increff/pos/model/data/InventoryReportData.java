package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InventoryReportData {
    private List<InventoryReportSingleRowData> inventoryReportSingleRowDataList;
}

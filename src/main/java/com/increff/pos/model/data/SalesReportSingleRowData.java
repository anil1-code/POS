package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportSingleRowData {
    private String category;
    private Integer quantity;
    private Double revenue;
}

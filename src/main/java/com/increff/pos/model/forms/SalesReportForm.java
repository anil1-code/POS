package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SalesReportForm {
    private String startDateTime;
    private String endDateTime;
    private String brand;
    private String category;
}

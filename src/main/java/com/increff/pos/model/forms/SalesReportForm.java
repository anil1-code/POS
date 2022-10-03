package com.increff.pos.model.forms;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SalesReportForm {
    @NotNull
    private String startDateTime;
    @NotNull
    private String endDateTime;
    @Nullable
    private String brand;
    @Nullable
    private String category;
}

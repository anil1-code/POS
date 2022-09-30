package com.increff.pos.model.data;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderData {
    @NotNull
    private Integer id;
    @NotNull
    private String dateTime; // empty string means order is unplaced
    @NotNull
    private List<OrderItemData> orderItemDataList;
}

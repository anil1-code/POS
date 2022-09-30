package com.increff.pos.model.forms;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class    OrderItemForm {
    @NotNull
    private Integer orderId;
    @NotNull
    private Integer productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double sellingPrice; // per piece
}

package com.increff.pos.model.data;

import com.increff.pos.model.forms.OrderItemForm;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm {
    @NotNull
    private Integer orderItemId;
    @NotNull
    private String productName;
    @NotNull
    private String barcode;
}

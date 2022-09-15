package com.increff.pos.model.forms;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    @NotNull
    private String barcode;
    private Integer brandCategory;
    @NotNull
    private String name;
    private Double mrp;
}

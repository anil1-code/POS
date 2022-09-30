package com.increff.pos.model.forms;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    @NotNull
    private String barcode;
    @NotNull
    private Integer brandCategory;
    @NotNull
    private String name;
    @NotNull
    private Double mrp;
}

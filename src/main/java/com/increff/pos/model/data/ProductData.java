package com.increff.pos.model.data;

import com.increff.pos.model.forms.ProductForm;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductData extends ProductForm {
    @NotNull
    private Integer id;
    @NotNull
    private String brandName;
    @NotNull
    private String categoryName;
}

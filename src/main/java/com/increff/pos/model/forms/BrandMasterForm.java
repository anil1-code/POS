package com.increff.pos.model.forms;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandMasterForm {
    @NotNull
    private String brandName;
    @NotNull
    private String categoryName;

}

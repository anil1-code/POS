package com.increff.pos.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class InventoryPojo {
    @Id
    private Integer productId;
    @NotNull
    private Integer quantity;
}

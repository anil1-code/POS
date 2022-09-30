package com.increff.pos.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class OrderItemPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Integer orderId;
    @NotNull
    private Integer productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double sellingPrice; // per piece
}

package com.increff.pos.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"barcode"})})
public class ProductPojo {
    /*
     * a product can't be inserted in the database if its brand-category combination is not already there
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String barcode;
    @NotNull
    private Integer brandCategory;
    @NotNull
    private String name;
    @NotNull
    private Double mrp;
}

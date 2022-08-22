package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class ProductMasterPojo {
    /*
    * a product can't be inserted in the database if its brand-category combination is not already there
    * */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String barcode;
    private int brandCategory;
    private String name;
    private double mrp;

}

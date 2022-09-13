package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
//@Table(name = "brand_category", indexes = @Index(columnList = "brand,category", unique = true))
@Table(uniqueConstraints = {@UniqueConstraint(name = "brand_cat_combo", columnNames = {"brandName","categoryName"})})
public class BrandPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String brandName;
    private String categoryName;
}

package com.increff.pos.pojo;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;


@Getter
@Setter
@Entity
public class OrderPojo {
    /*
     * this represents a single order(may contain multiple OrderItems) placed by a user at the given point of time
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nullable
    private ZonedDateTime zonedDateTime; // this will store the date of placement of this order, if the order is unplaced this will be null
}

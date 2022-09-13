package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
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
    private int id;
    private ZonedDateTime zonedDateTime;
}

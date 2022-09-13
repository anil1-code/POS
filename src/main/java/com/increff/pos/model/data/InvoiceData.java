package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class InvoiceData {
    private Integer orderId;
    private Double total;
    private String orderTime;
    private List<OrderItemData> orderItems;
    private String invoiceTime;

    public InvoiceData() {

    }

    public InvoiceData(List<OrderItemData> orderItems, ZonedDateTime orderTime, Double total, Integer orderId) {
        for(OrderItemData orderItemData : orderItems) {
            System.out.println(orderItemData);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss z");
        this.invoiceTime = ZonedDateTime.now().format(formatter);
        this.orderItems = orderItems;
        this.orderTime = orderTime.format(formatter);
        this.total = total;
        this.orderId = orderId;
    }
}

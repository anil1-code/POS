package com.increff.pos.model.data;

import com.sun.istack.NotNull;
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
    @NotNull
    private Integer orderId;
    @NotNull
    private Double total;
    @NotNull
    private String orderTime;
    @NotNull
    private List<OrderItemData> orderItems;
    @NotNull
    private String invoiceTime;

    public InvoiceData() {

    }

    public InvoiceData(List<OrderItemData> orderItems, ZonedDateTime orderTime, Double total, Integer orderId) {
        for (OrderItemData orderItemData : orderItems) {
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss z");
        this.invoiceTime = ZonedDateTime.now().format(formatter);
        this.orderItems = orderItems;
        this.orderTime = orderTime.format(formatter);
        this.total = total;
        this.orderId = orderId;
    }
}

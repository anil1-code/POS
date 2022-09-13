package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {
    private static final String selectAll = "select p from OrderPojo p";
    private static final String selectById = "select p from OrderPojo p where id=:id";


    public OrderPojo add(OrderPojo orderPojo) {
        em().persist(orderPojo);
        return orderPojo;
    }

    public List<OrderPojo> getAll() {
        TypedQuery<OrderPojo> typedQuery = getQuery(selectAll, OrderPojo.class);
        return typedQuery.getResultList();
    }

    public OrderPojo getById(int orderId) {
        TypedQuery<OrderPojo> typedQuery = getQuery(selectById, OrderPojo.class);
        typedQuery.setParameter("id", orderId);
        return getSingle(typedQuery);
    }
}

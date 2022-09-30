package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {
    private static final String selectAll = "select p from OrderPojo p";
    private static final String selectById = "select p from OrderPojo p where id=:id";
    private static final String selectBetweenDates = "select p from OrderPojo p where zonedDateTime BETWEEN :startDate AND :endDate";

    private static final String deleteById = "delete from OrderPojo p where id=:id";


    public OrderPojo add(OrderPojo orderPojo) {
        em().persist(orderPojo);
        return orderPojo;
    }

    public List<OrderPojo> getAll() {
        TypedQuery<OrderPojo> typedQuery = getQuery(selectAll, OrderPojo.class);
        return typedQuery.getResultList();
    }

    public void delete(int id) {
        Query query = em().createQuery(deleteById);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public OrderPojo getById(int orderId) {
        TypedQuery<OrderPojo> typedQuery = getQuery(selectById, OrderPojo.class);
        typedQuery.setParameter("id", orderId);
        return getSingle(typedQuery);
    }

    public List<OrderPojo> getOrdersBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate) {
        TypedQuery<OrderPojo> typedQuery = getQuery(selectBetweenDates, OrderPojo.class);
        typedQuery.setParameter("startDate", startDate);
        typedQuery.setParameter("endDate", endDate);

        return typedQuery.getResultList();
    }

}

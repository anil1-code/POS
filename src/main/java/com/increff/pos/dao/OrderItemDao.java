package com.increff.pos.dao;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {
    private static final String deleteById = "delete from OrderItemPojo p where id=:id";
    private static final String selectById = "select p from OrderItemPojo p where id=:id";
    private static final String selectByOrderId = "select p from OrderItemPojo p where orderId=:orderId";
    private static final String selectAll = "select p from OrderItemPojo p";

    @Transactional(rollbackFor = ApiException.class)
    public OrderItemPojo add(OrderItemPojo orderItemPojo) {
        em().persist(orderItemPojo);
        return orderItemPojo;
    }

    public List<OrderItemPojo> getAll() {
        TypedQuery<OrderItemPojo> typedQuery = getQuery(selectAll, OrderItemPojo.class);
        return typedQuery.getResultList();
    }

    public OrderItemPojo getByOrderItemId(int id) {
        TypedQuery<OrderItemPojo> typedQuery = getQuery(selectById, OrderItemPojo.class);
        typedQuery.setParameter("id", id);
        return getSingle(typedQuery);
    }

    public List<OrderItemPojo> getByOrderId(int orderId) {
        TypedQuery<OrderItemPojo> typedQuery = getQuery(selectByOrderId, OrderItemPojo.class);
        typedQuery.setParameter("orderId", orderId);
        return typedQuery.getResultList();
    }

    public OrderItemPojo update(OrderItemPojo orderItemPojo) {
        em().merge(orderItemPojo);
        return orderItemPojo;
    }

    public void delete(int id) {
        Query query = em().createQuery(deleteById);
        query.setParameter("id", id);
        query.executeUpdate();
    }
}

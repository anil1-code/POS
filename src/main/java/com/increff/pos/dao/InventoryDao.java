package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao {
    private static final String deleteById = "delete from InventoryPojo p where productId=:productId";
    private static final String selectById = "select p from InventoryPojo p where productId=:productId";
    private static final String selectAll = "select p from InventoryPojo p";
    public List<InventoryPojo> getAll() {
        TypedQuery<InventoryPojo> query = getQuery(selectAll, InventoryPojo.class);
        return query.getResultList();
    }

    public void delete(int productId) {
        Query query = em().createQuery(deleteById);
        query.setParameter("productId", productId);
        query.executeUpdate();
    }

    public InventoryPojo update(InventoryPojo inventoryPojo) {
        em().merge(inventoryPojo);
        return inventoryPojo;
    }

    public InventoryPojo add(InventoryPojo inventoryPojo) {
        em().persist(inventoryPojo);
        return inventoryPojo;
    }

    public InventoryPojo getByProductId(int productId) {
        TypedQuery<InventoryPojo> query = getQuery(selectById, InventoryPojo.class);
        query.setParameter("productId", productId);
        return getSingle(query);
    }

}

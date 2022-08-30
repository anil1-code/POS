package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    private static final String deleteById = "delete from ProductPojo p where id=:id";
    private static final String selectById = "select p from ProductPojo p where id=:id";
    private static final String selectByBarcode = "select p from ProductPojo p where barcode=:barcode";
    private static final String selectAll = "select p from ProductPojo p";

    public List<ProductPojo> getAll() {
        TypedQuery<ProductPojo> query = getQuery(selectAll, ProductPojo.class);
        return query.getResultList();
    }

    public void delete(int id) {
        Query query = em().createQuery(deleteById);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public ProductPojo update(ProductPojo productPojo) {
        em().merge(productPojo);
        return productPojo;
    }

    public ProductPojo add(ProductPojo productPojo) {
        em().persist(productPojo);
        return productPojo;
    }

    public ProductPojo getById(int id) {
        TypedQuery<ProductPojo> query = getQuery(selectById, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public ProductPojo getByBarcode(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(selectByBarcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }
}

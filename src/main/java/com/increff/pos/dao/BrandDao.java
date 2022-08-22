package com.increff.pos.dao;

import com.increff.pos.pojo.BrandMasterPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandDao {
    private static final String deleteById = "delete from BrandMasterPojo p where id=:id";
    private static final String selectById = "select p from BrandMasterPojo p where id=:id";
    private static final String selectByBrandCategory = "select p from BrandMasterPojo p where brandName=:brandName and categoryName=:categoryName";
    private static final String selectAll = "select p from BrandMasterPojo p";

    @PersistenceContext
    public EntityManager em;

    TypedQuery<BrandMasterPojo> getTypedQuery(String jpql) {
        return em.createQuery(jpql, BrandMasterPojo.class);
    }
    Query getQuery(String jpql) {
        return em.createQuery(jpql);
    }
    public List<BrandMasterPojo> getAll() {
        TypedQuery<BrandMasterPojo> query = getTypedQuery(selectAll);
        return query.getResultList();
    }
    public BrandMasterPojo getByBrandCategory(String brand, String category) {
        TypedQuery<BrandMasterPojo> query = getTypedQuery(selectByBrandCategory);
        query.setParameter("brandName", brand);
        query.setParameter("categoryName", category);
        return query.getSingleResult();
    }
    public BrandMasterPojo getById(int id) {
        TypedQuery<BrandMasterPojo> query = getTypedQuery(selectById);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
    public BrandMasterPojo add(BrandMasterPojo brandMasterPojo) {
        em.persist(brandMasterPojo);
        return brandMasterPojo;
    }

    public void delete(int id) {
        Query query = getQuery(deleteById);
        query.setParameter("id", id);
        query.executeUpdate();
    }
    public BrandMasterPojo update(BrandMasterPojo brandMasterPojo) {
        em.merge(brandMasterPojo);
        return brandMasterPojo;
    }

}

package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {
    private static final String deleteById = "delete from BrandPojo p where id=:id";
    private static final String selectById = "select p from BrandPojo p where id=:id";
    private static final String selectByBrandCategory = "select p from BrandPojo p where brandName=:brandName and categoryName=:categoryName";
    private static final String selectAll = "select p from BrandPojo p";

    public List<BrandPojo> getAll() {
        TypedQuery<BrandPojo> query = getQuery(selectAll, BrandPojo.class);
        return query.getResultList();
    }

    public BrandPojo getByBrandCategory(String brand, String category) {
        TypedQuery<BrandPojo> query = getQuery(selectByBrandCategory, BrandPojo.class);
        query.setParameter("brandName", brand);
        query.setParameter("categoryName", category);
        return getSingle(query);
    }

    public BrandPojo getById(int id) {
        TypedQuery<BrandPojo> query = getQuery(selectById, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public BrandPojo add(BrandPojo brandPojo) {
        em().persist(brandPojo);
        return brandPojo;
    }

    public void delete(int id) {
        Query query = em().createQuery(deleteById);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public BrandPojo update(BrandPojo brandPojo) {
        em().merge(brandPojo);
        return brandPojo;
    }

}

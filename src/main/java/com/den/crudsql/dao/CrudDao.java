package com.den.crudsql.dao;

import java.util.List;

public interface CrudDao<K, V> {
    K create(V entity);
    V findById(K id);
    boolean update(V entity);
    boolean delete(K id);
    List<V> findAll();
}

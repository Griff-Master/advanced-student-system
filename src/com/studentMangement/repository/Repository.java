package com.studentMangement.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity);
    List<T> saveAll(Collection<T> entities);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean deleteById(ID id);
    boolean existsById(ID id);
    long count();
}

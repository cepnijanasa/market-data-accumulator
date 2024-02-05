package com.whatever.accumulator.repository;


import java.util.Collection;
import java.util.Optional;

public interface CommonRepository<T, ID> {

    T add(T object);

    void remove(ID id);

    Optional<T> get(ID id);

    Collection<T> getAll();
}

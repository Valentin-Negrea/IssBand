package com.example.issband.Repository;

import com.example.issband.Domain.Entity;
import com.example.issband.DuplicateObjectException;
import com.example.issband.RepositoryException;

import java.util.Collection;

public interface IRepository<T extends Entity> extends Iterable<T> {
    public void add(T o) throws RepositoryException;
    public void remove(int id);
    public T find(int id);

    public Collection<T> getAll();
}

package com.example.issband.Repository;

import com.example.issband.Domain.Entity;
import com.example.issband.DuplicateObjectException;
import com.example.issband.RepositoryException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class MemoryRepository<T extends Entity> extends AbstractRepository<T> {
    @Override
    public void add(T o) throws RepositoryException {
        if (o == null) {
            // este RuntimeException
            throw new IllegalArgumentException();
        }
        if (find(o.getId()) != null) {
            // checked Exception
            throw new DuplicateObjectException("Cannot duplicate repository objects!");
        }

        this.data.add(o);
    }

    @Override
    public void remove(int id) {
        T entity = find(id);
        if (entity != null)
            data.remove(entity);
        else {
            throw new NoSuchElementException("Nu exista element cu id ul acesta");
        }
    }


    @Override
    public T find(int id) {
        for (T o : data) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    @Override
    public Collection<T> getAll() {
        // returnam o copie
        return new ArrayList<>(data);
    }
}

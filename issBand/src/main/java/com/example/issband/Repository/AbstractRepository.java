package com.example.issband.Repository;

import com.example.issband.Domain.Entity;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractRepository<T extends Entity> implements IRepository<T> {
    protected ArrayList<T> data = new ArrayList<>();

    @Override
    public Iterator<T> iterator() {
        // returnam un iterator pe un shallow copy :) al campului data
        return new ArrayList<T>(data).iterator();
//        return data.iterator();
    }
}

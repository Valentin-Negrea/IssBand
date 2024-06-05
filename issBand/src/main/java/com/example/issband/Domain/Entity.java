package com.example.issband.Domain;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 1000L;

    private int id;

    public Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

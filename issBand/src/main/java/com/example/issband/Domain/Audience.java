package com.example.issband.Domain;

public class Audience extends Entity{
    private String name;
    public Audience(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Audience{" +
                "name='" + name + '\'' +
                '}';
    }
}

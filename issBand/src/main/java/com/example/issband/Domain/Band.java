package com.example.issband.Domain;

public class Band extends Entity {
    private String name;
    private Manager manager;

    public Band(int id, String name, Manager manager) {
        super(id);
        this.name = name;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Band{" +
                "name='" + name + '\'' +
                ", manager=" + manager +
                '}';
    }
}

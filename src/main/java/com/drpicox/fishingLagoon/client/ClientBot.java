package com.drpicox.fishingLagoon.client;

public class ClientBot {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ClientBot{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

package com.drpicox.fishingLagoon.client;

public class ClientLagoonDescriptor {
    private long fishPopulation;

    public long getFishPopulation() {
        return fishPopulation;
    }

    @Override
    public String toString() {
        return "ClientLagoonDescriptor{" +
                "fishPopulation=" + fishPopulation +
                '}';
    }
}

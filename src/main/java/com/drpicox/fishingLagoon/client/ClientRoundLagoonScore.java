package com.drpicox.fishingLagoon.client;

public class ClientRoundLagoonScore {
    private long fishPopulation;

    public long getFishPopulation() {
        return fishPopulation;
    }

    @Override
    public String toString() {
        return "ClientRoundLagoonScore{" +
                "fishPopulation=" + fishPopulation +
                '}';
    }
}

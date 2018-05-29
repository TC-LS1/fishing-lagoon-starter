package com.drpicox.fishingLagoon.client;

import java.util.ArrayList;
import java.util.List;

public class ClientRoundDescriptor {

    private int weekCount;
    private double maxDensity;
    private long scoreMilliseconds;
    private long commandMilliseconds;
    private long seatMilliseconds;
    private List<ClientLagoonDescriptor> lagoons;

    public int getWeekCount() {
        return weekCount;
    }

    public double getMaxDensity() {
        return maxDensity;
    }

    public long getScoreMilliseconds() {
        return scoreMilliseconds;
    }

    public long getCommandMilliseconds() {
        return commandMilliseconds;
    }

    public long getSeatMilliseconds() {
        return seatMilliseconds;
    }

    public List<ClientLagoonDescriptor> getLagoons() {
        return new ArrayList<>(lagoons);
    }

    public ClientLagoonDescriptor getLagoon(int lagoonIndex) {
        return lagoons.get(lagoonIndex % lagoons.size());
    }

    @Override
    public String toString() {
        return "ClientRoundDescriptor{" +
                "weekCount=" + weekCount +
                ", maxDensity=" + maxDensity +
                ", scoreMilliseconds=" + scoreMilliseconds +
                ", commandMilliseconds=" + commandMilliseconds +
                ", seatMilliseconds=" + seatMilliseconds +
                ", lagoons=" + lagoons +
                '}';
    }
}

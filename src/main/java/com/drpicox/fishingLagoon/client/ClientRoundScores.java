package com.drpicox.fishingLagoon.client;

import java.util.List;
import java.util.Map;

public class ClientRoundScores {

    private List<ClientRoundLagoonScore> lagoons;
    private Map<String, ClientRoundBotScore> bots;

    public ClientRoundLagoonScore getLagoonScore(int lagoonIndex) {
        return lagoons == null ? null : lagoons.get(lagoonIndex);
    }

    public ClientRoundBotScore getBotScore(String botId) {
        return bots == null ? null : bots.get(botId);
    }

    @Override
    public String toString() {
        return "ClientRoundScores{" +
                "lagoons=" + lagoons +
                ", bots=" + bots +
                '}';
    }
}

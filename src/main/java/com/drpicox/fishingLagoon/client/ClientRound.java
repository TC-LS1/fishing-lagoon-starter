package com.drpicox.fishingLagoon.client;

import com.drpicox.fishingLagoon.actions.ActionParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientRound {

    private String id;
    private long startTs;
    private long endTs;
    private String state;
    private ClientRoundDescriptor descriptor;
    private Map<String, ClientRoundSeat> seats;
    private Map<String, ClientRoundCommand> commands;
    private ClientRoundScores scores;

    public String getId() {
        return id;
    }

    public long getStartTs() {
        return startTs;
    }

    public long getEndTs() {
        return endTs;
    }

    public String getState() {
        return state;
    }

    public ClientRoundDescriptor getDescriptor() {
        return descriptor;
    }

    public ClientRoundSeat getBotSeat(String botId) {
        return seats == null ? null : seats.get(botId);
    }

    public ClientRoundCommand getBotCommand(String botId) {
        return commands == null ? null : commands.get(botId);
    }

    public ClientRoundLagoonScore getLagoonScore(int lagoonIndex) {
        return scores == null ? null : scores.getLagoonScore(lagoonIndex);
    }

    public ClientRoundBotScore getBotScore(String botId) {
        return scores == null ? null : scores.getBotScore(botId);
    }

    public int getAvailableLagoonCount(String myBotId) {
        if (seats == null) return getAvailableLagoonCount(false);
        return getAvailableLagoonCount(seats.containsKey(myBotId));
    }

    public int getAvailableLagoonCount(boolean isSeated) {
        if (descriptor == null) return 1;

        var botCount = seats.size();
        var plusOne = isSeated ? 0 : 1;
        var maxDensity = descriptor.getMaxDensity();
        var result = (int)Math.ceil((botCount + plusOne) / maxDensity);
        return result;
    }

    public List<ClientLagoonDescriptor> getAvailableLagoons(String myBotId) {
        if (seats == null) return getAvailableLagoons(false);
        return getAvailableLagoons(seats.containsKey(myBotId));
    }

    public List<ClientLagoonDescriptor> getAvailableLagoons(boolean isSeated) {
        if (descriptor == null) return new ArrayList<>(descriptor.getLagoons().subList(0, 1));

        var result = new ArrayList<ClientLagoonDescriptor>();
        int lagoonCount = getAvailableLagoonCount(isSeated);
        for (int lagoonIndex = 0; lagoonIndex < lagoonCount; lagoonIndex++) {
            result.add(descriptor.getLagoons().get(lagoonIndex));
        }
        return result;
    }

    @Override
    public String toString() {
        return "ClientRound{" +
                "id='" + id + '\'' +
                ", startTs=" + startTs +
                ", endTs=" + endTs +
                ", state='" + state + '\'' +
                ", descriptor=" + descriptor +
                ", seats=" + seats +
                ", commands=" + commands +
                ", scores=" + scores +
                '}';
    }

    void parseActions(ActionParser actionParser) {
        if (commands == null) return;
        for (var command: commands.values()) {
            command.parseActions(actionParser);
        }
    }
}

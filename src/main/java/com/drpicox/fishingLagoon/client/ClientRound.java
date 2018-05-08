package com.drpicox.fishingLagoon.client;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.actions.ActionParser;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;

import java.util.*;

public class ClientRound {

    private String id;
    private long startTs;
    private long endTs;
    private long nowTs;
    private String state;
    private String selfId;
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

    public String getSelfId() {
        return selfId;
    }

    public ClientRound withSelfId(String selfId) {
        this.selfId = selfId;
        return this;
    }

    public long getNowTs() {
        return nowTs;
    }

    public long getMillisecondsForEndSeat() {
        if (descriptor == null) return -1;
        var endSeatTs = startTs + descriptor.getSeatMilliseconds();

        return endSeatTs - nowTs;
    }

    public long getMillisecondsForEndCommand() {
        if (descriptor == null) return -1;
        var endCommandTs = startTs + descriptor.getCommandMilliseconds() + descriptor.getCommandMilliseconds();

        return endCommandTs - nowTs;
    }

    public long getMillisecondsForEnd() {
        return endTs - nowTs;
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

    public int getAvailableLagoonCount() {
        return getAvailableLagoonCount(selfId);
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

    public List<ClientLagoonDescriptor> getAvailableLagoons() {
        return getAvailableLagoons(selfId);
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

    public LagoonRound getLagoonRound() {
        var round = new LagoonRound(descriptor.getWeekCount());

        for (var clientLagoon: getAvailableLagoons()) {
            var lagoon = new Lagoon(clientLagoon.getFishPopulation());
            round = round.addLagoons(lagoon);
        }

        if (seats != null) {
            for (var entry: seats.entrySet()) {
                var bot = new BotId(entry.getKey());
                var seat = entry.getValue();
                round = round
                        .addCompetitors(bot)
                        .seatBotAt(bot, seat.getLagoonIndex());
            }
        }

        if (commands != null) {
            for (var entry: commands.entrySet()) {
                var bot = new BotId(entry.getKey());
                var command = entry.getValue();
                var actions = command.getActions();
                round = round.putBotActions(bot, actions.toArray(new Action[actions.size()]));
            }
        }

        return round;
    }

    @Override
    public String toString() {
        return "ClientRound{" +
                "id='" + id + '\'' +
                ",\n startTs=" + startTs +
                ",\n endTs=" + endTs +
                ",\n state='" + state + '\'' +
                ",\n selfId='" + selfId + '\'' +
                ",\n descriptor=" + descriptor +
                ",\n seats=" + seats +
                ",\n commands=" + commands +
                ",\n scores=" + scores +
                '}';
    }

    void parseActions(ActionParser actionParser) {
        if (commands == null) return;
        for (var command: commands.values()) {
            command.parseActions(actionParser);
        }
    }
}

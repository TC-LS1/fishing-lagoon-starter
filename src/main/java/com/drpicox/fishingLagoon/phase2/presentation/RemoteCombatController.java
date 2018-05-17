package com.drpicox.fishingLagoon.phase2.presentation;

import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.client.ClientRound;
import com.drpicox.fishingLagoon.client.FishingLagoonClient;
import com.drpicox.fishingLagoon.strategy.Strategy;

import static java.util.Arrays.asList;

public class RemoteCombatController implements Runnable {

    private Strategy strategy;
    private FishingLagoonClient remoteServer;
    private BotId botId;

    public RemoteCombatController(Strategy strategy, FishingLagoonClient remoteServer) {
        this.strategy = strategy;
        this.remoteServer = remoteServer;
    }

    public void run() {
        botId = fetchBotIdFromRemoteServer();
        try {
            while (true) {
                combatRound();
                Thread.sleep(10000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BotId fetchBotIdFromRemoteServer() {
        return new BotId(remoteServer.getBot().getId());
    }

    private void combatRound() throws InterruptedException {
        var roundId = getActiveRoundOrCreate().getId();

        var roundValueWithStrategySeated = strategySeatsAtRemoteRound(roundId);
        Thread.sleep(roundValueWithStrategySeated.getMillisecondsForEndSeat());

        var roundValueAfterCommand = strategySendsCommandToRemoteRound(roundId);
        Thread.sleep(roundValueAfterCommand.getMillisecondsForEndCommand());

        var roundValueWithAllScoresCommandsAndSeats = strategyLearnsFromRemoteRound(roundId);
        System.out.println(roundValueWithAllScoresCommandsAndSeats);
        Thread.sleep(roundValueWithAllScoresCommandsAndSeats.getMillisecondsForEnd());
    }

    private ClientRound getActiveRoundOrCreate() {
        var activeRounds = remoteServer.listActiveRounds();
        if (!activeRounds.isEmpty()) {
            return activeRounds.get(0).withSelfId(botId.getValue());
        }

        return remoteServer.createRound(String.join("\n", "",
            "maxDensity=3.0",
            "weekCount=4",
            "lagoons=lagoonSmall,lagoonBig",
            "lagoonSmall.fishPopulation=10",
            "lagoonBig.fishPopulation=11"
        ));
    }

    private ClientRound strategySeatsAtRemoteRound(String roundId) throws InterruptedException {
        System.out.println("SEATING");
        var roundValueWithSomeStrategiesSeatedUntilNow = remoteServer.getRound(roundId);

        var lagoonIndex = strategy.seat(botId, roundValueWithSomeStrategiesSeatedUntilNow.getLagoonRound());
        var roundValueWithStrategySeated = remoteServer.seat(roundId, lagoonIndex);

        return roundValueWithStrategySeated;
    }

    private ClientRound strategySendsCommandToRemoteRound(String roundId) {
        System.out.println("COMMANDING");
        var roundValueWithAllStrategiesSeated = remoteServer.getRound(roundId);

        var actions = strategy.getOrders(botId, roundValueWithAllStrategiesSeated.getLagoonRound());
        var roundValueAfterCommand = remoteServer.command(roundValueWithAllStrategiesSeated.getId(), asList(actions));

        return roundValueAfterCommand;
    }

    private ClientRound strategyLearnsFromRemoteRound(String roundId) {
        System.out.println("SCORING");
        var roundValueWithAllScoresCommandsAndSeats = remoteServer.getRound(roundId);
        strategy.learnFromRound(botId, roundValueWithAllScoresCommandsAndSeats.getLagoonRound());
        return roundValueWithAllScoresCommandsAndSeats;
    }

}

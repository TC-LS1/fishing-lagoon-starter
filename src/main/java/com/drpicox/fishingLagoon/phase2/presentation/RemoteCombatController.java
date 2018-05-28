package com.drpicox.fishingLagoon.phase2.presentation;

import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.client.ClientRound;
import com.drpicox.fishingLagoon.client.FishingLagoonClient;
import com.drpicox.fishingLagoon.strategy.Strategy;

import static java.util.Arrays.asList;

public class RemoteCombatController implements Runnable {

    private static final long SECOND = 1000L;

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
                makeStrategyCombatIntoRemoteRound();
                waitForBeforeNewRound();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForBeforeNewRound() throws InterruptedException {
        Thread.sleep(1L * SECOND);
    }

    private BotId fetchBotIdFromRemoteServer() {
        return new BotId(remoteServer.getBot().getId());
    }

    private void makeStrategyCombatIntoRemoteRound() throws InterruptedException {
        var roundId = getActiveRoundOrCreate().getId();

        var roundValueSeated = seatStrategyIntoRemoteRound(roundId);
        waitForRemoteRoundSeatPhaseEnd(roundValueSeated);

        var roundValueAfterCommand = sendStrategyCommandToRemoteRound(roundId);
        waitForRemoteRoundCommandPhaseEnd(roundValueAfterCommand);

        var roundFinalValue = makeStrategyLearnFromRemoteRound(roundId);
        System.out.println(roundFinalValue);
        waitForRemoteRoundEnd(roundFinalValue);
    }

    private ClientRound getActiveRoundOrCreate() {
        var activeRounds = remoteServer.listActiveRounds();
        if (!activeRounds.isEmpty()) {
            return activeRounds.get(0).withSelfId(botId.getValue());
        }

        return remoteServer.createRound(String.join("\n", "",
            "maxDensity=3.0",
            "weekCount=4",
            "seatMilliseconds=5000",
            "commandMilliseconds=5000",
            "scoreMilliseconds=5000",
            "lagoons=lagoonSmall,lagoonBig",
            "lagoonSmall.fishPopulation=10",
            "lagoonBig.fishPopulation=11"
        ));
    }

    private ClientRound seatStrategyIntoRemoteRound(String roundId) throws InterruptedException {
        System.out.println("SEATING");
        var roundValueWithSomeSeats = remoteServer.getRound(roundId);

        var lagoonIndex = strategy.seat(botId, roundValueWithSomeSeats.getLagoonRound());
        var roundValueSeated = remoteServer.seat(roundId, lagoonIndex);

        return roundValueSeated;
    }

    private ClientRound sendStrategyCommandToRemoteRound(String roundId) {
        System.out.println("COMMANDING");
        var roundValueWithAllSeats = remoteServer.getRound(roundId);

        var actions = strategy.getOrders(botId, roundValueWithAllSeats.getLagoonRound());
        var roundValueAfterCommand = remoteServer.command(roundValueWithAllSeats.getId(), asList(actions));

        return roundValueAfterCommand;
    }

    private ClientRound makeStrategyLearnFromRemoteRound(String roundId) {
        System.out.println("SCORING");
        var roundFinalValue = remoteServer.getRound(roundId);

        strategy.learnFromRound(botId, roundFinalValue.getLagoonRound());

        return roundFinalValue;
    }

    private void waitForRemoteRoundSeatPhaseEnd(ClientRound roundValue) throws InterruptedException {
        Thread.sleep(roundValue.getMillisecondsForEndSeat());
    }

    private void waitForRemoteRoundCommandPhaseEnd(ClientRound roundValue) throws InterruptedException {
        Thread.sleep(roundValue.getMillisecondsForEndCommand());
    }

    private void waitForRemoteRoundEnd(ClientRound roundValue) throws InterruptedException {
        Thread.sleep(roundValue.getMillisecondsForEnd());
    }

}

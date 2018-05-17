package com.drpicox.fishingLagoon.phase2.presentation;

import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.client.ClientRound;
import com.drpicox.fishingLagoon.client.FishingLagoonClient;
import com.drpicox.fishingLagoon.strategy.Strategy;

import static java.util.Arrays.asList;

public class CombatController implements Runnable {

    private Strategy strategy;
    private FishingLagoonClient client;
    private BotId botId;

    public CombatController(Strategy strategy, FishingLagoonClient client) {
        this.strategy = strategy;
        this.client = client;
    }

    public void run() {
        fetchBotId();
        try {
            while (true) {
                combatRound();
                Thread.sleep(10000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fetchBotId() {
        botId = new BotId(client.getBot().getId());
    }

    private void combatRound() throws InterruptedException {
        var roundId = getActiveRoundOrCreate().getId();

        var round = seat(roundId);
        Thread.sleep(round.getMillisecondsForEndSeat());

        round = command(roundId);
        Thread.sleep(round.getMillisecondsForEndCommand());

        round = learn(roundId);
        System.out.println(round);
        Thread.sleep(round.getMillisecondsForEnd());
    }

    private ClientRound getActiveRoundOrCreate() {
        var activeRounds = client.listActiveRounds();
        if (!activeRounds.isEmpty()) {
            return activeRounds.get(0).withSelfId(botId.getValue());
        }

        return client.createRound(String.join("\n", "",
            "maxDensity=3.0",
            "weekCount=4",
            "lagoons=lagoonSmall,lagoonBig",
            "lagoonSmall.fishPopulation=10",
            "lagoonBig.fishPopulation=11"
        ));
    }

    private ClientRound seat(String roundId) throws InterruptedException {
        System.out.println("SEATING");
        var round = client.getRound(roundId);

        var lagoonIndex = strategy.seat(botId, round.getLagoonRound());
        round = client.seat(round.getId(), lagoonIndex);

        return round;
    }

    private ClientRound command(String roundId) {
        System.out.println("COMMANDING");
        var round = client.getRound(roundId);

        var actions = strategy.getOrders(botId, round.getLagoonRound());
        round = client.command(round.getId(), asList(actions));

        return round;
    }

    private ClientRound learn(String roundId) {
        System.out.println("SCORING");
        var round = client.getRound(roundId);
        strategy.learnFromRound(botId, round.getLagoonRound());
        return round;
    }

}

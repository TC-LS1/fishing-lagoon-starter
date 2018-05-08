package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.actions.ActionParser;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.client.ClientBot;
import com.drpicox.fishingLagoon.client.ClientRound;
import com.drpicox.fishingLagoon.client.FishingLagoonClient;
import com.drpicox.fishingLagoon.strategy.Strategy;
import com.drpicox.fishingLagoon.strategy.drpicox.TitForTatStrategy;

import java.io.IOException;
import java.util.List;

import static com.drpicox.fishingLagoon.actions.Actions.fish;
import static java.util.Arrays.asList;

public class Phase2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        var serverUrl = System.getenv("FISHING_LAGOON_HOST_URL");
        var botToken = System.getenv("FISHING_LAGOON_BOT_TOKEN");
        var actionParser = new ActionParser();

        var strategy = new TitForTatStrategy();
        var client = new FishingLagoonClient(serverUrl, botToken, actionParser);
        var bot = client.getBot();
        var botId = new BotId(bot.getId());

        for (var i = 0; i < 3; i++)
            combatRound(strategy, client, botId);
    }

    private static void combatRound(Strategy strategy, FishingLagoonClient client, BotId botId) throws InterruptedException {
        ClientRound round;
        try {
            round = getActiveRoundOrCreate(client, botId);
        } catch (Throwable th) {
            round = getActiveRoundOrCreate(client, botId);
        }

        System.out.println("SEATING");
        round = client.getRound(round.getId());
        var lagoonIndex = strategy.seat(botId, round.getLagoonRound());
        round = client.seat(round.getId(), lagoonIndex);
        System.out.println(round);
        Thread.sleep(round.getMillisecondsForEndSeat());

        System.out.println("COMMANDING");
        round = client.getRound(round.getId());
        var actions = strategy.getOrders(botId, round.getLagoonRound());
        round = client.command(round.getId(), asList(actions));
        System.out.println(round);
        Thread.sleep(round.getMillisecondsForEndCommand());

        System.out.println("SCORING");
        round = client.getRound(round.getId());
        strategy.learnFromRound(botId, round.getLagoonRound());
        System.out.println(round);
        Thread.sleep(round.getMillisecondsForEnd());
    }

    private static ClientRound getActiveRoundOrCreate(FishingLagoonClient client, BotId botId) {
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

}

package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.actions.ActionParser;
import com.drpicox.fishingLagoon.client.ClientRound;
import com.drpicox.fishingLagoon.client.FishingLagoonClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.drpicox.fishingLagoon.actions.Actions.fish;
import static java.util.Arrays.asList;

public class Phase2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        var serverUrl = "http://localhost:4567";
        var botToken = System.getenv("FISHING_LAGOON_BOT_TOKEN");
        var actionParser = new ActionParser();

        var client = new FishingLagoonClient(serverUrl, botToken, actionParser);
        var bot = client.getBot();
        var activeRounds = client.listActiveRounds();
        ClientRound round;
        if (!activeRounds.isEmpty()) {
            round = activeRounds.get(0).withSelfId(bot.getId());
        } else {
            round = client.createRound(String.join("\n", "",
                    "maxDensity=2.0",
                    "weekCount=3",
                    "lagoons=lagoonSmall,lagoonBig",
                    "lagoonSmall.fishPopulation=5",
                    "lagoonBig.fishPopulation=100"
            ));
        }
        round = client.seat(round.getId(), round.getAvailableLagoonCount());
        System.out.println(round);
        Thread.sleep(round.getMillisecondsForEndSeat());

        round = client.command(round.getId(), asList(fish(1), fish(2), fish(3)));
        System.out.println(round);
        Thread.sleep(round.getMillisecondsForEndCommand());

        round = client.getRound(round.getId());
        System.out.println(round);
    }

}

package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.actions.ActionParser;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.client.ClientRound;
import com.drpicox.fishingLagoon.client.FishingLagoonClient;
import com.drpicox.fishingLagoon.phase2.Bootstrap;
import com.drpicox.fishingLagoon.strategy.Strategy;
import com.drpicox.fishingLagoon.phase2.business.TitForTatStrategy;

import java.io.IOException;

import static java.util.Arrays.asList;

public class Phase2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        var serverUrl = System.getenv("FISHING_LAGOON_HOST_URL");
        var botToken = System.getenv("FISHING_LAGOON_BOT_TOKEN");

        Bootstrap bootstrap = new Bootstrap(serverUrl, botToken);
        bootstrap.getCombatController().run();
    }

}

package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.actions.ActionParser;
import com.drpicox.fishingLagoon.client.FishingLagoonClient;
import com.drpicox.fishingLagoon.phase2.persistence.TraitorsStore;
import com.drpicox.fishingLagoon.phase2.presentation.RemoteCombatController;
import com.drpicox.fishingLagoon.phase2.business.TitForTatStrategy;

import java.io.IOException;

public class Phase2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        var serverUrl = System.getenv("FISHING_LAGOON_HOST_URL");
        var botToken = System.getenv("FISHING_LAGOON_BOT_TOKEN");

        var traitorsStore = new TraitorsStore();
        var myStrategy = new TitForTatStrategy(traitorsStore);

        var actionParser = new ActionParser();
        var remoteServer = new FishingLagoonClient(serverUrl, botToken, actionParser);
        var remoteCombatController = new RemoteCombatController(myStrategy, remoteServer);

        remoteCombatController.run();
    }

}

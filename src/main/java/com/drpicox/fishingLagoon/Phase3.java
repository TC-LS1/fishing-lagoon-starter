package com.drpicox.fishingLagoon;

import com.drpicox.fishingLagoon.actions.ActionParser;
import com.drpicox.fishingLagoon.client.FishingLagoonClient;
import com.drpicox.fishingLagoon.phase2.business.TitForTatStrategy;
import com.drpicox.fishingLagoon.phase2.persistence.TraitorsStore;
import com.drpicox.fishingLagoon.phase2.presentation.RemoteCombatController;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Phase3 {

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        var serverUrl = System.getenv("FISHING_LAGOON_HOST_URL");
        var botToken = System.getenv("FISHING_LAGOON_BOT_TOKEN");
        var connectionUrl = System.getenv("FISHING_LAGOON_CONNECTION_URL");

        var connection = DriverManager.getConnection(connectionUrl);
        var traitorsStore = new TraitorsStore(connection);
        var myStrategy = new TitForTatStrategy(traitorsStore);

        var actionParser = new ActionParser();
        var remoteServer = new FishingLagoonClient(serverUrl, botToken, actionParser);
        var remoteCombatController = new RemoteCombatController(myStrategy, remoteServer);

        remoteCombatController.run();
    }

}

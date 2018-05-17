package com.drpicox.fishingLagoon.phase2;

import com.drpicox.fishingLagoon.actions.ActionParser;
import com.drpicox.fishingLagoon.client.FishingLagoonClient;
import com.drpicox.fishingLagoon.phase2.business.TitForTatStrategy;
import com.drpicox.fishingLagoon.phase2.persistence.TraitorsStore;
import com.drpicox.fishingLagoon.phase2.presentation.CombatController;
import com.drpicox.fishingLagoon.strategy.Strategy;

public class Bootstrap {

    private final String serverUrl;
    private final String botToken;

    public Bootstrap(String serverUrl, String botToken) {
        this.serverUrl = serverUrl;
        this.botToken = botToken;
    }

    private ActionParser actionParser;
    public ActionParser getActionParser() {
        if (actionParser == null) {
            actionParser = new ActionParser();
        }
        return actionParser;
    }

    private FishingLagoonClient client;
    public FishingLagoonClient getClient() {
        if (client == null) {
            client = new FishingLagoonClient(serverUrl, botToken, getActionParser());
        }
        return client;
    }

    private CombatController combatController;
    public CombatController getCombatController() {
        if (combatController == null) {
            combatController = new CombatController(getStrategy(), getClient());
        }
        return combatController;
    }

    private Strategy strategy;
    public Strategy getStrategy() {
        if (strategy == null) {
            strategy = new TitForTatStrategy(getTraitorsStore());
        }
        return strategy;
    }

    private TraitorsStore traitorsStore;
    private TraitorsStore getTraitorsStore() {
        if (traitorsStore == null) {
            traitorsStore = new TraitorsStore();
        }
        return traitorsStore;
    }
}

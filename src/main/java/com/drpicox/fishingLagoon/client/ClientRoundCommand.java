package com.drpicox.fishingLagoon.client;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.actions.ActionParser;

import java.util.ArrayList;
import java.util.List;

public class ClientRoundCommand {

    private List<String> actions;
    private List<Action> parsedActions;

    public List<Action> getActions() {
        return new ArrayList<>(parsedActions);
    }

    @Override
    public String toString() {
        return "ClientRoundCommand{" +
                "actions=" + parsedActions +
                '}';
    }

    void parseActions(ActionParser actionParser) {
        parsedActions = new ArrayList<>(actions.size());
        for (var action: actions) {
            parsedActions.add(actionParser.parseAction(action));
        }
    }
}

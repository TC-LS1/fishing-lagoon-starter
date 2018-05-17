package com.drpicox.fishingLagoon.strategy.drpicox;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.actions.FishAction;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;
import com.drpicox.fishingLagoon.strategy.Strategy;

import java.util.*;

import static com.drpicox.fishingLagoon.actions.Actions.fish;
import static com.drpicox.fishingLagoon.actions.Actions.rest;

public class CollaborativeStrategy extends Strategy {

    @Override
    public int seat(BotId botId, LagoonRound round) {
        return new Random().nextInt(round.getLagoonCount());
    }

    public Action[] getOrders(BotId myBotId, LagoonRound round) {
        List<Action> actions = new ArrayList<>();

        LagoonHistory myImaginaryHistory = round.getLagoonHistoryOf(myBotId);
        int weekCount = round.getWeekCount();
        int lastWeekIndex = weekCount - 1;
        for (int weekIndex = 0; weekIndex < lastWeekIndex; weekIndex++) {
            actions.add(rest());
        }

        Lagoon lastWeekLagoon = myImaginaryHistory.getLagoonAt(lastWeekIndex);
        long lagoonFishCount = lastWeekLagoon.getLagoonFishCount();
        int botCount = myImaginaryHistory.getBots().size();
        actions.add(fish(lagoonFishCount / botCount));

        return toArray(actions);
    }

    private Action[] toArray(List<Action> actions) {
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public void learnFromRound(BotId botId, LagoonRound lagoonRound) {

    }
}

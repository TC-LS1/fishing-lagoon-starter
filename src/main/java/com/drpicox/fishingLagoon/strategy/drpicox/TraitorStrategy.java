package com.drpicox.fishingLagoon.strategy.drpicox;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.bots.BotId;
import com.drpicox.fishingLagoon.lagoon.Lagoon;
import com.drpicox.fishingLagoon.lagoon.LagoonHistory;
import com.drpicox.fishingLagoon.lagoon.LagoonRound;
import com.drpicox.fishingLagoon.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.drpicox.fishingLagoon.actions.Actions.fish;
import static com.drpicox.fishingLagoon.actions.Actions.rest;

public class TraitorStrategy extends Strategy {

    @Override
    public int seat(BotId botId, LagoonRound round) {
        return new Random().nextInt(round.getLagoonCount());
    }

    public Action[] getOrders(BotId myBotId, LagoonRound round) {
        List<Action> actions = new ArrayList<>();

        LagoonHistory myImaginaryHistory = round.getLagoonHistoryOf(myBotId);
        for (int weekIndex = 0; weekIndex < round.getWeekCount(); weekIndex++) {
            Lagoon lagoon = myImaginaryHistory.getLagoonAt(weekIndex);

            long traitorFishes = getTraitorFishesFor(lagoon);

            Action action = fish(traitorFishes);
            actions.add(action);

            myImaginaryHistory = myImaginaryHistory.putAction(myBotId, weekIndex, action);
        }

        return toArray(actions);
    }

    private long getTraitorFishesFor(Lagoon lagoon) {
        long availableFish = lagoon.getLagoonFishCount();
        double botCount = lagoon.getBots().size();
        return (long) Math.ceil(availableFish + 0.99 / botCount);
    }

    private Action[] toArray(List<Action> actions) {
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public void learnFromRound(BotId botId, LagoonRound lagoonRound) {
        
    }
}

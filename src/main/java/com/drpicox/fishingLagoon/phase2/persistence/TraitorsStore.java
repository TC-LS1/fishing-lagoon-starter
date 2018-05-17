package com.drpicox.fishingLagoon.phase2.persistence;

import com.drpicox.fishingLagoon.bots.BotId;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TraitorsStore {

    private Set<BotId> traitors = new HashSet<>();

    public Set<BotId> list() {
        return new HashSet<>(traitors);
    }

    public void save(BotId traitorId) {
        traitors.add(traitorId);
    }
}

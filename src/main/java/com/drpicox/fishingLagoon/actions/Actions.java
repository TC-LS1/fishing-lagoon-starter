package com.drpicox.fishingLagoon.actions;

public class Actions {
    public static FishAction fish(long n) {
        return new FishAction(n);
    }

    public static RestAction rest() {
        return new RestAction();
    }
}

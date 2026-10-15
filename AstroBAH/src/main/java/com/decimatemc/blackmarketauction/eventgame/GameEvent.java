package com.decimatemc.blackmarketauction.eventgame;

import java.util.List;

public interface GameEvent extends Runnable {

    void startCountdown();

    List<Double> warnTimes();

    void doWarning(double seconds);

    int getTicksLeft();

    void decrementTicksLeft();

    String getName();

}

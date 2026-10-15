package com.decimatemc.blackmarketauction.eventgame;

public interface RecurringEvent extends GameEvent {

    int getTickInterval();

    void resetTicks();

}

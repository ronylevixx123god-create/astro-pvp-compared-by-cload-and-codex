package com.decimatemc.blackmarketauction.eventgame;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventGameRunnable implements Runnable {

    private final EventManager eventManager;

    public EventGameRunnable(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void run() {
        Set<UUID> toRemove = new HashSet<>();
        this.eventManager.getEventUUIDs().forEach(uuid -> {
            GameEvent event = this.eventManager.getGameEvent(uuid);
            boolean isStarting = false;
            if (event instanceof DelayedEvent) {
                if (((DelayedEvent) event).tickDelay() == event.getTicksLeft())
                    isStarting = true;
            } else if (event instanceof RecurringEvent) {
                if (((RecurringEvent) event).getTickInterval() == event.getTicksLeft())
                    isStarting = true;
            }
            if (isStarting)
                event.startCountdown();
            if (event.getTicksLeft() == 0) {
                if (event instanceof AsyncEvent) {
                    Bukkit.getScheduler().runTaskAsynchronously(BlackMarketAuction.get(), event);
                } else {
                    event.run();
                }
                if (event instanceof DelayedEvent) {
                    toRemove.add(uuid);
                }
                if (event instanceof RecurringEvent) {
                    ((RecurringEvent) event).resetTicks();
                }
            }
            if (event.warnTimes().contains((double) event.getTicksLeft() / 20)) {
                event.doWarning((double) event.getTicksLeft() / 20);
            }
            event.decrementTicksLeft();
        });
        toRemove.forEach(this.eventManager::deregisterEvent);
    }

}

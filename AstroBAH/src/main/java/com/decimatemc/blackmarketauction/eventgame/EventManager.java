package com.decimatemc.blackmarketauction.eventgame;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.stream.IntStream;

public class EventManager {

    private final Map<UUID, GameEvent> gameEvents;

    public EventManager() {
        this.gameEvents = new HashMap<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BlackMarketAuction.get(), new EventGameRunnable(this), 0, 1);
    }

    public UUID registerEvent(GameEvent event) {
        return this.registerEvent(UUID.randomUUID(), event);
    }

    public UUID registerEvent(UUID uuid, GameEvent event) {
        this.gameEvents.put(uuid, event);
        return uuid;
    }

    public void registerEvents(List<GameEvent> events) {
        events.forEach(this::registerEvent);
    }

    public void registerEvents(List<UUID> uuids, List<GameEvent> events) {
        IntStream.range(0, uuids.size()).forEach(i -> this.registerEvent(uuids.get(i), events.get(i)));
    }

    public boolean deregisterEvent(UUID uuid) {
        return this.gameEvents.remove(uuid) != null;
    }

    public boolean deregisterEvent(GameEvent event) {
        UUID toRemove = null;
        for (UUID key : this.gameEvents.keySet()) {
            if (this.gameEvents.get(key).equals(event))
                toRemove = key;
        }
        return this.deregisterEvent(toRemove);
    }

    public GameEvent getGameEvent(UUID uuid) {
        return this.gameEvents.get(uuid);
    }

    public List<GameEvent> getEvents() {
        return new ArrayList<>(this.gameEvents.values());
    }

    public Set<UUID> getEventUUIDs() {
        return this.gameEvents.keySet();
    }

    public static boolean isRecurring(GameEvent event) {
        return event instanceof RecurringEvent;
    }

    public static boolean isAsync(GameEvent event) {
        return event instanceof AsyncEvent;
    }

    public static int getStartingTicks(GameEvent event) {
        if (event instanceof DelayedEvent) {
            return ((DelayedEvent) event).tickDelay();
        } else if (event instanceof RecurringEvent) {
            return ((RecurringEvent) event).getTickInterval();
        }
        return -1;
    }

    public static double getNextWarning(GameEvent event) {
        for (Double warningTime : event.warnTimes()) {
            if (warningTime * 20 >= event.getTicksLeft())
                continue;
            return warningTime;
        }
        return 0.0;
    }

    public static int getTicksUntilNextWarning(GameEvent event) {
        return event.getTicksLeft() - ((int) Math.ceil(getNextWarning(event) * 20));
    }

}

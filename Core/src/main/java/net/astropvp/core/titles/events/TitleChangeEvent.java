/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.astropvp.core.titles.events;

import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TitleChangeEvent
extends Event {
    private UUID uuid;
    private String active_title;
    private static final HandlerList handlers = new HandlerList();

    public TitleChangeEvent(UUID id, String title) {
        this.uuid = id;
        this.active_title = title;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getTitle() {
        return this.active_title;
    }
}


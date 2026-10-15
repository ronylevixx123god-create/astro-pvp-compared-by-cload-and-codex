package me.ifamasssxd.cosmicbosses.struct;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BossDespawnEvent extends Event {
    private static HandlerList handlers;
    private Entity entity;
    private Boss boss;

    public BossDespawnEvent(final Entity entity, final Boss boss) {
        this.entity = entity;
        this.boss = boss;
    }

    public HandlerList getHandlers() {
        return BossDespawnEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BossDespawnEvent.handlers;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Boss getBoss() {
        return this.boss;
    }

    static {
        BossDespawnEvent.handlers = new HandlerList();
    }
}

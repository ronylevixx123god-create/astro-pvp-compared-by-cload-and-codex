package me.ifamasssxd.cosmicbosses.struct;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class PreBossDeathEvent extends Event {
    private static HandlerList handlers;
    private Entity entity;
    private EntityDamageEvent lastDamageEvent;
    private Boss boss;

    public PreBossDeathEvent(final Entity entity, final Boss boss, final EntityDamageEvent event) {
        this.entity = entity;
        this.boss = boss;
        this.lastDamageEvent = event;
    }

    public HandlerList getHandlers() {
        return PreBossDeathEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PreBossDeathEvent.handlers;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public EntityDamageEvent getLastDamageEvent() {
        return this.lastDamageEvent;
    }

    public Boss getBoss() {
        return this.boss;
    }

    static {
        PreBossDeathEvent.handlers = new HandlerList();
    }
}

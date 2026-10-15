package me.astropvp.astrospawners.utils;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class StackedMobDeathEvent  extends Event implements Cancellable {
    private static final HandlerList handlers;
    private boolean cancel;
    private Entity entity;
    private Entity damager;
    private Map<EntityType, ItemStack> drops;

    public StackedMobDeathEvent(Entity entity, Entity damager) {
        this.entity = entity;
        this.damager = damager;
        Random random = new Random();
        drops = new EnumMap<>(EntityType.class);
        drops.put(EntityType.VILLAGER, new ItemStack(Material.EMERALD, random.nextInt((3 - 1)+1) + 1));
        drops.put(EntityType.CREEPER, new ItemStack(Material.TNT, 1));
        drops.put(EntityType.WITCH, new ItemStack(Material.DIAMOND,random.nextInt((3 - 1)+1) + 1));
        drops.put(EntityType.IRON_GOLEM, new ItemStack(Material.IRON_INGOT,random.nextInt((3 - 1)+1) + 1));
        drops.put(EntityType.PIG_ZOMBIE, new ItemStack(Material.GOLD_INGOT,1));
        drops.put(EntityType.SKELETON, new ItemStack(Material.BONE));
        drops.put(EntityType.ZOMBIE, new ItemStack(Material.ROTTEN_FLESH));
        drops.put(EntityType.BLAZE, new ItemStack(Material.BLAZE_ROD, 1));
    }

    public static HandlerList getHandlerList() {
        return StackedMobDeathEvent.handlers;
    }

    public ItemStack getDrops(EntityType entityType) {
        return drops.get(entityType);
    }

    public Entity getDamager() {
        return damager;
    }

    @Override
    public final HandlerList getHandlers() {
        return StackedMobDeathEvent.handlers;
    }

    @Override
    public final void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public final boolean isCancelled() {
        return this.cancel;
    }

    static {
        handlers = new HandlerList();
    }



    public Entity getEntity() {
        return entity;
    }
}

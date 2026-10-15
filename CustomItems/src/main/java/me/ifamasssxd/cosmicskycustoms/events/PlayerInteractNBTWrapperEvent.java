package me.ifamasssxd.cosmicskycustoms.events;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractNBTWrapperEvent
extends Event
implements Cancellable {
    private static HandlerList list = new HandlerList();
    boolean cancelled = false;
    private Player player;
    private Material itemType;
    private Action action;
    private ItemStack item;
    private Block block;
    private NBTWrapper wrapper;
    private PlayerInteractEvent rawEvent;

    public PlayerInteractNBTWrapperEvent(Player player, PlayerInteractEvent event, ItemStack item, NBTWrapper wrapper) {
        this.wrapper = wrapper;
        this.rawEvent = event;
        this.player = player;
        this.setItem(item);
        this.block = this.rawEvent.getClickedBlock();
        this.action = this.rawEvent.getAction();
    }

    public void setItem(ItemStack item) {
        this.item = item;
        this.itemType = item != null ? item.getType() : null;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    public HandlerList getHandlers() {
        return list;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Material getItemType() {
        return this.itemType;
    }

    public Action getAction() {
        return this.action;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public Block getBlock() {
        return this.block;
    }

    public NBTWrapper getWrapper() {
        return this.wrapper;
    }

    public void setWrapper(NBTWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public PlayerInteractEvent getRawEvent() {
        return this.rawEvent;
    }
}


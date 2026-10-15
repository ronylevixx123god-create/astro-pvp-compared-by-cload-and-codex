package me.ifamasssxd.cosmicskycustoms.events;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItems;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PrePlayerUseCosmicItemEvent
extends Event
implements Cancellable {
    private boolean cancelled = false;
    private static HandlerList handlerList = new HandlerList();
    private Player player;
    private CosmicItems parentItem;
    private CosmicItem cosmicItem;
    private ItemStack rawItem;
    private NBTWrapper rawNBT;

    public PrePlayerUseCosmicItemEvent(Player player, CosmicItems item, CosmicItem customItem, ItemStack raw, NBTWrapper wrapp) {
        this.player = player;
        this.parentItem = item;
        this.cosmicItem = customItem;
        this.rawItem = raw;
        this.rawNBT = wrapp;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public Player getPlayer() {
        return this.player;
    }

    public CosmicItems getParentItem() {
        return this.parentItem;
    }

    public CosmicItem getCosmicItem() {
        return this.cosmicItem;
    }

    public ItemStack getRawItem() {
        return this.rawItem;
    }

    public NBTWrapper getRawNBT() {
        return this.rawNBT;
    }
}


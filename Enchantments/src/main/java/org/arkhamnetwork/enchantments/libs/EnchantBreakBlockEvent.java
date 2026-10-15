package org.arkhamnetwork.enchantments.libs;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EnchantBreakBlockEvent extends Event implements Cancellable {
    public static final HandlerList list;

    static {
        list = new HandlerList();
    }

    private boolean cancelled;
    private Block block;
    private Material blockType;
    private Player player;

    public EnchantBreakBlockEvent(final Player player, final Block block, final Material material) {
        this.cancelled = false;
        this.player = player;
        this.block = block;
        this.blockType = material;
    }

    public static HandlerList getHandlerList() {
        return EnchantBreakBlockEvent.list;
    }

    public HandlerList getHandlers() {
        return EnchantBreakBlockEvent.list;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }

    public Material getBlockType() {
        return this.blockType;
    }

    public void setBlockType(final Material blockType) {
        this.blockType = blockType;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }
}

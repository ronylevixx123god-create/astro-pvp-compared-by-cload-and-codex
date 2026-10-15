/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  org.bukkit.entity.Player
 */
package me.recolance.auction.menu.bidmenus;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class BiddableMenu
        extends CosmicGUI {
    public boolean forceUpdate = false;
    public long lastLeftClick;
    public long lastRightClick;
    public long lastBid;
    private UUID lastSlotClicked;

    public BiddableMenu(Player player, String title, int size) {
        super(player, title, size);
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getTitle() {
        return this.title;
    }
}


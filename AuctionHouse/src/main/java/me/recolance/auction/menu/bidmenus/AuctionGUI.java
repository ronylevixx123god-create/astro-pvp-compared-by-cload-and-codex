/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.bukkit.entity.Player
 */
package me.recolance.auction.menu.bidmenus;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.entity.Player;

public abstract class AuctionGUI
        extends CosmicGUI {
    private long lastClicked;

    public AuctionGUI(Player player, String title, int size) {
        super(player, title, size);
    }

    public boolean canClick() {
        if (this.lastClicked <= System.currentTimeMillis()) {
            return true;
        }
        this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Please wait before clicking again!");
        return false;
    }

    public void click() {
        this.lastClicked = System.currentTimeMillis() + 500L;
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.InventoryUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  com.google.common.collect.Lists
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package net.astropvp.core.cosmictoggles.menu;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import java.util.ArrayList;

import net.astropvp.core.cosmictoggles.toggles.CosmicToggle;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ToggleMenu
extends CosmicGUI {
    public ToggleMenu(Player player) {
        super(player, "AstroPvP Toggles:", 27);
    }

    public void redraw() {
        int index = 0;
        FPlayer fplayer = FPlayers.getInstance().getByPlayer(this.player);
        for (CosmicToggle toggle : CosmicToggle.values()) {
            boolean toggled = toggle.getToggle(this.player, fplayer);
            ArrayList<String> lore = Lists.newArrayList(CC.Gray + toggle.getCommand());
            this.setItem(index++, new InventoryItem(new ItemBuilder(Material.STAINED_GLASS_PANE).data(toggled ? (short)DyeColor.LIME.getWoolData() : (short)DyeColor.RED.getWoolData()).lore(lore).name((toggled ? CC.GreenB : CC.RedB) + toggle.getDisplayName()).build()).click(() -> {
                this.player.chat(toggle.getCommand());
                this.redraw();
            }));
        }
    }
}


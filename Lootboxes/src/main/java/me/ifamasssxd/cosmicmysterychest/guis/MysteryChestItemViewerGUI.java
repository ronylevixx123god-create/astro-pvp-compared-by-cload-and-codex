/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.InventoryUtils
 *  com.google.common.collect.Lists
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicmysterychest.guis;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.InventoryUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicmysterychest.struct.CustomLoot;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public class MysteryChestItemViewerGUI
        extends CosmicGUI {
    private MysteryChest chest;

    public MysteryChestItemViewerGUI(Player player, MysteryChest chest) {
        super(player, ChatColor.stripColor((String) chest.getDisplayName()).length() > 32 ? "Lootbox Items" : ChatColor.stripColor((String) chest.getDisplayName()), InventoryUtils.fitSize((int) (chest.getLootItems().size() + chest.getGarunteedItems().size())));
        this.chest = chest;
    }

    public void redraw() {
        int slot = 0;
        LinkedList<ItemStack> items = Lists.newLinkedList();
        for (CustomLoot item : this.chest.getLootItems()) {
            items.add(item.getItem());
        }
        if (this.chest.getGarunteedItems() != null) {
            items.addAll(this.chest.getGarunteedItems());
        }
        for (ItemStack item2 : items) {
            if (slot >= this.inventory.getSize()) break;
            this.setItem(slot++, new InventoryItem(item2).click(() -> {
                this.closeWithDelay();
                this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Unlock this Lootbox at " + CC.RedU + "buy.astropvp.net" + CC.Red + "!");
                this.player.sendMessage(CC.Gray + "Visit the AstroPvP store for more information!");
            }));
        }
    }
}


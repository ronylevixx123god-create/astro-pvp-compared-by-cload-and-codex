/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.google.common.collect.Lists
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.ifamasssxd.cosmicmysterychest.guis;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.GlobalMysteryChest;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ActiveMysteryChestGUI
        extends CosmicGUI {
    private GlobalMysteryChest globalMysteryChest;
    private ItemStack chestItem;
    InventoryItem pane = new InventoryItem(new ItemBuilder(Material.STAINED_GLASS_PANE, " ", new String[0]).data((short) DyeColor.GRAY.getWoolData()).build());

    public ActiveMysteryChestGUI(Player player, GlobalMysteryChest chest) {
        super(player, "Astro Lootbox", 9, InventoryType.DISPENSER);
        this.globalMysteryChest = chest;
        if (this.globalMysteryChest.getActiveMysteryChest() != null) {
            this.chestItem = this.globalMysteryChest.getActiveMysteryChest().createClaimableItem(false);
        }
    }

    public void redraw() {
        ItemStack item = this.chestItem != null ? this.chestItem.clone() : new ItemBuilder(Material.CHEST, CC.GoldB + "???", new String[]{CC.Gray + "No Lootbox currently available!"}).build();
        ItemMeta im = item.getItemMeta();
        List lore = im.getLore();
        if (lore == null) {
            lore = Lists.newArrayList();
        }
        lore.add("");
        //lore.add(this.globalMysteryChest.getExpirationString());
        lore.add(CC.Gray + "Right-Click to view lootbox items");
        im.setLore(lore);
        item.setItemMeta(im);
        this.setItem(4, new InventoryItem(item).click(ClickType.LEFT, () -> {
            if (this.globalMysteryChest.getExpiration() <= System.currentTimeMillis()) {
                this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Unlock this Lootbox at " + CC.RedU + "buy.astropvp.net" + CC.Red + "!");
                this.player.sendMessage(CC.Gray + "Visit the AstroPvP store for more information!");
                return;
            }
            this.closeWithDelay();
            this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Unlock this Lootbox at " + CC.RedU + "buy.astropvp.net" + CC.Red + "!");
            this.player.sendMessage(CC.Gray + "Visit the AstroPvP store for more information!");
        }).click(ClickType.RIGHT, () -> new MysteryChestItemViewerGUI(this.player, this.globalMysteryChest.getActiveMysteryChest()).openGUI(CosmicMysteryChest.get())));
        for (int i = 0; i < this.inventory.getSize(); ++i) {
            ItemStack found = this.inventory.getItem(i);
            if (found != null && found.getType() != Material.AIR) continue;
            this.setItem(i, this.pane);
        }
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.InventoryUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicmasks.menu;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.InventoryUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import java.util.Arrays;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MaskHelpMenu
extends CosmicGUI {
    public MaskHelpMenu(Player player) {
        super(player, "Astro Masks", InventoryUtils.fitSize((int)((int)Arrays.stream(MaskType.values()).filter(MaskType::isActive).count())));
    }

    public void redraw() {
        int index = 0;
        for (MaskType type : MaskType.values()) {
            if (!type.getMaskData().isActive()) continue;
            this.setItem(index++, type.createMask(false), () -> this.closeWithDelay(pl -> pl.sendMessage(CC.RedB + "(!)" + CC.Red + " Unlock Player Masks at " + CC.RedU + "buy.astropvp.net" + CC.Red + "!")));
        }
        ItemStack pane = new ItemBuilder(Material.STAINED_GLASS_PANE, CC.RedB + "???", new String[]{CC.Gray + "This mask has not", CC.Gray + "been discovered yet."}).data((short)DyeColor.RED.getWoolData()).build();
        for (int i = 0; i < this.size; ++i) {
            ItemStack current = this.inventory.getItem(i);
            if (current != null && current.getType() != Material.AIR) continue;
            this.setItem(i, new InventoryItem(pane));
        }
    }
}


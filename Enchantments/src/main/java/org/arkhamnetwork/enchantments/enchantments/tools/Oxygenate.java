/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockEvent
 */
package org.arkhamnetwork.enchantments.enchantments.tools;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;

public class Oxygenate
extends CustomEnchantment {
    public Oxygenate() {
        super("Oxygenate", GeneralUtil.tools, 6);
        this.max = 1;
        this.base = 10.0;
        this.interval = 10.0;
    }

    public void applyToolEffect(Player player, Block block, int enchantLevel, BlockEvent event) {
        if (event instanceof BlockBreakEvent && player.getRemainingAir() + 20 <= player.getMaximumAir()) {
            player.setRemainingAir(player.getRemainingAir() + 20);
        }
    }
}


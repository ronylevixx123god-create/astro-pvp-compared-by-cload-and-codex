/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.tools;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Haste
extends CustomEnchantment {
    public Haste() {
        super("Haste", GeneralUtil.tools, 2);
        this.max = 3;
        this.base = 15.0;
        this.interval = 10.0;
    }

    public void applyToolEffect(Player player, Block block, int enchantLevel, BlockEvent event) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 40, enchantLevel - 1), true);
    }
}


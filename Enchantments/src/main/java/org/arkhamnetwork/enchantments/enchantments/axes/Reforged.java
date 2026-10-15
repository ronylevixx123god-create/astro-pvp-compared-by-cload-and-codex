/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemUtils
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Reforged
extends CustomEnchantment {
    public Reforged() {
        super("Reforged", GeneralUtil.weapons_and_tools, 7);
        this.max = 10;
        this.base = 20.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        ItemStack weapon;
        double chance;
        if (!(user instanceof Player)) {
            return;
        }
        Player playerAttacker = (Player)user;
        double random = Math.random();
        if (random < (chance = 0.08 * (double)enchantLevel) && (weapon = playerAttacker.getItemInHand()) != null && weapon.getDurability() != 0 && weapon.getMaxStackSize() == 1) {
            //playerAttacker.setItemInHand(ItemUtils.addDurability((ItemStack)weapon, (int)1));
            playerAttacker.updateInventory();
        }
    }

    public void applyToolEffect(Player player, Block block, int enchantLevel, BlockEvent event) {
        if (event instanceof BlockBreakEvent) {
            ItemStack weapon;
            double chance;
            Player playerAttacker = player;
            double random = Math.random();
            if (random < (chance = 0.08 * (double)enchantLevel) && (weapon = playerAttacker.getItemInHand()) != null && weapon.getDurability() != 0 && weapon.getMaxStackSize() == 1) {
                //playerAttacker.setItemInHand(ItemUtils.addDurability((ItemStack)weapon, (int)1));
                playerAttacker.updateInventory();
            }
        }
    }
}


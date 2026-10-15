/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  me.ifamasssxd.cosmicguishop.CosmicGUIShop
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockEvent
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.enchantments.enchantments.tools;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicguishop.CosmicGUIShop;
import org.arkhamnetwork.enchantments.libs.EnchantBreakBlockEvent;
import org.arkhamnetwork.enchantments.mastery.MasteryAutoSell;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

public class AutoSmelt
extends CustomEnchantment {
    public AutoSmelt() {
        super("Auto Smelt", GeneralUtil.pickaxe, 6);
        this.max = 3;
        this.base = 10.0;
        this.interval = 10.0;
    }

    public void applyToolEffect(Player player, Block block, int enchantLevel, BlockEvent event) {
        BlockBreakEvent bbe;
        Material m;
        if (event instanceof BlockBreakEvent && this.getSmeltedItem(m = (bbe = (BlockBreakEvent)event).getBlock().getType()) != m) {
            boolean hasDetonate;
            boolean bl = hasDetonate = EnchantmentAPI.itemHasEnchantment((ItemStack)player.getItemInHand(), (String)"Detonate") || EnchantmentAPI.itemHasEnchantment((ItemStack)player.getItemInHand(), (String)"Atomic Detonate");
            if (hasDetonate) {
                return;
            }
            double chance = (double)enchantLevel * 0.34;
            if (Math.random() < chance) {
                double price;
                ItemStack smelted = new ItemStack(this.getSmeltedItem(m), enchantLevel);
                EnchantBreakBlockEvent enchantBreak = new EnchantBreakBlockEvent(player, bbe.getBlock(), m);
                Bukkit.getPluginManager().callEvent((Event)enchantBreak);
                if (enchantBreak.isCancelled()) {
                    return;
                }
                bbe.getBlock().setTypeIdAndData(0, (byte)0, false);
                player.giveExp(bbe.getExpToDrop());
                if (EnchantmentAPI.itemHasEnchantment((ItemStack)player.getItemInHand(), (String)"Auto Sell") && (price = MasteryAutoSell.getSellPrice(smelted.getData()) * (double)smelted.getAmount()) > 0.0) {
                    block.setTypeIdAndData(0, (byte)0, true);
                    CosmicGUIShop.getEconomy().depositPlayer((OfflinePlayer)player, price);
                    Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(player, "SHOP_SELL", new Object[]{price, smelted.getAmount(), smelted.getType(), smelted.getData()}));
                    return;
                }
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(new ItemStack[]{smelted});
                } else {
                    player.getWorld().dropItem(player.getLocation(), smelted);
                }
            }
        }
    }

    private Material getSmeltedItem(Material m) {
        if (m == Material.IRON_ORE) {
            return Material.IRON_INGOT;
        }
        if (m == Material.GOLD_ORE) {
            return Material.GOLD_INGOT;
        }
        return m;
    }
}


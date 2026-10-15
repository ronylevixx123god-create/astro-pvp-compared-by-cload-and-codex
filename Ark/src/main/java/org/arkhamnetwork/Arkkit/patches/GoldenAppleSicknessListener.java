/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

final class GoldenAppleSicknessListener
        implements Listener {
    GoldenAppleSicknessListener() {
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerConsumeGoldenApple(PlayerItemConsumeEvent e) {
        Player pl = e.getPlayer();
        if (e.getItem().getType() == Material.GOLDEN_APPLE || e.getItem().getTypeId() == 322) {
            try {
                long l = pl.hasMetadata("golden_apple_count") ? (long) pl.getMetadata("golden_apple_count").get(0).asInt() : 0L;
            } catch (Exception err) {
                pl.setMetadata("golden_apple_count", new FixedMetadataValue(Arkkit.get(), 0));
                pl.setMetadata("golden_apple_time", new FixedMetadataValue(Arkkit.get(), 0L));
            }
            int apple_count = pl.hasMetadata("golden_apple_count") ? pl.getMetadata("golden_apple_count").get(0).asInt() : 0;
            long apple_time = pl.hasMetadata("golden_apple_time") ? pl.getMetadata("golden_apple_time").get(0).asLong() : 0L;
            int nutrition_mod = pl.hasMetadata("nutritionEnchantment") ? pl.getMetadata("nutritionEnchantment").get(0).asInt() : 0;
            ++apple_count;
            if (System.currentTimeMillis() - apple_time <= 30000L) {
                int adjustedAppleCount = apple_count - nutrition_mod;
                if (adjustedAppleCount == 2) {
                    pl.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.YELLOW + " You are about to contract " + ChatColor.UNDERLINE + "Golden Apple Sickness!");
                    pl.sendMessage(ChatColor.GRAY + "Don't eat anymore golden apples for 30s!");
                }
                if (adjustedAppleCount > 2) {
                    e.setCancelled(true);
                    ItemStack apple_stack = e.getItem();
                    if (apple_stack.getAmount() <= 1) {
                        pl.setItemInHand(new ItemStack(Material.AIR));
                    } else {
                        apple_stack.setAmount(apple_stack.getAmount() - 1);
                        pl.setItemInHand(apple_stack);
                    }
                    pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " You have " + ChatColor.UNDERLINE + "Golden Apple Sickness!");
                    pl.sendMessage(ChatColor.GRAY + "Stop eating golden apples before it's too late!");
                    pl.updateInventory();
                }
                if (adjustedAppleCount >= 3) {
                    pl.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 0));
                }
                if (adjustedAppleCount >= 4) {
                    if (!pl.hasPotionEffect(PotionEffectType.SLOW)) {
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 0));
                    }
                    if (!pl.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 300, 0));
                    }
                }
                if (adjustedAppleCount >= 5) {
                    pl.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 1));
                }
                if (adjustedAppleCount >= 6) {
                    pl.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300, 5));
                }
            } else {
                apple_count = 1;
                apple_time = System.currentTimeMillis();
            }
            pl.setMetadata("golden_apple_count", new FixedMetadataValue(Arkkit.get(), apple_count));
            if (apple_count == 1) {
                pl.setMetadata("golden_apple_time", new FixedMetadataValue(Arkkit.get(), apple_time));
            }
        }
    }
}


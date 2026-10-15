/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.block.BlockPhysicsEvent
 *  org.bukkit.event.enchantment.EnchantItemEvent
 *  org.bukkit.event.enchantment.PrepareItemEnchantEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryOpenEvent
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.Plugin;

public class EnchantmentAnvilFix
        extends ArkPlugin {
    public EnchantmentAnvilFix(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        Bukkit.getScheduler().runTaskLater(Arkkit.get(), new Runnable() {

            @Override
            public void run() {
                if (Bukkit.getPluginManager().getPlugin("EnchantmentAPI") != null) {
                    InventoryOpenEvent.getHandlerList().unregister(Bukkit.getPluginManager().getPlugin("EnchantmentAPI"));
                    EnchantItemEvent.getHandlerList().unregister(Bukkit.getPluginManager().getPlugin("EnchantmentAPI"));
                    PrepareItemEnchantEvent.getHandlerList().unregister(Bukkit.getPluginManager().getPlugin("EnchantmentAPI"));
                    BlockPhysicsEvent.getHandlerList().unregister(Bukkit.getPluginManager().getPlugin("ObsidianDestroyer"));
                }
            }
        }, 20L);
    }

    @Override
    public void disable() {
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ConcurrentHashMap;

public class Commander
extends CustomEnchantment {
    protected static ConcurrentHashMap<String, Integer> commanders;
    private CommanderTask commanderTask;

    public Commander() {
        super("Commander", GeneralUtil.armor, 2);
        this.max = 5;
        this.base = 20.0;
        this.interval = 5.0;
        commanders = new ConcurrentHashMap();
        Bukkit.getPluginManager().registerEvents((Listener)new CommanderListener(), (Plugin)Enchantments.getInstance());
        this.commanderTask = new CommanderTask();
        this.commanderTask.runTaskTimer((Plugin)Enchantments.getInstance(), 20L, 160L);
    }

    public void applyEquipEffect(Player player, int enchantLevel) {
        commanders.put(player.getName(), enchantLevel);
    }

    public void applyUnequipEffect(Player player, int enchantLevel) {
        commanders.remove(player.getName());
    }
}


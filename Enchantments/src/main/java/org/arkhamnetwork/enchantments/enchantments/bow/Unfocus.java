/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class Unfocus
extends CustomEnchantment {
    public Unfocus() {
        super("Unfocus", new Material[]{Material.BOW}, 2);
        this.max = 5;
        this.base = 20.0;
        this.interval = 0.0;
        Bukkit.getPluginManager().registerEvents((Listener)new UnfocusListener(), (Plugin)Enchantments.getInstance());
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        double chance;
        double random = Math.random() * 1.0;
        if (random < (chance = 0.5 * (double)level) && playerHit instanceof Player && (!playerHit.hasMetadata("unfocusEnchantmentExpire") || System.currentTimeMillis() > ((MetadataValue)playerHit.getMetadata("unfocusEnchantmentExpire").get(0)).asLong())) {
            ((Player)playerHit).sendMessage((Object)ChatColor.DARK_GREEN + "** UNFOCUS [" + level * 2 + "s] **");
            ((Player)playerHit).playSound(playerHit.getLocation(), Sound.PORTAL_TRIGGER, 1.2f, 0.6f);
            ((Player)player).playSound(playerHit.getLocation(), Sound.ARROW_HIT, 1.2f, 2.5f);
            playerHit.setMetadata("unfocusEnchantmentLevel", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)level));
            playerHit.setMetadata("unfocusEnchantmentExpire", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)(System.currentTimeMillis() + (long)(level * 2000))));
        }
    }
}


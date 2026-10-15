/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class Resilience
extends CustomEnchantment {
    public Resilience() {
        super("Resilience", GeneralUtil.helmets, 2);
        this.max = 4;
        this.base = 17.0;
        this.interval = 4.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        double dmg = e.getFinalDamage();
        double minDmgToProc = 10 - level;
        double chance = (double)level * 0.05;
        if (player.getHealth() - dmg <= 0.0 && dmg >= minDmgToProc && Math.random() < chance) {
            e.setDamage(e.getDamage()*0.5);

            ((Player)player).sendMessage((Object)ChatColor.DARK_GREEN + "" + (Object)ChatColor.BOLD + "** RESILIENCE (" + (Object)ChatColor.DARK_GREEN + "50% DMG TAKEN" + (Object)ChatColor.BOLD + ") **");
        }
    }

    public void applyEquipEffect(Player player, int enchantLevel) {
        player.setMetadata("resilienceEnchant", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)enchantLevel));
    }

    public void applyUnequipEffect(Player player, int enchantLevel) {
        player.removeMetadata("resilienceEnchant", (Plugin)Enchantments.getInstance());
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  com.rit.sucy.CustomEnchantment
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.rit.sucy.CustomEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Snare
extends CustomEnchantment {
    public Snare() {
        super("Snare", new Material[]{Material.BOW}, 2);
        this.max = 4;
        this.base = 15.0;
        this.interval = 5.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        double chance = (double)level * 0.09;
        double random = Math.random();
        if (random < chance) {
            int metaphysicalLevel;
            if (playerHit.hasMetadata("metaphysicalEnchant") && random > (chance -= (double)(metaphysicalLevel = ((MetadataValue)playerHit.getMetadata("metaphysicalEnchant").get(0)).asInt()) * 0.07)) {
                ((Player)playerHit).sendMessage((Object)ChatColor.DARK_GRAY + "" + (Object)ChatColor.BOLD + "** METAPHYSICAL (" + (Object)ChatColor.DARK_GRAY + "Snare blocked!" + (Object)ChatColor.BOLD + ") **");
                return;
            }
            playerHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, level * 20 + 20, level));
            if (level >= 3) {
                playerHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, level * 20 + 20, level - 3));
            }
            Location l = playerHit.getLocation().add(0.0, 0.5, 0.0);
            WorldUtil.sendWorldEffect((Block)l.getBlock(), (int)106, (int)2001);
        }
    }
}


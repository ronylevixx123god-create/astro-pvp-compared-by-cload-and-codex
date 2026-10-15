/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.ArmorUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Lifebloom
extends CustomEnchantment {
    public Lifebloom() {
        super("Lifebloom", GeneralUtil.leggings, 3);
        this.max = 5;
        this.base = 25.0;
        this.interval = 10.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (player instanceof Player) {
            Player p = (Player)player;
            double dmg = e.getDamage();
            dmg *= (double)ArmorUtil.getArmorDamageNullificationPercent(ArmorUtil.getArmorValue(p));
            if (player.getHealth() - dmg <= 0.0) {
                if (p.hasMetadata("Lifeblooming") && System.currentTimeMillis() - ((MetadataValue)p.getMetadata("Lifeblooming").get(0)).asLong() <= 10000L) {
                    return;
                }
                p.setMetadata("Lifeblooming", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)System.currentTimeMillis()));
                int radius = (int)((float)level * 2.5f);
                ArrayList<Player> allies = new ArrayList<Player>();
                for (Entity ent : player.getNearbyEntities((double)radius, (double)radius, (double)radius)) {
                    Player pEnt;
                    if (!(ent instanceof Player) || !this.isAlly(p, pEnt = (Player)ent)) continue;
                    allies.add(pEnt);
                }
                try {
                    //ParticleEffect.RED_DUST.sendToPlayers(Bukkit.getOnlinePlayers(), p.getEyeLocation().add(0.0, 1.0, 0.0), 1.4f, 1.4f, 1.4f, 0.1f, 65);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                for (Player ally : allies) {
                    ally.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.GREEN + p.getName() + "'s Lifebloom was activated, fight on!");
                    if (level >= 4) {
                        ally.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120, level - 4));
                    }
                    try {
                        //ParticleEffect.HAPPY_VILLAGER.sendToPlayers(Bukkit.getOnlinePlayers(), ally.getLocation().add(0.0, 1.0, 0.0), 2.0f, 2.0f, 2.0f, 0.1f, 16);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean isAlly(Player p1, Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }
}


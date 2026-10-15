/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityShootBowEvent
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
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EnderShift
extends CustomEnchantment {
    protected static Map<UUID, Long> last_ender_shift = new HashMap();
    protected static List<UUID> ender_shifters = new ArrayList<>();

    public EnderShift() {
        super("Ender Shift", GeneralUtil.helmets_and_boots, 3);
        this.max = 3;
        this.base = 25.0;
        this.interval = 5.0;
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if (player instanceof Player) {
            if (player.hasMetadata("neutralized"))
                return;
            double dmg = e.getFinalDamage();
            double chance = 0.1;
            if (player.isOp() && ((Player) player).isSneaking()) {
                chance = 1;
            }
            if (player.getHealth() - dmg <= 0.0 && this.canEnderShift((Player)player) && Math.random() < chance) {
                Player pl = (Player)player;
                last_ender_shift.put(pl.getUniqueId(), System.currentTimeMillis());
                e.setDamage(0);
                e.setCancelled(true);
                pl.sendMessage("");
                pl.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.LIGHT_PURPLE + "You were about to die, so you have entered the Ender dimension, escape to safety!");
                pl.sendMessage("");
                pl.playSound(pl.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 0.54f);
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10*20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, level + 2), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10*20, level - 1), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 10*20, level + 2));
                final UUID uuid = player.getUniqueId();
                ender_shifters.add(uuid);
                Location loc = pl.getLocation();
                Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)Enchantments.getInstance(), () -> {
                    ender_shifters.remove(uuid);
                }, (long)(20L * 10));
            }
        }
    }

    private boolean canEnderShift(Player pl) {
        if (!last_ender_shift.containsKey(pl.getUniqueId())) {
            return true;
        }
        return System.currentTimeMillis() - last_ender_shift.get(pl.getUniqueId()) > 30000L;
    }

    private static boolean isEnderShifting(Player pl) {
        return ender_shifters.contains(pl.getUniqueId());
    }



}


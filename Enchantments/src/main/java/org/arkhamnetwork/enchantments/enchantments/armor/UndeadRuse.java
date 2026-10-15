/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Zombie
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class UndeadRuse
extends CustomEnchantment {
    public UndeadRuse() {
        super("Undead Ruse", GeneralUtil.armor, 2);
        this.max = 10;
        this.base = 20.0;
        this.interval = 5.0;
        Bukkit.getPluginManager().registerEvents(new UndeadRuseListener(), Enchantments.getInstance());
    }

    public void applyDefenseEffect(LivingEntity player, LivingEntity attacker, int level, EntityDamageEvent e) {
        if(level > 0) return;
        if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)e).getDamager() instanceof Player) {

            if (player.hasMetadata("neutralized") || player.hasMetadata("exterminated"))
                return;
            if (player.hasMetadata("noDefenseProcs"))
                return;
            double chance = 0.001;
            if (chance > 0.04) {
                chance = 0.04;
            }
            if (Math.random() < chance) {
                int zombie_count = (int)(Math.random() / 2.0 * (double)level) + 1;
                final Player p = (Player)player;
                Location spawnLoc = p.getTargetBlock((HashSet<Byte>) null, 5).getLocation().add(0.0, 1.0, 0.0);
                try {
                    //ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), spawnLoc, 0.5f, 0.5f, 0.5f, 0.1f, 20);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                while (zombie_count > 0) {
                    --zombie_count;
                    if (spawnLoc.getChunk().getEntities().length >= 50) continue;
                    Zombie z = (Zombie)player.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
                    z.setMetadata("undeadruseSummoner", new FixedMetadataValue(Enchantments.getInstance(), p.getName()));
                    z.setCustomName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + p.getName() + "'s Undead Minion");
                    z.setCustomNameVisible(true);
                    z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, level > 6 ? 2 : (level > 3 ? 1 : 0)));
                    if (level > 4) {
                        z.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
                    }
                    if (level <= 7) continue;
                    z.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2));
                }
                if (attacker instanceof Player) {
                    final Player pAttacker = (Player)attacker;
                    pAttacker.hidePlayer(p);
                    try {
                        //ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), p.getLocation(), 0.75f, 0.75f, 0.75f, 0.1f, 35);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), new Runnable(){

                        @Override
                        public void run() {
                            pAttacker.showPlayer(p);
                            try {
                                //ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), p.getLocation(), 0.75f, 0.75f, 0.75f, 0.1f, 35);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }, (long)zombie_count * 20L + 20L);
                }
            }
        }
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.Chest
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package org.arkhamnetwork.enchantments.enchantments.bow;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DimensionRift
extends CustomEnchantment {
    public static HashMap<Integer, HashMap<Location, Material>> blockRevertTasks = new HashMap();

    public DimensionRift() {
        super("Dimension Rift", new Material[]{Material.BOW}, 3);
        this.max = 4;
        this.base = 17.0;
        this.interval = 4.0;
    }

    public void applyEffect(LivingEntity player, LivingEntity playerHit, int level, EntityDamageByEntityEvent e) {
        int x;
        if (Math.random() > (double)level * 0.1) {
            return;
        }
        if (!(e.getDamager() instanceof Projectile)) {
            return;
        }
        if (!(playerHit instanceof Player)) {
            return;
        }
        final HashMap<Location, Material> blocks = new HashMap<Location, Material>();
        Location enemyLoc = playerHit.getLocation().subtract(0.0, 1.0, 0.0).clone();
        if (playerHit.getWorld().getName().equals("world_koth") || player.getWorld().getName().startsWith("dungeon")) {
            return;
        }

        if (!FactionUtils.isWilderness(playerHit.getLocation()) && !FactionUtils.isWarZone(playerHit.getLocation())) {
            return;
        }


        FLocation fL = new FLocation(enemyLoc);

        int n = x = level >= 4 ? -1 : 0;
        while (x <= 1) {
            int z;
            int n2 = z = level >= 3 ? -1 : 0;
            while (z <= 1) {
                Location l = enemyLoc.clone().add((double)x, 0.0, (double)z);
                Material m = l.getBlock().getType();
                if ((m.isBlock() || m == Material.AIR) && !(l.getBlock().getState() instanceof Chest) && m != Material.SOUL_SAND && m != Material.WEB && m != Material.BEDROCK && m != Material.ENDER_PORTAL && m != Material.PORTAL) {
                    Location lAbove;
                    Material mAbove;
                    blocks.put(l.clone(), m);
                    l.getBlock().setType(Material.SOUL_SAND);
                    try {
                        //ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), l.clone().add(0.0, 1.0, 0.0), 0.7f, 0.7f, 0.7f, 0.1f, 10);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    if (Math.random() < (double)level * 0.1 && ((mAbove = (lAbove = l.getBlock().getLocation().add(0.0, 1.0, 0.0)).getBlock().getType()).isBlock() || mAbove == Material.AIR) && !(lAbove.getBlock().getState() instanceof Chest) && mAbove != Material.SOUL_SAND && mAbove != Material.WEB && mAbove != Material.BEDROCK && mAbove != Material.ENDER_PORTAL && mAbove != Material.PORTAL) {
                        blocks.put(lAbove.clone(), mAbove);
                        lAbove.getBlock().setType(Material.WEB);
                        try {
                            //ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), lAbove.clone().add(0.0, 0.5, 0.0), 0.7f, 0.7f, 0.7f, 0.1f, 10);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                    }
                }
                ++z;
            }
            ++x;
        }
        final BukkitTask bt = Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable(){

            @Override
            public void run() {
                HashSet<Player> playersToBoost = new HashSet<Player>();
                for (Map.Entry data : blocks.entrySet()) {
                    Location l = (Location)data.getKey();
                    for (Player p : l.getWorld().getPlayers()) {
                        if (playersToBoost.contains((Object)p) || !p.getWorld().equals((Object)l.getWorld()) || Math.abs(p.getLocation().getChunk().getX() - l.getChunk().getX()) > 1 || Math.abs(p.getLocation().getChunk().getZ() - l.getChunk().getZ()) > 1 || !(p.getLocation().distanceSquared(l) <= 4.0)) continue;
                        playersToBoost.add(p);
                    }
                    ((Location)data.getKey()).getBlock().setType((Material)data.getValue());
                    WorldUtil.sendWorldEffect((Block)((Location)data.getKey()).getBlock(), (int)((Material)data.getValue()).getId(), (int)2001);
                    for (Player p : playersToBoost) {
                        p.removePotionEffect(PotionEffectType.JUMP);
                        p.setVelocity(new Vector(0.0, 0.5, 0.0));
                    }
                }
            }
        }, (long)(level * 15 + 40));
        blockRevertTasks.put(bt.getTaskId(), (HashMap)blocks.clone());
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), new Runnable(){

            @Override
            public void run() {
                blockRevertTasks.remove(bt.getTaskId());
            }
        }, (long)(level * 15 + 40 + 1));
    }

    public void applyProjectileEffect(LivingEntity player, int level, ProjectileLaunchEvent e) {
        try {
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}


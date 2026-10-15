/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

final class CommanderTask
extends BukkitRunnable {
    CommanderTask() {
    }

    public void run() {
        for (Map.Entry<String, Integer> commanderData : Commander.commanders.entrySet()) {
            String playerName = commanderData.getKey();
            int level = commanderData.getValue();
            if (Bukkit.getPlayer((String)playerName) == null) {
                Commander.commanders.remove(playerName);
                continue;
            }
            Player p = Bukkit.getPlayer((String)playerName);
            String worldAttacker = p.getWorld().getName();
            boolean isDungeonWorld = worldAttacker.equals("dungeon_yijki_a") || worldAttacker.equals("dungeon_destroyed_outpost") || worldAttacker.equals("dungeon_planet_nul");
            if (isDungeonWorld) continue;
            Location l = p.getEyeLocation().add(0.0, 0.5, 0.0);
            int x = 0;
            for (Entity ent : p.getNearbyEntities((double)(level * 2 + 3), (double)(level * 2 + 3), (double)(level * 2 + 3))) {
                if (!(ent instanceof Player) || !this.isAlly(p, (Player)ent)) continue;
                ++x;
                Player pAlly = (Player)ent;
                //pAlly.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, (level > 3 ? 30 : 15) * 20, level > 4 ? 1 : 0));
                if (level >= 2 && pAlly.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                    pAlly.removePotionEffect(PotionEffectType.WEAKNESS);
                }
                if (level >= 3 && pAlly.hasPotionEffect(PotionEffectType.CONFUSION)) {
                    pAlly.removePotionEffect(PotionEffectType.CONFUSION);
                }
                if (level >= 4) {
                    //pAlly.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, level == 4 ? 0 : 1));
                }
                if (level != 5 || !pAlly.hasPotionEffect(PotionEffectType.POISON)) continue;
                pAlly.removePotionEffect(PotionEffectType.POISON);
            }
            if (x <= 0 || p.getWorld().getName().equals("world_duels") || p.getWorld().getName().equals("world_duels2") || p.getWorld().getName().equals("cosmic-pvparenas")) continue;
            WorldUtil.sendWorldEffect((Block)l.getBlock(), (int)57, (int)2001);
        }
    }

    private boolean isAlly(Player p1, Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }
}


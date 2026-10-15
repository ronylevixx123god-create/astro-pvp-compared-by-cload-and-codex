/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldguard.protection.flags.DefaultFlag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.patches;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class PvPPotionEffectsTask
        extends BukkitRunnable {
    private final List<UUID> pvp_enabled_players = new ArrayList<UUID>();

    public void run() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if ((!this.pvp_enabled_players.contains(pl.getUniqueId()) || !this.isPvPDisabled(pl.getLocation())) && (this.pvp_enabled_players.contains(pl.getUniqueId()) || this.isPvPDisabled(pl.getLocation())))
                continue;
            for (PotionEffect pe : pl.getActivePotionEffects()) {
                pl.removePotionEffect(pe.getType());
            }
            pl.playSound(pl.getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 0.75f);
            if (!this.pvp_enabled_players.contains(pl.getUniqueId())) {
                this.pvp_enabled_players.add(pl.getUniqueId());
                continue;
            }
            this.pvp_enabled_players.remove(pl.getUniqueId());
        }
    }

    private boolean isPvPDisabled(Location l) {
        try {
            Class<?> bukkitUtil = PvPPotionEffects.wg.getClass().getClassLoader().loadClass("com.sk89q.worldguard.bukkit.BukkitUtil");
            Method toVector = bukkitUtil.getMethod("toVector", Block.class);
            Vector blockVector = (Vector) toVector.invoke(null, new Object[]{l.getBlock()});
            List<String> regionSet = PvPPotionEffects.wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                return true;
            }
            boolean return_flag = false;
            int return_priority = -1;
            for (String region : regionSet) {
                if (!PvPPotionEffects.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().containsKey(DefaultFlag.PVP))
                    continue;
                StateFlag.State pvp_flag = (StateFlag.State) PvPPotionEffects.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().get(DefaultFlag.PVP);
                int region_priority = PvPPotionEffects.wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();
                if (return_priority == -1) {
                    return_flag = pvp_flag != StateFlag.State.ALLOW;
                    return_priority = region_priority;
                    continue;
                }
                if (region_priority <= return_priority) continue;
                return_flag = pvp_flag != StateFlag.State.ALLOW;
                return_priority = region_priority;
            }
            return return_flag;
        } catch (Exception exception) {
            return true;
        }
    }
}


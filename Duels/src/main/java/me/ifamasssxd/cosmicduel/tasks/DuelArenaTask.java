/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicduel.tasks;

import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.Arena;
import me.ifamasssxd.cosmicduel.struct.enums.DuelCreateState;
import me.ifamasssxd.cosmicduel.struct.enums.DuelState;
import me.ifamasssxd.cosmicduel.struct.enums.Reason;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.ifamasssxd.cosmicduel.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class DuelArenaTask
implements Runnable {
    public static List<String> arena_regions = new ArrayList<String>();

    public DuelArenaTask() {
        for (Arena arena : Arena.arenas) {
            arena_regions.add(arena.getRegion());
        }
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            NewDuel d = DuelAPI.getDuel(p);
            if (d != null) {
                if (d.getDuelState() == DuelState.PLAYING && d.getCreateState() == DuelCreateState.DONE) {
                    if (p.getLocation().getY() < -5.0 || !WorldGuardUtils.getRegionName(p.getLocation()).toLowerCase().startsWith(d.getArena().getRegion().toLowerCase())) {
                        d.removePlayer(p, Reason.LEFT_ARENA, false);
                        continue;
                    }
                    if (p.isOp() && !DuelArenaTask.isYoutuber(p) || !p.isFlying() || d.getSettings().hasSetting(Setting.FLY)) continue;
                    p.setAllowFlight(false);
                    p.setFlying(false);
                    continue;
                }
                if (d.getDuelState() != DuelState.IN_QUEUE || !p.getWorld().getName().equalsIgnoreCase(Arena.getDuelWorldName())) continue;
                d.forceTeleport(p, ((World)Bukkit.getWorlds().get(0)).getSpawnLocation());
                p.sendMessage((Object)ChatColor.RED + "You were inside of a duel arena without a duel.");
                continue;
            }
            if (DuelAPI.isSpectator(p)) {
                NewDuel duel = DuelAPI.getDuelSpectating(p);
                if (duel == null || duel.getDuelState() != DuelState.PLAYING || duel.getCreateState() != DuelCreateState.DONE) continue;
                if (p.getLocation().getY() < -5.0 || !WorldGuardUtils.getRegionName(p.getLocation()).toLowerCase().startsWith(duel.getArena().getRegion().toLowerCase())) {
                    duel.removeSpectator(p);
                    p.sendMessage((Object)ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You have exited the duel arena as a spectator.");
                    continue;
                }
                p.setAllowFlight(true);
                continue;
            }
            if (p.isOp() || !p.getWorld().getName().equals(Arena.getDuelWorldName())) continue;
            p.setMetadata("ignore_combat_tag", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDuel.get(), (Object)""));
            p.teleport(((World)Bukkit.getWorlds().get(0)).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            p.removeMetadata("ignore_combat_tag", (Plugin)CosmicDuel.get());
            p.sendMessage((Object)ChatColor.RED + "You were inside of a duel arena without a duel.");
        }
    }

    public static boolean isYoutuber(Player p) {
        return p.getName().equalsIgnoreCase("Woofless") || p.getName().equalsIgnoreCase("TBNRfrags");
    }
}


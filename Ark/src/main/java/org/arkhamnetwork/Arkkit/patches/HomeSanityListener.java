/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FactionUtils
 *  com.earth2me.essentials.User
 *  com.massivecraft.factions.Faction
 *  markehme.factionsplus.Cmds.CmdWarp
 *  markehme.factionsplus.extras.Warp
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.earth2me.essentials.User;
import com.massivecraft.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

final class HomeSanityListener
        implements Listener {
    private final boolean factionsPlus;
    List<World> whitelistedWorlds = new ArrayList<World>();

    public HomeSanityListener() {
        for (World w : Bukkit.getWorlds()) {
            if (!w.getName().equals("world") && !w.getName().equals("world_nether")) continue;
            this.whitelistedWorlds.add(w);
        }
        this.factionsPlus = Bukkit.getPluginManager().isPluginEnabled("FactionsPlus");
    }

    private boolean isValidHome(Location l) {
        return this.whitelistedWorlds.contains(l.getWorld()) && l.getY() >= 0.0 && (l.getWorld().getEnvironment() != World.Environment.NETHER || l.getY() < 128.0);
    }

    private boolean isEventWorldException(Location l, String cmd) {
        return l.getWorld().getName().equals("world_event") && l.getY() >= 0.0 && cmd.startsWith("/sethome");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Faction faction;
        if (e.getPlayer().isOp()) {
            return;
        }
        User u = SmarterEssentials.ess3.getUser(e.getPlayer());
        for (String home : u.getHomes()) {
            try {
                Location homeLocation = u.getHome(home);
                if (this.isValidHome(homeLocation) && homeLocation.getWorld().getName().equals("world_event")) continue;
                Bukkit.getLogger().info("[Arkkit (SmarterEssentials)] Deleted invalid home '" + home + "' (" + homeLocation + ") for player " + e.getPlayer().getName() + " (" + e.getPlayer().getUniqueId() + ")");
                u.delHome(home);
            } catch (Exception e1) {
                Bukkit.getLogger().info("[Arkkit (SmarterEssentials)] Could not parse home '" + home + "' for player " + e.getPlayer().getName() + " (" + e.getPlayer().getUniqueId() + "), attempting to delete...");
                e1.printStackTrace();
                try {
                    u.delHome(home);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase();
        if (cmd.startsWith("/sethome") || cmd.startsWith("/esethome") || cmd.startsWith("/createhome") || cmd.startsWith("/ecreatehome") || cmd.startsWith("/f setwarp") || cmd.startsWith("/f sethome") || cmd.startsWith("/f setallywarp") || cmd.startsWith("/f addwarp") || cmd.startsWith("/f createwarp")) {
            if (e.getPlayer().isOp()) {
                return;
            }
            if (!this.isValidHome(e.getPlayer().getLocation()) && !this.isEventWorldException(e.getPlayer().getLocation(), cmd)) {
                e.setMessage("/null");
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot create a /home or /f warp at this location.");
                e.getPlayer().sendMessage(ChatColor.GRAY + "You can only create homes and warps in the overworld and nether, and only above y:0 (or beneath y:128 in nether).");
            } else {
                Bukkit.getLogger().info("[Arkkit (SmarterEssentials)] Player " + e.getPlayer().getName() + " (" + e.getPlayer().getUniqueId() + ") issued command " + cmd + " at location " + e.getPlayer().getLocation());
            }
        }
    }
}


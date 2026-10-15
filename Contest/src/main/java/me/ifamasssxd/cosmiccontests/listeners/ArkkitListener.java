/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.events.EXPDirectDepositEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.ServerCommandEvent
 */
package me.ifamasssxd.cosmiccontests.listeners;

import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.arkhamnetwork.Arkkit.events.EXPDirectDepositEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ArkkitListener
implements Listener {
    @EventHandler
    public void onPlayerExp(EXPDirectDepositEvent event) {
        ContestUser user = ContestUser.getUser(event.getPlayer().getUniqueId());
        if (user == null) {
            Bukkit.getLogger().info("Null user for " + event.getPlayer().getName());
            return;
        }
        user.incrementValue(Contests.EXP_GAINED, event.getAmountToGive());
    }

    @EventHandler
    public void onPlayerCommandPreProcess(ServerCommandEvent event) {
        if (event.getCommand().startsWith("/fquests") && !Bukkit.getPluginManager().isPluginEnabled("CosmicFactionQuests")) {
            event.setCommand("/null");
        }
    }
}


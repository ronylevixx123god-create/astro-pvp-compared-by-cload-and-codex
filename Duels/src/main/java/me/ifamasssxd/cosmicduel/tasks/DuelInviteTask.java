/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.ifamasssxd.cosmicduel.tasks;

import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.DuelInvite;
import me.ifamasssxd.cosmicduel.struct.enums.CancelReason;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class DuelInviteTask
implements Runnable {
    @Override
    public void run() {
        for (final DuelInvite invites : new ArrayList<DuelInvite>(DuelAPI.invite_timer)) {
            if (invites.isPending()) continue;
            if (invites.getTime() <= 0) {
                new BukkitRunnable(){

                    public void run() {
                        Player pl;
                        DuelAPI.invite_timer.remove(invites);
                        Player p = Bukkit.getPlayer((String)invites.getInviter());
                        if (p != null) {
                            p.sendMessage((Object)ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "Your invite to " + invites.getInvited() + " has expired.");
                            p.playSound(p.getLocation(), Sound.GHAST_MOAN, 0.4f, 1.4f);
                            DuelAPI.cancelDuel(DuelAPI.getDuel(p), CancelReason.INVITE_EXPIRED);
                        }
                        if ((pl = Bukkit.getPlayer((String)invites.getInvited())) != null) {
                            pl.sendMessage((Object)ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "Your duel invite from " + invites.getInviter() + " has expired.");
                        }
                    }
                }.runTask((Plugin)CosmicDuel.get());
                continue;
            }
            DuelInvite duelInvite = invites;
            --duelInvite.time;
        }
    }
}


/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

final class RebootTask
        implements Runnable {
    int countdown = 61;

    RebootTask(int c) {
        this.countdown = c;
    }

    @Override
    public void run() {
        if (this.countdown > 0) {
            if (this.countdown >= 60 && this.countdown % 60 == 0 || this.countdown > 10 && this.countdown % 10 == 0 || this.countdown <= 5) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + " *** THE SERVER IS REBOOTING IN " + this.countdown + " second(s) ***");
                if (this.countdown == 60 || this.countdown == 59) {
                    Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "The server has detected a memory leak, rebooting to auto-fix.");
                    Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "** This is " + ChatColor.UNDERLINE + "NOT" + ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + " a rollback!");
                }
                Bukkit.broadcastMessage("");

            }
        } else {
            Bukkit.getServer().shutdown();
        }
        --this.countdown;
    }
}


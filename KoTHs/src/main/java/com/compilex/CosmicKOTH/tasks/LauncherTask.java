/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 */
package com.compilex.CosmicKOTH.tasks;

import com.compilex.CosmicKOTH.CosmicKOTHAPI;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.StringUtils;
import org.bukkit.Bukkit;

public class LauncherTask implements Runnable {
    public static int countdown;

    public LauncherTask(int secondsUntilKoth) {
        countdown = secondsUntilKoth;
    }

    public void run() {
        if (CosmicKOTHAPI.isKOTHRunning()) {
            return;
        }

        countdown--;

        if (countdown == 1800) {
            this.notifyPlayers("30 Minutes");
        }

        if (countdown == 900) {
            this.notifyPlayers("15 Minutes");
        }

        if (countdown == 600) {
            this.notifyPlayers("10 Minutes");
        }

        if (countdown == 300) {
            this.notifyPlayers("5 Minutes");
        }

        if (countdown == 240) {
            this.notifyPlayers("4 Minutes");
        }

        if (countdown == 180) {
            this.notifyPlayers("3 Minutes");
        }

        if (countdown == 120) {
            this.notifyPlayers("2 Minutes");
        }

        if (countdown == 60) {
            this.notifyPlayers("1 Minute");
        }

        if (countdown <= 10 && countdown > 0) {
            this.notifyPlayers(countdown + " Seconds");
        }

        if (countdown == 0) {
            CosmicKOTHAPI.resetKOTH();

        }

        /*if (countdown < 0) {
            countdown = 0;
        }*/





    }



    private void notifyPlayers(String delay) {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&6&lAstro&f&lPvP &8| &6&lDesert K.O.T.H")));
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&fA &6/koth&f event is starting soon")));
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&fCapture koth to win a &610$ Buycraft Giftcard&f.")));
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(""));
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&6&lTime Left&7: &f" + delay)));
        Bukkit.broadcastMessage("");
    }
}


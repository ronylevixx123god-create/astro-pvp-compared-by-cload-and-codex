package com.cosmicpvp.cosmicwildpvp.util;

import com.cosmicpvp.cosmicwildpvp.WildPvPPlugin;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NoCleanTimer extends BukkitRunnable
{
    private Player player;
    private int timeRemaining;

    public NoCleanTimer(final Player player, final int duration) {
        this.player = player;
        this.timeRemaining = duration;
        this.runTaskTimer((Plugin) WildPvPPlugin.getInstance(), 20L, 20L);
    }

    public void run() {
        if (this.timeRemaining == 0) {
            try {
                BryUtil.sendMessage((CommandSender)this.player, "&cNo Clean Protection - &lEXPIRED");
                BryUtil.playSound(this.player, Sound.NOTE_BASS);
            }
            catch (Exception ex) {}
            return;
        }
        try {
            BryUtil.sendMessage((CommandSender)this.player, "&cNo Clean Protection - &lExpiring in " + this.timeRemaining + "s... &7(&C/spawn&7 to exit)");
            BryUtil.playSound(this.player, Sound.ORB_PICKUP);
        }
        catch (Exception ex2) {}
        --this.timeRemaining;
    }

    public synchronized void cancel() throws IllegalStateException {
        this.player = null;
        super.cancel();
    }
}

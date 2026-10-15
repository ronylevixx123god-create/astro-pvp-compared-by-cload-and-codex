package com.cosmicpvp.cosmicwildpvp.util;

import com.cosmicpvp.cosmicwildpvp.WildPvPPlugin;
import com.cosmicpvp.cosmicwildpvp.queue.SingleWildQueue;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.FixedMetadataValue;

import org.bukkit.scheduler.BukkitRunnable;

public class InvincibilityTimer extends BukkitRunnable
{
    private SingleWildQueue queue;
    private int timeRemaining;

    public InvincibilityTimer(final SingleWildQueue queue, final int duration) {
        this.queue = queue;
        this.timeRemaining = duration;
        queue.getBoth().forEach(player -> player.setMetadata("wildpvp_timer", (MetadataValue)new FixedMetadataValue((Plugin) WildPvPPlugin.getInstance(), (Object)true)));
        this.runTaskTimer((Plugin)WildPvPPlugin.getInstance(), 20L, 20L);
    }

    public void run() {
        if (this.timeRemaining == 0) {
            try {
                this.queue.getBoth().forEach(player -> {
                    BryUtil.sendMessage((CommandSender)player, "&aInvincibility - &lEXPIRED");
                    BryUtil.playSound(player, Sound.NOTE_BASS);
                    player.removeMetadata("wildpvp_timer", (Plugin)WildPvPPlugin.getInstance());
                    return;
                });
            }
            catch (Exception ex) {}
            this.cancel();
            this.queue.endSuccesfully();
            this.remove();
            return;
        }
        try {
            this.queue.getBoth().forEach(player -> {
                BryUtil.sendMessage((CommandSender)player, "&aInvincibility - &lExpiring in " + this.timeRemaining + "s... &7(&C/spawn&7 to exit)");
                BryUtil.playSound(player, Sound.ORB_PICKUP);
                return;
            });
        }
        catch (Exception ex2) {}
        --this.timeRemaining;
    }

    public void remove() {
        try {
            this.queue.getBoth().forEach(player -> player.removeMetadata("wildpvp_timer", (Plugin)WildPvPPlugin.getInstance()));
        }
        catch (Exception ex) {}
        this.queue = null;
        this.cancel();
    }
}

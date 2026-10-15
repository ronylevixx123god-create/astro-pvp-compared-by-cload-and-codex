package com.astropvp.wildpvp.util;


import com.astropvp.wildpvp.AstroWildPvP;
import com.astropvp.wildpvp.queue.WildQueue;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class InvincibilityTimer extends BukkitRunnable {
    private WildQueue queue;
    private int duration;
    private int timeRemaining;

    public InvincibilityTimer(WildQueue queue, int duration) {
        this.queue = queue;
        this.duration = duration;
        this.timeRemaining = duration;
        this.runTaskTimer(AstroWildPvP.getInstance(), 20L, 20L);
    }

    public void run() {
        try {
            if (this.timeRemaining == 0) {
                this.queue.getBoth().forEach((player) -> {
                    KrizzUtil.sendMessage((LivingEntity) player, (String) "&aInvincibility - &lEXPIRED");
                    KrizzUtil.playSound(player, Sound.NOTE_BASS);
                });
                this.cancel();
                this.queue.endSuccesfully();
                this.remove();
            } else {
                this.queue.getBoth().forEach((player) -> {
                    KrizzUtil.sendMessage((LivingEntity) player, (String) ("&aInvincibility - &lExpiring in " + this.timeRemaining + "s... &7(&C/spawn&7 to exit)"));
                    KrizzUtil.playSound(player, Sound.ORB_PICKUP);
                });
                --this.timeRemaining;
            }
        } catch (Exception ignored) {}
    }

    public void remove() {
        this.queue = null;
        this.cancel();
    }
}

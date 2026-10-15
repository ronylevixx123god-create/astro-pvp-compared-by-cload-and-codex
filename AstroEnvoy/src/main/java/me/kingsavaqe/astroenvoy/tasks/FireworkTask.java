package me.kingsavaqe.astroenvoy.tasks;

import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class FireworkTask extends BukkitRunnable {

    @Override
    public void run() {
        for (int i = 0; i < EnvoyPlugin.getInstance().getEnvoyManager().getCurrentChests().size(); i++) {

            Location location = EnvoyPlugin.getInstance().getEnvoyManager().getCurrentChests().get(i);
            Firework firework = location.getWorld().spawn(location.clone().add(0.5D, 0.0D, 0.5D), Firework.class);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.WHITE)
                    .with(FireworkEffect.Type.BALL_LARGE).flicker(true).withTrail().build());
            firework.setFireworkMeta(fireworkMeta);
            fireworkMeta.setPower(5);
            Bukkit.getScheduler().runTaskLater(EnvoyPlugin.getInstance(), firework::detonate, 20L);
        }
    }
}

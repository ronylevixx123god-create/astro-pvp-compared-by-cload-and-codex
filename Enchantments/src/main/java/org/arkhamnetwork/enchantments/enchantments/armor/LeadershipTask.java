package org.arkhamnetwork.enchantments.enchantments.armor;

import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import de.inventivegames.particle.ParticleEffect;
import lombok.SneakyThrows;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

final class LeadershipTask extends BukkitRunnable {
    @SneakyThrows
    public void run() {
        for (final Map.Entry<String, Integer> LeadershipData : Leadership.Leaderships.entrySet()) {
            final String playerName = LeadershipData.getKey();
            final int level = LeadershipData.getValue();
            final Player p = Bukkit.getPlayer(playerName);
            if (p == null) {
                Leadership.Leaderships.remove(playerName);
            } else {
                final Location l = p.getEyeLocation().add(0.0, 0.5, 0.0);
                int x = 0;
                for (final Entity ent : p.getNearbyEntities((double) (level * 2 + 3), (double) (level * 2 + 3), (double) (level * 2 + 3))) {
                    if (ent instanceof Player && this.isAlly(p, (Player) ent)) {
                        ++x;
                    }
                }
                p.setMetadata("leadershipLevel", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) x));
                if (x <= 0 || l.getWorld().getName().equals("world_duels") || l.getWorld().getName().equals("world_duels2") || l.getWorld().getName().equals("cosmic-pvparenas")) {
                    continue;
                }
                WorldUtil.sendWorldEffect(l.getBlock(), Material.SNOW_BLOCK.getId(), 2001);
            }
        }
    }

    private boolean isAlly(final Player p1, final Player p2) {
        return GeneralUtil.isAlly(p1, p2);
    }
}
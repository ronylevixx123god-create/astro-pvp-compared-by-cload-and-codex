package me.ifamasssxd.cosmicbosses.bosses.yijki;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.BlockModifier;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossAbility;
import me.ifamasssxd.cosmicbosses.tasks.BleedTask;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Cage extends BossAbility {
    Random random;

    public Cage(final Boss boss, final BlockModifier modifier) {
        super(boss, modifier);
        this.random = new Random();
    }

    @Override
    public void onAbility() {
        for (final Entity ent : this.getBoss().getNearbyEntities(20.0, EntityType.PLAYER)) {
            final Player p = (Player) ent;
            final Location location = p.getLocation();
            if (!BossUtils.canAffectPlayer(p, true)) {
                continue;
            }
            final int minX = location.getBlockX() - 1;
            final int maxX = location.getBlockX() + 2;
            final int minY = location.getBlockY() - 1;
            final int maxY = location.getBlockY() + 4;
            final int minZ = location.getBlockZ() - 1;
            final int maxZ = location.getBlockZ() + 2;
            p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0f, 1.4f);
            Location loc = p.getLocation().clone();
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 4));
            BleedTask.bleedPlayer(p, 4);
            for (int x = minX; x < maxX; ++x) {
                for (int y = minY; y < maxY; ++y) {
                    for (int z = minZ; z < maxZ; ++z) {
                        loc.setY((double) y);
                        loc.setZ((double) z);
                        loc.setX((double) x);
                        loc = loc.getBlock().getLocation();
                        if (!this.getBlockModifier().containsLocation(this, loc)) {
                            final Block b = loc.getBlock();
                            Material mat = Material.IRON_FENCE;
                            if (b.getType() == Material.AIR) {
                                if (y == minY || y == maxY - 1) {
                                    mat = Material.OBSIDIAN;
                                }
                                if (x == location.getBlockX() && z == location.getBlockZ() && y > minY && y < maxY - 1) {
                                    this.getBlockModifier().storeBlockToRemove(this, b.getLocation());
                                } else {
                                    this.getBlockModifier().storeBlockToRemove(this, b.getLocation());
                                    b.setType(mat);
                                }
                            }
                        }
                    }
                }
            }
        }
        final Cage instance = this;
        new BukkitRunnable() {
            public void run() {
                if (Cage.this.getBoss() == null || !Cage.this.getBoss().isBossSpawned()) {
                    return;
                }
                Cage.this.getBlockModifier().removeBlocks(instance, true, true);
            }
        }.runTaskLater((Plugin) CosmicBosses.getInstance(), 120L);
        this.activateCooldown();
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(60L);
    }

    @Override
    public String getAbilityName() {
        return "Cage";
    }
}

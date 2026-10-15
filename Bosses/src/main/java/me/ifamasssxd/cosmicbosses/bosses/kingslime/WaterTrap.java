package me.ifamasssxd.cosmicbosses.bosses.kingslime;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.BlockModifier;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossAbility;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WaterTrap extends BossAbility {
    Random random;
    int maxPackets;

    public WaterTrap(final Boss boss, final BlockModifier modifier) {
        super(boss, modifier);
        this.random = new Random();
        this.maxPackets = 0;
    }

    @Override
    public void onAbility() {
        for (final Entity e : this.getBoss().getNearbyEntities(20.0, EntityType.PLAYER)) {
            final Player p = (Player) e;
            if (!BossUtils.canAffectPlayer(p, false)) {
                continue;
            }
            final int minX = p.getLocation().getBlockX() - 1;
            final int maxX = p.getLocation().getBlockX() + 2;
            final int minY = p.getLocation().getBlockY() - 1;
            final int maxY = p.getLocation().getBlockY() + 3;
            final int minZ = p.getLocation().getBlockZ() - 1;
            final int maxZ = p.getLocation().getBlockZ() + 2;
            final Location location = p.getLocation().clone();
            for (int x = minX; x < maxX; ++x) {
                for (int y = minY; y < maxY; ++y) {
                    for (int z = minZ; z < maxZ; ++z) {
                        location.setX((double) x);
                        location.setY((double) y);
                        location.setZ((double) z);
                        final Block block = location.getBlock();
                        if (!this.getBlockModifier().containsLocation(this, block.getLocation())) {
                            if (block.getType() == Material.AIR) {
                                if (x == p.getLocation().getBlockX() && z == p.getLocation().getBlockZ() && y < maxY - 1 && y > minY) {
                                    if (this.getBoss().getPhase() == Phase.TWO) {
                                        block.setType(Material.LAVA);
                                    } else {
                                        block.setType(Material.WATER);
                                    }
                                } else {
                                    block.setType(Material.STAINED_GLASS);
                                    if (this.getBoss().getPhase() == Phase.TWO) {
                                        block.setData((byte) 14);
                                    } else {
                                        block.setData((byte) 5);
                                    }
                                }
                                this.getBlockModifier().storeBlockToRemove(this, block.getLocation());
                            }
                        }
                    }
                }
            }
            p.setRemainingAir(1);
            final Location loc = p.getLocation().getBlock().getLocation().clone().add(0.5, 0.0, 0.5);
            loc.setYaw(p.getLocation().getYaw());
            loc.setPitch(p.getLocation().getPitch());
            p.teleport(loc);
        }
        final WaterTrap instance = this;
        new BukkitRunnable() {
            public void run() {
                if (WaterTrap.this.getBlockModifier() != null) {
                    WaterTrap.this.getBlockModifier().removeBlocks(instance, true, true);
                }
            }
        }.runTaskLater((Plugin) CosmicBosses.getInstance(), 100L);
        this.activateCooldown();
    }

    @Override
    public String getAbilityName() {
        return "Water Trap";
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(20L);
    }
}

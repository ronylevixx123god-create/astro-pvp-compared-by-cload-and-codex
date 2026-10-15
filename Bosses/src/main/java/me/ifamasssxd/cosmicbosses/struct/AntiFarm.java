package me.ifamasssxd.cosmicbosses.struct;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.GuardBoss;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class AntiFarm {
    private final int THRESHHOLD = 20;
    Boss boss;
    private Location lastLocation;
    int stuckTicks;
    private int unstickAttempts;

    public AntiFarm(final Boss boss) {
        this.stuckTicks = 0;
        this.unstickAttempts = 0;
        this.boss = boss;
    }

    public boolean isStuck() {
        return this.stuckTicks >= 20;
    }

    public boolean checkStuck() {
        if (this.lastLocation != null && this.lastLocation.getWorld() == this.getBoss().getEntity().getWorld()) {
            final double distance = this.lastLocation.distanceSquared(this.getBoss().getEntity().getLocation());
            if (distance <= 10.0) {
                ++this.stuckTicks;
            } else {
                this.stuckTicks = 0;
            }
        }
        this.lastLocation = this.getEntity().getLocation().clone();
        if (BossUtils.isWarzone(this.lastLocation) || this.lastLocation.getWorld().getName().equals(CosmicBosses.battlefieldWorld) || this.boss instanceof GuardBoss || this.boss instanceof MiniBoss) {
            return this.isStuck();
        }
        if (this.unstickAttempts >= 5) {
            this.getBoss().alertNearby(30, "Removing boss due to luring out of warzone > 5 times.", Sound.ENDERDRAGON_GROWL, 1.4f);
            this.getBoss().onDeath(null);
            this.getBoss().getEntity().remove();
            return false;
        }
        ++this.unstickAttempts;
        return true;
    }

    public boolean checkStuck(final Boss boss) {
        return boss.checkStuck();
    }

    public void unstickBoss() {
        final Entity target = this.getTarget();
        final double distance = (target != null && target.getWorld().equals(this.getEntity().getWorld())) ? target.getLocation().distanceSquared(this.getEntity().getLocation()) : -1.0;
        final List<Entity> nearby = (List<Entity>) this.getBoss().getEntity().getNearbyEntities(15.0, 15.0, 15.0);
        Location targetTeleport;
        if (target != null && target instanceof Player && target.getWorld().equals(this.getEntity().getWorld()) && distance <= 1000.0 && BossUtils.canAffectPlayer((Player) target, false)) {
            final Location newLocation = target.getLocation().clone();
            if (this.getBoss().getEntity().isInsideVehicle()) {
                this.getBoss().getEntity().getVehicle().teleport(newLocation);
            }
            targetTeleport = newLocation;
        } else {
            if (this.getBoss().getEntity().isInsideVehicle()) {
                this.getBoss().getEntity().getVehicle().teleport(this.getBoss().getLocation());
            }
            targetTeleport = this.getBoss().getLocation();
        }
        if (this.getBoss().getEntity().getLocation().distanceSquared(targetTeleport) > 10.0 && !(this.getBoss() instanceof GuardBoss) && !(this.getBoss() instanceof MotherOfYijki)) {
            for (final Entity entity : nearby) {
                if (entity instanceof Player) {
                    final Player pl = (Player) entity;
                    pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "The " + this.getBoss().getCustomName() + ChatColor.YELLOW + " has teleported back to their spawn location!");
                }
            }
        }
        this.getBoss().getEntity().teleport(targetTeleport);
        this.stuckTicks = 0;
        System.out.println("Unsticking boss " + this.boss.getName());
    }

    public Entity getTarget() {
        if (this.getLivingEntity() instanceof Creature) {
            final Creature creature = (Creature) this.getLivingEntity();
            if (creature.getTarget() != null) {
                return (Entity) creature.getTarget();
            }
        }
        return null;
    }

    public Entity getEntity() {
        return (Entity) this.getBoss().getEntity();
    }

    public LivingEntity getLivingEntity() {
        return (LivingEntity) this.getEntity();
    }

    public Boss getBoss() {
        return this.boss;
    }

    public void setLastLocation(final Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void setStuckTicks(final int stuckTicks) {
        this.stuckTicks = stuckTicks;
    }

    public int getStuckTicks() {
        return this.stuckTicks;
    }
}

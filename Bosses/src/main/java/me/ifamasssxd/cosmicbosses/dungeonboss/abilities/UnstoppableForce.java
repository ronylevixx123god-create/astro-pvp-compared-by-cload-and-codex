package me.ifamasssxd.cosmicbosses.dungeonboss.abilities;

import lombok.SneakyThrows;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijkiEntity;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.concurrent.TimeUnit;

public class UnstoppableForce extends DungeonAbility {
    private Location chargeLocation;
    private Vector direction;
    int ticks;
    long lastSent;

    public UnstoppableForce(final Boss boss) {
        super(boss);
        this.ticks = 0;
        this.chargeLocation = boss.getLocation();
    }

    @Override
    public void onTick() {
        ++this.ticks;
        final long time = System.currentTimeMillis() - this.initTime;
        if (time >= 5000L && this.direction == null) {
            final DungeonYijkiEntity dungeonYijkiEntity = ((DungeonYijki) this.getBoss()).getBossEntity();
            if (dungeonYijkiEntity.getGoalTarget() != null) {
                this.direction = dungeonYijkiEntity.getGoalTarget().getBukkitEntity().getLocation().toVector().subtract(this.getBoss().getEntity().getLocation().toVector());
                if (this.direction.length() != 0.0) {
                    this.direction = this.direction.normalize();
                    this.direction = this.direction.multiply(1.26);
                }
            } else {
                this.direction = this.getBoss().getEntity().getLocation().getDirection();
            }
        }
        if (!this.shouldCharge()) {
            if (System.currentTimeMillis() - this.lastSent >= 1000L || this.lastSent == 0L) {
                this.lastSent = System.currentTimeMillis();
                for (final Player player : this.getBoss().getEntity().getWorld().getPlayers()) {
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Yijki >> " + ChatColor.GRAY + "Charging in " + ChatColor.RED + ChatColor.BOLD + (int) Math.max(0.0, Math.ceil((double) (5L - (System.currentTimeMillis() - this.initTime) / 1000L))) + "s");
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0f, 1.1f);
                }
            }
            if (this.ticks % 4 == 0) {
                try {
                    de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), this.getBoss().getEntity().getLocation(), 1f, 1f, 1f, 0.1f, 20);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            if (this.ticks % 3 == 0) {
                drawParticleLineInDirection(this.getBoss().getEntity());
            }
        }
    }

    public static void drawParticleLineInDirection(final LivingEntity entity) {
        try {
            final BlockIterator iterator = new BlockIterator(entity, 8);
            iterator.forEachRemaining(bl -> {
                try {
                    de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), bl.getLocation(), 1f, 1f, 1f, 0.1f, 20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception ex) {
        }
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(30L);
    }

    @Override
    public boolean shouldStopTicking() {
        final long timeSinceStarted = System.currentTimeMillis() - this.initTime;
        return timeSinceStarted >= 6400L;
    }

    public boolean shouldCharge() {
        return System.currentTimeMillis() - this.initTime >= 5000L && this.direction != null;
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();
        this.boss.sendMessage("I cannot be stopped!");
    }

    @SneakyThrows
    @Override
    public void onAbilityDisable() {
        if (this.boss.getEntity() != null) {
            ((DungeonYijki) this.boss).getBossEntity().getNavigation().n();
            this.boss.getEntity().teleport(((DungeonYijki) this.boss).getRandomSpawnLocation());
            ((DungeonYijki) this.boss).getBossEntity().getNavigation().n();
            Bukkit.getLogger().info("Teleporting Yijki back to " + this.boss.getLocation());
        }
        de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), this.boss.getEntity().getLocation(), 3f, 3f, 3f, 0.09f, 15);

    }

    @Override
    public void onAbilityEvent(final Event event) {
    }

    @Override
    public String getAbilityName() {
        return "Unstoppable Force";
    }

    public Location getChargeLocation() {
        return this.chargeLocation;
    }

    public Vector getDirection() {
        return this.direction;
    }
}

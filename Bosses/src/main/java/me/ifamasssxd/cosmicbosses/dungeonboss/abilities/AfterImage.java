package me.ifamasssxd.cosmicbosses.dungeonboss.abilities;

import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

public class AfterImage extends DungeonAbility {
    private Set<Entity> imagesAlive;
    private long start;

    public AfterImage(final Boss boss) {
        super(boss);
        this.imagesAlive = Collections.newSetFromMap(new WeakHashMap<Entity, Boolean>());
    }

    @Override
    public void onTick() {
        if (this.start != -1L && System.currentTimeMillis() - this.start >= 3000L) {
            this.start = -1L;
            for (final Player pl : this.boss.getEntity().getWorld().getPlayers()) {
                pl.sendMessage(ChatColor.RED + "Yijki's After Images are preparing to charge!");
            }
        }
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(8L);
    }

    public long getSoftCooldown() {
        return TimeUnit.SECONDS.toMillis(10L);
    }

    @Override
    public String getAbilityName() {
        return "After Image";
    }

    @Override
    public boolean shouldStopTicking() {
        final long timeSinceStarted = System.currentTimeMillis() - this.initTime;
        return (!this.isAnyImagesAlive() && timeSinceStarted >= this.getCooldown()) || timeSinceStarted >= this.getSoftCooldown();
    }

    @Override
    public void onAbilityEnable() {
        this.imagesAlive.stream().filter(ent -> !ent.isDead()).forEach(Entity::remove);
        this.imagesAlive.clear();
        this.start = System.currentTimeMillis();
        this.initAbility();
        for (int i = 0; i < 3; ++i) {
            this.imagesAlive.add(this.boss.spawnMinion(EntityType.WITHER, this.getBoss().getEntity().getLocation()));
        }
        this.boss.sendMessage(ChatColor.RED + "Lets make this more interesting!", Sound.WITHER_SPAWN, 1.0f);
    }

    public boolean isAnyImagesAlive() {
        return this.imagesAlive.stream().anyMatch(ent -> !ent.isDead());
    }

    @Override
    public void onAbilityDisable() {
        for (final Entity ent : this.boss.getMinions()) {
            if (ent != null && !ent.isDead()) {
                try {
                    de.inventivegames.particle.ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), ent.getLocation().add(0.0, 1.0, 0.0), 0f, 0f, 0f, 0.1f, 30);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        this.boss.removeMinions();
        this.imagesAlive.clear();
    }

    @Override
    public void onAbilityEvent(final Event event) {
    }
}

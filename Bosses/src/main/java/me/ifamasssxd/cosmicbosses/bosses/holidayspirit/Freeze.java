package me.ifamasssxd.cosmicbosses.bosses.holidayspirit;

import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossAbility;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class Freeze extends BossAbility {
    public Freeze(final Boss boss) {
        super(boss);
    }

    @Override
    public void onAbility() {
        for (final Entity ent : this.getBoss().getNearbyEntities(20.0, EntityType.PLAYER)) {
            final Player player = (Player) ent;
            final float walkSpeed = player.getWalkSpeed();
            final Location nearby = BossUtils.getRandomLocationNearby(player.getLocation(), 3, 1);
            this.getBoss().spawnMinion(EntityType.PIG_ZOMBIE, nearby);
            PotionEffect activeJumpBoost = null;
            for (final PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().getName().contains("JUMP")) {
                    activeJumpBoost = new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier());
                    break;
                }
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, -5), true);
            player.setWalkSpeed(0.0f);
            final PotionEffect found = activeJumpBoost;
            new BukkitRunnable() {
                public void run() {
                    float speed = walkSpeed;
                    if (speed > 0.2) {
                        speed = 0.2f;
                    }
                    if (speed <= 0.0f) {
                        speed = 0.2f;
                    }
                    player.setWalkSpeed(speed);
                    player.removePotionEffect(PotionEffectType.JUMP);
                    if (found != null) {
                        player.addPotionEffect(found, true);
                    }
                }
            }.runTaskLater((Plugin) CosmicBosses.getInstance(), 100L);
        }
        this.activateCooldown();
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(30L);
    }

    @Override
    public String getAbilityName() {
        return "Freeze";
    }
}

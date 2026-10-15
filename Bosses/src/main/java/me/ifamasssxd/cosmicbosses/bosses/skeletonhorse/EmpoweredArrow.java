package me.ifamasssxd.cosmicbosses.bosses.skeletonhorse;

import lombok.SneakyThrows;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossAbility;
import me.ifamasssxd.cosmicbosses.struct.Phase;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EmpoweredArrow extends BossAbility {
    boolean isEmpowered;
    Random random;

    public EmpoweredArrow(final Boss boss) {
        super(boss);
        this.isEmpowered = false;
        this.random = new Random();
    }

    @Override
    public void onAbility() {
        if (!this.getBoss().getEntity().hasMetadata("empower_arrow")) {
            new BukkitRunnable() {
                int counter = 140;

                @SneakyThrows
                public void run() {
                    --this.counter;
                    if (EmpoweredArrow.this.getBoss() == null || EmpoweredArrow.this.getBoss().getEntity().isDead() || EmpoweredArrow.this.getBoss().getPhase() == Phase.TWO) {
                        this.cancel();
                        return;
                    }
                    if (EmpoweredArrow.this.getBoss().getEntity().hasMetadata("empower_arrow")) {
                        de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), EmpoweredArrow.this.getBoss().getEntity().getLocation(), 0, 0, 0, 0.6f, 50);
                        de.inventivegames.particle.ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), EmpoweredArrow.this.getBoss().getEntity().getLocation(), 0, 0, 0, 0.6f, 10);

                        return;
                    }
                    if (EmpoweredArrow.this.isEmpowered) {
                        this.cancel();
                        EmpoweredArrow.this.isEmpowered = false;
                        return;
                    }
                    if (this.counter <= 0) {
                        EmpoweredArrow.this.getBoss().getEntity().setMetadata("empower_arrow", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
                        EmpoweredArrow.this.getBoss().getEntity().getWorld().playSound(EmpoweredArrow.this.getBoss().getEntity().getLocation(), Sound.SKELETON_IDLE, 10.0f, 0.5f);
                        EmpoweredArrow.this.isEmpowered = true;
                    } else {
                        de.inventivegames.particle.ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), EmpoweredArrow.this.getBoss().getEntity().getLocation(), 0, 0, 0, 0.6f, 50);
                    }
                }
            }.runTaskTimer((Plugin) CosmicBosses.getInstance(), 20L, 1L);
        }
        this.activateCooldown();
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(10L);
    }

    @Override
    public String getAbilityName() {
        return "Empowered Arrow";
    }
}

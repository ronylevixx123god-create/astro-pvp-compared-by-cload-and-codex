package me.ifamasssxd.cosmicbosses.bosses.plaguebloater;

import lombok.SneakyThrows;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossAbility;
import me.ifamasssxd.cosmicbosses.struct.EntityContainer;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlagueBomb extends BossAbility implements EntityContainer {
    private Set<Entity> primedTNT;
    public static List<UUID> tntList = new ArrayList<>();

    Random random;

    public PlagueBomb(final Boss boss) {
        super(boss);
        this.primedTNT = Collections.newSetFromMap(new WeakHashMap<Entity, Boolean>());
        this.random = new Random();
        new BukkitRunnable() {
            int loops = 0;

            @SneakyThrows
            public void run() {
                if (PlagueBomb.this.getBoss() == null || !PlagueBomb.this.getBoss().isBossSpawned()) {
                    this.cancel();
                    return;
                }
                ++this.loops;
                if (PlagueBomb.this.primedTNT == null || PlagueBomb.this.primedTNT.isEmpty()) {
                    return;
                }
                final Iterator<Entity> iter = PlagueBomb.this.primedTNT.iterator();
                while (iter.hasNext()) {
                    final Entity primed = iter.next();
                    if (primed == null || !primed.isValid()) {
                        iter.remove();
                    } else {
                        final Location location = primed.getLocation();
                        if (PlagueBomb.this.random.nextInt(10) <= 2) {
                            //ParticleEffect.sendToLocation(ParticleEffect.LARGE_SMOKE, location, 3.0f, 3.0f, 3.0f, 0.05f, 30);
                            de.inventivegames.particle.ParticleEffect.LARGE_SMOKE.sendToPlayers(Bukkit.getOnlinePlayers(), location, 3f, 3f, 3f, 0.05f, 30);
                        }
                        if (this.loops % 20 != 0) {
                            continue;
                        }
                        for (final Entity ent : primed.getNearbyEntities(10.0, 5.0, 10.0)) {
                            if (ent instanceof Player) {
                                final Player pl = (Player) ent;
                                pl.playNote(location, Instrument.PIANO, new Note(4));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer((Plugin) CosmicBosses.getInstance(), 2L, 1L);
    }

    @Override
    public void onAbility() {
        this.primedTNT.forEach(Entity::remove);
        this.primedTNT.clear();
        for (int i = 0; i < 3; ++i) {
            this.createPlagueBomb(BossUtils.getRandomLocationNearby(this.getBoss().getEntity().getLocation(), 15, 10), 100);
        }
        this.getBoss().sendMessage("Plague Bomb Dropped", Sound.FIRE_IGNITE, 0.7f);
        this.activateCooldown();
    }

    public TNTPrimed createPlagueBomb(final Location l, final int fuse) {
        final TNTPrimed primed = (TNTPrimed) l.getWorld().spawnEntity(l, EntityType.PRIMED_TNT);
        primed.setMetadata("do_not_clear", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        primed.setMetadata("death_plague", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
        tntList.add(primed.getUniqueId());
        primed.setFuseTicks(fuse);
        this.primedTNT.add((Entity) primed);
        return primed;
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(15L);
    }

    @Override
    public String getAbilityName() {
        return "Plague Bomb";
    }

    @Override
    public Set<Entity> getEntities() {
        return this.primedTNT;
    }

    @Override
    public void clearEntities() {
        for (final Entity e : this.getEntities()) {
            if (e != null) {
                e.remove();
            }
        }
        this.primedTNT.clear();
    }

    public Set<Entity> getPrimedTNT() {
        return this.primedTNT;
    }
}

package me.ifamasssxd.cosmicbosses.dungeonboss.abilities;

import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.struct.BlockModifier;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TemporalRift extends DungeonAbility {
    int shotsFired;
    private long delay;
    private int landed;

    public TemporalRift(final Boss boss) {
        super(boss);
        this.shotsFired = 0;
        this.delay = System.currentTimeMillis();
        this.landed = 0;
    }

    @Override
    public void onTick() {
        if (this.delay != 0L && System.currentTimeMillis() - this.delay >= 500L) {
            this.delay = System.currentTimeMillis();
            final LivingEntity ent = this.boss.getEntity();
            final Location location = ent.getEyeLocation();
            final Random r = ThreadLocalRandom.current();
            Vector vec = new Vector((r.nextBoolean() ? -1 : 1) * Math.random(), -1.5, (r.nextBoolean() ? -1 : 1) * Math.random());
            final WorldServer server = ((CraftWorld) location.getWorld()).getHandle();
            final EntityWitherSkull skull = new EntityWitherSkull(server, ((DungeonYijki) this.boss).getBossEntity(), vec.getX(), vec.getY(), vec.getZ()) {
                protected void a(final MovingObjectPosition movingobjectposition) {
                    final Entity hit = movingobjectposition.entity;
                    if (hit != null && !(hit instanceof EntityHuman)) {
                        return;
                    }
                    super.a(movingobjectposition);
                }
            };
            location.getWorld().playSound(location, Sound.WITHER_SHOOT, 1.0f, 0.7f);
            skull.setCharged(true);
            if (vec.length() != 0.0) {
                vec = vec.normalize();
            }
            skull.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            skull.getBukkitEntity().setVelocity(vec);
            skull.getBukkitEntity().setMetadata("temporalRift", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) this.boss.getUuid()));
            server.addEntity((Entity) skull, CreatureSpawnEvent.SpawnReason.CUSTOM);
            ++this.shotsFired;
        }
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(10L);
    }

    @Override
    public boolean shouldStopTicking() {
        return this.shotsFired >= 20 && (this.landed >= 12 || System.currentTimeMillis() - this.initTime >= this.getCooldown());
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();
        this.delay = System.currentTimeMillis();
        this.boss.sendMessage("Fall into my universe mortal!");
    }

    @Override
    public void onAbilityDisable() {
    }

    @Override
    public void onAbilityEvent(final Event event) {
        if (event instanceof ProjectileHitEvent) {
            ++this.landed;
            final ProjectileHitEvent e = (ProjectileHitEvent) event;
            Block block = e.getEntity().getLocation().getBlock();
            if (block.getType() == Material.AIR) {
                block = block.getRelative(BlockFace.DOWN);
            }
            if (block.getType().isSolid() && block.getRelative(BlockFace.UP).getType() == Material.AIR && !this.boss.getModifier().containsLocation(block.getLocation())) {
                final List<Block> changed = (List<Block>) Lists.newArrayList(new Block[]{block});
                for (int radius = 1, x = -radius; x < radius; ++x) {
                    for (int z = -radius; z < radius; ++z) {
                        final Block toExplode = block.getLocation().add((double) x, 0.0, (double) z).getBlock();
                        if (!toExplode.equals(block)) {
                            if (!ThreadLocalRandom.current().nextBoolean()) {
                                if (!WorldGuardUtils.hasFlagDisabled(toExplode.getLocation(), (Flag<?>) DefaultFlag.OTHER_EXPLOSION)) {
                                    final Block above = toExplode.getRelative(BlockFace.UP);
                                    if (above.getType() == Material.AIR && toExplode.getType().isSolid()) {
                                        if (!toExplode.getType().name().toLowerCase().endsWith("_PLATE")) {
                                            this.boss.getModifier().storeBlockAndSet(this, toExplode.getLocation(), Material.ENDER_PORTAL, (byte) 0);
                                            toExplode.setMetadata("temporalRift", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) this.boss.getUuid()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (WorldGuardUtils.hasFlagDisabled(block.getLocation(), (Flag<?>) DefaultFlag.OTHER_EXPLOSION)) {
                    return;
                }
                final BlockModifier mod = this.boss.getModifier();
                mod.storeBlockAndSet(this, block.getLocation(), Material.ENDER_PORTAL, (byte) 0);
                block.setMetadata("temporalRift", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) this.boss.getUuid()));
                final List<Block> list;
                final BlockModifier blockModifier;
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicBosses.getInstance(), () -> {
                    Iterator<Block> iterator = changed.iterator();
                    while (iterator.hasNext()) {
                        Block bl = iterator.next();
                        mod.restoreBlock(this, bl);
                    }
                }, (long) ((ThreadLocalRandom.current().nextInt(5) + 5) * 20));
            }
        }
    }

    @Override
    public String getAbilityName() {
        return "Temporal Rift";
    }
}

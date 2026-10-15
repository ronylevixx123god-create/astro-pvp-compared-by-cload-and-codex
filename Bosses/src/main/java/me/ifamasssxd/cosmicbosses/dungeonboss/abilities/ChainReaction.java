package me.ifamasssxd.cosmicbosses.dungeonboss.abilities;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.base.Predicate;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.NewBlockData;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.citizensnpcs.nms.v1_8_R3.entity.EntityHumanNPC;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ChainReaction extends DungeonAbility {
    private int shotsFired;
    private int landed;

    public ChainReaction(final Boss boss) {
        super(boss);
        this.shotsFired = 0;
        this.landed = 0;
    }

    @Override
    public void onTick() {
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(5L);
    }

    @Override
    public String getAbilityName() {
        return "Chain Reaction";
    }

    @Override
    public boolean shouldStopTicking() {
        final long initTimeDistance = System.currentTimeMillis() - this.initTime;
        return initTimeDistance >= this.getCooldown() && this.shotsFired >= 3 && (this.landed >= 3 || initTimeDistance >= this.getCooldown() + 5000L);
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();
        this.boss.sendMessage("Let it rain the world around you!");
    }

    @Override
    public void onAbilityDisable() {
    }

    @Override
    public void onAbilityEvent(final Event event) {
        if (event instanceof ProjectileLaunchEvent) {
            final ProjectileLaunchEvent e = (ProjectileLaunchEvent) event;
            if (this.shotsFired >= 5) {
                return;
            }
            Bukkit.getLogger().info("(Dungeon Yijki) Tracking Projectile fired for chainReaction..");
            e.getEntity().setMetadata("chainReaction", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.getInstance(), (Object) ""));
            if (e.getEntity() instanceof WitherSkull) {
                ((WitherSkull) e.getEntity()).setCharged(true);
            }
            ++this.shotsFired;
        } else if (event instanceof ProjectileHitEvent) {
            final ProjectileHitEvent e2 = (ProjectileHitEvent) event;
            if (e2.getEntity().hasMetadata("chainReaction")) {
                ++this.landed;
                if (e2.getEntity().getLocation().getY() > 2.0) {
                    int radius = 5;
                    if (this.boss.getEntity().getHealth() < this.boss.getMaxHealth() / 2.0) {
                        --radius;
                    }
                    final int depth = 1;
                    int exploded = 0;
                    final Location entLocation = e2.getEntity().getLocation();
                    Bukkit.getLogger().info("Chain reaction exploding!");
                    for (int x = -radius; x < radius; ++x) {
                        for (int z = -radius; z < radius; ++z) {
                            for (int y = -depth; y < depth; ++y) {
                                final org.bukkit.block.Block toExplode = entLocation.clone().add((double) x, (double) y, (double) z).getBlock();
                                if (toExplode.getY() > 1) {
                                    if (toExplode.getType() != Material.BEDROCK) {
                                        if (toExplode.getType() != Material.AIR) {
                                            final String regionName = WorldGuardUtils.getRegionName(toExplode.getLocation());
                                            if (!toExplode.getWorld().getName().startsWith("dungeon") || this.boss instanceof MotherOfYijki || regionName.equals("hanger") || regionName.equals("bridge")) {
                                                if (!WorldGuardUtils.hasFlagDisabled(toExplode.getLocation(), (Flag<?>) DefaultFlag.OTHER_EXPLOSION)) {
                                                    if (!(toExplode.getState() instanceof InventoryHolder)) {
                                                        if (BossUtils.isWarzone(toExplode.getLocation())) {
                                                            final NewBlockData data = this.boss.getModifier().removeBlockAndStore(this, toExplode.getLocation());
                                                            final Location spawn = toExplode.getLocation().add(0.5, 1.1, 0.5);
                                                            final EntityFallingBlock block = new EntityFallingBlock(spawn, ((CraftWorld) toExplode.getWorld()).getHandle(), spawn.getX(), spawn.getY(), spawn.getZ(), Block.getById(data.getMaterial().getId()).getBlockData()) {
                                                                public void h() {
                                                                    if (this.ticksLived >= 8) {
                                                                        try {
                                                                            Predicate<Entity> pred = entity -> entity instanceof EntityHuman && !(entity instanceof EntityHumanNPC);
                                                                            final List<Entity> arraylist = (List<Entity>) this.world.getEntities((Entity) this, this.getBoundingBox());
                                                                            int killed = 0;
                                                                            for (final Entity ent : arraylist) {
                                                                                if (ent instanceof EntityHuman && !(ent instanceof EntityHumanNPC)) {
                                                                                    if (ent instanceof EntityHuman) {
                                                                                        final CraftPlayer player = (CraftPlayer) ent.getBukkitEntity();
                                                                                        if (player.getGameMode() == GameMode.CREATIVE || player.isDead()) {
                                                                                            continue;
                                                                                        }
                                                                                        if (BossUtils.isInGodMode((Player) player)) {
                                                                                            continue;
                                                                                        }
                                                                                        player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.1f);
                                                                                        player.setMetadata("lastWitherDamage", (MetadataValue) new FixedMetadataValue((Plugin) CosmicBosses.get(), (Object) (System.currentTimeMillis() + 1000L)));
                                                                                        player.damage(0.0, (org.bukkit.entity.Entity) this.getBukkitEntity());
                                                                                        final double damage = Math.max(5.0, player.getHealth() / 2.0);
                                                                                        if (damage > player.getHealth()) {
                                                                                            player.setHealth(0.0);
                                                                                        } else {
                                                                                            player.setHealth(player.getHealth() - damage);
                                                                                        }
                                                                                        ++killed;
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (killed > 0) {
                                                                                this.die();
                                                                                Bukkit.getLogger().info("Damaging " + killed + " players damage from falling block!");
                                                                                return;
                                                                            }
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                    super.h();
                                                                }
                                                            };
                                                            block.ticksLived = 1;
                                                            ((CraftWorld) toExplode.getWorld()).getHandle().addEntity((Entity) block, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                                            final FallingBlock fallingBlock = (FallingBlock) block.getBukkitEntity();
                                                            fallingBlock.setVelocity(new Vector((ThreadLocalRandom.current().nextBoolean() ? -1 : 1) * Math.random(), 0.8 + Math.random(), (ThreadLocalRandom.current().nextBoolean() ? -1 : 1) * Math.random()));
                                                            fallingBlock.setMetadata("chainReactionBlock", (MetadataValue)new FixedMetadataValue((Plugin)CosmicBosses.getInstance(), this.boss.getUuid()));                                                            fallingBlock.setDropItem(false);
                                                            ++exploded;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Bukkit.getLogger().info("(Dungeon Yijki) Exploded " + exploded + " blocks.");
                }
            }
        } else if (event instanceof EntityChangeBlockEvent) {
            final EntityChangeBlockEvent changeBlockEvent = (EntityChangeBlockEvent) event;
            changeBlockEvent.setCancelled(true);
        } else if (event instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            if (damageEvent.getDamager().hasMetadata("chainReactionBlock")) {
                damageEvent.setDamage(300.0);
                Bukkit.getLogger().info("Setting damage to: " + damageEvent.getDamage(EntityDamageEvent.DamageModifier.BASE));
            }
        }
    }
}

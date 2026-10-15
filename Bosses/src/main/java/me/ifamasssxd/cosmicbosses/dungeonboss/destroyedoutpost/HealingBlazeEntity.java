package me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost;

import de.inventivegames.particle.ParticleEffect;
import lombok.SneakyThrows;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class HealingBlazeEntity extends EntityBlaze {
    private MotherOfYijki parent;
    private int br;
    long timeSinceShot;

    public HealingBlazeEntity(final World world) {
        super(world);
    }

    public HealingBlazeEntity(final MotherOfYijki parent, final World world) {
        super(world);
        this.parent = parent;
        this.getAttributeInstance(GenericAttributes.c).setValue(1.0);
    }

    @SneakyThrows
    public void m() {
        if (this.parent == null || !this.parent.isBossSpawned()) {
            if (this.parent != null) {
                this.parent.removeMinion((Entity) this.bukkitEntity);
            }
            this.die();
            return;
        }
        super.m();
        if (this.ticksLived < 140) {
            final boolean beenSecond = this.ticksLived % 20 == 0;
            if (beenSecond || this.ticksLived % 3 == 0) {
                de.inventivegames.particle.ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), this.getBukkitEntity().getLocation(), 0.24f, 0.24f, 0.24f, 0.1f, beenSecond ? 20 : 3);
            }
            return;
        }
        if ((this.timeSinceShot == 0L || System.currentTimeMillis() - this.timeSinceShot >= 5000L) && !this.parent.isInDifferentDimension()) {
            this.timeSinceShot = System.currentTimeMillis();
            this.shootHealingFireball();
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) CosmicBosses.getInstance(), () -> {
                if (this.parent != null && this.parent.isBossSpawned()) {
                    this.parent.onHeal(7);
                }
            }, 20L);
        }
    }

    private void shootHealingFireball() {
        final Vector spawnVec = this.parent.getEntity().getLocation().add(0.0, 1.0, 0.0).toVector().subtract(this.getBukkitEntity().getLocation().add(0.0, 1.0, 0.0).toVector());
        if (spawnVec.length() != 0.0) {
            spawnVec.normalize();
        }
        spawnVec.multiply(2.5);
        this.world.addEntity((net.minecraft.server.v1_8_R3.Entity) this.createAccurateFireball(this.world, spawnVec.getX(), spawnVec.getY(), spawnVec.getZ(), false, true));
    }

    protected void a(final net.minecraft.server.v1_8_R3.Entity var1, final float var2) {
        if (this.hurtTicks <= 0 && var2 < 2.0f && var1.getBoundingBox().e > this.getBoundingBox().b && var1.getBoundingBox().b < this.getBoundingBox().e) {
            this.hurtTicks = 20;
            this.n(var1);
        } else if (var2 < 30.0f) {
            final double var3 = var1.locX - this.locX;
            final double var4 = var1.getBoundingBox().b + var1.length / 2.0f - (this.locY + this.length / 2.0f);
            final double var5 = var1.locZ - this.locZ;
            if (this.hurtTicks == 0) {
                ++this.br;
                if (this.br == 1) {
                    this.hurtTicks = 40;
                    this.a(true);
                } else if (this.br <= 4) {
                    this.hurtTicks = 6;
                } else {
                    this.hurtTicks = 60;
                    this.br = 0;
                    this.a(false);
                }
                if (this.br > 1) {
                    this.world.a((EntityHuman) null, 1009, new BlockPosition((int) this.locX, (int) this.locY, (int) this.locZ), 0);
                    for (int var6 = 0; var6 < 1; ++var6) {
                        final EntitySmallFireball fireball = this.createAccurateFireball(this.world, var3, var4, var5, true, false);
                        fireball.locY = this.locY + this.length / 2.0f + 0.5;
                        this.world.addEntity((net.minecraft.server.v1_8_R3.Entity) fireball);
                    }
                }
            }
            this.yaw = (float) (Math.atan2(var5, var3) * 180.0 / 3.1415927410125732) - 90.0f;
            //this.bm = true;
        }
    }

    private EntitySmallFireball createAccurateFireball(final World world, final double dirX, final double dirY, final double dirZ, final boolean collides, final boolean heal) {
        return new EntitySmallFireball(world, this, dirX, dirY, dirZ) {
            public void setDirection(final double d0, final double d1, final double d2) {
                final double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                this.dirX = d0 / d3 * 0.1;
                this.dirY = d1 / d3 * 0.1;
                this.dirZ = d2 / d3 * 0.1;
            }

            @SneakyThrows
            public void h() {
                super.h();
                if (this.isAlive() && !collides) {
                    ParticleEffect.HAPPY_VILLAGER.sendToPlayers(Bukkit.getOnlinePlayers(), this.getBukkitEntity().getLocation(), 3f, 3f, 3f, 0.09f, 15);
                }
            }

            protected void a(final MovingObjectPosition movingobjectposition) {
                if (!this.world.isClientSide) {
                    if (movingobjectposition.entity != null) {
                        if (heal && movingobjectposition.entity instanceof MotherOfYijkiEntity && !collides) {
                            HealingBlazeEntity.this.parent.onHeal(20);
                            this.die();
                            return;
                        }
                        if (!(movingobjectposition.entity instanceof EntityHuman)) {
                            return;
                        }
                        if (!movingobjectposition.entity.isFireProof() && movingobjectposition.entity.damageEntity(DamageSource.fireball((EntityFireball) this, (net.minecraft.server.v1_8_R3.Entity) this.shooter), 5.0f)) {
                            final EntityCombustByEntityEvent event = new EntityCombustByEntityEvent((Entity) this.getBukkitEntity(), (Entity) movingobjectposition.entity.getBukkitEntity(), 5);
                            movingobjectposition.entity.world.getServer().getPluginManager().callEvent((Event) event);
                            if (!event.isCancelled()) {
                                movingobjectposition.entity.setOnFire(event.getDuration());
                            }
                        }
                    } else {
                        if (!collides && this.ticksLived < 200) {
                            return;
                        }
                        double i = movingobjectposition.pos.a;
                        double j = movingobjectposition.pos.b;
                        double k = movingobjectposition.pos.c;

                        if (movingobjectposition.direction == EnumDirection.DOWN) {
                            --j;
                        } else if (movingobjectposition.direction == EnumDirection.UP) {
                            ++j;
                        } else if (movingobjectposition.direction == EnumDirection.NORTH) {
                            --k;
                        } else if (movingobjectposition.direction == EnumDirection.SOUTH) {
                            ++k;
                        } else if (movingobjectposition.direction == EnumDirection.WEST) {
                            --i;
                        } else if (movingobjectposition.direction == EnumDirection.EAST) {
                            ++i;
                        }

                        if (this.world.isEmpty(new BlockPosition(i, j, k)) && !CraftEventFactory.callBlockIgniteEvent(this.world, (int) i, (int) j, (int) k, (net.minecraft.server.v1_8_R3.Entity) this).isCancelled()) {
                            this.world.setTypeUpdate(new BlockPosition(i, j, k), Blocks.FIRE.getBlockData());
                        }
                    }
                    this.die();
                }
            }
        };
    }

    protected Item getLoot() {
        return null;
    }
}

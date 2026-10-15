package me.ifamasssxd.cosmicbosses.bosses.dragon.phases;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.base.Predicate;
import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragonEntity;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PhaseSittingFlaming extends PhaseSittingBase {
    private int flameTicks;
    private int flameCount;
    private AxisAlignedBB flameBreathBox;

    public PhaseSittingFlaming(final EndDragonEntity dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doClientRenderEffects() {
        ++this.flameTicks;
        if (this.flameTicks % 2 == 0) {
            final Vec3D vec3d = this.dragon.getHeadLookVec(1.0f).a();
            vec3d.b(-0.7853982f);
            final double d0 = this.dragon.head.locX;
            final double d2 = this.dragon.head.locY - this.dragon.head.getHeadHeight() / 2.0f - 2.5;
            final double d3 = this.dragon.head.locZ;
            final EntityTrackerEntry entry = (EntityTrackerEntry) ((WorldServer) this.dragon.world).getTracker().trackedEntities.get(this.dragon.getId());
            for (int i = 0; i < 8; ++i) {
                final double d4 = d0 + this.dragon.random.nextGaussian() / 2.0;
                final double d5 = d2 - this.dragon.random.nextGaussian() / 2.0;
                final double d6 = d3 + this.dragon.random.nextGaussian() / 2.0;
                for (int j = 0; j < 6; ++j) {
                    final float offX = (float) (-vec3d.a * 0.07999999821186066 * j);
                    final float offY = (float) (-vec3d.b * 0.6000000238418579);
                    final float offZ = (float) (-vec3d.c * 0.07999999821186066 * j);
                    if (entry != null) {
                        final double n = d4;
                        final double n2 = d5;
                        final double n3 = d6;
                        final float n4 = offX;
                        final float n5 = offY;
                        final float n6 = offZ;
                        entry.trackedPlayers.forEach(pl -> {
                            if (pl != null && pl.playerConnection != null && !pl.playerConnection.isDisconnected()) {
                                pl.playerConnection.sendPacket((Packet) new PacketPlayOutWorldParticles(EnumParticle.SMOKE_NORMAL, true, (float) n, (float) n2, (float) n3, n4, n5, n6, 1.3f, 0));
                                pl.playerConnection.sendPacket((Packet) new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) n, (float) n2, (float) n3, n4, n5, n6, 1.3f, 0));
                            }
                            return;
                        });
                    }
                }
                vec3d.b(0.19634955f);
            }
            if (this.flameTicks % 30 == 0) {
                this.dragon.shootTNT();
            }
            if (this.flameBreathBox != null) {
                Predicate<Entity> pred = new Predicate() {
                    public boolean a(Entity entity) {
                        return entity instanceof EntityHuman;
                    }

                    public boolean apply(Object object) {
                        return a((Entity) object);
                    }
                };
                this.dragon.world.getEntities((Entity) this.dragon, this.flameBreathBox).forEach(ent -> {
                    if (ent instanceof EntityHuman) {
                        Player player = (Player) ((EntityHuman) ent).getBukkitEntity();
                        boolean success = false;
                        if (!player.isDead() && BossUtils.dealPercentMaxHealthDamage(player, 2.0, 1, true, true)) {
                            player.setFireTicks(60);
                            success = true;
                            player.playEffect(EntityEffect.HURT);
                        }
                        Bukkit.getLogger().info("Flamming  " + player.getName() + " because they were inside flame breath: " + success);
                    }
                });
            }
        }
    }

    @Override
    public void doLocalUpdate() {
        this.doClientRenderEffects();
        if (this.flameTicks >= 40) {
            if (this.flameCount >= 5) {
                this.dragon.motX = 0.0;
                this.dragon.motY = 0.0;
                this.dragon.motZ = 0.0;
                this.dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
            } else {
                this.dragon.shootTNT();
                this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
            }
        }
    }

    @Override
    public void initPhase() {
        this.flameTicks = 0;
        final Vec3D lookLok = this.dragon.getHeadLookVec(1.0f);
        lookLok.a();
        this.flameBreathBox = this.dragon.head.getBoundingBox().grow(lookLok.a * 40.0, -lookLok.b * 30.0, lookLok.c * 40.0);
        ++this.flameCount;
        this.dragon.bossInstance.sendMessage(CC.White + "Behold, now you will know what it means to " + CC.RedB + "BURN" + CC.White + "!", Sound.ENDERDRAGON_GROWL, 1.1f);
    }

    @Override
    public PhaseList<PhaseSittingFlaming> getType() {
        return PhaseList.SITTING_FLAMING;
    }

    public void resetFlameCount() {
        this.flameCount = 0;
    }
}

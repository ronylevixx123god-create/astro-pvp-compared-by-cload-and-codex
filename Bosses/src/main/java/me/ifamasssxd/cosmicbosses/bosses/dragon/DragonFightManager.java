package me.ifamasssxd.cosmicbosses.bosses.dragon;

import net.minecraft.server.v1_8_R3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class DragonFightManager {
    private static final Logger LOGGER;
    private static final Predicate<EntityPlayer> VALID_PLAYER;
    private final World world;
    private int ticksSinceDragonSeen;
    private int aliveCrystals;
    private int ticksSinceCrystalsScanned;
    private int ticksSinceLastPlayerScan;
    private boolean dragonKilled;
    private boolean previouslyKilled;
    private UUID dragonUniqueId;
    private List<EntityEnderCrystal> crystals;
    private EndDragon dragonBoss;

    public static <T extends Entity> com.google.common.base.Predicate<T> withinRange(final double x, final double y, final double z, final double range) {
        final double d0 = range * range;
        return (com.google.common.base.Predicate<T>) (p_apply_1_ -> p_apply_1_ != null && p_apply_1_.f(x, y, z) <= d0);
    }

    public DragonFightManager(final World worldIn) {
        this.world = worldIn;
    }

    public void tick() {
        if (++this.ticksSinceLastPlayerScan >= 20) {
            this.ticksSinceLastPlayerScan = 0;
        }
        if (this.dragonBoss == null) {
            return;
        }
        final EndDragonEntity entity = this.dragonBoss.getNMSEntity();
        if (entity == null || !entity.isAlive()) {
            return;
        }
        if (!this.world.players.isEmpty() && !this.dragonKilled && ++this.ticksSinceCrystalsScanned >= 100) {
            this.findAliveCrystals();
            this.ticksSinceCrystalsScanned = 0;
        }
    }

    public void dragonUpdate(final EndDragonEntity entity) {
    }

    private void loadChunks() {
        for (int i = -8; i <= 8; ++i) {
            for (int j = -8; j <= 8; ++j) {
                this.world.getChunkAt(i, j);
            }
        }
    }

    public int getNumAliveCrystals() {
        return this.aliveCrystals;
    }

    private void findAliveCrystals() {
        this.ticksSinceCrystalsScanned = 0;
        this.aliveCrystals = 0;
        this.world.entityList.forEach(ent -> {
            if (ent instanceof EntityEnderCrystal) {
                ++this.aliveCrystals;
            }
            return;
        });
        DragonFightManager.LOGGER.debug("Found {} end crystals still alive", new Object[]{this.aliveCrystals});
    }

    public boolean hasPreviouslyKilledDragon() {
        return this.previouslyKilled;
    }

    static {
        LOGGER = LogManager.getLogger();
        VALID_PLAYER = ((Predicate<EntityPlayer>) IEntitySelector.a::apply).and(withinRange(0.0, 128.0, 0.0, 192.0)::apply);
    }
}

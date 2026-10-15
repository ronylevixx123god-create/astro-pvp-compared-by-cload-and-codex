package me.ifamasssxd.cosmicbosses.bosses.dragon;

import me.ifamasssxd.cosmicbosses.struct.BossAbility;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DragonFireball extends BossAbility<EndDragon> {
    public DragonFireball(final EndDragon boss) {
        super(boss);
    }

    @Override
    public void onAbility() {
        final EndDragonEntity entity = ((EndDragon) this.boss).getNMSEntity();
        if (entity.shootFireball()) {
            this.activateCooldown();
        }
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(3L) + ThreadLocalRandom.current().nextInt(2000);
    }

    @Override
    public String getAbilityName() {
        return "Firespit";
    }
}

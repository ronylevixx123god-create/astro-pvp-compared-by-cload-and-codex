package me.ifamasssxd.cosmicbosses.dungeonboss;

import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.BossAbility;
import org.bukkit.event.Event;

public abstract class DungeonAbility extends BossAbility {
    protected Boss boss;
    protected long initTime;

    public DungeonAbility(final Boss boss) {
        super(boss);
        this.boss = boss;
    }

    public abstract void onTick();

    public void initAbility() {
        this.initTime = System.currentTimeMillis();
    }

    @Override
    public Boss getBoss() {
        return this.boss;
    }

    @Override
    public void onAbility() {
        this.onAbilityEnable();
    }

    @Override
    public abstract long getCooldown();

    public abstract boolean shouldStopTicking();

    public abstract void onAbilityEnable();

    public abstract void onAbilityDisable();

    public abstract void onAbilityEvent(final Event p0);
}

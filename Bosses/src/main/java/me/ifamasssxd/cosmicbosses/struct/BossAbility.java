package me.ifamasssxd.cosmicbosses.struct;

public abstract class BossAbility<T extends Boss> {
    public T boss;
    protected BlockModifier blockModifier;

    public BossAbility(final T boss) {
        this.boss = boss;
    }

    public BossAbility(final T boss, final BlockModifier modifier) {
        this.boss = boss;
        this.blockModifier = modifier;
    }

    public abstract void onAbility();

    public abstract long getCooldown();

    public abstract String getAbilityName();

    public boolean canUse() {
        if (this.boss.getAbilityCooldown().containsKey(this)) {
            final long cooldown = this.boss.getAbilityCooldown().get(this);
            if (cooldown >= System.currentTimeMillis()) {
                return false;
            }
            this.boss.getAbilityCooldown().remove(this);
        }
        return true;
    }

    public void activateCooldown() {
        this.boss.getAbilityCooldown().put(this, System.currentTimeMillis() + this.getCooldown());
    }

    public T getBoss() {
        return this.boss;
    }

    public BlockModifier getBlockModifier() {
        return this.blockModifier;
    }
}

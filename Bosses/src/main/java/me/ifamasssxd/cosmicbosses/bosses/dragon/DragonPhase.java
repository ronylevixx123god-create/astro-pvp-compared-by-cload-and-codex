package me.ifamasssxd.cosmicbosses.bosses.dragon;

import java.util.Arrays;

public enum DragonPhase {
    FIREBALL(1),
    BLAZES(2),
    TNT(3),
    SWOOPING(4);

    private int phase;

    public static DragonPhase getDragonPhase(final int phase) {
        return Arrays.stream(values()).filter(ph -> ph.getPhase() == phase).findFirst().orElse(null);
    }

    private DragonPhase(final int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return this.phase;
    }
}

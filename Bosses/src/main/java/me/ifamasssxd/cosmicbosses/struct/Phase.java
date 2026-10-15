package me.ifamasssxd.cosmicbosses.struct;

public enum Phase {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7);

    private int phase;

    public Phase getNextPhase() {
        final int next = this.phase + 1;
        return getPhase(next);
    }

    public static Phase getPhase(final int phase) {
        for (final Phase ph : values()) {
            if (ph.getPhase() == phase) {
                return ph;
            }
        }
        return Phase.ONE;
    }

    private Phase(final int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return this.phase;
    }
}

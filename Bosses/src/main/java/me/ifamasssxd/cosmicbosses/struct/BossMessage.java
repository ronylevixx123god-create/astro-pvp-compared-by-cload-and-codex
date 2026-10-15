package me.ifamasssxd.cosmicbosses.struct;

public class BossMessage {
    String message;
    boolean phaseDependent;
    int phase;

    public BossMessage(final String message, final boolean phaseDependent, final int phase) {
        this.message = message;
        this.phaseDependent = phaseDependent;
        this.phase = phase;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isPhaseDependent() {
        return this.phaseDependent;
    }

    public int getPhase() {
        return this.phase;
    }
}

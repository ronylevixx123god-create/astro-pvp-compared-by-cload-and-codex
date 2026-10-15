package org.demonicpvp.demonictrials.trial.room;

import org.demonicpvp.demonictrials.trial.TrialTier;

public class RoomProgress {
    private final TrialRoom room;
    private int secondsTaken;
    private boolean completed;
    private boolean didSkip;
    private final TrialTier tier;

    public RoomProgress(TrialRoom room, int secondsTaken, boolean completed, boolean didSkip, TrialTier tier) {
        this.room = room;
        this.secondsTaken = secondsTaken;
        this.completed = completed;
        this.didSkip = didSkip;
        this.tier = tier;
    }

    public boolean isDidSkip() {
        return this.didSkip;
    }

    public TrialRoom getRoom() {
        return this.room;
    }

    public int getSecondsTaken() {
        return this.secondsTaken;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public TrialTier getTier() {
        return this.tier;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void incrementSecondsTaken() {
        this.secondsTaken++;
    }
}

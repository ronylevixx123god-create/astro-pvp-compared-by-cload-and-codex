package org.demonicpvp.demonictrials.trial.trinket;

import org.demonicpvp.demonictrials.trial.trinket.impl.MadnessBallotOptionsTrinket;
import org.demonicpvp.demonictrials.trial.trinket.impl.MaxPlayersTrinket;
import org.demonicpvp.demonictrials.trial.trinket.impl.SkipRoomsTrinket;

public enum TrialTrinket {
    MAX_PLAYERS(new MaxPlayersTrinket()),
    SKIP_ROOMS(new SkipRoomsTrinket()),
    MADNESS_BALLOT_OPTIONS(new MadnessBallotOptionsTrinket());

    private final ITrialTrinket instance;

    TrialTrinket(ITrialTrinket instance) {
        this.instance = instance;
    }

    public ITrialTrinket getInstance() {
        return this.instance;
    }
}

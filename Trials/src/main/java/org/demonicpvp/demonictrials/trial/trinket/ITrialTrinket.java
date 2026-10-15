package org.demonicpvp.demonictrials.trial.trinket;

import java.util.List;

public interface ITrialTrinket {
    String getId();
    String getFormattedName(int amplifier);
    List<String> getLore(int amplifier);
}

package org.demonicpvp.demonictrials.trial.trinket.impl;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import org.demonicpvp.demonictrials.trial.trinket.ITrialTrinket;

import java.util.List;

public class MadnessBallotOptionsTrinket implements ITrialTrinket {
    @Override
    public String getId() {
        return "MADNESS_BALLOT_OPTIONS";
    }

    @Override
    public String getFormattedName(int amplifier) {
        return CommonsUtil.color("&4&l+" + amplifier + " Madness Ballot Options");
    }

    @Override
    public List<String> getLore(int amplifier) {
        return Lists.newArrayList(
                CommonsUtil.color("&7Increases the number of available options in"),
                CommonsUtil.color("&7the Madness Modifier Ballot from 2 to " + (2 + amplifier))
        );
    }
}

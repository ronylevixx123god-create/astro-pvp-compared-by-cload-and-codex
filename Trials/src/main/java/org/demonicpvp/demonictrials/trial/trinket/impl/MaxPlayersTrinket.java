package org.demonicpvp.demonictrials.trial.trinket.impl;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import org.demonicpvp.demonictrials.trial.trinket.ITrialTrinket;

import java.util.List;

public class MaxPlayersTrinket implements ITrialTrinket {
    @Override
    public String getId() {
        return "MAX_PLAYERS";
    }

    @Override
    public String getFormattedName(int amplifier) {
        return CommonsUtil.color("&3&l+" + amplifier + " Max Players");
    }

    @Override
    public List<String> getLore(int amplifier) {
        return Lists.newArrayList(
            CommonsUtil.color("&7Increases the maximum number of participants"),
            CommonsUtil.color("&7from 8 to " + (8 + amplifier) + ".")
        );
    }
}

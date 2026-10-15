package org.demonicpvp.demonictrials.trial.trinket.impl;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import org.demonicpvp.demonictrials.trial.trinket.ITrialTrinket;

import java.util.List;

public class SkipRoomsTrinket implements ITrialTrinket {
    @Override
    public String getId() {
        return "SKIP_ROOMS";
    }

    @Override
    public String getFormattedName(int amplifier) {
        return CommonsUtil.color("&b&lSkip " + amplifier + " Rooms");
    }

    @Override
    public List<String> getLore(int amplifier) {
        return Lists.newArrayList(
                CommonsUtil.color("&7Automatically skips " + amplifier + " rooms of the trial.")
        );
    }
}

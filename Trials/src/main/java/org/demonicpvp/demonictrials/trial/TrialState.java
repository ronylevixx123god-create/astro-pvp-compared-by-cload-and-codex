package org.demonicpvp.demonictrials.trial;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;

public enum TrialState {
    LOADING("&b&lLoading"),
    RUNNING("&a&lRunning"),
    FAILED("&c&lFailed");

    private final String coloredName;

    TrialState(String coloredName) {
        this.coloredName = CommonsUtil.color(coloredName);
    }

    public String getColoredName() {
        return this.coloredName;
    }
}

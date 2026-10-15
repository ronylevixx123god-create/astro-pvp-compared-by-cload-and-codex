package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import org.bukkit.Material;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

public class TimeGlitchModifier extends AbstractMadnessModifier {
    public TimeGlitchModifier(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Time Glitch";
    }

    @Override
    public String getDescription() {
        return "The Time Left on the scoreboard is obfuscated 80% of the time.";
    }

    @Override
    public Material getMaterial() {
        return Material.DAYLIGHT_DETECTOR;
    }

    @Override
    public void onRoomEnd() {
        this.clearCooldown();
    }

    @Override
    public void reset() {
        this.sendInfiniteCooldown();
    }
}

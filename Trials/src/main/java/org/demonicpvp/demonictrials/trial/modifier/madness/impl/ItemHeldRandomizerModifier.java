package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

import java.util.Random;

public class ItemHeldRandomizerModifier extends AbstractMadnessModifier {
    private int secondsCounted;

    public ItemHeldRandomizerModifier(TrialSession trial) {
        super(trial);
        this.secondsCounted = 0;
    }

    @Override
    public String getName() {
        return "Item Held Randomizer";
    }

    @Override
    public String getDescription() {
        return "Every 10 seconds, the item you're holding will change.";
    }

    @Override
    public Material getMaterial() {
        return Material.DETECTOR_RAIL;
    }

    @Override
    public void onTick() {
        if (this.secondsCounted == 0) {
            this.sendCooldown(10);
        }

        this.secondsCounted++;

        if (this.secondsCounted >= 10) {
            for (Player p : this.getTrial().getPlayers()) {
                p.getInventory().setHeldItemSlot(new Random().nextInt(9));
            }

            this.secondsCounted = 0;
        }
    }

    @Override
    public void onRoomEnd() {
        this.clearCooldown();
    }

    @Override
    public void reset() {
        this.secondsCounted = 0;
    }
}

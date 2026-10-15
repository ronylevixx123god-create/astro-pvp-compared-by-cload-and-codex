package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

import java.util.Collections;
import java.util.List;

public class HotbarShuffleModifier extends AbstractMadnessModifier {
    private int secondsCounted;

    public HotbarShuffleModifier(TrialSession trial) {
        super(trial);
        this.secondsCounted = 0;
    }

    @Override
    public String getName() {
        return "Hotbar Shuffle";
    }

    @Override
    public String getDescription() {
        return "Every 10 seconds, all items in your hotbar are swapped around.";
    }

    @Override
    public Material getMaterial() {
        return Material.DISPENSER;
    }

    @Override
    public void onTick() {
        if (this.secondsCounted == 0) {
            this.sendCooldown(10);
        }

        this.secondsCounted++;

        if (this.secondsCounted >= 10) {
            for (Player p : this.getTrial().getPlayers()) {
                List<ItemStack> hotbarItems = Lists.newArrayList();

                for (int i = 0; i <= 8; i++) {
                    hotbarItems.add(p.getInventory().getItem(i));
                }

                Collections.shuffle(hotbarItems);

                for (int i = 0; i <= 8; i++) {
                    p.getInventory().setItem(i, hotbarItems.get(i));
                }
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

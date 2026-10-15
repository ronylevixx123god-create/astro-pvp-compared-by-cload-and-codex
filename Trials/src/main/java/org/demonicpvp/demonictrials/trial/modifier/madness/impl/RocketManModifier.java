package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

public class RocketManModifier extends AbstractMadnessModifier {
    private int secondsCounted;

    public RocketManModifier(TrialSession trial) {
        super(trial);
        this.secondsCounted = 0;
    }

    @Override
    public String getName() {
        return "Rocket Man";
    }

    @Override
    public String getDescription() {
        return "Every 15 seconds, players have a chance to be pulled into the air.";
    }

    @Override
    public Material getMaterial() {
        return Material.FIREWORK;
    }

    @Override
    public void onTick() {
        if (this.secondsCounted == 0) {
            this.sendCooldown(15);
        }

        this.secondsCounted++;

        if (this.secondsCounted >= 15) {
            for (Player p : this.getTrial().getPlayers()) {
                if (Math.random() < 0.25) {
                    p.setMetadata("pushAwayEntityEvent", new FixedMetadataValue(DemonicTrials.getInstance(), MinecraftServer.currentTick));
                    p.setVelocity(new Vector(0, 3.25, 0));
                    this.sendMessage(p, "is on a mission... &7FIRE AND FURY!");
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


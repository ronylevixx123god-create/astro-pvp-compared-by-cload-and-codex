package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

public class OwlGeneModifier extends AbstractMadnessModifier {
    private int secondsCounted;

    public OwlGeneModifier(TrialSession trial) {
        super(trial);
        this.secondsCounted = 0;
    }

    @Override
    public String getName() {
        return "Owl Gene";
    }

    @Override
    public String getDescription() {
        return "All players will turn 180 degrees every 30 seconds, and face the opposite direction.";
    }

    @Override
    public Material getMaterial() {
        return Material.ENDER_PEARL;
    }

    @Override
    public void onTick() {
        if (this.secondsCounted == 0) {
            this.sendCooldown(30);
        }

        this.secondsCounted++;

        if (this.secondsCounted < 30 && this.secondsCounted >= 27) {
            this.sendMessage("Players will be turned around in " + (30 - this.secondsCounted) + "s!");
        } else if (this.secondsCounted >= 30) {
            for (Player p : this.getTrial().getPlayers()) {
                Location turnedLocation = p.getLocation();
                turnedLocation.setYaw(turnedLocation.getYaw() + 180.f);

                if (turnedLocation.getYaw() > 360.f) {
                    turnedLocation.setYaw(turnedLocation.getYaw() - 360.f);
                }

                this.getTrial().teleportPlayer(p, turnedLocation);
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


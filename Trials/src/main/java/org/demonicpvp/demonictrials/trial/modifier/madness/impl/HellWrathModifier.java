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

public class HellWrathModifier extends AbstractMadnessModifier {
    private int secondsCounted;

    public HellWrathModifier(TrialSession trial) {
        super(trial);
        this.secondsCounted = 0;
    }

    @Override
    public String getName() {
        return "Hell Wrath";
    }

    @Override
    public String getDescription() {
        return "Every 20 seconds, every player who is not sneaking is pulled in a random direction.";
    }

    @Override
    public Material getMaterial() {
        return Material.FIREBALL;
    }

    @Override
    public void onTick() {
        if (this.secondsCounted == 0) {
            this.sendCooldown(20);
        }

        this.secondsCounted++;

        if (this.secondsCounted == 18) {
            this.sendMessage("You must sneak in " + (20 - this.secondsCounted) + "s to not be pulled!");
        } else if (this.secondsCounted >= 20) {
            for (Player p : this.getTrial().getPlayers()) {
                if (p.isSneaking()) continue;

                p.setMetadata("pushAwayEntityEvent", new FixedMetadataValue(DemonicTrials.getInstance(), MinecraftServer.currentTick));
                p.setVelocity(new Vector((0.5 + Math.random()) * (Math.random() < 0.5 ? -1 : 1), 0, (0.5 + Math.random()) * (Math.random() < 0.5 ? -1 : 1)));
                this.sendMessage(p, "You have been pulled!");
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


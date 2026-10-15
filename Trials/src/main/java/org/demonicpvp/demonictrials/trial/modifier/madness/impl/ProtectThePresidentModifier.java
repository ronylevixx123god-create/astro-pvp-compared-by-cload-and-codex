package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ProtectThePresidentModifier extends AbstractMadnessModifier {
    private UUID president;

    public ProtectThePresidentModifier(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Protect the President";
    }

    @Override
    public String getDescription() {
        return "A random player becomes the President. If that player dies, everyone dies.";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_HELMET;
    }

    public UUID getPresident() {
        return this.president;
    }

    @Override
    public void onRoomEnd() {
        this.clearCooldown();
    }

    @Override
    public void reset() {
        if (this.president == null) {
            List<Player> list = new ArrayList<>(this.getTrial().getPlayers());
            this.president = list.get(new Random().nextInt(list.size())).getUniqueId();

            this.sendMessage(Bukkit.getPlayer(this.president).getName() + " is the president! Do not let them die!");
        }

        /*for (Player p : this.getTrial().getPlayers()) {
            CosmicPacketHandler.sendPacketToPlayer(p, new SCooldownPacket(
                    "madnessmodifier" + this.getName().toLowerCase(),
                    "DEBUFF",
                    true,
                    CosmicPacketHandler.rgbaToInt(255, 0, 0, 255),
                    "Madness Modifier: " + this.getName() + ": " + Bukkit.getPlayer(this.president).getName(),
                    0,
                    0,
                    this.getMaterial().getId()
            ));
        }*/
    }
}

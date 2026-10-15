package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

public class CursedLifeModifier extends AbstractMadnessModifier implements Listener {
    public CursedLifeModifier(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Cursed Life";
    }

    @Override
    public String getDescription() {
        return "All players heal by 50% less.";
    }

    @Override
    public Material getMaterial() {
        return Material.POTION;
    }

    @EventHandler
    public void on(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();

            if (this.getTrial().getPlayers().contains(player)) {
                event.setAmount(event.getAmount() / 2.0);
            }
        }
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

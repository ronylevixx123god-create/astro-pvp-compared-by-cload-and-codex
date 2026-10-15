package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

import java.util.Map;
import java.util.UUID;

public class StatuesModifier extends AbstractMadnessModifier {
    private int secondsCounted;

    public StatuesModifier(TrialSession trial) {
        super(trial);
        this.secondsCounted = 0;
    }

    @Override
    public String getName() {
        return "Statues";
    }

    @Override
    public String getDescription() {
        return "Every 45 seconds, all players must stand still or receive massive damage.";
    }

    @Override
    public Material getMaterial() {
        return Material.ARMOR_STAND;
    }

    @Override
    public void onTick() {
        if (this.secondsCounted == 0) {
            this.sendCooldown(45);
        }

        this.secondsCounted++;

        if (this.secondsCounted == 43) {
            this.sendMessage("You must stand still in " + (45 - this.secondsCounted) + "s to not receive massive damage!");
        } else if (this.secondsCounted >= 45) {
            Map<UUID, Location> initialLocations = Maps.newHashMap();

            for (Player player : this.getTrial().getPlayers()) {
                initialLocations.put(player.getUniqueId(), player.getLocation());
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!TrialManager.getTrials().contains(StatuesModifier.this.getTrial())) return;

                    for (Player player : StatuesModifier.this.getTrial().getPlayers()) {
                        if (initialLocations.containsKey(player.getUniqueId()) && !initialLocations.get(player.getUniqueId()).equals(player.getLocation())) {
                            double removedHealth = Math.floor(player.getMaxHealth() * 0.2);
                            double newHealth = Math.max(0, player.getHealth() - removedHealth);

                            player.damage(0);
                            StatuesModifier.this.sendMessage(player, "You moved! -" + (int)removedHealth + "HP");

                            if (newHealth <= 0) {
                                StatuesModifier.this.getTrial().removePlayer(player, TrialSession.TrialPlayerRemoveReason.DIED);
                            } else {
                                player.setHealth(newHealth);
                            }
                        }
                    }
                }
            }.runTaskLater(DemonicTrials.getInstance(), 1L);

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


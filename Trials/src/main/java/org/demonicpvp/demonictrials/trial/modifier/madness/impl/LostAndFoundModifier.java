package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LostAndFoundModifier extends AbstractMadnessModifier {
    private int secondsCounted;

    public LostAndFoundModifier(TrialSession trial) {
        super(trial);
        this.secondsCounted = 0;
    }

    @Override
    public String getName() {
        return "Lost And Found";
    }

    @Override
    public String getDescription() {
        return "Every 20 seconds, all players are randomly teleported to another player's location.";
    }

    @Override
    public Material getMaterial() {
        return Material.LEASH;
    }

    @Override
    public void onTick() {
        if (this.secondsCounted == 0) {
            this.sendCooldown(20);
        }

        this.secondsCounted++;

        if (this.secondsCounted >= 20) {
            List<Pair<Player, Location>> players = Lists.newArrayList();

            for (Player p : this.getTrial().getPlayers()) {
                players.add(Pair.of(p, p.getLocation()));
            }

            Collections.shuffle(players);

            List<Pair<Player, Location>> playersCopy = new ArrayList<>(players);
            Collections.rotate(playersCopy, 1);

            for (int i = 0; i < players.size(); i++) {
                Pair<Player, Location> playerOne = players.get(i);
                Pair<Player, Location> playerTwo = playersCopy.get(i);

                this.getTrial().teleportPlayer(playerOne.getLeft(), playerTwo.getRight());
            }

            this.sendMessage("All players have been teleported to another player's location!");
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

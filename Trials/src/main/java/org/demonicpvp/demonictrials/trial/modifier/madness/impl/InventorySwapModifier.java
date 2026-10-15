package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class InventorySwapModifier extends AbstractMadnessModifier {
    private int secondsCounted;

    public InventorySwapModifier(TrialSession trial) {
        super(trial);
        this.secondsCounted = 0;
    }

    @Override
    public String getName() {
        return "Inventory Swap";
    }

    @Override
    public String getDescription() {
        return "Every 30 seconds, all players are given another player's inventory.";
    }

    @Override
    public Material getMaterial() {
        return Material.CHEST;
    }

    @Override
    public void onTick() {
        if (this.secondsCounted == 0) {
            this.sendCooldown(30);
        }

        this.secondsCounted++;

        if (this.secondsCounted >= 30) {
            List<Player> players = new ArrayList<>(this.getTrial().getPlayers());
            List<ItemStack[]> inventories = Lists.newLinkedList();

            Collections.shuffle(players);

            for (Player player : players) {
                inventories.add(player.getInventory().getContents());
            }

            Collections.rotate(inventories, 1);
            Iterator<ItemStack[]> inventoriesIterator = inventories.iterator();

            for (Player player : players) {
                player.getInventory().setContents(inventoriesIterator.next());
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


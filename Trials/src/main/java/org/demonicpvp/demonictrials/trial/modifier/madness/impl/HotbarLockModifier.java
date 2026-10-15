package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;

public class HotbarLockModifier extends AbstractMadnessModifier implements Listener {
    public HotbarLockModifier(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Hotbar Lock";
    }

    @Override
    public String getDescription() {
        return "All items in your hotbar are locked in their position.";
    }

    @Override
    public Material getMaterial() {
        return Material.BARRIER;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        if (this.getTrial().getCurrentRoom().getName().equals("Portal Panic")) return;

        if (event.getSlotType() == InventoryType.SlotType.QUICKBAR || event.getHotbarButton() >= 0) {
            if (this.getTrial().getPlayers().contains((Player) event.getWhoClicked()) && !this.getTrial().isPausingTimer() && this.getTrial().getCurrentRoom().hasStarted()) {
                this.sendMessage((Player) event.getWhoClicked(), "Your hotbar is locked!");
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void reset() {
        this.sendInfiniteCooldown();
    }

    @Override
    public void onRoomEnd() {
        this.clearCooldown();
    }
}

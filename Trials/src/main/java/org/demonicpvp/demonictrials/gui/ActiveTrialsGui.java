package org.demonicpvp.demonictrials.gui;


import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;
import org.demonicpvp.demonictrials.trial.modifier.madness.MadnessModifier;
import org.demonicpvp.demonictrials.trial.modifier.stackable.StackableModifier;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ActiveTrialsGui implements InventoryProvider {
    private final SmartInventory inventory;

    public ActiveTrialsGui(SmartInventory parent) {
        this.inventory = SmartInventory.builder()
                .title("Active Trials")
                .id(UUID.randomUUID().toString())
                .size(3, 9)
                .manager(CosmicUtils.get().getInventoryManager())
                .provider(this)
                .parent(parent)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 4, ClickableItem.of(
                CommonsUtil.createItem(
                        Material.ARROW,
                        "&c&lBack",
                        "&7Click to return to",
                        "&7the main GUI."
                ), event -> {
                    this.inventory.getParent().ifPresent(parent -> parent.open(player));
                }
        ));

        contents.fillRow(1, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&r", 1, 15)));

        Utils.fillEmptySlots(this.inventory, contents, "&f");
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        List<TrialSession> trials = new ArrayList<>(TrialManager.getTrials()).stream().filter(t -> t.getAllPlayers() != null).collect(Collectors.toList());
        List<Integer> slots = Arrays.stream(Utils.getCenteredSlots(trials.size())).boxed().collect(Collectors.toList());

        for (int i = 0; i < 9; i++) {
            if (!slots.contains(i) && contents.get(2, i).isPresent() && contents.get(2, i).get().getItem().getType() != Material.STAINED_GLASS_PANE) {
                contents.set(2, i, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&r", 1, 8)));
            }
        }

        for (int i = 0; i < trials.size(); i++) {
            TrialSession trial = trials.get(i);

            List<String> lore = Lists.newArrayList("");

            lore.add("&e&lStatus: " + trial.getState().getColoredName());
            lore.add("&e&lRuntime: &f" + Utils.formatTimeAbbreviated(trial.getRuntimeInSeconds()));
            lore.add("&e&lParticipants: &f" + trial.getPlayers().size() + " &7/ " + trial.getAllPlayers().size());

            int idx = 0;

            for (OfflinePlayer participant : trial.getAllPlayers()) {
                idx++;

                boolean isAlive = trial.getPlayers().stream().anyMatch(p -> p.getUniqueId().equals(participant.getUniqueId()));
                lore.add("&e&l" + idx + ". &f" + (isAlive ? "" : "&m") + participant.getName());
            }

            TrialTier tier = trial.getCurrentTier();
            lore.add("&e&lCurrent Tier: " + tier.getColor() + "&l" + tier.getName() + "&7 (" + (tier.ordinal() + 1) + "/" + TrialTier.values().length + ")");

            TrialRoom currentRoom = trial.getCurrentRoom();

            if (currentRoom != null) {
                lore.add("&e&lCurrent Room: " + currentRoom.getName() + " &7(#" + trial.getCurrentRoomNumber() + ")");

                if (trial.hasMadnessModifier(MadnessModifier.TIME_GLITCH)) {
                    lore.add("&e&lTime Left: &f&k1m 30s");
                } else {
                    lore.add("&e&lTime Left: &f" + Utils.formatTimeAbbreviated(trial.getRoomSecondsLeft()));
                }
            }

            if (!trial.getMadnessModifiers().isEmpty()) {
                lore.add("");
                lore.add("&4&lMadness Modifiers");

                for (AbstractMadnessModifier madnessModifier : trial.getMadnessModifiers().values()) {
                    lore.add("&4&l* &c" + madnessModifier.getName());
                }
            }

            if (!trial.getStackableModifiers().isEmpty()) {
                if (!lore.get(lore.size() - 1).isEmpty()) lore.add("");
                lore.add("&6&lStackable Modifiers");

                for (StackableModifier stackableModifier : trial.getStackableModifiers().keySet()) {
                    lore.add("&6&l* &e" + stackableModifier.getName() + " &7(" + trial.getStackableModifiers().get(stackableModifier) + " / " + stackableModifier.getMaxStacks() + ")");
                }
            }

            contents.set(2, slots.get(i), ClickableItem.empty(
                    CommonsUtil.createItem(
                            Material.DIAMOND_SWORD,
                            "&6&l" + trial.getSpawnPlayer().getName() + "'s Trial",
                            trial.getPlayers().size(),
                            0,
                            lore
                    )
            ));
        }

        player.updateInventory();
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

package org.demonicpvp.demonictrials.gui;

import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;
import org.demonicpvp.demonictrials.trial.modifier.madness.MadnessModifier;
import org.demonicpvp.demonictrials.util.Utils;
import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryListener;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;
import org.demonicpvp.demonictrials.util.Utils;
import org.github.paperspigot.Title;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class MadnessModifierBallotGui implements InventoryProvider {
    private final SmartInventory inventory;
    private final TrialSession trial;
    private final Map<MadnessModifier, AbstractMadnessModifier> options;
    private final Map<MadnessModifier, Integer> votes;
    private final Set<UUID> voters;
    private int[] slots;

    public MadnessModifierBallotGui(TrialSession trial) {
        this.trial = trial;
        this.options = Maps.newLinkedHashMap();
        this.voters = Sets.newHashSet();

        for (int i = 0; i < this.trial.getMadnessModifierBallotOptions(); i++) {
            List<MadnessModifier> possibleModifiers = Arrays.stream(MadnessModifier.values())
                    .filter(m -> !this.trial.getMadnessModifiers().containsKey(m) && !this.options.containsKey(m))
                    .collect(Collectors.toList());

            Collections.shuffle(possibleModifiers);

            if (!possibleModifiers.isEmpty()) {
                MadnessModifier key = possibleModifiers.get(0);
                AbstractMadnessModifier instance = null;

                try {
                    instance = key.getClazz().getConstructor(TrialSession.class).newInstance((Object) null);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }

                this.options.put(key, instance);
            }
        }

        this.inventory = SmartInventory.builder()
                .title("Trial: Madness Modifier Ballot")
                .size(3, 9)
                .manager(CosmicUtils.get().getInventoryManager())
                .provider(this)
                .id(UUID.randomUUID().toString())
                .listener(new InventoryListener<>(InventoryCloseEvent.class, event -> {
                    if (this.canEndVote((Player)event.getPlayer())) {
                        this.trial.onMadnessModifierBallotComplete();
                    }
                }))
                .build();

        this.slots = Utils.getCenteredSlots(this.options.size());

        this.votes = Maps.newHashMap();
        this.options.forEach((k, v) -> this.votes.put(k, 0));
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(0, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&r", 1, 15)));

        contents.set(0, 4, ClickableItem.empty(
                CommonsUtil.createItem(
                        Material.CAULDRON_ITEM,
                        "&4&lMadness Modifier Ballot",
                        "&7Madness Modifiers can drastically alter the way",
                        "&7specific rooms need to be completed in your Trial.",
                        "&7They are given after completing the following rooms:",
                        " &f&lRoom 4, 7, 10, 12+: &4&l+1 Madness Modifier",
                        "",
                        "&7You have &f1 vote&f left to cast.",
                        "",
                        "&7The &foption with the most votes&f when the next",
                        "&7room begins will be the Madness Modifier selected!",
                        "",
                        "&c&lNOTE: &cIf the vote ends in a tie, a random",
                        "&coption will be chosen."
                )
        ));

        contents.fillRow(1, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&r", 1, 7)));
        contents.fillRow(2, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&r", 1, 8)));

        this.update(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int index = 0;

        for (MadnessModifier modifier : this.options.keySet()) {
            AbstractMadnessModifier instance = this.options.get(modifier);
            int slot = this.slots[index];

            List<String> lore = Lists.newArrayList();

            for (String s : WordUtils.wrap(instance.getDescription(), 40).split("\n")) {
                lore.add("&7" + s);
            }

            lore.add("");
            lore.add("&7Total Votes: &f" + this.votes.get(modifier));
            lore.add("");
            lore.add("&7Click to vote for this option!");

            ItemStack item = CommonsUtil.createItem(instance.getMaterial(), "&4&lOption #" + (index + 1) + ": &c&l" + instance.getName(), lore);

            item.setAmount(this.votes.get(modifier));

            contents.set(2, slot, ClickableItem.of(item, event -> {
                this.votes.put(modifier, this.votes.get(modifier) + 1);
                CommonsUtil.sendMessage(player, "&a&l(!) &aYou have voted for " + instance.getName());
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                player.closeInventory();

                this.voters.add(player.getUniqueId());

                if (this.canEndVote(player)) {
                    this.trial.onMadnessModifierBallotComplete();
                }
            }));

            index++;
        }
    }

    private boolean canEndVote(Player exclude) {
        boolean noPlayersWithInvOpen = true;

        for (Player trialPlayer : this.trial.getPlayers()) {
            if (trialPlayer.getUniqueId() != exclude.getUniqueId() && trialPlayer.getOpenInventory() != null && trialPlayer.getOpenInventory().getTitle().equals(this.getInventory().getTitle())) {
                noPlayersWithInvOpen = false;
                break;
            }
        }

        return noPlayersWithInvOpen;
    }

    public Map<MadnessModifier, AbstractMadnessModifier> getOptions() {
        return this.options;
    }

    public Map<MadnessModifier, Integer> getVotes() {
        return this.votes;
    }

    public Set<UUID> getVoters() {
        return this.voters;
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

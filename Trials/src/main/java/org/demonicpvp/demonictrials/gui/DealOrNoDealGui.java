package org.demonicpvp.demonictrials.gui;

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

public class DealOrNoDealGui implements InventoryProvider {
    private final TrialSession trial;
    private final SmartInventory inventory;
    private final List<Pair<ItemStack, TrialTier>> items;
    private final int[] slots;
    private boolean hasPicked = false;

    public DealOrNoDealGui(TrialSession trial, Player player) {
        this.items = Lists.newLinkedList();

        int currentRoomNumber = trial.getCurrentRoomNumber() - 1;

        if (trial.getCurrentTier() == TrialTier.RECRUIT) {
            for (int i = 0; i < currentRoomNumber; i++) this.addItem(TrialTier.RECRUIT);
        } else {
            int thisTierItems;
            int belowTierItems;

            if (currentRoomNumber < 15) {
                thisTierItems = currentRoomNumber % 3;
                belowTierItems = 3 - thisTierItems;
            } else {
                thisTierItems = Math.min(currentRoomNumber - 12, 6);
                belowTierItems = 0;
            }

            for (int i = 0; i < thisTierItems; i++) {
                this.addItem(trial.getCurrentTier());
            }

            for (int i = 0; i < belowTierItems; i++) {
                this.addItem(TrialTier.values()[trial.getCurrentTier().ordinal() - 1]);
            }
        }

        this.slots = Utils.getCenteredSlots(this.items.size());

        this.trial = trial;
        this.inventory = SmartInventory.builder()
                .title("Trial: Deal or No Deal?")
                .size(this.items.size() > 1 ? 2 : 1, 9)
                .manager(CosmicUtils.get().getInventoryManager())
                .listener(new InventoryListener<>(InventoryCloseEvent.class, event -> {
                    if (event.getPlayer().getUniqueId() == player.getUniqueId()) {
                        if (!this.hasPicked) {
                            player.sendTitle(new Title(CommonsUtil.color("&c&lNO DEAL"), CommonsUtil.color("&7You will play on... for better and more loot!"), 5, 40, 5));

                            if (this.canProceed((Player)event.getPlayer())) {
                                this.trial.onDealOrNoDealComplete();
                            }
                        }

                        //CosmicPacketHandler.sendPacketToPlayer((Player)event.getPlayer(), new SSetColorMapPacket(Maps.newHashMap()));
                    }
                }))
                .provider(this)
                .id(UUID.randomUUID().toString())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        for (int i = 0; i <= 3; i++) {
            contents.set(i, ClickableItem.of(
                    CommonsUtil.createItem(
                            Material.STAINED_GLASS_PANE,
                            "&a&lDeal",
                            1,
                            5,
                            "&7Click to forfeit this trial!",
                            "",
                            "&7You will receive the items shown in",
                            "&7your &f/collect&7."
                    ), event -> {
                        this.hasPicked = true;

                        player.sendTitle(new Title(CommonsUtil.color("&a&lDEAL"), CommonsUtil.color("&7Your rewards are in &n/collect&7!"), 5, 40, 5));
                        player.closeInventory();

                        this.trial.removePlayer(player, TrialSession.TrialPlayerRemoveReason.ACCEPTED_LOOT);

                        for (Pair<ItemStack, TrialTier> item : this.items) {
                            //RewardInventory.addReward(player, new NBTWrapper(item.getValue0().clone()).build());
                        }

                        if (this.canProceed(player)) {
                            this.trial.onDealOrNoDealComplete();
                        }
                    }
            ));
        }

        for (int i = 5; i <= 8; i++) {
            contents.set(i, ClickableItem.of(
                    CommonsUtil.createItem(
                            Material.STAINED_GLASS_PANE,
                            "&c&lNo Deal",
                            1,
                            14,
                            "&7Click to play on!",
                            "",
                            "&7If you get eliminated, you will receive",
                            "&7nothing, so hedge your bets wisely!"
                    ), event -> {
                        this.hasPicked = true;

                        player.sendTitle(new Title(CommonsUtil.color("&c&lNO DEAL"), CommonsUtil.color("&7You will play on... for better and more loot!"), 5, 40, 5));
                        player.closeInventory();

                        if (this.canProceed(player)) {
                            this.trial.onDealOrNoDealComplete();
                        }
                    }
            ));
        }

        Map<String, Integer> colorMap = Maps.newHashMap();
        int row = this.items.size() > 1 ? 1 : 0;

        for (int i = 0; i < this.slots.length; i++) {
            int slot = this.slots[i];

            ItemStack item = this.items.get(i).getValue0().clone();
            TrialTier tier = this.items.get(i).getValue1();

            List<String> lore = item.hasItemMeta() ? (item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : Lists.newArrayList()) : Lists.newArrayList();
            lore.add("");
            lore.add(tier.getColor() + ChatColor.BOLD + tier.getName() + " Item");

            contents.set(row, slot, ClickableItem.empty(CommonsUtil.modifyItem(item, "&f" + CommonsUtil.formatDisplayName(item), lore)));
            colorMap.put(String.valueOf(Math.max(0, row * 9) + slot), tier.getItemColor());
        }

        //CosmicPacketHandler.sendPacketToPlayer(player, new SSetColorMapPacket(colorMap));
    }

    private boolean canProceed(Player exclude) {
        boolean noPlayersWithInvOpen = true;

        for (Player trialPlayer : this.trial.getPlayers()) {
            if (trialPlayer.getUniqueId() != exclude.getUniqueId() && trialPlayer.getOpenInventory() != null && trialPlayer.getOpenInventory().getTitle().equals(this.getInventory().getTitle())) {
                noPlayersWithInvOpen = false;
                break;
            }
        }

        return noPlayersWithInvOpen;
    }

    private void addItem(TrialTier tier) {
        //ItemStack item = DecimateRewardItem.removeMetaLines(TrialManager.getLootTables().get(tier).getRandomReward().generatePlayerViewableStack(TrialManager.getLootboxManager()), true);
        ItemStack item = new ItemStack(Material.GRASS);

        this.items.add(Pair.with(item, tier));
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

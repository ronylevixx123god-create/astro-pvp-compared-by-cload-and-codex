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
import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.trial.modifier.stackable.StackableModifier;
import org.demonicpvp.demonictrials.trial.trinket.TrialTrinket;
import org.demonicpvp.demonictrials.util.Utils;

import java.util.List;
import java.util.UUID;

public class TrinketsGui implements InventoryProvider {
    private final List<ItemStack> items;
    private final SmartInventory inventory;

    public TrinketsGui(SmartInventory parent) {
        this.items = Lists.newArrayList();

        for (int j = 0; j < TrialTrinket.values().length; j++) {
            for (int i = 1; i <= 5; i++) {
                this.items.add(this.getTrinket(TrialTrinket.values()[j], i));
            }
        }

        this.inventory = SmartInventory.builder()
                .title("Trial Trinkets")
                .id(UUID.randomUUID().toString())
                .size((int) Math.ceil((this.items.size() + 1) / 9.0), 9)
                .manager(CosmicUtils.get().getInventoryManager())
                .provider(this)
                .parent(parent)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.add(ClickableItem.of(
                CommonsUtil.createItem(
                        Material.ARROW,
                        "&c&lBack",
                        "&7Click to return to",
                        "&7the main GUI."
                ), event -> {
                    this.inventory.getParent().ifPresent(parent -> parent.open(player));
                }
        ));

        for (ItemStack item : this.items) {
            contents.add(ClickableItem.of(
                    item,
                    event -> {
                        if (player.hasPermission("demonictrials.admin")) {
                            player.getInventory().addItem(item.clone());
                        }
                    }
            ));
        }

        Utils.fillEmptySlots(this.inventory, contents, "&f&l???");
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }

    private ItemStack getTrinket(TrialTrinket trinket, int amplifier) {
        ItemStack item = DemonicTrials.getInstance().getTrialTrinketItem().createItem(null, trinket.name(), String.valueOf(amplifier));
        item.setAmount(amplifier);

        return item;
    }
}

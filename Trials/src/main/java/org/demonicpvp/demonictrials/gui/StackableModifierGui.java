package org.demonicpvp.demonictrials.gui;


import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
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
import org.bukkit.entity.Player;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;
import org.demonicpvp.demonictrials.trial.modifier.madness.MadnessModifier;
import org.demonicpvp.demonictrials.trial.modifier.stackable.StackableModifier;
import org.demonicpvp.demonictrials.util.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class StackableModifierGui implements InventoryProvider {
    private final SmartInventory inventory;

    public StackableModifierGui(SmartInventory parent) {
        this.inventory = SmartInventory.builder()
                .title("Stackable Modifiers")
                .id(UUID.randomUUID().toString())
                .size((int) Math.ceil((StackableModifier.values().length + 1) / 9.0), 9)
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

        for (StackableModifier modifier : StackableModifier.values()) {
            List<String> lore = Lists.newArrayList("&8Stackable Modifier");

            for (String line : WordUtils.wrap(modifier.getDescription(), 40).split("\n")) {
                lore.add("&7" + line);
            }

            contents.add(ClickableItem.empty(
                    CommonsUtil.createItem(
                            modifier.getMaterial(),
                            "&6&l" + modifier.getName(),
                            lore
                    )
            ));
        }

        Utils.fillEmptySlots(this.inventory, contents, "&f&l???");
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

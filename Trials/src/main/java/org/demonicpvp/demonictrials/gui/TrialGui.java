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
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;
import org.demonicpvp.demonictrials.trial.modifier.madness.MadnessModifier;
import org.demonicpvp.demonictrials.trial.modifier.stackable.StackableModifier;
import org.demonicpvp.demonictrials.util.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class TrialGui implements InventoryProvider {
    private final SmartInventory inventory;
    private boolean rewardsExpanded = false;

    public TrialGui() {
        this.inventory = SmartInventory.builder()
                .title("Trials")
                .id(UUID.randomUUID().toString())
                .size(2, 9)
                .manager(CosmicUtils.get().getInventoryManager())
                .provider(this)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) skullItem.getItemMeta();
        meta.setOwner(player.getName());
        skullItem.setItemMeta(meta);

        contents.set(0, ClickableItem.empty(CommonsUtil.modifyItem(skullItem, "&c&l" + player.getName(), Lists.newArrayList())));

        List<String> madnessModifierLore = Lists.newArrayList(
                "&7Madness Modifiers can drastically alter the way",
                "&7specific rooms need to be completed in your Trial.",
                "&7They are given after completing the following rooms:",
                " &f&lRoom 4, 7, 10, 12+: &4+1 Madness Modifier",
                ""
        );

        for (MadnessModifier value : MadnessModifier.values()) {
            try {
                AbstractMadnessModifier modifier = value.getClazz().getConstructor(TrialSession.class).newInstance((Object) null);
                madnessModifierLore.add("&4&l* &c" + modifier.getName());
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        madnessModifierLore.add("");
        madnessModifierLore.add("&7Click to view more information.");

        contents.set(1, 3, ClickableItem.of(
                CommonsUtil.createItem(
                        Material.CAULDRON_ITEM,
                        "&4&lMadness Modifiers",
                        madnessModifierLore
                ), event -> new MadnessModifierGui(this.getInventory()).getInventory().open(player)
        ));

        List<String> stackableModifierLore = Lists.newArrayList(
                "&7Stackable Modifiers gradually make the Trial more",
                "&7difficult the further you progress in your Trial.",
                "&7They are given after completing the following rooms:",
                " &f&lRoom 2-10: &6+1 Stackable Modifier",
                " &f&lRoom 11: &6+2 Stackable Modifiers",
                " &f&lRoom 12+: &6+3 Stackable Modifiers",
                ""
        );

        for (StackableModifier modifier : StackableModifier.values()) {
            stackableModifierLore.add("&6&l* &e" + modifier.getName());
        }

        stackableModifierLore.add("");
        stackableModifierLore.add("&7Click to view more information.");

        contents.set(1, 4, ClickableItem.of(
                CommonsUtil.createItem(
                        Material.BREWING_STAND_ITEM,
                        "&6&lStackable Modifiers",
                        stackableModifierLore
                ), event -> new StackableModifierGui(this.getInventory()).getInventory().open(player)
        ));

        contents.set(1, 5, ClickableItem.of(
                CommonsUtil.createItem(
                        Material.INK_SACK,
                        "&6&lTrinkets",
                        1,
                        14,
                        "&7Click to view Trial Trinkets."
                ), event -> new TrinketsGui(this.getInventory()).getInventory().open(player)
        ));

        contents.set(4, ClickableItem.empty(
                CommonsUtil.createItem(
                        Material.BOOK,
                        "&b&lCosmic &6&lTrials",
                        "&7Bringing a new spin to the Demonic experience,",
                        "&7Trials are made up of &fchallenging labyrinth",
                        "&fcombinations&7 designed to test your knowledge",
                        "&7and skill set to the max!",
                        "",
                        "&fBattle monsters, avoid traps, solve puzzles,",
                        "&fand master parkour&7 before the timer runs out",
                        "&7to gain high tier loot, or be left with nothing.",
                        "",
                        "&e&lTrial Access",
                        "&7In order to enter a trial, &fyou must obtain",
                        "&fa Trial Portal.&7 Trial Portals can be &fplaced in Warzone",
                        "&for your faction/ally territory."
                )
        ));

        contents.set(3, ClickableItem.of(
                CommonsUtil.createItem(
                        Material.IRON_DOOR,
                        "&e&lRooms",
                        "&7To complete a Trial, you must successfully",
                        "&7complete room objectives. Every 3 rooms,",
                        "&7your tier will increase, resetting your",
                        "&7Time Left to complete the next tier:",
                        "",
                        "&f&l* &fRecruit: &710m to complete &8(Starting Tier)",
                        "&a&l* &aApprentice: &78m to complete",
                        "&b&l* &bMaster: &76m to complete",
                        "&e&l* &eHardcore: &75m to complete",
                        "&6&l* &6Demonic: &74m to complete"
                ), event -> {
                    new RoomsGui(this.getInventory()).getInventory().open(player);
                }
        ));

        List<String> rewardsLore = Lists.newArrayList(
                "&7How far will you be willing to go?!",
                "",
                "&7Your rewards are determined by the tier",
                "&7and quantity of rooms you complete. Each",
                "&7successful completion offers a reward.",
                "",
                "&7Take the Deal and you leave the trial,",
                "&7accepting the Deal as your reward. Lose",
                "&7and receive nothing, so hedge your bets wisely!",
                "",
                "&3&lItems"
        );

        for (int i = 0; i < (this.rewardsExpanded ? 15 : 6); i++) {
            TrialTier tier = TrialTier.values()[i / 3];
            int fromCurrentRoom = ((i + 1) % 3 == 0 ? 3 : (i + 1) % 3);
            int fromLastRoom = 3 - fromCurrentRoom;

            String toAdd = "&3&lRoom " + (i + 1) + ": " + tier.getColor() + fromCurrentRoom + " " + tier.getName() + " Item" + (fromCurrentRoom > 1 ? "s" : "");

            if (fromLastRoom > 0 && tier != TrialTier.RECRUIT) {
                TrialTier lastTier = TrialTier.values()[tier.ordinal() - 1];
                toAdd += " &3&l+ " + lastTier.getColor() + fromLastRoom + " " + lastTier.getName() + " Item" + (fromLastRoom > 1 ? "s" : "");
            }

            rewardsLore.add(toAdd);
        }

        if (this.rewardsExpanded) {
            rewardsLore.add("&3&lRoom 16+: &64+ Demonic Items &7(up to a max of 6)");
            rewardsLore.add(" &7(Room 17+ items will increasingly");
            rewardsLore.add(" &7roll better and higher value loot)");
        } else {
            rewardsLore.add("&3... &7(Right-Click to expand)");
        }

        rewardsLore.add("");

        //rewardsLore.add("&7Left-Click to preview rewards.");
        rewardsLore.add("&7Right-Click to " + (this.rewardsExpanded ? "collapse" : "expand") + " item rewards.");

        contents.set(5, ClickableItem.of(
                CommonsUtil.createItem(
                        Material.DIAMOND,
                        "&3&lRewards",
                        rewardsLore
                ), event -> {
                    if (event.getClick() == ClickType.RIGHT) {
                        this.rewardsExpanded = !this.rewardsExpanded;
                        this.init(player, contents);
                    }
                }
        ));

        contents.set(8, ClickableItem.of(
                CommonsUtil.createItem(
                        Material.INK_SACK,
                        "&a&lActive Trials",
                        1,
                        10,
                        "&7Click to view active trials."
                ), event -> {
                    new ActiveTrialsGui(this.getInventory()).getInventory().open(player);
                }
        ));

        Utils.fillEmptySlots(this.inventory, contents, "&7");
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

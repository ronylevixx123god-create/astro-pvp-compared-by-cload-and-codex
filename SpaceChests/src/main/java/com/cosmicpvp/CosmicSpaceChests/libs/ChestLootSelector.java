/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.cosmicpvp.CosmicSpaceChests.libs;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestLootSelector {
    public Player player;
    public ItemRarity chestRarity;
    public Inventory lootInventory;
    public LootSelectorPhase phase;
    public List<Integer> selectedSlots;
    public LootSelectorAnimationTask animationTask;
    public int lootRevealedCount = 0;

    public ChestLootSelector(Player p, ItemRarity chestRarity) {
        this.player = p;
        this.chestRarity = chestRarity;
        this.selectedSlots = new ArrayList<Integer>();
        this.phase = LootSelectorPhase.GENERATING;
    }

    public void open() {
        this.lootInventory = Bukkit.createInventory(null, (int)45, (String)(this.chestRarity.name + " Space Chest"));
        ItemStack borderItem = ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short)15, (Object)ChatColor.BLACK + " ", new String[0]);
        for (int i = 0; i < this.lootInventory.getSize(); ++i) {
            if (i % 9 == 0 || (i + 1) % 9 == 0 || i < 9 || i > 35) {
                this.lootInventory.setItem(i, borderItem.clone());
                continue;
            }
            ItemStack hiddenItem = ItemUtils.buildItem(Material.STAINED_GLASS, this.getStainedGlassBlockColor(), (Object)CosmicSpaceChestsAPI.getColor(this.chestRarity) + "" + (Object)ChatColor.BOLD + "??? " + (Object)ChatColor.GRAY + "#" + (i + 1 - 10), (Object)ChatColor.GRAY + "Choose " + (Object)CosmicSpaceChestsAPI.getColor(this.chestRarity) + (Object)ChatColor.UNDERLINE + "5 mystery me.astropvp.cratekeys.items" + (Object)ChatColor.GRAY + " and,", (Object)ChatColor.GRAY + "your " + (Object)CosmicSpaceChestsAPI.getColor(this.chestRarity) + (Object)ChatColor.BOLD + this.chestRarity.name + (Object)ChatColor.GRAY + " loot will be revealed.");
            this.lootInventory.setItem(i, hiddenItem);
        }
        this.phase = LootSelectorPhase.PICKING_PANELS;
        this.player.openInventory(this.lootInventory);
        this.player.playSound(this.player.getLocation(), Sound.CHEST_OPEN, 1.0f, 1.0f);
    }

    public short getStainedGlassBlockColor() {
        if (this.chestRarity == ItemRarity.SIMPLE) {
            return 0;
        }
        if (this.chestRarity == ItemRarity.UNIQUE) {
            return 5;
        }
        if (this.chestRarity == ItemRarity.ELITE) {
            return 3;
        }
        if (this.chestRarity == ItemRarity.ULTIMATE) {
            return 4;
        }
        if (this.chestRarity == ItemRarity.LEGENDARY) {
            return 1;
        }
        if (this.chestRarity == ItemRarity.GOD) {
            return 14;
        }
        return 0;
    }

    public void animation() {
        this.phase = LootSelectorPhase.ANIMATION;
        this.animationTask = new LootSelectorAnimationTask(this);
        this.animationTask.runTaskTimer((Plugin)CosmicSpaceChestsPlugin.plugin, 4L, 4L);
    }

    public void endAnimation() {
        this.animationTask.cancel();
        this.animationTask = null;
        this.phase = LootSelectorPhase.OPENNING_PANELS;
    }

    public class LootSelectorAnimationTask
    extends BukkitRunnable {
        ChestLootSelector cls;
        List<Integer> junkSlots;
        List<Integer> junkSlots_copy;
        int iterations = 0;

        LootSelectorAnimationTask(ChestLootSelector cls) {
            this.cls = cls;
            this.junkSlots = new ArrayList<Integer>();
            for (int i = 0; i < cls.lootInventory.getSize(); ++i) {
                if (cls.selectedSlots.contains(i) || cls.lootInventory.getItem(i).getType() == Material.STAINED_GLASS_PANE) continue;
                cls.lootInventory.setItem(i, CosmicSpaceChestsAPI.getRandomStainedGlassItem(this.cls.chestRarity));
                this.junkSlots.add(i);
            }
            Collections.shuffle(this.junkSlots);
            this.junkSlots_copy = new ArrayList<Integer>();
        }

        public void run() {
            this.iterations += 4;
            if (this.iterations < 20) {
                return;
            }
            if (this.junkSlots.size() == 0 && this.junkSlots_copy.size() == 0) {
                int mysteryNum = 0;
                for (int i = 0; i < this.cls.lootInventory.getSize(); ++i) {
                    if (!this.cls.selectedSlots.contains(i)) {
                        if (this.cls.lootInventory.getItem(i).getType() == Material.STAINED_GLASS_PANE && this.cls.lootInventory.getItem(i).getDurability() == 15) continue;
                        this.cls.lootInventory.setItem(i, ItemUtils.buildItem(Material.STAINED_GLASS, (short)15, (Object)ChatColor.BLACK + " ", new String[0]));
                        continue;
                    }
                    ++mysteryNum;
                    this.cls.lootInventory.setItem(i, ItemUtils.setAmount(ItemUtils.buildItem(Material.CHEST, (short)0, (Object)CosmicSpaceChestsAPI.getColor(this.cls.chestRarity) + ChatColor.BOLD.toString() + "Mystery Item " + (Object)ChatColor.UNDERLINE + "#" + (i + 1 - 10), (Object)ChatColor.GRAY + "You have selected this mystery item.", "", (Object)ChatColor.GRAY + "" + (Object)ChatColor.UNDERLINE + "Click to reveal slot rarity...", (Object)ChatColor.WHITE + "* Simple", (Object)ChatColor.GREEN + "* Unique", (Object)ChatColor.AQUA + "* Elite", (Object)ChatColor.YELLOW + "* Ultimate", (Object)ChatColor.GOLD + "* Legendary"), 1));
                }
                this.cls.player.playSound(this.cls.player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.2f);
                this.cls.endAnimation();
                return;
            }
            if (this.iterations >= 100 && this.junkSlots_copy.size() > 0) {
                int x = this.junkSlots_copy.get(0);
                this.junkSlots_copy.remove(0);
                if (!this.cls.selectedSlots.contains(new Integer(x))) {
                    this.cls.lootInventory.setItem(x, ItemUtils.buildItem(Material.STAINED_GLASS, (short)15, (Object)ChatColor.BLACK + " ", ""));
                }
            } else if (this.iterations < 100 && this.junkSlots.size() > 0) {
                int x = this.junkSlots.get(0);
                this.junkSlots.remove(0);
                this.junkSlots_copy.add(x);
                if (!this.cls.selectedSlots.contains(new Integer(x))) {
                    this.cls.lootInventory.setItem(x, CosmicSpaceChestsAPI.getItems(ChestLootSelector.this.chestRarity, CosmicSpaceChestsAPI.getStainedGlassRarity(this.cls.lootInventory.getItem(x)), 1)[0]);
                }
            }
            this.cls.player.playSound(this.cls.player.getLocation(), Sound.CLICK, 1.0f, 0.2f);
        }
    }

    public static enum LootSelectorPhase {
        GENERATING,
        PICKING_PANELS,
        ANIMATION,
        OPENNING_PANELS,
        LOOTING,
        DONE;

    }
}


/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package com.rit.sucy.Anvil;

import com.rit.sucy.Anvil.AnvilView;
import com.rit.sucy.config.LanguageNode;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class CustomAnvil
implements AnvilView,
Listener {
    private static final String NAME = "Anvil (CraftBukkit broke!)";
    private static final String COST = (Object)ChatColor.DARK_RED + "Cost - ";
    private static final ItemStack COMPONENT = CustomAnvil.makeComponentIndicator();
    private static final ItemStack RESULT = CustomAnvil.makeResultIndicator();
    private static final ItemStack MIDDLE = CustomAnvil.makeMiddleIndicator();
    private static final ItemStack[] CONTENTS = new ItemStack[]{COMPONENT, null, null, COMPONENT, MIDDLE, MIDDLE, RESULT, null, RESULT};
    final Plugin plugin;
    final Inventory anvil;
    final Player player;
    int repairCost;

    public CustomAnvil(Plugin plugin, Player player) {
        this.anvil = plugin.getServer().createInventory(null, 9, NAME);
        this.anvil.setContents(CONTENTS);
        this.plugin = plugin;
        this.player = player;
        player.openInventory(this.anvil);
    }

    @Override
    public ItemStack[] getInputSlots() {
        return new ItemStack[]{this.anvil.getItem(1), this.anvil.getItem(2)};
    }

    @Override
    public ItemStack[] getInputSlots(int slot, ItemStack newItem) {
        if (slot == 1) {
            return new ItemStack[]{newItem, this.anvil.getItem(2)};
        }
        if (slot == 2) {
            return new ItemStack[]{this.anvil.getItem(1), newItem};
        }
        throw new IllegalArgumentException(slot + " is not an input slot!");
    }

    @Override
    public int getInputSlotID(int input) {
        if (input == 1 || input == 2) {
            return input;
        }
        throw new IllegalArgumentException("Invalid input number: " + input);
    }

    @Override
    public void setResultSlot(ItemStack item) {
        if (item == null) {
            this.anvil.clear(7);
            return;
        }
        ItemMeta meta = (item = item.clone()).hasItemMeta() ? item.getItemMeta() : this.plugin.getServer().getItemFactory().getItemMeta(item.getType());
        List lore = meta.hasLore() ? meta.getLore() : new ArrayList();
        lore.add(COST + this.repairCost);
        meta.setLore(lore);
        item.setItemMeta(meta);
        this.anvil.setItem(7, item);
    }

    @Override
    public ItemStack getResultSlot() {
        if (this.anvil.getItem(7) == null) {
            return null;
        }
        ItemStack result = this.anvil.getItem(7).clone();
        ItemMeta meta = result.getItemMeta();
        List lore = meta.getLore();
        lore.remove(lore.size() - 1);
        meta.setLore(lore);
        result.setItemMeta(meta);
        return result;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setRepairCost(int repairCost) {
        this.repairCost = repairCost;
        this.setResultSlot(this.getResultSlot());
    }

    @Override
    public int getRepairCost() {
        return this.repairCost;
    }

    @Override
    public boolean isInputSlot(int slot) {
        return slot == 1 || slot == 2;
    }

    @Override
    public int getResultSlotID() {
        return 7;
    }

    @Override
    public void clearInputs() {
        this.anvil.clear(1);
        this.anvil.clear(2);
        this.anvil.setItem(7, this.getResultSlot());
    }

    @Override
    public void close() {
        if (this.anvil.getItem(1) != null) {
            this.player.getInventory().addItem(new ItemStack[]{this.anvil.getItem(1)});
        }
        if (this.anvil.getItem(2) != null) {
            this.player.getInventory().addItem(new ItemStack[]{this.anvil.getItem(2)});
        }
    }

    @Override
    public Inventory getInventory() {
        return this.anvil;
    }

    static ItemStack makeComponentIndicator() {
        List<String> component = CustomAnvil.getText(LanguageNode.ANVIL_COMPONENT);
        return CustomAnvil.makeIndicator(component);
    }

    static ItemStack makeMiddleIndicator() {
        List<String> separator = CustomAnvil.getText(LanguageNode.ANVIL_SEPARATOR);
        return CustomAnvil.makeIndicator(separator);
    }

    static ItemStack makeResultIndicator() {
        List<String> result = CustomAnvil.getText(LanguageNode.ANVIL_RESULT);
        return CustomAnvil.makeIndicator(result);
    }

    static ItemStack makeIndicator(List<String> lines) {
        ItemStack item = new ItemStack(Material.BOOK);
        ArrayList<String> lore = new ArrayList<String>();
        for (int i = 1; i < lines.size(); ++i) {
            lore.add(lines.get(i));
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(lines.get(0));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    static List<String> getText(LanguageNode node) {
        List<String> list = Bukkit.getPluginManager().getPlugin("EnchantmentAPI").getConfig().getStringList(node.getFullPath());
        List<String> coloredList = new ArrayList<>();
        for (String line : list)
            coloredList.add(line.replace('&', '\u00a7'));
        return coloredList;
    }
}

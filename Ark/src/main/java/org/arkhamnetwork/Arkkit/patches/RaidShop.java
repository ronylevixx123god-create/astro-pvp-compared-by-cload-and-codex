/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.Essentials;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.List;

public class RaidShop
        extends ArkPlugin
        implements Listener {
    Inventory inventory;
    Essentials ess;
    DecimalFormat format = new DecimalFormat("#.##");

    public RaidShop(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.inventory = null;
        this.registerListener(this);
        this.registerCommand(new CommandRaidShop("raidshop", "Raid shop command."));
    }

    @Override
    public void disable() {
        this.unregisterCommands();
        this.unregisterListeners();
    }

    public Inventory getRaidShop() {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, 9, "Raid Shop");
            this.inventory.addItem(this.createItem(Material.TNT, 1));
            this.inventory.addItem(this.createItem(Material.SAND, 1));
            this.inventory.addItem(this.createItem(Material.WATER_BUCKET, 1));
            this.inventory.addItem(this.createItem(Material.WEB, 1));
            this.inventory.addItem(this.createItem(Material.DISPENSER, 1));
            this.inventory.addItem(this.createItem(Material.PISTON_BASE, 1));
            this.inventory.addItem(this.createItem(Material.REDSTONE, 16));
            this.inventory.addItem(this.createItem(Material.DIODE, 8));
            this.inventory.addItem(this.createItem(Material.STONE_BUTTON, 1));
        }
        return this.inventory;
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        if (e.getInventory().getName().equals("Raid Shop")) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if (e.getRawSlot() > e.getInventory().getSize() - 1) {
                return;
            }
            ItemStack is = e.getCurrentItem();
            if (is == null) {
                return;
            }
            if ((is = is.clone()).hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().size() == 2) {
                List lore = is.getItemMeta().getLore();
                Player p = (Player) e.getWhoClicked();
                String costLine = (String) lore.get(lore.size() - 1);
                if (e.getClick().isShiftClick()) {
                    is.setAmount(is.getMaxStackSize());
                }
                if (costLine.contains("COST: $")) {
                    double cost = this.getCost(is, true);
                    if (p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(ChatColor.RED + "Your inventory is currently full!");
                        return;
                    }
                    if (cost == -1.0 || cost == 0.0) {
                        return;
                    }
                    if (Arkkit.economy.has(p, cost)) {
                        Arkkit.economy.withdrawPlayer(p, cost);
                        p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "- $" + ChatColor.RED + cost);
                        p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "+ " + ChatColor.GREEN + is.getAmount() + "x " + (is.getType() == Material.DIODE ? "Redstone Repeater" : StringUtils.capitaliseAllWords(is.getType().name().replace("_", " ").toLowerCase())));
                        p.getInventory().addItem(new ItemStack(is.getType(), is.getAmount()));
                    } else {
                        p.sendMessage(ChatColor.RED + "You cannot afford this item!");
                        p.sendMessage(ChatColor.RED + "Cost: $" + cost);
                        p.sendMessage(ChatColor.GRAY + "Current Balance: $" + Arkkit.economy.getBalance(p));
                    }
                }
            }
        }
    }

    public ItemStack createItem(Material material, int amount) {
        ItemStack is = new ItemStack(material, amount);
        double cost = this.getCost(is, true);
        ItemMeta im = is.getItemMeta();
        im.setLore(Lists.newArrayList("", ChatColor.RED + ChatColor.BOLD.toString() + "COST: $" + ChatColor.RED + this.format.format(cost)));
        is.setItemMeta(im);
        return is;
    }

    public double getCost(ItemStack is, boolean respectCount) {
        double costVal = Arkkit.get().getConfig().getDouble("settings.raid_shop." + is.getType().name());
        if (costVal == 0.0) {
            costVal = -1.0;
        }
        if (respectCount && is.getAmount() > 1) {
            costVal *= is.getAmount();
        }
        return costVal;
    }

    public class CommandRaidShop
            extends AbstractCommand {
        public CommandRaidShop(String command, String usage) {
            super(command, usage);
        }

        @Override
        public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
            if (!(sender instanceof Player)) {
                return true;
            }
            Player p = (Player) sender;
            p.openInventory(RaidShop.this.getRaidShop());
            return false;
        }
    }
}


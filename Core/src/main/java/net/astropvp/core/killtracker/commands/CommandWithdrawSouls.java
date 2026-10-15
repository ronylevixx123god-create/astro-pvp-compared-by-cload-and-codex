/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package net.astropvp.core.killtracker.commands;

import net.astropvp.core.killtracker.KillTrackerAPI;
import net.astropvp.core.AstroCore;
import org.apache.commons.lang3.StringUtils;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandWithdrawSouls
        implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
        int shards;
        int kills;
        ItemStack is;
        int amount;
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 1) {
            if (!StringUtils.isNumeric((CharSequence) args[0])) {
                p.sendMessage((Object) ChatColor.RED + "Please enter a valid number!");
                return true;
            }
            amount = Integer.parseInt(args[0]);
            if (amount <= 0) {
                p.sendMessage((Object) ChatColor.RED + "Please enter a valid amount.");
                return true;
            }
            if (p.getItemInHand() != null && KillTrackerAPI.isSoulGem(p.getItemInHand())) {
                if (p.getItemInHand().getAmount() > 1) {
                    p.sendMessage((Object) ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object) ChatColor.RED + "You cannot split stacked Soul Gems.");
                    return true;
                }
                int souls = KillTrackerAPI.getSouls(p.getItemInHand());
                if (souls <= amount) {
                    p.sendMessage((Object) ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object) ChatColor.RED + "You do not have enough souls on this item.");
                    p.sendMessage((Object) ChatColor.GRAY + "Item Souls: " + souls);
                    return true;
                }
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage((Object) ChatColor.RED + "You do not have enough inventory space to withdraw souls!");
                    return true;
                }
                int newSouls = souls - amount;
                KillTrackerAPI.setSouls(p.getItemInHand(), newSouls);
                ItemStack newSoulItem = KillTrackerAPI.createSoulShard(amount);
                if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                    long currentID = ItemID.getItemID((ItemStack) p.getItemInHand());
                    if (currentID == -1L) {
                        ItemID.assignItemID(null, (ItemStack) p.getItemInHand());
                    }
                    ItemID.assignItemID(null, (ItemStack) newSoulItem);
                }
                p.getInventory().addItem(new ItemStack[]{newSoulItem});
                p.sendMessage((Object) ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + (Object) ChatColor.YELLOW + "Your Soul Gem has been split into two gems successfully.");
                p.sendMessage((Object) ChatColor.GREEN + ChatColor.BOLD.toString() + "+ " + amount + " Soul Gem(s)");
                p.playSound(p.getLocation(), Sound.ANVIL_USE, 1.0f, 1.4f);
                return true;
            }
            p.sendMessage((Object) ChatColor.RED + "You must have a valid Soul Gem in your hand to withdraw souls from.");
            return true;
        }
        if (p.getInventory().firstEmpty() == -1) {
            p.sendMessage((Object) ChatColor.RED + "You do not have enough inventory space for the Soul Gem.");

        } else {
            sender.sendMessage("");
            sender.sendMessage((Object) ChatColor.YELLOW + ChatColor.UNDERLINE.toString() + ChatColor.BOLD.toString() + "Soul Commands");
            sender.sendMessage("");
            sender.sendMessage((Object) ChatColor.WHITE + "/withdrawsouls <amount>");
            sender.sendMessage((Object) ChatColor.GRAY + "Withdraw harvested souls from the item in your hand.");
            sender.sendMessage("");
            sender.sendMessage((Object) ChatColor.WHITE + "/splitsouls <amount>");
            sender.sendMessage((Object) ChatColor.GRAY + "Split harvested souls from a Soul Gem in your hand.");
            sender.sendMessage("");
        }
        return true;
    }

    public int getEmptyInventorySpace(Player p) {
        int num = 0;
        for (ItemStack is : p.getInventory()) {
            if (is != null && is.getType() != Material.AIR) continue;
            ++num;
        }
        return num;
    }
}


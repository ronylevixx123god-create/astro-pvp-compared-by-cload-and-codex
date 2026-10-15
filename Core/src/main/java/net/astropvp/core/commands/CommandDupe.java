package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EssentialsUtils;
import com.earth2me.essentials.Enchantments;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.EssentialsConf;
import com.earth2me.essentials.Worth;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CommandDupe implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.DarkRed + "You have to be a player to use this command...");
            return false;
        }
        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName() || !item.getItemMeta().hasLore()) {
            sender.sendMessage(CC.Red + "You cannot dupe this item!");
            return false;
        }
        ItemMeta itemMeta = item.getItemMeta();
        String display = itemMeta.getDisplayName();
        String color = display.substring(0, 2);
        List<String> lore = itemMeta.getLore();
        boolean isOpened = lore.contains(CC.Gray + "Drag n' Drop onto item to enchant.");
        boolean isBook = lore.contains(CC.Gray + "Examine to recieve a random");

        if (item.getType() != Material.BOOK && item.getType() != Material.EMPTY_MAP && item.getType() != Material.INK_SACK) {
            player.sendMessage(CC.translate("&cYou cannot dupe this item."));
            return false;
        }

        if (item.getItemMeta().hasEnchants()) {
            player.sendMessage(CC.translate("&cYou cannot dupe this item."));
            return false;
        }

        if (display.contains(CC.DarkRed)) {
            sender.sendMessage(CC.Red + "You cannot dupe this item.");
            return false;
        }

        if (EnchantmentBook.isEnchantmentBook(item)) {
            int level = EnchantmentBook.getEnchantmentLevel(item);
            if (level > EnchantmentBook.getEnchantment(item).getMaxLevel()) {
                sender.sendMessage(CC.Red + "You cannot dupe this item.");
                return false;
            }
        }

        if(display.contains("Omni")) {
            sender.sendMessage(CC.Red + "You cannot dupe this item.");
            return false;
        }

        if (isBook) {
            item.setAmount(64);
            player.sendMessage(CC.Green + "You have successfully duplicated your item.");
            return false;
        }


        if (display.contains(CC.WhiteB + "Black Scroll") && item.getType() == Material.INK_SACK ||
                display.contains(CC.LightPurpleB + "Heroic Black Scroll") && item.getType() == Material.INK_SACK) {
            item.setAmount(64);
            player.sendMessage(CC.Green + "You have successfully duplicated your item.");
            return false;
        }

        if (display.contains(ChatColor.YELLOW + "White Scroll") && item.getType() == Material.EMPTY_MAP) {
            item.setAmount(64);
            player.sendMessage(CC.Green + "You have successfully duplicated your item.");
            return false;
        }

        if (isOpened && (!display.contains("Rogue") && !display.contains("Revoke"))) {
            item.setAmount(64);

            player.sendMessage(CC.Green + "You have successfully duplicated your item.");
            return false;
        }

        if (!display.startsWith("Enchantment Book")) {
            sender.sendMessage(CC.Red + "You cannot dupe this item.");
            return false;
        }
        sender.sendMessage(CC.Red + "You cannot dupe this item.");
        //item.setAmount(64);
        //player.sendMessage(CC.Green + "You have successfully duplicated your item.");
        return false;
    }
}

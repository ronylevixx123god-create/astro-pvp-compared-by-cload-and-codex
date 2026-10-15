/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.BookMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package com.rit.sucy.commands;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.enchanting.VanillaEnchantment;
import com.rit.sucy.service.ENameParser;
import com.rit.sucy.service.ICommand;
import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BookCommand
implements ICommand {
    @Override
    public boolean execute(EnchantmentAPI plugin, CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta meta = (BookMeta)book.getItemMeta();
        meta.addPage(new String[]{"EnchantmentAPI\nMade by Steven Sucy\n(Eniripsa96)\n\n Enchantment details \n\nCommand:\n\n/enchantapi list #"});
        meta.setAuthor("Eniripsa96");
        meta.setTitle("EnchantmentAPI");
        ArrayList<CustomEnchantment> enchants = new ArrayList<CustomEnchantment>(EnchantmentAPI.getEnchantments());
        Collections.sort(enchants);
        for (CustomEnchantment enchantment : enchants) {
            if (enchantment instanceof VanillaEnchantment || enchantment.getDescription() == null) continue;
            String page = enchantment.name() + " - " + enchantment.getDescription() + "\n\nItems: ";
            if (enchantment.getNaturalMaterials().length > 0) {
                for (Material item : enchantment.getNaturalMaterials()) {
                    page = page + ChatColor.stripColor((String)ENameParser.getName(item)) + ", ";
                }
                page = page.substring(0, page.length() - 2);
            } else {
                page = page + "None";
            }
            meta.addPage(new String[]{page});
        }
        book.setItemMeta((ItemMeta)meta);
        ((Player)sender).getInventory().addItem(new ItemStack[]{book});
        return true;
    }
}


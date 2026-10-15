package org.arkhamnetwork.enchantments;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RandomizationScroll {
    public static ItemStack getRandomizationScroll(final ItemRarity ir) {
        return ItemUtils.buildItem(Material.PAPER, (short) 0, CosmicSpaceChestsAPI.getColor(ir) + "" + ChatColor.BOLD + ir.name + " Randomization Scroll", "", ChatColor.GRAY + "Apply to a(n) " + CosmicSpaceChestsAPI.getColor(ir) + ((ir == ItemRarity.GOD) ? "ANY" : ir.name) + ChatColor.GRAY + " enchantment book", ChatColor.GRAY + "to reroll the success and destroy rates.", "", ChatColor.GRAY + "Drag n' Drop onto " + ChatColor.UNDERLINE + "enchantment book" + ChatColor.GRAY + " to apply.");
    }

    public static boolean isRandomizationScroll(final ItemStack i) {
        return i != null && i.getType() == Material.PAPER && i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().size() >= 3 && i.getItemMeta().getLore().get(2).contains("to reroll the success and destroy rates.");
    }

    public static ItemRarity getRandomizationScrollRarity(final ItemStack i) {
        if (isRandomizationScroll(i)) {
            return CosmicSpaceChestsAPI.getItemRarityByColor(i);
        }
        return ItemRarity.UNKNOWN;
    }

    public static void applyRandomizationScroll(final ItemStack enchantmentBook, final ItemStack scroll) {
        final CustomEnchantment ce = EnchantmentBook.getEnchantment(enchantmentBook);
        final ItemStack i = EnchantmentBook.getEnchantmentBook(ce, EnchantmentBook.getEnchantmentLevel(enchantmentBook));
        final int successRate = EnchantmentBook.getEnchantmentBookSuccessRate(i);
        final int destroyRate = EnchantmentBook.getEnchantmentBookDestructionRate(i);
        final List<String> lore = enchantmentBook.getItemMeta().hasLore() ? enchantmentBook.getItemMeta().getLore() : new ArrayList<String>();
        final List<String> newLore = new ArrayList<String>();
        for (String s : lore) {
            if (s.startsWith(ChatColor.GREEN.toString()) && s.contains("Success Rate")) {
                s = ChatColor.GREEN + "" + successRate + "% Success Rate";
            }
            if (s.startsWith(ChatColor.RED.toString()) && s.contains("Destroy Rate")) {
                s = ChatColor.RED + "" + destroyRate + "% Destroy Rate";
            }
            newLore.add(s);
        }
        final ItemMeta im = enchantmentBook.getItemMeta();
        im.setLore(newLore);
        enchantmentBook.setItemMeta(im);
    }
}

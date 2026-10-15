package net.astropvp.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ChunkBusterUtils {

    public static ItemStack getItem() {
        ItemStack item = new ItemStack(Material.ENDER_PORTAL_FRAME);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Chunk Buster");
        meta.setLore(new ArrayList(Arrays.asList(ChatColor.WHITE + "Place this " + ChatColor.RED + ChatColor.UNDERLINE
                .toString() + "Chunk Buster" + ChatColor.WHITE + " in your claimed", ChatColor.WHITE + "faction land to destroy the chunk!", "", ChatColor.RED + ChatColor.BOLD

                .toString() + "WARNING" + ChatColor.GRAY + ": Will destroy the entire chunk!")));

        item.setItemMeta(meta);

        return item;
    }
}


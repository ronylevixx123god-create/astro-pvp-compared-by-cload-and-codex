package com.cosmicpvp.cosmicwildpvp.util;

import org.bukkit.entity.Entity;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Chunk;
import com.earth2me.essentials.Essentials;
import java.util.Collection;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.command.CommandSender;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import org.bukkit.ChatColor;
import java.util.Iterator;
import java.util.ArrayList;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Arrays;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class BryUtil
{
    private static final char COLOR_CHAR = '&';
    private static final String LOGGER_PREFIX;

    public static ItemStack createItem(final Material material, final String name, final int amount, final int data, final String... lore) {
        final ItemStack item = new ItemStack(material, amount, (short)data);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        meta.setLore((List)color(Arrays.asList(lore)));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(final Material material, final String name, final String... lore) {
        return createItem(material, name, 1, 0, lore);
    }

    public static ItemStack createItem(final Material material, final String name, final int amount, final String... lore) {
        return createItem(material, name, amount, 1, lore);
    }

    public static ItemStack createItem(final Material material, final String name, final int amount, final int data, final List<String> lore) {
        return createItem(material, name, amount, data, (String[])lore.toArray(new String[lore.size()]));
    }

    public static ItemStack createItem(final Material material, final String name, final List<String> lore) {
        return createItem(material, name, (String[])lore.toArray(new String[lore.size()]));
    }

    public static ItemStack createPlayerSkull(final String owner, final String name, final String... lore) {
        final ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(color(name));
        meta.setLore((List)color(Arrays.asList(lore)));
        item.setItemMeta((ItemMeta)meta);
        return item;
    }

    public static ItemStack createPlayerSkull(final String owner, final String name, final List<String> lore) {
        return createPlayerSkull(owner, name, (String[])lore.toArray(new String[lore.size()]));
    }

    public static ItemStack createEntitySkull(final EntityType entityType, final String name, final String... lore) {
        return createPlayerSkull("MHF_" + entityType.name(), name, lore);
    }

    public static List<ItemStack> removeNulls(final List<ItemStack> itemStacks) {
        final List<ItemStack> list = new ArrayList<ItemStack>();
        for (final ItemStack item : itemStacks) {
            if (item != null) {
                if (item.getType() == Material.AIR) {
                    continue;
                }
                list.add(item);
            }
        }
        return list;
    }

    public static List<ItemStack> removeMaterial(final List<ItemStack> itemStacks, final Material material) {
        final List<ItemStack> list = new ArrayList<ItemStack>();
        for (final ItemStack item : itemStacks) {
            if (item != null) {
                if (item.getType() == material) {
                    continue;
                }
                list.add(item);
            }
        }
        return removeNulls(list);
    }

    public static List<ItemStack> removeMaterials(final List<ItemStack> itemStacks, final Material... materials) {
        List<ItemStack> list = itemStacks;
        for (final Material mat : materials) {
            list = removeMaterial(list, mat);
        }
        return list;
    }

    public static int roundInventorySize(final int count) {
        return (count + 8) / 9 * 9;
    }

    public static String oridinate(final String string) {
        return colorAndStrip(string.toLowerCase().replaceAll(" ", "").replace("_", ""));
    }

    public static String colorAndStrip(final String message) {
        return ChatColor.stripColor(color(message));
    }

    public static String color(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> color(final List<String> messages) {
        return messages.stream().map(BryUtil::color).collect(Collectors.toList());
    }

    public static void playSound(final Player player, final Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }

    public static void sendMessage(final CommandSender sender, final String message) {
        if (sender != null) {
            sender.sendMessage(color(message));
        }
    }

    public static void sendMessage(final CommandSender sender, final List<String> messages) {
        sender.sendMessage((String[])color(messages).toArray(new String[messages.size()]));
    }

    public static void fillInventory(final Inventory inventory, final ItemStack item) {
        for (int i = 0; i < inventory.getSize(); ++i) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, item);
            }
        }
    }

    public static void fillInventory(final Inventory inventory) {
        fillInventory(inventory, createItem(Material.STAINED_GLASS_PANE, "&7", 1, 15, "&7"));
    }

    public static boolean hasArmorOn(final Player player) {
        for (final ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && item != null && item.getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }

    public static void log(final String message, final LogLevel level) {
        Bukkit.getConsoleSender().sendMessage(color(level.getColor() + BryUtil.LOGGER_PREFIX.replace("%level%", level.name()) + " " + message));
    }

    public static String formatBoolean(final boolean enabled) {
        return color(enabled ? "&a&lENABLED" : "&c&lDISABLED");
    }

    public static List<ItemStack> getAllContents(final Player player) {
        final List<ItemStack> contents = Lists.newArrayList(player.getInventory().getArmorContents());
        contents.addAll(Lists.newArrayList(player.getInventory().getContents()));
        return removeNulls(contents);
    }

    public static ItemStack findEssItem(final String itemName) {
        try {
            final Essentials ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
            return ess.getItemDb().get(itemName);
        }
        catch (Exception e) {
            System.out.println("could not find item: " + itemName);
            return null;
        }
    }

    public static List getNearbyPlayers(Player player, int radius) {
        return player.getNearbyEntities((double)radius, (double)radius, (double)radius).stream().filter((entity) -> entity instanceof Player).map((entity) -> (Player)entity).collect(Collectors.toList());
    }

    public static List<Location> getChunkBorderLocations(final Chunk c, final int y, final int height) {
        final List<Location> blocks = Lists.newArrayList();
        final int maxX = 15;
        final int maxY = y + height;
        final int maxZ = 15;
        final int minX = -16;
        final int minY = y;
        final int minZ = -16;
        for (int locX = -16; locX <= 15; ++locX) {
            for (int locZ = -16; locZ <= 15; ++locZ) {
                for (int locY = minY; locY <= maxY; ++locY) {
                    final Block block = c.getBlock(locX, locY, locZ);
                    if (locX == -16 || locX == 15 || locZ == -16 || locZ == 15) {
                        blocks.add(block.getLocation());
                    }
                }
            }
        }
        return blocks;
    }

    public static List<Location> getChunkBorderLocations(final Location loc, final int height) {
        final List<Location> blocks = Lists.newArrayList();
        final Chunk c = loc.getChunk();
        final int maxX = 15;
        final int maxY = loc.getBlockY() + height;
        final int maxZ = 15;
        final int minX = -16;
        final int minY = loc.getBlockY() - height;
        final int minZ = -16;
        for (int locX = -16; locX <= 15; ++locX) {
            for (int locZ = -16; locZ <= 15; ++locZ) {
                for (int locY = minY; locY <= maxY; ++locY) {
                    final Block block = c.getBlock(locX, locY, locZ);
                    if (locX == -16 || locX == 15 || locZ == -16 || locZ == 15) {
                        blocks.add(block.getLocation());
                    }
                }
            }
        }
        return blocks;
    }

    static {
        LOGGER_PREFIX = color("[AdvancedDuels] %level%:");
    }

    public enum LogLevel
    {
        INFO(ChatColor.WHITE),
        ERROR(ChatColor.RED),
        SUCCESS(ChatColor.GREEN);

        private ChatColor color;

        private LogLevel(final ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return this.color;
        }
    }
}

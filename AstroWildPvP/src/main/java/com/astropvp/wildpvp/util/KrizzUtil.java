package com.astropvp.wildpvp.util;


import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class KrizzUtil {
    private static final char COLOR_CHAR = '&';
    private static final String LOGGER_PREFIX = color("[AdvancedDuels] %level%:");

    public static ItemStack createItem(Material material, String name, int amount, int data, String... lore) {
        ItemStack item = new ItemStack(material, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        meta.setLore(color(Arrays.asList(lore)));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, String... lore) {
        return createItem(material, name, 1, 0, (String[])lore);
    }

    public static ItemStack createItem(Material material, String name, int amount, String... lore) {
        return createItem(material, name, amount, 1, (String[])lore);
    }

    public static ItemStack createItem(Material material, String name, int amount, int data, List<String> lore) {
        return createItem(material, name, amount, data, (String[])lore.toArray(new String[lore.size()]));
    }

    public static ItemStack createItem(Material material, String name, List<String> lore) {
        return createItem(material, name, (String[])lore.toArray(new String[lore.size()]));
    }

    public static ItemStack createPlayerSkull(String owner, String name, String... lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(color(name));
        meta.setLore(color(Arrays.asList(lore)));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createPlayerSkull(String owner, String name, List<String> lore) {
        return createPlayerSkull(owner, name, (String[])lore.toArray(new String[lore.size()]));
    }

    public static ItemStack createEntitySkull(EntityType entityType, String name, String... lore) {
        return createPlayerSkull("MHF_" + entityType.name(), name, lore);
    }

    public static List<ItemStack> removeNulls(List<ItemStack> itemStacks) {
        List<ItemStack> list = new ArrayList();
        Iterator var2 = itemStacks.iterator();

        while(var2.hasNext()) {
            ItemStack item = (ItemStack)var2.next();
            if (item != null && item.getType() != Material.AIR) {
                list.add(item);
            }
        }

        return list;
    }

    public static List<ItemStack> removeMaterial(List<ItemStack> itemStacks, Material material) {
        List<ItemStack> list = new ArrayList();
        Iterator var3 = itemStacks.iterator();

        while(var3.hasNext()) {
            ItemStack item = (ItemStack)var3.next();
            if (item != null && item.getType() != material) {
                list.add(item);
            }
        }

        return removeNulls(list);
    }

    public static List<ItemStack> removeMaterials(List<ItemStack> itemStacks, Material... materials) {
        List<ItemStack> list = itemStacks;
        Material[] var3 = materials;
        int var4 = materials.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Material mat = var3[var5];
            list = removeMaterial(list, mat);
        }

        return list;
    }

    public static int roundInventorySize(int count) {
        return (count + 8) / 9 * 9;
    }

    public static String oridinate(String string) {
        return colorAndStrip(string.toLowerCase().replaceAll(" ", "").replace("_", ""));
    }

    public static String colorAndStrip(String message) {
        return ChatColor.stripColor(color(message));
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> color(List<String> messages) {
        return (List)messages.stream().map(KrizzUtil::color).collect(Collectors.toList());
    }

    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
    }

    public static void sendMessage(LivingEntity entity, String message) {
        entity.sendMessage(color(message));
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public static void sendMessage(CommandSender sender, List<String> messages) {
        sender.sendMessage((String[])color(messages).toArray(new String[messages.size()]));
    }

    public static void fillInventory(Inventory inventory, ItemStack item) {
        for(int i = 0; i < inventory.getSize(); ++i) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, item);
            }
        }

    }

    public static void fillInventory(Inventory inventory) {
        fillInventory(inventory, createItem(Material.STAINED_GLASS_PANE, "&7", 1, 15, ("&7")));
    }

    public static boolean hasArmorOn(Player player) {
        ItemStack[] var1 = player.getInventory().getArmorContents();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack item = var1[var3];
            if (item != null && item != null && item.getType() != Material.AIR) {
                return true;
            }
        }

        return false;
    }

    public static void log(String message, KrizzUtil.LogLevel level) {
        Bukkit.getConsoleSender().sendMessage(color(level.getColor() + LOGGER_PREFIX.replace("%level%", level.name()) + " " + message));
    }

    public static String formatBoolean(boolean enabled) {
        return color(enabled ? "&a&lENABLED" : "&c&lDISABLED");
    }

    public static List<ItemStack> getAllContents(Player player) {
        List<ItemStack> contents = Lists.newArrayList(player.getInventory().getArmorContents());
        contents.addAll(Lists.newArrayList(player.getInventory().getContents()));
        return removeNulls(contents);
    }



    public static List getNearbyPlayers(Player player, int radius) {
        return (List)player.getNearbyEntities((double)radius, (double)radius, (double)radius).stream().filter((entity) -> {
            return entity instanceof Player;
        }).map((entity) -> {
            return (Player)entity;
        }).collect(Collectors.toList());
    }

    public static List<Location> getChunkBorderLocations(Location loc, int height) {
        List<Location> blocks = Lists.newArrayList();
        Chunk c = loc.getChunk();
        int maxX = 15;
        int maxY = loc.getBlockY() + height;
        int maxZ = 15;
        int minX = -16;
        int minY = loc.getBlockY() - height;
        int minZ = -16;

        for(int locX = minX; locX <= maxX; ++locX) {
            for(int locZ = minZ; locZ <= maxZ; ++locZ) {
                for(int locY = minY; locY <= maxY; ++locY) {
                    Block block = c.getBlock(locX, locY, locZ);
                    if (locX == minX || locX == maxX || locZ == minZ || locZ == maxZ) {
                        blocks.add(block.getLocation());
                    }
                }
            }
        }

        return blocks;
    }

    public static enum LogLevel {
        INFO(ChatColor.WHITE),
        ERROR(ChatColor.RED),
        SUCCESS(ChatColor.GREEN);

        private ChatColor color;

        public ChatColor getColor() {
            return this.color;
        }

        private LogLevel(ChatColor color) {
            this.color = color;
        }
    }
}
 
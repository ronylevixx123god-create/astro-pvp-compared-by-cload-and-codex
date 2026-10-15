package com.bryside.astroattributes.utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static ItemStack pane(int id) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) id);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(translate("&7"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static String convertToStringLocation(Location loc) {
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();
        String world = loc.getWorld().getName();
        String result = world + " " + x + " " + y + " " + z;
        return result;
    }

    public static Location convertToLocation(String str) {
        String[] list = str.split(" ");
        World world = Bukkit.getWorld(list[0]);
        double x = Double.parseDouble(list[1]);
        double y = Double.parseDouble(list[2]);
        double z = Double.parseDouble(list[3]);
        return new Location(world, x, y, z);
    }

    public static void setPlayerContents(ItemStack[] items, Player player) {
        ItemStack[] var2 = items;
        int var3 = items.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            ItemStack item = var2[var4];
            if (item != null) {
                String type = item.getType().name();
                if (type.endsWith("_HELMET")) {
                    player.getInventory().setHelmet(item);
                } else if (type.endsWith("_CHESTPLATE")) {
                    player.getInventory().setChestplate(item);
                } else if (type.endsWith("_LEGGINGS")) {
                    player.getInventory().setLeggings(item);
                } else if (type.endsWith("_BOOTS")) {
                    player.getInventory().setBoots(item);
                }
            }
        }

    }

    public static void fullInventoryClear(Player player) {
        player.setItemOnCursor((ItemStack) null);
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.setItemOnCursor(new ItemStack(Material.AIR));
    }

    public static ItemStack createItemStack(Material material) {
        return new ItemStack(material);
    }

    public static ItemStack createItemStack(Material material, int amount) {
        return new ItemStack(material, amount);
    }

    public static ItemStack createItemStack(Material material, int amount, int durability) {
        return new ItemStack(material, amount, (short) durability);
    }

    public static ItemStack setDisplayName(ItemStack item, String display) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(translate(display));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack setLore(ItemStack item, List lore) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack setLore(ItemStack item, ArrayList lore) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static boolean canPickupItems(Player player) {
        ItemStack[] contents;
        int length = (contents = player.getInventory().getContents()).length;

        for (int i = 0; i < length; ++i) {
            ItemStack item = contents[i];
            if (item == null) {
                return true;
            }
        }

        return false;
    }

    public static double distance(Player player, Player other) {
        return player.getLocation().distance(other.getLocation());
    }

    public static String addComma(int moneyAmount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format((long) moneyAmount);
        return numberAsString;
    }

    public static String addComma(long moneyAmount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(moneyAmount);
        return numberAsString;
    }

    public static String addComma(double moneyAmount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(moneyAmount);
        return numberAsString;
    }

    public static boolean isWeapon(ItemStack item) {
        Material type = item.getType();
        String name = type.name();
        return name.endsWith("_SWORD") || name.endsWith("_AXE");
    }

    public static boolean isInventoryFull(Player player) {
        ItemStack[] contents;
        int length = (contents = player.getInventory().getContents()).length;

        for (int i = 0; i < length; ++i) {
            ItemStack x = contents[i];
            if (x == null) {
                return false;
            }
        }

        return true;
    }

    public static String format(double number) {
        String numberString = String.valueOf(number);
        if (!numberString.contains(".")) {
            return String.valueOf(number);
        } else {
            String last = numberString.substring(0, 1);
            int index = 0;
            ArrayList locations = new ArrayList();
            boolean passed = false;

            for (int len = numberString.length(); index < len; ++index) {
                String current = numberString.substring(index, index + 1);
                if (current.contentEquals(".") && !last.contentEquals("0")) {
                    locations.add(index);
                } else if (passed || !current.contentEquals("0") && !passed) {
                    locations.add(index);
                }

                if (current.equals(".")) {
                    passed = true;
                }

                last = current;
            }

            StringBuilder builder = new StringBuilder();
            Iterator iterator = locations.iterator();

            while (iterator.hasNext()) {
                int l = Integer.valueOf((Integer) iterator.next());
                builder.append(numberString, l, l + 1);
            }

            String line = builder.toString();
            if (line.endsWith(".0")) {
                line = line.substring(0, line.indexOf("."));
            }

            return line;
        }
    }
}

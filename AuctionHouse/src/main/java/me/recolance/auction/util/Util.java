/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.util;

import me.recolance.auction.Auction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class Util {
    private static DecimalFormat format = new DecimalFormat("#,###");

    public static String setStringColors(String message) {
        return ChatColor.translateAlternateColorCodes((char) '&', (String) message);
    }

    public static void message(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes((char) '&', (String) message));
    }

    public static String formatMoney(long value) {
        return format.format(value);
    }

    public static String formatMoney(double value) {
        return format.format(value);
    }

    public static void closePlayerInventoryOnClick(Player p) {
        Bukkit.getScheduler().runTaskLater((Plugin) Auction.getPlugin(), ((Player) p)::closeInventory, 1L);
    }

    public static Long parseLongOrNull(String strg) {
        try {
            return Long.parseLong(strg);
        } catch (Exception exception) {
            return null;
        }
    }

    public static long getMoneyValue(String formatted_value) {
        String numb_val = ChatColor.stripColor((String) formatted_value.replaceAll(",", "").replace("$", "")).toLowerCase();
        numb_val = numb_val.replace("k", "000");
        numb_val = numb_val.replace("million", "000000");
        numb_val = numb_val.replace("billion", "000000000");
        numb_val = numb_val.replace("trillion", "000000000000");
        numb_val = numb_val.replace("mil", "000000");
        numb_val = numb_val.replace("bil", "000000000");
        numb_val = numb_val.replace("tril", "000000000000");
        numb_val = numb_val.replace("m", "000000");
        numb_val = numb_val.replace("b", "000000000");
        numb_val = numb_val.replace("t", "000000000000");
        return Long.parseLong(numb_val);
    }

    public static boolean hasInventorySpace(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) continue;
            return true;
        }
        return false;
    }

    public static int getFreeInventorySpace(Player player) {
        int i = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) continue;
            ++i;
        }
        return i;
    }

    public static boolean itemLoreContains(ItemStack i, String s) {
        if (i == null || !i.hasItemMeta() || !i.getItemMeta().hasLore()) {
            return false;
        }
        for (String lore : i.getItemMeta().getLore()) {
            if (!lore.equals(s)) continue;
            return true;
        }
        return false;
    }

    public static String getItemName(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }
        String name = item.getType().name();
        name = name.replace('_', ' ');
        StringBuilder sb = new StringBuilder(name = name.toLowerCase());
        if (sb.length() > 0) {
            sb.setCharAt(0, Character.toUpperCase(name.charAt(0)));
        }
        int i = 0;
        for (char character : name.toCharArray()) {
            if (character == ' ') {
                sb.setCharAt(i + 1, Character.toUpperCase(sb.charAt(i + 1)));
            }
            ++i;
        }
        return sb.toString();
    }

    public static boolean stringIsNumerical(String string) {
        Pattern pattern = Pattern.compile("[^0-9]");
        return !pattern.matcher(string).find();
    }

    public static String getRemainingTimeString(long startTime, long lengthOfTime) {
        return Util.timeToDHMSString(startTime - System.currentTimeMillis() + lengthOfTime);
    }

    public static String timeToDHMSString(long time) {
        int second = 1000;
        int minute = second * 60;
        int hour = minute * 60;
        int day = hour * 24;
        long playTimeManipulator = time;
        StringBuilder timeString = new StringBuilder();
        if (playTimeManipulator >= (long) day) {
            long finalDay = playTimeManipulator / (long) day;
            playTimeManipulator -= finalDay * (long) day;
            timeString.append(finalDay + (finalDay > 1L ? "d " : "d "));
        }
        if (playTimeManipulator >= (long) hour) {
            long finalHour = playTimeManipulator / (long) hour;
            playTimeManipulator -= finalHour * (long) hour;
            timeString.append(finalHour + (finalHour > 1L ? "h " : "h "));
        }
        if (playTimeManipulator >= (long) minute) {
            long finalMinute = playTimeManipulator / (long) minute;
            playTimeManipulator -= finalMinute * (long) minute;
            timeString.append(finalMinute + (finalMinute > 1L ? "m " : "m "));
        }
        if (playTimeManipulator >= (long) second) {
            long finalSecond = playTimeManipulator / (long) second;
            playTimeManipulator -= finalSecond * (long) second;
            timeString.append(finalSecond + (finalSecond > 1L ? "s" : "s"));
        }
        return timeString.toString();
    }
}


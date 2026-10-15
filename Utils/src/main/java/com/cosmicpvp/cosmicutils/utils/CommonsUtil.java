/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  net.minecraft.server.v1_8_R3.NBTTagList
 *  org.apache.commons.lang.WordUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.cosmicpvp.cosmicutils.utils;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class CommonsUtil {
    public static String formatDisplayName(ItemStack item) {
        String displayName = item.getItemMeta().getDisplayName();
        if (displayName == null) {
            displayName = WordUtils.capitalizeFully(item.getType().name().replace("_", " "));
        }
        return displayName;
    }

    public static ItemStack addGlow(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) {
            tag = nmsStack.getTag();
        }
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public static ItemStack createItem(Material material, String name, int amount, int data, String... lore) {
        ItemStack item = new ItemStack(material, amount, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CommonsUtil.color(name));
        meta.setLore(CommonsUtil.color(Lists.newArrayList(lore)));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, String... lore) {
        return CommonsUtil.createItem(material, name, 1, 0, lore);
    }

    public static ItemStack createItem(Material material, String name, int amount, String... lore) {
        return CommonsUtil.createItem(material, name, amount, 0, lore);
    }

    public static ItemStack createItem(Material material, String name, int amount, int data, List<String> lore) {
        return CommonsUtil.createItem(material, name, amount, data, lore.toArray(new String[lore.size()]));
    }

    public static ItemStack createItem(Material material, String name, List<String> lore) {
        return CommonsUtil.createItem(material, name, lore.toArray(new String[lore.size()]));
    }

    public static ItemStack modifyItem(ItemStack item, ItemMeta meta) {
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack modifyItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CommonsUtil.color(name));
        return CommonsUtil.modifyItem(item, meta);
    }

    public static ItemStack modifyItem(ItemStack item, String name, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CommonsUtil.color(name));
        meta.setLore(CommonsUtil.color(lore));
        return CommonsUtil.modifyItem(item, meta);
    }

    public static int roundInventorySize(int count) {
        int result = (count + 8) / 9 * 9;
        return result <= 0 ? 9 : result;
    }

    public static String getCenteredMessage(String message) {
        if (message != null && !message.equals("")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            int messagePxSize = 0;
            boolean previousCode = false;
            boolean isBold = false;
            char[] var4 = message.toCharArray();
            int toCompensate = var4.length;

            int spaceLength;
            for(spaceLength = 0; spaceLength < toCompensate; ++spaceLength) {
                char c = var4[spaceLength];
                if (c == 167) {
                    previousCode = true;
                } else if (previousCode) {
                    previousCode = false;
                    if (c != 'l' && c != 'L') {
                        isBold = false;
                    } else {
                        isBold = true;
                    }
                } else {
                    StringUtils.DefaultFontInfo dFI = StringUtils.DefaultFontInfo.getDefaultFontInfo(c);
                    messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                    ++messagePxSize;
                }
            }

            int halvedMessageSize = messagePxSize / 2;
            toCompensate = 154 - halvedMessageSize;
            spaceLength = StringUtils.DefaultFontInfo.SPACE.getLength() + 1;
            StringBuilder sb = new StringBuilder();

            for(int compensated = 0; compensated < toCompensate; compensated += spaceLength) {
                sb.append(" ");
            }

            return sb.toString() + message;
        } else {
            return "";
        }
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String stripColor(String message) {
        return ChatColor.stripColor(message);
    }

    public static List<String> color(List<String> messages, ChatColor color) {
        return messages.stream().map(str -> color + str).collect(Collectors.toList());
    }

    public static List<String> color(List<String> messages) {
        return messages.stream().map(CommonsUtil::color).collect(Collectors.toList());
    }

    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(CommonsUtil.color(message));
    }

    public static void sendMessage(CommandSender sender, List<String> messages) {
        sender.sendMessage(CommonsUtil.color(messages).toArray(new String[messages.size()]));
    }

    public static void broadcastMessage(List<CommandSender> users, String message) {
        String colouredMessage = CommonsUtil.color(message);
        users.forEach(user -> user.sendMessage(colouredMessage));
    }

    public static void broadcastMessage(String message) {
        String colouredMessage = CommonsUtil.color(message);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(colouredMessage));
    }

    public static void broadcastMessage(List<CommandSender> users, List<String> messages) {
        messages.forEach(target -> CommonsUtil.broadcastMessage(users, target));
    }

    public static void broadcastMessage(List<String> message) {
        message.forEach(CommonsUtil::broadcastMessage);
    }

    public static void fillInventory(Inventory inventory, ItemStack item) {
        for (int i = 0; i < inventory.getSize(); ++i) {
            if (inventory.getItem(i) != null) continue;
            inventory.setItem(i, item);
        }
    }

    public static void fillInventory(Inventory inventory) {
        CommonsUtil.fillInventory(inventory, CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&7", 1, 15, "&7"));
    }

    public static boolean hasArmorOn(Player player) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item == null || item == null || item.getType() == Material.AIR) continue;
            return true;
        }
        return false;
    }

    public static double extractDouble(String original) {
        String raw = ChatColor.stripColor(original);
        raw = raw.replaceAll("[\\s+a-zA-Z :]", "").replace("%", "");
        return CommonsUtil.tryParseDouble(raw);
    }

    public static int extractInt(String original) {
        String raw = ChatColor.stripColor(original);
        return CommonsUtil.tryParseInt(raw.replaceAll("[^0-9]", ""));
    }

    public static int tryParseInt(String unparsed) {
        try {
            return Integer.parseInt(unparsed);
        } catch (Exception e) {
            return -1;
        }
    }

    public static double tryParseDouble(String unparsed) {
        try {
            return Double.parseDouble(unparsed);
        } catch (Exception e) {
            return -1.0;
        }
    }

    public static String formatBoolean(boolean enabled) {
        return CommonsUtil.color(enabled ? "&a&lENABLED" : "&c&lDISABLED");
    }
}


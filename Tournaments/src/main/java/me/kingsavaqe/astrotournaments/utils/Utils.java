package me.kingsavaqe.astrotournaments.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.arkhamnetwork.enchantments.libs.SpecificCustomEnchantment;
import org.arkhamnetwork.enchantments.listeners.PlayerPotionEffectListener;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static String fixColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    public static Location getLocationFromString(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Tournament location is missing");
        }
        String[] arg = string.split(",");
        if (arg.length != 4 && arg.length != 6) {
            throw new IllegalArgumentException("Tournament location must be world,x,y,z[,yaw,pitch]");
        }
        double[] parsed = new double[5];
        // Old Astro configs omitted orientation. Preserve their coordinates without requiring a reset.
        for (int a = 0; a < arg.length - 1; a++) {
            parsed[a] = Double.parseDouble(arg[a + 1]);
        }

        return new Location(Bukkit.getWorld(arg[0]), parsed[0], parsed[1], parsed[2], (float) parsed[3], (float) parsed[4]);
    }

    public static String getProperCase(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        char[] charArray = text.toLowerCase().toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        sb.append(new String(charArray));
        return sb.toString().trim();
    }

    public static ItemStack createPlayerSkull(String owner, String name, List<String> lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(CC.translate(name));
        meta.setLore(color(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static List<String> color(List<String> messages) {
        return messages.stream().map(CC::translate).collect(Collectors.toList());
    }

    public static boolean isEmptyInventory(Player player) {
        return Stream.of(player.getInventory().getContents()).allMatch(itemStack -> itemStack == null || itemStack.getType() == Material.AIR) &&
                Stream.of(player.getInventory().getArmorContents()).allMatch(itemStack -> itemStack == null || itemStack.getType() == Material.AIR);
    }


    public static boolean hasAvaliableSlot(Player player, int amount) {
        int amountOpen = 0;
        Inventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item == null) {
                amountOpen += 1;
            }
        }
        return amountOpen >= amount;
    }

    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 2.0F, 2.0F);
    }

    public static void emptyInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        for (PotionEffect pe : player.getActivePotionEffects()) {
            player.removePotionEffect(pe.getType());
        }
        Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), () -> {
            if (!player.getPlayer().isDead() && player.getPlayer().isValid()) {
                PlayerPotionEffectListener.rescanPlayerArmor(player);
            }
        }, 5L);
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package net.astropvp.core.killtracker;

import com.google.common.collect.Lists;
import net.astropvp.core.killtracker.events.PlayerSubtractSoulsEvent;
import net.astropvp.core.utils.ItemBuilder;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillTrackerAPI {
    public static List<String> soulToggle = new ArrayList<String>();

    public static boolean toggleGodMode(Player p) {
        if (soulToggle.contains(p.getName())) {
            soulToggle.remove(p.getName());
        } else {
            soulToggle.add(p.getName());
        }
        return soulToggle.contains(p.getName());
    }

    public static boolean hasGodMode(Player p) {
        return soulToggle.contains(p.getName());
    }

    public static boolean isSoulGem(ItemStack is) {
        return is != null && is.getType() == Material.EMERALD && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().hasLore() && ChatColor.stripColor(is.getItemMeta().getDisplayName()).contains("Soul Gem [") && is.getItemMeta().getLore().size() > 1;
    }

    public static void removeSoulsFromGems(Player p, int amount) {
        PlayerSubtractSoulsEvent soulsEvent = new PlayerSubtractSoulsEvent(p, amount);
        if (!soulsEvent.callEvent()) {
            return;
        }
        int to_remove = amount = soulsEvent.getSoulsToSubtract();
        for (ItemStack is : p.getInventory()) {
            if (to_remove <= 0) {
                return;
            }
            if (is == null || is.getType() == Material.AIR || !KillTrackerAPI.isSoulGem(is)) continue;
            int souls = KillTrackerAPI.getSouls(is);
            if (to_remove >= souls) {
                p.getInventory().removeItem(is);
                to_remove -= souls;
                continue;
            }
            KillTrackerAPI.setSouls(is, souls -= to_remove);
            return;
        }
    }

    public static boolean hasSoulAmount(Player p, int amount) {
        return KillTrackerAPI.getAllSouls(p) >= amount;
    }

    public static boolean isBlacklistedSoul(ItemStack item, Map<Long, List<ItemStack>> list) {
        if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
            long id = ItemID.getItemID(item);
            if (id == -1L) {
                return false;
            }
            List found = list.computeIfAbsent(id, e -> Lists.newArrayList());
            found.add(item);
            return ItemID.blacklist_iid.contains(id);
        }
        return false;
    }

    public static boolean isBlacklistedSoul(Long id, ItemStack item, Map<Long, List<ItemStack>> list) {
        if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
            if (id == null) {
                id = ItemID.getItemID(item);
            }
            if (id == -1L) {
                return false;
            }
            List found = list.computeIfAbsent(id, e -> Lists.newArrayList());
            found.add(item);
            return ItemID.blacklist_iid.contains(id);
        }
        return false;
    }

    public static boolean isSameID(ItemStack item, ItemStack is) {
        long secondID;
        long id;
        return ArkPlugins.ITEM_ID.getArkPlugin().isRunning() && (id = ItemID.getItemID(item)) != -1L && (secondID = ItemID.getItemID(is)) != -1L && secondID == id;
    }

    public static int getAllSouls(Player p) {
        int souls = 0;
        HashMap<Long, List<ItemStack>> dupedGems = new HashMap<Long, List<ItemStack>>();
        for (int slot = 0; slot < p.getInventory().getSize(); ++slot) {
            ItemStack is = p.getInventory().getItem(slot);
            if (is == null || is.getType() == Material.AIR || !KillTrackerAPI.isSoulGem(is)) continue;
            long id = ItemID.getItemID(is);
            if (KillTrackerAPI.isBlacklistedSoul(id, is, dupedGems)) {
                Bukkit.getLogger().info("Removing " + is.getAmount() + " (" + KillTrackerAPI.getSouls(is) + ") ID=" + id + " duped souls from " + p.getName());
                p.getInventory().removeItem(is);
                continue;
            }
            if (id == -1L && is.getAmount() == 1) {
                is = ItemID.assignItemID(null, is);
                p.getInventory().setItem(slot, is);
                //p.updateInventory();
            }
            souls += KillTrackerAPI.getSouls(is);
        }
        dupedGems.forEach((iid, lists) -> {
            if (lists.size() > 1) {
                lists.forEach(is -> p.getInventory().removeItem(new ItemStack[]{is}));
                if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                    ItemID.blacklistIID(iid, "Duped Soul Gem (" + p.getName() + ")");
                }
                Bukkit.getLogger().info("Removing " + lists.size() + " Counts of Duped Gems (" + iid + ") for " + p.getName());
            }
        });
        return souls;
    }

    public static void setSouls(ItemStack is, int amount) {
        if (KillTrackerAPI.isSoulGem(is)) {
            is.setItemMeta(KillTrackerAPI.createSoulShard(amount).clone().getItemMeta());
        }
    }

    public static int getSouls(ItemStack is) {
        if (is != null && is.getType() == Material.EMERALD && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().hasLore()) {
            String name = is.getItemMeta().getDisplayName();
            if (!name.startsWith(ChatColor.RED + ChatColor.BOLD.toString() + "Soul Gem [")) {
                return 0;
            }
            name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
            return Integer.parseInt(name.split("\\[")[1].split("\\]")[0]);
        }
        return -1;
    }



    public static ItemStack createSoulShard(int amount) {
        return new ItemBuilder(Material.EMERALD, ChatColor.RED + ChatColor.BOLD.toString() + "Soul Gem [" + KillTrackerAPI.getColor(amount) + amount + ChatColor.RED + ChatColor.RED + ChatColor.BOLD.toString() + "]", "", ChatColor.RED + ChatColor.BOLD.toString() + "* " + ChatColor.RED + "Click this item to toggle " + ChatColor.RED + ChatColor.UNDERLINE.toString() + "Soul Mode", ChatColor.GRAY + "While in Soul Mode your ACTIVE god tier", ChatColor.GRAY + "enchantment will activate and drain souls", ChatColor.GRAY + "for as long as this mode is enabled.", "", ChatColor.RED + ChatColor.BOLD.toString() + "* " + ChatColor.GRAY + "Use " + ChatColor.RED + ChatColor.UNDERLINE.toString() + "/splitsouls" + ChatColor.GRAY + " with this item", ChatColor.GRAY + "to split souls off of it.", "", ChatColor.RED + ChatColor.BOLD.toString() + "* " + ChatColor.GRAY + "Stack other " + ChatColor.RED + "Soul Gems" + ChatColor.GRAY + " ontop of this", ChatColor.GRAY + "one to combine their soul counts.").build();
    }

    private static String getColor(int amount) {
        if (amount >= 5000) {
            return ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString();
        }
        if (amount >= 1000) {
            return ChatColor.GOLD.toString() + ChatColor.BOLD.toString();
        }
        if (amount >= 500) {
            return ChatColor.AQUA.toString();
        }
        if (amount >= 100) {
            return ChatColor.GREEN.toString();
        }
        if (amount >= 0) {
            return ChatColor.WHITE.toString();
        }
        return "";
    }
}


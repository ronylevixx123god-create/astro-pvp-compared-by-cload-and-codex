// 
// Decompiled by Procyon v0.5.36
// 

package me.ifamasssxd.cosmicconquest;

import me.ifamasssxd.cosmicconquest.libs.ItemBuilder;
import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.ChatColor;
import java.util.ListIterator;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class CosmicConquestAPI
{
    private static List<String> lore;
    private static List<String> heroicLore;
    private static ItemStack clone;
    private static ItemStack heroicCrystal;
    
    public static boolean isFactionToken(final ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().equals(CosmicConquestAPI.lore) && item.getItemMeta().getDisplayName().contains("Faction Crystal");
    }
    
    public static boolean isHeroicFactionToken(final ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().equals(CosmicConquestAPI.heroicLore) && item.getItemMeta().getDisplayName().contains("Heroic Faction Crystal");
    }
    
    private static ItemStack addGlow(final ItemStack item) {
        final net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) {
            tag = nmsStack.getTag();
        }
        final NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }
    
    public static ItemStack createHeroicToken(final int amount) {
        final ItemStack item = CosmicConquestAPI.heroicCrystal.clone();
        item.setAmount(amount);
        return item;
    }
    
    public static ItemStack createFactionToken(final int amount) {
        final ItemStack item = CosmicConquestAPI.clone.clone();
        item.setAmount(amount);
        return item;
    }
    
    public static int getHeroicTokensInInventory(final Player player) {
        int retr = 0;
        for (final ItemStack item : player.getInventory()) {
            if (item != null && item.getType() != Material.AIR && isHeroicFactionToken(item)) {
                retr += item.getAmount();
            }
        }
        return retr;
    }
    
    public static boolean hasHeroicTokensInInventory(final Player player, final int tokenCount) {
        return getHeroicTokensInInventory(player) >= tokenCount;
    }
    
    public static void removeHeroicTokensFromInventory(final Player player, final int toRemove) {
        int leftToRemove = toRemove;
        for (final ItemStack item : player.getInventory()) {
            if (leftToRemove <= 0) {
                break;
            }
            if (item == null || item.getType() == Material.AIR || !isHeroicFactionToken(item)) {
                continue;
            }
            if (item.getAmount() <= leftToRemove) {
                leftToRemove -= item.getAmount();
                player.getInventory().removeItem(new ItemStack[] { item });
            }
            else {
                item.setAmount(item.getAmount() - leftToRemove);
                leftToRemove = 0;
            }
        }
        player.updateInventory();
    }
    
    public static int getTokensInInventory(final Player player) {
        int retr = 0;
        for (final ItemStack item : player.getInventory()) {
            if (item != null && isFactionToken(item)) {
                retr += item.getAmount();
            }
        }
        return retr;
    }
    
    public static boolean hasTokensInInventory(final Player player, final int tokenCount) {
        return getTokensInInventory(player) >= tokenCount;
    }
    
    public static void removeTokensFromInventory(final Player player, final int toRemove) {
        int leftToRemove = toRemove;
        for (final ItemStack item : player.getInventory()) {
            if (leftToRemove <= 0) {
                break;
            }
            if (item == null || !isFactionToken(item)) {
                continue;
            }
            if (item.getAmount() <= leftToRemove) {
                leftToRemove -= item.getAmount();
                player.getInventory().removeItem(new ItemStack[] { item });
            }
            else {
                item.setAmount(item.getAmount() - leftToRemove);
                leftToRemove = 0;
            }
        }
        player.updateInventory();
    }
    
    static {
        CosmicConquestAPI.lore = (List<String>)Lists.newArrayList(new String[] { ChatColor.GRAY + "This crystal can be used to purchase", ChatColor.AQUA + "/f upgrades" + ChatColor.GRAY + " for your Faction." });
        CosmicConquestAPI.heroicLore = (List<String>)Lists.newArrayList(new String[] { ChatColor.GRAY + "This heroic crystal can be used to purchase", ChatColor.AQUA + "/f upgrades" + ChatColor.GRAY + " for your Faction." });
        CosmicConquestAPI.clone = addGlow(ItemBuilder.buildItem(Material.QUARTZ, 1, (short)0, ChatColor.AQUA.toString() + ChatColor.BOLD + "Faction Crystal", CosmicConquestAPI.lore));
        CosmicConquestAPI.heroicCrystal = addGlow(ItemBuilder.buildItem(Material.INK_SACK, 1, (short)14, ChatColor.GOLD.toString() + ChatColor.BOLD + "Heroic Faction Crystal", CosmicConquestAPI.heroicLore));
    }
}

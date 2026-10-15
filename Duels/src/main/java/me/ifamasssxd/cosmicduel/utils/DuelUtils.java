/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI
 *  com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity
 *  com.google.common.collect.Lists
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.enchanting.EListener
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.Chest
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.ifamasssxd.cosmicduel.utils;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.enchanting.EListener;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.enums.DuelState;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

public class DuelUtils {
    static List<Material> disabledMaterials = Lists.newArrayList(Material.PAPER, Material.EXP_BOTTLE, Material.REDSTONE_TORCH_ON, Material.NAME_TAG, Material.CHEST, Material.CHEST, Material.TNT, Material.MONSTER_EGG, Material.MOB_SPAWNER, Material.DIAMOND, Material.EMERALD, Material.QUARTZ, Material.PAINTING, Material.BED, Material.MAGMA_CREAM, Material.BONE);
    static List<Material> whitelistedEnvoyItems = Lists.newArrayList(Material.INK_SACK, Material.EMPTY_MAP, Material.ENCHANTED_BOOK, Material.BOOK, Material.ARROW, Material.FLINT_AND_STEEL, Material.LAVA_BUCKET, Material.FIREWORK_CHARGE, Material.SUGAR);

    public static boolean isValidRankedMaterial(ItemStack item) {
        if (item != null) {
            Material material = item.getType();
            String name = item.getType().name();
            return name.endsWith("_CHESTPLATE") || name.endsWith("_HELMET") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS") || name.endsWith("_AXE") || name.endsWith("_SWORD") || material == Material.BOW || material == Material.ENDER_PEARL || material == Material.GOLDEN_APPLE || material == Material.ARROW || material == Material.POTION;
        }
        return false;
    }

    public static void unequipEffects(Player player) {
        for (Map.Entry data : EListener.getValidEnchantments(player, Lists.newArrayList(player.getInventory().getArmorContents())).entries()) {
            ((CustomEnchantment)data.getKey()).applyUnequipEffect(player, ((Integer)data.getValue()).intValue());
        }
    }

    public static void removeDisabledItems(Block b) {
        if (b.getType() == Material.CHEST) {
            Chest chest = (Chest)b.getState();
            for (ItemStack is : chest.getInventory()) {
                if (is == null || is.getType() == Material.AIR) continue;
                if (DuelUtils.isRestrictedItem(is)) {
                    chest.getInventory().removeItem(is);
                    ItemStack newItem = CosmicSpaceChestsAPI.getItemList(ItemRarity.LEGENDARY, 1).get(0);
                    int trys = 100;
                    while (DuelUtils.isRestrictedItem(newItem) && trys > 0) {
                        if (--trys == 0) {
                            newItem = null;
                            break;
                        }
                        newItem = CosmicSpaceChestsAPI.getItemList((ItemRarity)ItemRarity.LEGENDARY, (int)1).get(0);
                    }
                    if (newItem != null) {
                        KitUtils.addEnvoyWatermark(newItem);
                        chest.getInventory().addItem(newItem);
                    }
                    chest.update();
                    System.out.println("CosmicDuels: Removing disabled item from Envoy Duel Chest: " + (is.hasItemMeta() && is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() : is.getType().name()));
                    continue;
                }
                KitUtils.addEnvoyWatermark(is);
                chest.update();
            }
        }
    }

    public static boolean isRestrictedItem(ItemStack is) {
        return !whitelistedEnvoyItems.contains(is.getType()) && !is.getType().isEdible() && !DuelUtils.isArmor(is) && !DuelUtils.isWeapon(is);
    }

    public static void checkArmor(final Player p) {
        NewDuel d;
        if (DuelAPI.hasDuel(p) && DuelAPI.isDuelValid(d = DuelAPI.getDuel(p)) && d != null && d.getDuelState() != DuelState.ENDED && d.getDuelState() != DuelState.ENDING && !d.getSettings().hasSetting(Setting.ARMOR)) {
            new BukkitRunnable(){

                public void run() {
                    if (d != null && (d.getDuelState() == DuelState.ENDED || d.getDuelState() == DuelState.ENDING)) {
                        return;
                    }
                    ItemStack[] armor = p.getInventory().getArmorContents();
                    for (int i = 0; i < armor.length; ++i) {
                        ItemStack is = armor[i];
                        if (is == null || is.getType() == Material.AIR) continue;
                        armor[i] = new ItemStack(Material.AIR);
                        p.getInventory().setArmorContents(armor);
                        p.getInventory().addItem(is);
                        p.updateInventory();
                        p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "Your duel has Armor " + ChatColor.RED + ChatColor.UNDERLINE + "disabled.");
                        for (PotionEffect pe : p.getActivePotionEffects()) {
                            p.removePotionEffect(pe.getType());
                        }
                        p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                    }
                }
            }.runTaskLater(CosmicDuel.get(), 2L);
        }
    }

    public static boolean isWeapon(ItemStack is) {
        if (is == null) {
            return false;
        }
        String name = is.getType().name();
        return name.endsWith("_SWORD") || name.endsWith("_AXE") || name.endsWith("_SPADE") || name.endsWith("_PICKAXE") || name.endsWith("_HOE") || name.endsWith("BOW");
    }

    public static boolean isArmor(ItemStack is) {
        if (is == null) {
            return false;
        }
        String name = is.getType().name();
        return name.endsWith("_CHESTPLATE") || name.endsWith("_HELMET") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
    }

    public static boolean isValidKitItem(ItemStack is) {
        return is != null && (DuelUtils.isArmor(is) || DuelUtils.isWeapon(is) || is.getType() == Material.GOLDEN_APPLE || is.getType() == Material.POTION || is.getType().isEdible() || is.getType() == Material.ENDER_PEARL || is.getType() == Material.ARROW);
    }
}


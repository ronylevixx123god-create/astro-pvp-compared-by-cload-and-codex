/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicOutposts.CosmicOutpostsAPI
 *  com.compilex.CosmicOutposts.outpost.me.astropvp.cratekeys.enums.OutpostTier
 *  org.arkhamnetwork.Arkkit.Arkkit
 *  org.arkhamnetwork.enchantments.util.TieredEnchantments
 *  org.bukkit.Material
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.rit.sucy.enchanting;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.enchanting.EListener;
import com.rit.sucy.service.ENameParser;
import java.util.Hashtable;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EEquip
extends BukkitRunnable {
    static Hashtable<String, ItemStack[]> equipment = new Hashtable();
    Player player;

    public static void loadPlayer(Player player) {
        equipment.put(player.getName(), player.getEquipment().getArmorContents());
    }

    public static void clearPlayer(Player player) {
        equipment.remove(player.getName());
    }

    public static void unequipPlayer(Player player) {
        EEquip.unequipPlayer(player, false);
    }

    public static void unequipPlayer(Player player, boolean equipmentWearing) {
        ItemStack[] cached = equipment.remove(player.getName());
        ItemStack[] equipped = player.getInventory().getArmorContents();
        if (cached == null) {
            cached = equipped;
        }
        if (cached != null) {
            for (ItemStack item : cached) {
                if (item == null || item.getType() == Material.AIR) continue;
                EnchantmentAPI.getEnchantments(item).forEach((enchantment, level) -> EEquip.applyEnchantUnequip(enchantment, player, level, item));
            }
        }
        if (equipmentWearing && equipment != null) {
            for (ItemStack equip : equipped) {
                if (equip == null || equip.getType() == Material.AIR) continue;
                EnchantmentAPI.getEnchantments(equip).forEach((enchantment, level) -> EEquip.applyEnchantUnequip(enchantment, player, level, equip));
            }
        }
    }

    private static void applyEnchantUnequip(CustomEnchantment enchantment, Player player, int level, ItemStack item) {
        if (enchantment.name().equals("Repair Guard")) {
            enchantment.applyUnequipEffect(player, level, item);
        } else {
            enchantment.applyUnequipEffect(player, level);
        }
    }

    public static void clear() {
        equipment.clear();
    }

    public EEquip(Player player) {
        this.player = player;
    }

    public void run() {
        ItemStack[] equips = this.player.getEquipment().getArmorContents();
        ItemStack[] previous = equipment.get(this.player.getName());
        try {
            for (int i = 0; i < equips.length; ++i) {
                ItemStack previouslyEquipped;
                ItemStack equipped = equips[i];
                ItemStack itemStack = previouslyEquipped = previous != null ? previous[i] : null;
                if (equipped == null && previouslyEquipped != null) {
                    this.doUnequip((LivingEntity)this.player, previous[i]);
                    continue;
                }
                if (equipped != null && (previous == null || previouslyEquipped == null)) {
                    this.doEquip((LivingEntity)this.player, equipped);
                    continue;
                }
                if (previous == null || this.isEqual(equipped, previouslyEquipped)) continue;
                this.doUnequip((LivingEntity)this.player, previouslyEquipped);
                this.doEquip((LivingEntity)this.player, equipped);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        equipment.put(this.player.getName(), equips);
    }

    private boolean isEqual(ItemStack equipped, ItemStack previouslyEquipped) {
        block6: {
            block7: {
                if (equipped == null && previouslyEquipped != null || equipped != null && previouslyEquipped == null) {
                    return false;
                }
                if (equipped == null && previouslyEquipped == null) {
                    return true;
                }
                if (equipped.getType() != previouslyEquipped.getType()) {
                    return false;
                }
                if (equipped.hasItemMeta() != previouslyEquipped.hasItemMeta()) break block6;
                if (!equipped.hasItemMeta()) break block7;
                ItemMeta im = equipped.getItemMeta();
                ItemMeta equippedIm = previouslyEquipped.getItemMeta();
                if (im.hasDisplayName() != equippedIm.hasDisplayName() || im.hasDisplayName() && !im.getDisplayName().equals(equippedIm.getDisplayName()) || im.hasLore() != equippedIm.hasLore() || im.hasLore() && !im.getLore().equals(equippedIm.getLore())) break block6;
            }
            return true;
        }
        return false;
    }

    private void doEquip(LivingEntity le, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        le.removeMetadata("armorLock", (Plugin)Arkkit.get());
        if (meta == null) {
            return;
        }
        if (!meta.hasLore()) {
            return;
        }
        for (String lore : meta.getLore()) {
            String name = ENameParser.parseName(lore);
            int level = ENameParser.parseLevel(lore);
            if (name == null || level == 0 || !EnchantmentAPI.isRegistered(name)) continue;
            int tier = TieredEnchantments.getEnchantmentTier((String)name);
            if (le.getWorld().getName().equalsIgnoreCase("space") && tier > 6) continue;

            if (name.equals("Repair Guard")) {
                EnchantmentAPI.getEnchantment(name).applyEquipEffect(this.player, level, item);
                continue;
            }
            EnchantmentAPI.getEnchantment(name).applyEquipEffect(this.player, level);
        }
    }

    private void doUnequip(LivingEntity le, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        le.removeMetadata("armorLock", (Plugin)Arkkit.get());
        if (meta == null) {
            return;
        }
        if (!meta.hasLore()) {
            return;
        }
        for (String lore : meta.getLore()) {
            String name = ENameParser.parseName(lore);
            int level = ENameParser.parseLevel(lore);
            if (name == null || level == 0 || !EnchantmentAPI.isRegistered(name)) continue;
            if (name.equals("Repair Guard")) {
                EnchantmentAPI.getEnchantment(name).applyUnequipEffect(this.player, level, item);
                continue;
            }
            EnchantmentAPI.getEnchantment(name).applyUnequipEffect(this.player, level);
        }
    }
}


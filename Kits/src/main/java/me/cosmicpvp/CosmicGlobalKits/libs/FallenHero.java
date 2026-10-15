/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKit
 *  me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKits
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.PigZombie
 *  org.bukkit.entity.Skeleton
 *  org.bukkit.entity.Skeleton$SkeletonType
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.cosmicpvp.CosmicGlobalKits.libs;

import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FallenHero {
    public static ItemStack getFallenHeroItem(GlobalKits gkit) {
        return ItemUtils.buildItem(Material.BONE, (short)0, ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + (Object)ChatColor.UNDERLINE + "Fallen Hero:" + (Object)ChatColor.RESET + " " + gkit.getItemName().replace("Kit", ""), (Object)ChatColor.GRAY + "Summons a fallen hero equipped with", (Object)ChatColor.GRAY + "the " + gkit.getItemName() + (Object)ChatColor.GRAY + " equipment.", "", (Object)ChatColor.GRAY + "Defeat this hero for a " + (Object)ChatColor.UNDERLINE + "guranteed", (Object)ChatColor.GRAY + "drop of the " + (Object)ChatColor.WHITE + "/gkit item set" + (Object)ChatColor.GRAY + " and a ", (Object)ChatColor.WHITE + "" + (Object)ChatColor.UNDERLINE + "chance" + (Object)ChatColor.GRAY + " to get a " + (Object)ChatColor.WHITE + "/gkit Redemption Gem", (Object)ChatColor.GRAY + "that can be used to " + (Object)ChatColor.UNDERLINE + "unlock the /gkit", (Object)ChatColor.GRAY + "on your own account for regular use!", "", ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "USE: " + (Object)ChatColor.GRAY + "To summon this fallen hero", (Object)ChatColor.WHITE + "Right-Click" + (Object)ChatColor.GRAY + " this item inside the Warzone.", "", ChatColor.RED.toString() + ChatColor.BOLD.toString() + "WARNING: " + (Object)ChatColor.GRAY + "Whoever defeats the Fallen Hero", (Object)ChatColor.GRAY + "will receive its legendary treasures.", (Object)ChatColor.GRAY + "Do " + (Object)ChatColor.RED + "" + (Object)ChatColor.UNDERLINE + "not" + (Object)ChatColor.GRAY + " place in confined spaces,", (Object)ChatColor.GRAY + "as minecraft mobs tend to block glitch.");
    }


    public static GlobalKits getGlobalKitFromFallenHeroItem(ItemStack i) {
        if (!FallenHero.isFallenHeroItem(i)) {
            return null;
        }
        String itemName = i.getItemMeta().getDisplayName().split(":" + (Object)ChatColor.RESET + " ")[1];
        String gkitName = ChatColor.stripColor((String)itemName).replace("!!!Heroic!!! ", "");
        for (GlobalKits gkit : GlobalKits.values()) {
            String cleanedName = ChatColor.stripColor((String)gkit.getItemName()).replace("!!!Heroic!!! ", "");
            if (!cleanedName.startsWith(gkitName)) continue;
            return gkit;
        }
        return null;
    }



    public static boolean isFallenHeroItem(ItemStack i) {
        return i != null && i.getType() == Material.BONE && i.hasItemMeta() && i.getItemMeta().hasLore() && ((String)i.getItemMeta().getLore().get(0)).equals((Object)ChatColor.GRAY + "Summons a fallen hero equipped with");
    }

    public static LivingEntity spawnFallenHero(GlobalKits gkit, Location l) {
        LivingEntity le = (LivingEntity)l.getWorld().spawnEntity(l, EntityType.SKELETON);
        Skeleton s = (Skeleton)le;
        s.setSkeletonType(Skeleton.SkeletonType.WITHER);
        s.setMaxHealth(100.0);
        s.setHealth(100.0);
        s.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        s.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        s.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        s.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0));
        s.setFireTicks(32767);
        s.setCustomName(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + (Object)ChatColor.UNDERLINE + "Fallen Hero:" + (Object)ChatColor.RESET + " " + gkit.getItemName().replace("Kit", ""));
        s.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        s.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        s.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        s.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        if (gkit == GlobalKits.VIKING || gkit == GlobalKits.Butcher) {
            s.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_AXE));
        } else if (gkit == GlobalKits.Cannoner || gkit == GlobalKits.ARCANE || gkit == GlobalKits.GUARDIAN || gkit == GlobalKits.TRICKSTER) {
            s.getEquipment().setItemInHand(new ItemStack(Material.BOW));
        } else {
            s.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
        }
        s.setCustomNameVisible(true);
        le.setMetadata("do_not_clear", (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)true));
        le.setMetadata("fallenHero", (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)gkit.name()));
        return le;
    }



}


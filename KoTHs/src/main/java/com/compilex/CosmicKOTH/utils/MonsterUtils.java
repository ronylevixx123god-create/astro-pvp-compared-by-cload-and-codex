/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Ghast
 *  org.bukkit.entity.IronGolem
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
package com.compilex.CosmicKOTH.utils;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MonsterUtils {
    public static LivingEntity spawnEnvoyMonster(Location l) {
        EntityType et = null;
        double rand = Math.random();
        if (rand <= 0.1) {
            et = EntityType.GHAST;
        }
        if (rand <= 0.35) {
            et = EntityType.IRON_GOLEM;
        }
        if (rand <= 0.95) {
            et = EntityType.SKELETON;
        }
        LivingEntity le = null;
        if (et == EntityType.PIG_ZOMBIE) {
            PigZombie b = (PigZombie)l.getWorld().spawnEntity(l, EntityType.PIG_ZOMBIE);
            b.setMaxHealth(b.getMaxHealth() * 3.0);
            b.setHealth(b.getMaxHealth());
            b.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Demon");
            b.setCustomNameVisible(true);
            b.setBaby(true);
            b.setAngry(true);
            le = b;
        } else if (et == EntityType.IRON_GOLEM) {
            IronGolem b2 = (IronGolem)l.getWorld().spawnEntity(l, EntityType.IRON_GOLEM);
            b2.setMaxHealth(b2.getMaxHealth() * 3.0);
            b2.setHealth(b2.getMaxHealth());
            b2.setPlayerCreated(false);
            b2.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Droid");
            b2.setCustomNameVisible(true);
            le = b2;
        } else if (et == EntityType.SKELETON) {
            Skeleton b3 = (Skeleton)l.getWorld().spawnEntity(l, EntityType.SKELETON);
            b3.setSkeletonType(Skeleton.SkeletonType.WITHER);
            b3.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
            b3.setMaxHealth(b3.getMaxHealth() * 3.0);
            b3.setHealth(b3.getMaxHealth());
            b3.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Undead");
            b3.setCustomNameVisible(true);
            le = b3;
        } else if (et == EntityType.GHAST) {
            Ghast b4 = (Ghast)l.getWorld().spawnEntity(l, EntityType.GHAST);
            b4.setMaxHealth(b4.getMaxHealth() * 7.0);
            b4.setHealth(b4.getMaxHealth());
            b4.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Phantom");
            b4.setCustomNameVisible(true);
            le =  b4;
        }
        /*if (CosmicKOTHPlugin.isVanillaGalaxy()) {
            le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
            le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, new Random().nextInt(3)));
            le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
        } else {
            le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
            le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, new Random().nextInt(3)));
            le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, new Random().nextInt(2)));
        }*/
        ItemStack i = new ItemStack(Material.DIAMOND_SWORD);
        i.addEnchantment(Enchantment.KNOCKBACK, new Random().nextBoolean() ? 1 : 2);
        le.getEquipment().setItemInHand(i);
        le.getEquipment().setHelmet(new ItemStack(new Random().nextBoolean() ? Material.IRON_HELMET : Material.DIAMOND_HELMET));
        le.getEquipment().setChestplate(new ItemStack(new Random().nextBoolean() ? Material.IRON_CHESTPLATE : Material.DIAMOND_CHESTPLATE));
        le.getEquipment().setLeggings(new ItemStack(new Random().nextBoolean() ? Material.IRON_LEGGINGS : Material.DIAMOND_LEGGINGS));
        le.getEquipment().setBoots(new ItemStack(new Random().nextBoolean() ? Material.IRON_BOOTS : Material.DIAMOND_BOOTS));
        le.setCanPickupItems(false);
        le.setMetadata("envoyMonster", (MetadataValue)new FixedMetadataValue((Plugin)CosmicKOTHPlugin.get_(), (Object)true));
        return le;
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
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
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.compilex.CosmicDungeons.utils;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
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
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MonsterUtils {
    public static LivingEntity spawnOutpostPirate(Location location) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        LivingEntity ent = (LivingEntity)location.getWorld().spawnEntity(location, EntityType.SKELETON);
        if (((Random)random).nextInt(10) <= 8) {
            Skeleton skeleton = (Skeleton)ent;
            skeleton.setSkeletonType(Skeleton.SkeletonType.WITHER);
        }
        EntityEquipment equipment = ent.getEquipment();
        equipment.setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).enchant(Enchantment.DURABILITY, 100).build());
        equipment.setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).enchant(Enchantment.DURABILITY, 100).build());
        equipment.setHelmet(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).enchant(Enchantment.DURABILITY, 100).build());
        equipment.setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5).enchant(Enchantment.DURABILITY, 100).build());
        equipment.setItemInHand(((Random)random).nextBoolean() ? new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 5).enchant(Enchantment.ARROW_FIRE, 1).enchant(Enchantment.DURABILITY, 10).build() : new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 4).enchant(Enchantment.DURABILITY, 5).enchant(Enchantment.KNOCKBACK, ((Random)random).nextBoolean() ? 2 : 1).build());
        equipment.setItemInHandDropChance(0.0f);
        equipment.setHelmetDropChance(0.0f);
        equipment.setChestplateDropChance(0.0f);
        equipment.setLeggingsDropChance(0.0f);
        equipment.setBootsDropChance(0.0f);
        ent.setMaxHealth(ent.getMaxHealth() * 2.0);
        ent.setHealth(ent.getMaxHealth());
        ent.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Fallen Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Cadet");
        ent.setCustomNameVisible(true);
        ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        ent.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
        ent.setMetadata("destroyedSpacePirate", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)true));
        try {
            de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), ent.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return ent;
    }

    public static LivingEntity spawnSpacePirate(Location l) {
        Skeleton skeleton = null;
        Ghast ghast = null;
        EntityType et = null;
        double rand = Math.random();
        if (rand <= 0.7D)
            et = EntityType.SKELETON;
        if (et == null)
            et = EntityType.PIG_ZOMBIE;
        LivingEntity le = null;
        if (et == EntityType.PIG_ZOMBIE) {
            PigZombie b = (PigZombie)l.getWorld().spawnEntity(l, EntityType.PIG_ZOMBIE);
            b.setMaxHealth(b.getMaxHealth() * 2.0D);
            b.setHealth(b.getMaxHealth());
            b.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Pirate");
            b.setCustomNameVisible(true);
            b.setBaby(true);
            b.setAngry(true);
            PigZombie pigZombie1 = b;
        } else if (et == EntityType.IRON_GOLEM) {
            IronGolem b = (IronGolem)l.getWorld().spawnEntity(l, EntityType.IRON_GOLEM);
            b.setMaxHealth(b.getMaxHealth() * 2.0D);
            b.setHealth(b.getMaxHealth());
            b.setPlayerCreated(false);
            b.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Golem");
            b.setCustomNameVisible(true);
            IronGolem ironGolem1 = b;
        } else if (et == EntityType.SKELETON) {
            Skeleton b = (Skeleton)l.getWorld().spawnEntity(l, EntityType.SKELETON);
            b.setSkeletonType(Skeleton.SkeletonType.WITHER);
            b.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
            b.setMaxHealth(b.getMaxHealth() * 2.0D);
            b.setHealth(b.getMaxHealth());
            b.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Pirate");
            b.setCustomNameVisible(true);
            skeleton = b;
        } else if (et == EntityType.GHAST) {
            Ghast b = (Ghast)l.getWorld().spawnEntity(l, EntityType.GHAST);
            b.setMaxHealth(b.getMaxHealth() * 5.0D);
            b.setHealth(b.getMaxHealth());
            b.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Phantom");
            b.setCustomNameVisible(true);
            ghast = b;
        }
        assert skeleton != null;
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2147483647, 1));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, (new Random()).nextInt(3)));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, (new Random()).nextInt(2)));
        ItemStack i = new ItemStack(Material.DIAMOND_SWORD);
        i.addEnchantment(Enchantment.KNOCKBACK, (new Random()).nextBoolean() ? 1 : 2);
        skeleton.getEquipment().setItemInHand(i);
        skeleton.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        skeleton.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        skeleton.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        skeleton.setCanPickupItems(false);
        skeleton.setMetadata("spacePirate", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), Boolean.valueOf(true)));
        try {
            de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), skeleton.getLocation().add(0, 1, 0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);

        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return skeleton;
    }
}


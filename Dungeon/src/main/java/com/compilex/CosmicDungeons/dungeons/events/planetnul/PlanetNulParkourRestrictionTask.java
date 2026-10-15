/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldGuardUtils
 *  me.quantiom.itemskin.skins.boots.ItemSkinRainbowSlides
 *  me.quantiom.util.ItemSkinUtils
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.dungeons.libs.PlanetNulDungeon;
import com.compilex.CosmicDungeons.struct.DungeonDebuff;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlanetNulParkourRestrictionTask
extends BukkitRunnable {
    private PlanetNulDungeon dungeon;
    private List<String> regions;

    public PlanetNulParkourRestrictionTask(PlanetNulDungeon dungeon) {
        this.dungeon = dungeon;
        this.regions = new ArrayList<>(Arrays.asList("first_parkour", "second_parkour", "fireball_alley", "invisible_maze", "find_corrupt_room"));
    }

    public void run() {
        for (Player p : this.dungeon.getPlayers()) {
            boolean hasBoots;
            DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p);
            if (dp.getCurrentDungeon() == null || !dp.getCurrentDungeon().equals(this.dungeon)) continue;
            if (p.getAllowFlight() && !p.isOp()) {
                p.setAllowFlight(false);
            }
            if (p.isFlying() && !p.isOp()) {
                p.setFlying(false);
            }
            String region = WorldGuardUtils.getRegionName((Location)p.getLocation());
            boolean bl = hasBoots = !DungeonsAPI.hasDungeonDebuff(p, DungeonDebuff.NO_ITEM_SKINS) && p.getInventory().getBoots() != null && AstroItemSkinsAPI.hasItemSkin((ItemStack)p.getInventory().getBoots()) && AstroItemSkinsAPI.getItemSkinByItem((ItemStack)p.getInventory().getBoots()).contains(SkinType.SPRING_2020_BOOTS);
            if (p.hasMetadata("inDungeonParkour")) {
                if (hasBoots) {
                    for (PotionEffect pe : p.getActivePotionEffects()) {
                        if (!pe.getType().getName().equals("JUMP") || pe.getAmplifier() <= 0) continue;
                        p.removePotionEffect(pe.getType());
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0));
                } else {
                    for (PotionEffect pe : p.getActivePotionEffects()) {
                        if (!pe.getType().getName().equals("JUMP")) continue;
                        p.removePotionEffect(pe.getType());
                    }
                }
            }
            if (this.regions.contains(region)) {
                for (PotionEffect pe : p.getActivePotionEffects()) {
                    if (pe.getType().getName().equals("JUMP")) continue;
                    p.removePotionEffect(pe.getType());
                }
                p.setMetadata("inDungeonParkour", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (Object)region));
                continue;
            }
            p.removeMetadata("inDungeonParkour", (Plugin)CosmicDungeonsPlugin.get());
        }
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.google.common.collect.Lists
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Zombie
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.struct.DungeonChallenge;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InvisibleMazeRegionEvent
extends RegionEvent {
    private List<Zombie> mazeZombies;

    public InvisibleMazeRegionEvent(Dungeon d, String regionName) {
        super(d, regionName);
    }

    private Set<Location> getXZLocationsIn(Location from, Location to, List<Location> dontAdd) {
        HashSet<Location> locations = new HashSet<Location>();
        int xMin = Math.min(from.getBlockX(), to.getBlockX());
        int xMax = Math.max(from.getBlockX(), to.getBlockX());
        int zMin = Math.min(from.getBlockZ(), to.getBlockZ());
        int zMax = Math.max(from.getBlockZ(), to.getBlockZ());
        for (int x = xMin; x < xMax; ++x) {
            for (int z = zMin; z < zMax; ++z) {
                Location loc = new Location(this.dungeon.world, (double)x, 0.0, (double)z);
                if (dontAdd.contains((Object)loc)) continue;
                locations.add(loc);
            }
        }
        return locations;
    }

    @Override
    public void run() {
        if(this.dungeon.getPlayers().isEmpty())
            return;
        String chosenPath;
        if (this.dungeon.getTimeTask().getExpireTime() - System.currentTimeMillis() > (long)(this.dungeon.isHalfTime() ? 45000 : 75000)) {
            this.dungeon.getDungeonChallengesCompleted().remove((Object)DungeonChallenge.APESTOS);
        }
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + (long)(this.dungeon.isHalfTime() ? 600000 : 900000), "Solve Invisible Maze");
        this.dungeon.broadcastMessage("Something isn't right about this room - there is powerful arcane magic being employed here - watch yourselves and keep your wits about you, Cosmonauts.");
        Location pointOne = new Location(this.dungeon.world, 862.0, 205.0, 549.0);
        Location pointTwo = new Location(this.dungeon.world, 777.0, 205.0, 459.0);
        ArrayList dontAdd = Lists.newArrayList();
        for (int x = 811; x <= 825; ++x) {
            for (int z = 453; z <= 464; ++z) {
                dontAdd.add(new Location(this.dungeon.world, (double)x, 0.0, (double)z));
            }
        }
        Set<Location> set = this.getXZLocationsIn(pointOne, pointTwo, dontAdd);
        set.stream().map(l -> new Location(l.getWorld(), l.getX(), 199.0, l.getZ()).getBlock()).filter(b -> b.getType() == Material.AIR).collect(Collectors.toList());
        List<Block> allLocations = set.stream().map(l -> new Location(l.getWorld(), l.getX(), 199.0, l.getZ()).getBlock()).filter(b -> b.getType() == Material.AIR).collect(Collectors.toList());
        this.mazeZombies = Lists.newArrayList();
        allLocations.stream().limit(40 + new Random().nextInt(5)).forEach(b -> this.spawnMazeZombie(b.getLocation()));
        this.setSouth(false);
        this.setSouthWest(false);
        this.setEast(false);
        ArrayList<String> possiblePaths = new ArrayList<String>(Arrays.asList("SOUTH", "SOUTH-WEST", "EAST"));
        switch (chosenPath = (String)possiblePaths.get(new Random().nextInt(possiblePaths.size()))) {
            case "SOUTH": {
                this.setSouth(true);
                break;
            }
            case "SOUTH-WEST": {
                this.setSouthWest(true);
                break;
            }
            case "EAST": {
                this.setEast(true);
            }
        }
    }

    private void setEast(boolean open) {
        this.setBlocks(new ArrayList<Location>(Arrays.asList(new Location(this.dungeon.getWorld(), 830.0, 199.0, 518.0), new Location(this.dungeon.getWorld(), 829.0, 199.0, 519.0), new Location(this.dungeon.getWorld(), 828.0, 199.0, 520.0))), open);
    }

    private void setSouthWest(boolean open) {
        this.setBlocks(new ArrayList<Location>(Arrays.asList(new Location(this.dungeon.getWorld(), 838.0, 199.0, 553.0), new Location(this.dungeon.getWorld(), 838.0, 199.0, 552.0), new Location(this.dungeon.getWorld(), 839.0, 199.0, 551.0), new Location(this.dungeon.getWorld(), 811.0, 199.0, 511.0), new Location(this.dungeon.getWorld(), 810.0, 199.0, 511.0), new Location(this.dungeon.getWorld(), 810.0, 199.0, 510.0), new Location(this.dungeon.getWorld(), 809.0, 199.0, 510.0), new Location(this.dungeon.getWorld(), 770.0, 199.0, 537.0), new Location(this.dungeon.getWorld(), 770.0, 199.0, 538.0))), open);
    }

    private void setSouth(boolean open) {
        ArrayList<Location> inverseBlocks = new ArrayList<Location>(Arrays.asList(new Location(this.dungeon.getWorld(), 839.0, 199.0, 551.0), new Location(this.dungeon.getWorld(), 838.0, 199.0, 552.0), new Location(this.dungeon.getWorld(), 838.0, 199.0, 553.0)));
        inverseBlocks.forEach(loc -> {
            loc.getBlock().setType(open ? Material.BARRIER : Material.AIR);
            loc.add(0.0, 1.0, 0.0).getBlock().setType(open ? Material.BARRIER : Material.AIR);
            loc.add(0.0, 2.0, 0.0).getBlock().setType(open ? Material.BARRIER : Material.AIR);
            loc.add(0.0, 3.0, 0.0).getBlock().setType(open ? Material.BARRIER : Material.AIR);
            loc.add(0.0, 4.0, 0.0).getBlock().setType(open ? Material.BARRIER : Material.AIR);
        });
        this.setBlocks(new ArrayList<Location>(Arrays.asList(new Location(this.dungeon.getWorld(), 839.0, 199.0, 534.0), new Location(this.dungeon.getWorld(), 838.0, 199.0, 534.0), new Location(this.dungeon.getWorld(), 820.0, 199.0, 513.0), new Location(this.dungeon.getWorld(), 819.0, 199.0, 513.0), new Location(this.dungeon.getWorld(), 819.0, 199.0, 514.0))), open);
    }

    private void setBlocks(List<Location> locations, boolean open) {
        for (Location loc : locations) {
            loc.getBlock().setType(open ? Material.AIR : Material.BARRIER);
            loc.add(0.0, 1.0, 0.0).getBlock().setType(open ? Material.AIR : Material.BARRIER);
            loc.add(0.0, 2.0, 0.0).getBlock().setType(open ? Material.AIR : Material.BARRIER);
            loc.add(0.0, 3.0, 0.0).getBlock().setType(open ? Material.AIR : Material.BARRIER);
            loc.add(0.0, 4.0, 0.0).getBlock().setType(open ? Material.AIR : Material.BARRIER);
        }
    }

    private void spawnMazeZombie(Location location) {
        Zombie zombie = (Zombie)location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        zombie.setCustomNameVisible(true);
        zombie.setCustomName(CC.translate((String)"&3&lMaze Monster"));
        zombie.setVillager(false);
        zombie.setBaby(false);
        zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        zombie.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
        zombie.getEquipment().setItemInHandDropChance(0.0f);
        zombie.getEquipment().setHelmetDropChance(0.0f);
        zombie.getEquipment().setChestplateDropChance(0.0f);
        zombie.getEquipment().setLeggingsDropChance(0.0f);
        zombie.getEquipment().setBootsDropChance(0.0f);
        zombie.setMetadata("inInvisibleMaze", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (Object)true));
        zombie.setMetadata("invisibleMazeZombie", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (Object)true));
        zombie.setHealth(20.0);
        this.mazeZombies.add(zombie);
    }

    public List<Zombie> getZombies() {
        return this.mazeZombies;
    }
}


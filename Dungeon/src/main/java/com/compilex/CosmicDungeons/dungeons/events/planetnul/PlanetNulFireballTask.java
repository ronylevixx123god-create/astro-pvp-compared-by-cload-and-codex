/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldGuardUtils
 *  me.ifamasssxd.cosmicdisguises.CosmicDisguises
 *  me.libraryaddict.disguise.DisguiseAPI
 *  me.libraryaddict.disguise.disguisetypes.Disguise
 *  me.libraryaddict.disguise.disguisetypes.DisguiseType
 *  me.libraryaddict.disguise.disguisetypes.MobDisguise
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.EntityCustomFireball;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.dungeons.libs.PlanetNulDungeon;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.ifamasssxd.cosmicdisguises.CosmicDisguises;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlanetNulFireballTask
extends BukkitRunnable {
    private PlanetNulDungeon dungeon;

    public PlanetNulFireballTask(PlanetNulDungeon dungeon) {
        this.dungeon = dungeon;
    }

    private void updateGlass(List<Location> locations, boolean remove) {
        locations.forEach(loc -> {
            if (remove) {
                loc.getBlock().setType(Material.AIR);
            } else {
                loc.getBlock().setType(Material.STAINED_GLASS);
                loc.getBlock().setData((byte)1);
            }
        });
    }

    public void run() {
        int i;
        ArrayList<Location> dispenserLocations = new ArrayList<>(this.dungeon.getDungeonEvents().getFireballRegionEvent().dispenserLocations);
        Collections.shuffle(dispenserLocations);
        for (int i2 = 0; i2 < dispenserLocations.size() / 8; ++i2) {
            Location loc = dispenserLocations.get(i2);


            CraftWorld handle = (CraftWorld)loc.getWorld();
            EntityCustomFireball fireball = new EntityCustomFireball(handle.getHandle());
            EntityFireball nms = ((CraftFireball)fireball.getBukkitEntity()).getHandle();
            Vector dir = new Vector(0.0, 0.0, -0.2);
            double x = dir.getX();
            double y = dir.getY();
            double z = dir.getZ();
            double length = Math.sqrt(x * x + y * y + z * z);
            nms.dirX = x / length * 0.1D;
            nms.dirY = y / length * 0.1D;
            nms.dirZ = z / length * 0.1D;
            handle.getHandle().addEntity(fireball, CreatureSpawnEvent.SpawnReason.CUSTOM);


            fireball.setPosition(loc.getX(), loc.getY(), loc.getZ() - 1.0);
            fireball.getBukkitEntity().setMetadata("fireballAlley", new FixedMetadataValue(CosmicDungeonsPlugin.get(), true));
            fireball.getBukkitEntity().setMetadata("do_not_clear", new FixedMetadataValue(CosmicDungeonsPlugin.get(), true));
        }
        ArrayList<Location> findCorruptRoomGlass = new ArrayList<>();
        boolean removeCorruptRoomGlass = false;
        ArrayList<Location> secondParkourGlass = new ArrayList<>();
        boolean removeSecondParkourGlass = false;
        for (i = 0; i < 5; ++i) {
            findCorruptRoomGlass.add(new Location(this.dungeon.world, 818.0, (double)(143 + i), 633.0));
            findCorruptRoomGlass.add(new Location(this.dungeon.world, 817.0, (double)(143 + i), 633.0));
            findCorruptRoomGlass.add(new Location(this.dungeon.world, 816.0, (double)(143 + i), 633.0));
        }
        for (i = 0; i < 3; ++i) {
            secondParkourGlass.add(new Location(this.dungeon.world, 760.0, (double)(136 + i), 504.0));
            secondParkourGlass.add(new Location(this.dungeon.world, 761.0, (double)(136 + i), 504.0));
            secondParkourGlass.add(new Location(this.dungeon.world, 762.0, (double)(136 + i), 504.0));
        }
        for (Player p : this.dungeon.getPlayers()) {
            DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p);
            if (dp.getCurrentDungeon() == null || !dp.getCurrentDungeon().equals(this.dungeon)) continue;
            String region = WorldGuardUtils.getRegionName((Location)p.getLocation());
            if (region.equalsIgnoreCase("invisible_maze")) {
                if (DisguiseAPI.getDisguise((Entity)p) == null || DisguiseAPI.getDisguise((Entity)p).getType() != DisguiseType.ZOMBIE) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicDisguises.get(), () -> {
                        MobDisguise disguise = new MobDisguise(DisguiseType.ZOMBIE);
                        disguise.getWatcher().setCustomName(CC.translate((String)"&3&lMaze Monster"));
                        disguise.getWatcher().setCustomNameVisible(true);
                        disguise.getWatcher().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                        disguise.getWatcher().setArmor(new ItemStack[]{new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS)});
                        DisguiseAPI.disguiseToAll((Entity)p, (Disguise)disguise);
                    });
                }
                if (!p.isOp()) {
                    p.setGameMode(GameMode.ADVENTURE);
                }
                p.setMetadata("inInvisibleMaze", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (Object)region));
            } else if (p.hasMetadata("inInvisibleMaze")) {
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicDisguises.get(), () -> DisguiseAPI.undisguiseToAll((Entity)p));
                if (!p.isOp()) {
                    p.setGameMode(GameMode.SURVIVAL);
                }
                p.removeMetadata("inInvisibleMaze", (Plugin)CosmicDungeonsPlugin.get());
            }
            Location location = new Location(this.dungeon.world, 817.0, 143.0, 633.0);
            if (location.distance(p.getLocation()) <= 4.0) {
                removeCorruptRoomGlass = true;
            }
            Location location2 = new Location(this.dungeon.world, 761.0, 136.0, 504.0);
            if (!(location2.distance(p.getLocation()) <= 4.0)) continue;
            removeSecondParkourGlass = true;
        }
        this.updateGlass(findCorruptRoomGlass, removeCorruptRoomGlass);
        this.updateGlass(secondParkourGlass, removeSecondParkourGlass);
    }
}


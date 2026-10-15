/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils
 *  com.gmail.filoghost.holograms.api.HolographicDisplaysAPI
 *  me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki
 *  me.ifamasssxd.cosmicbosses.net.astropvp.armorsets.struct.Boss
 *  me.ifamasssxd.cosmicbosses.net.astropvp.armorsets.utils.BossUtils
 *  org.arkhamnetwork.enchantments.Enchantments
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.FallingBlock
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

class PlayerEnterBridge
extends RegionEvent {
    public PlayerEnterBridge(Dungeon d, String regionName) {
        super(d, regionName);
    }

    @Override
    public void run() {
        Boss yijki;
        this.dungeon.broadcastMessage((Object)ChatColor.WHITE + "You have discovered the ship's bridge, perhaps now you can finally find some answers...");
        this.dungeon.broadcastMessage((Object)ChatColor.RED + "No! Yijki has destroyed the ship's main computer - defeat it!");
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 720000L / (long)(this.dungeon.isHalfTime() ? 2 : 1), "Defeat Yijki Phase #2");
        this.dungeon.getWorld().getBlockAt(-75, 79, 172).setTypeIdAndData(95, (byte)20, true);
        this.dungeon.getWorld().getBlockAt(-75, 80, 172).setTypeIdAndData(95, (byte)20, true);
        Location yijkiSpawnLocation = new Location(this.dungeon.getWorld(), -137.0, 92.0, 172.0);
        Entity spawned = BossUtils.spawnDungeonBoss((Location)yijkiSpawnLocation, (int)2);
        if (spawned != null && (yijki = (Boss)Boss.bosses.get(spawned.getUniqueId())) instanceof DungeonYijki) {
            ((DungeonYijki)yijki).getSpawnLocations().add(new Location(this.dungeon.getWorld(), -136.0, 80.0, 210.0));
            ((DungeonYijki)yijki).getSpawnLocations().add(new Location(this.dungeon.getWorld(), -138.0, 80.0, 189.0));
        }
        ((AbandonedShipEvents)this.dungeon.getDungeonEvents()).setSecondBossUUID(spawned.getUniqueId());
        this.dungeon.setBossSpawnTime(System.currentTimeMillis());
        this.dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get_(), (Location)new Location(this.dungeon.getWorld(), -129.0, 82.0, 165.0), (String[])new String[]{(Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "LOCKED", (Object)ChatColor.GRAY + "Defeat boss to proceed."}));
        for (int x = -128; x <= -113; ++x) {
            for (int z = 189; z <= 202; ++z) {
                for (int y = 80; y <= 97; ++y) {
                    Block b = this.dungeon.getWorld().getBlockAt(x, y, z);
                    Material m = b.getType();
                    byte data = b.getData();
                    b.setType(Material.AIR);
                    FallingBlock fb = this.dungeon.getWorld().spawnFallingBlock(b.getLocation(), m, data);
                    fb.setDropItem(false);
                    PlayerEnterBridge.pushAwayEntity(yijkiSpawnLocation, (Entity)fb, 2.0);
                }
            }
        }
    }

    public static void pushAwayEntity(Location center, Entity entity, double speed) {
        entity.setMetadata("pushAwayEntityEvent", (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)MinecraftServerUtils.getCurrentServerTick()));
        Vector unitVector = entity.getLocation().toVector().subtract(center.toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }
}


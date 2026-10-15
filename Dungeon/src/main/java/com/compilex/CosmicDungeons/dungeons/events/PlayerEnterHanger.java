/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki
 *  me.ifamasssxd.cosmicbosses.net.astropvp.armorsets.struct.Boss
 *  me.ifamasssxd.cosmicbosses.net.astropvp.armorsets.utils.BossUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

class PlayerEnterHanger
extends RegionEvent {
    public PlayerEnterHanger(Dungeon d, String regionName) {
        super(d, regionName);
    }

    @Override
    public void run() {
        Boss yijki;
        int z = 120;
        for (int x = 0; x >= -4; --x) {
            for (int y = 79; y <= 82; ++y) {
                Block b = this.dungeon.getWorld().getBlockAt(x, y, 120);
                b.setTypeIdAndData(95, (byte)2, true);
            }
        }
        this.dungeon.broadcastMessage((Object)ChatColor.WHITE + "The airlock has been blocked by a forcefield!");
        Location yijkiSpawnLocation = new Location(this.dungeon.getWorld(), -3.0, 84.0, 193.0);
        Entity spawned = BossUtils.spawnDungeonBoss((Location)yijkiSpawnLocation, (int)1);
        if (spawned != null && (yijki = (Boss)Boss.bosses.get(spawned.getUniqueId())) instanceof DungeonYijki) {
            ((DungeonYijki)yijki).getSpawnLocations().add(new Location(this.dungeon.getWorld(), -29.0, 80.0, 205.0));
            ((DungeonYijki)yijki).getSpawnLocations().add(new Location(this.dungeon.getWorld(), -29.0, 81.0, 167.0));
            ((DungeonYijki)yijki).getSpawnLocations().add(new Location(this.dungeon.getWorld(), 24.0, 80.0, 210.0));
        }
        ((AbandonedShipEvents)this.dungeon.getDungeonEvents()).setFirstBossUUID(spawned.getUniqueId());
        this.dungeon.setBossSpawned(true);
        this.dungeon.setBossSpawnTime(System.currentTimeMillis());
        this.dungeon.broadcastMessage((Object)ChatColor.RED + "A dark energy engulfs the ship hanger...");
        this.dungeon.broadcastMessage((Object)ChatColor.RED + "Yijki?! Here?! Impossible! Defeat it, quickly!");
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 600000L / (long)(this.dungeon.isHalfTime() ? 2 : 1), "Defeat Yijki Phase #1");
    }
}


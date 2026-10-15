/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.ifamasssxd.cosmicbosses.dungeonboss.planetnul.CorruptCosmonaut
 *  me.ifamasssxd.cosmicbosses.dungeonboss.planetnul.CorruptCosmonaut$BossPhase
 *  me.ifamasssxd.cosmicbosses.net.astropvp.armorsets.struct.Boss
 *  me.ifamasssxd.cosmicbosses.net.astropvp.armorsets.struct.enums.BossType
 *  org.bukkit.Location
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.PlanetNulDungeon;
import java.util.Random;
import me.ifamasssxd.cosmicbosses.dungeonboss.planetnul.CorruptCosmonaut;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import org.bukkit.Location;

public class BossRoomRegionEvent
extends RegionEvent {
    PlanetNulDungeon dungeon;

    public BossRoomRegionEvent(Dungeon d, String regionName) {
        super(d, regionName);
        this.dungeon = (PlanetNulDungeon)d;
    }

    @Override
    public void run() {
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + (long)(this.dungeon.isHalfTime() ? 300000 : 600000), "Defeat the Corrupt Cosmonaut");
        this.dungeon.broadcastMessage("Cosmonauts - you're closing in, this room is heavily patrolled by Cosmo-Killer robots. Don't let them spot you!");
        if (Boss.bosses.values().stream().noneMatch(b -> b.getBossType() == BossType.getBossType("CORRUPT_COSMONAUT"))) {
            CorruptCosmonaut boss = (CorruptCosmonaut)BossType.getBossType("CORRUPT_COSMONAUT").createInstance(new Location(this.dungeon.getWorld(), 782.0, 117.0, 729.0));
            boss.onBossSpawn();
            boss.setBossPhase(CorruptCosmonaut.BossPhase.values()[new Random().nextInt(CorruptCosmonaut.BossPhase.values().length)]);
            this.dungeon.boss = (CorruptCosmonaut)Boss.bosses.get(boss.getEntity().getUniqueId());
            this.dungeon.setBossSpawnTime(System.currentTimeMillis());
        }
    }
}


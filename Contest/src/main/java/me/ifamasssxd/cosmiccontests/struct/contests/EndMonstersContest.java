/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.metadata.MetadataValue
 */
package me.ifamasssxd.cosmiccontests.struct.contests;

import me.ifamasssxd.cosmiccontests.contests.EntityKillContest;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.MetadataValue;

public class EndMonstersContest
extends EntityKillContest {
    public EndMonstersContest(String name, EntityType et, Material display) {
        super(name, et, display);
    }

    @Override
    public void onEntityKill(ContestUser user, EntityDeathEvent event) {
        if (event.getEntity().hasMetadata("endMonsterType") && event.getEntity().getMetadata("endMonsterType").size() > 0 && ((MetadataValue)event.getEntity().getMetadata("endMonsterType").get(0)).asString().equals("CUSTOM")) {
            user.incrementValue(Contests.END_MONSTERS_KILLED, "mobsKilled", 1.0);
        }
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.END_MONSTERS_KILLED, "mobsKilled");
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.entity.EntityDeathEvent
 */
package me.ifamasssxd.cosmiccontests.struct.contests;

import com.google.common.collect.Lists;
import java.util.List;
import me.ifamasssxd.cosmiccontests.contests.EntityKillContest;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;

public class AgressiveMobKillContest
extends EntityKillContest {
    private List<EntityType> types = Lists.newArrayList(EntityType.CAVE_SPIDER, EntityType.SPIDER, EntityType.ENDERMAN, EntityType.BLAZE, EntityType.CREEPER, EntityType.GHAST, EntityType.MAGMA_CUBE, EntityType.SKELETON, EntityType.ZOMBIE);

    public AgressiveMobKillContest(String name, EntityType et, Material display) {
        super(name, et, display);
    }

    @Override
    public void onEntityKill(ContestUser user, EntityDeathEvent event) {
        EntityType type = event.getEntityType();
        if (this.types.contains((Object)type)) {
            user.incrementValue(Contests.AGGRESSIVE_MOBS_KILLED, "mobsKilled", 1.0);
        }
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.AGGRESSIVE_MOBS_KILLED, "mobsKilled");
    }
}


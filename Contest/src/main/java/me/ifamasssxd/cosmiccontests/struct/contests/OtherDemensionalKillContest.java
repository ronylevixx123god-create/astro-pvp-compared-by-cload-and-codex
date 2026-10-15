/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity
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
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;

public class OtherDemensionalKillContest
extends EntityKillContest {
    private List<EntityType> types = Lists.newArrayList(EntityType.ENDERMAN, EntityType.BLAZE, EntityType.GHAST, EntityType.MAGMA_CUBE, EntityType.PIG_ZOMBIE);

    public OtherDemensionalKillContest(String name, EntityType et, Material display) {
        super(name, et, display);
    }

    @Override
    public void onEntityKill(ContestUser user, EntityDeathEvent event) {
        EntityType type = event.getEntityType();
        if (this.types.contains(type) || ((CraftEntity)event.getEntity()).getHandle().getName().toLowerCase().contains("enderman")) {
            user.incrementValue(Contests.OTHER_DEMENTIONAL_MOBS_KILLED, "mobsKilled", 1.0);
        }
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.OTHER_DEMENTIONAL_MOBS_KILLED, "mobsKilled");
    }
}


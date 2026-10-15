/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.entity.EntityDeathEvent
 */
package me.ifamasssxd.cosmiccontests.struct.contests;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import me.ifamasssxd.cosmiccontests.contests.EntityKillContest;
import me.ifamasssxd.cosmiccontests.contests.Rerollable;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobSpecificKillContest
extends EntityKillContest
implements Rerollable {
    private EntityType newEntityType;
    private List<EntityType> mobs = Lists.newArrayList(EntityType.CAVE_SPIDER, EntityType.SKELETON, EntityType.IRON_GOLEM, EntityType.MAGMA_CUBE, EntityType.SPIDER, EntityType.ENDERMAN, EntityType.CREEPER);

    public MobSpecificKillContest(String name, EntityType et, Material display) {
        super(name, et, display);
        this.reroll();
    }

    @Override
    public void onEntityKill(ContestUser user, EntityDeathEvent event) {
        if (event.getEntityType().equals((Object)this.newEntityType)) {
            user.incrementValue(Contests.MOB_SPECIFIC, "mobsKilled", 1.0);
        }
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.MOB_SPECIFIC, "mobsKilled");
    }

    @Override
    public String getName() {
        return StringUtils.capitaliseAllWords((String)this.newEntityType.name().replace("_", " ").toLowerCase()) + " Killed";
    }

    @Override
    public void reroll() {
        this.newEntityType = this.mobs.get(ThreadLocalRandom.current().nextInt(this.mobs.size()));
    }

    @Override
    public void loadData(String data) {
        this.newEntityType = EntityType.valueOf((String)data);
    }

    @Override
    public String getExtraData() {
        return "&" + this.newEntityType.name();
    }

    public EntityType getNewEntityType() {
        return this.newEntityType;
    }

    public void setNewEntityType(EntityType newEntityType) {
        this.newEntityType = newEntityType;
    }
}


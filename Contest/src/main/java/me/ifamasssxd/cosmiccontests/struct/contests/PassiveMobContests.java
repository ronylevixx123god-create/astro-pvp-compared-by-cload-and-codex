package me.ifamasssxd.cosmiccontests.struct.contests;

import com.google.common.collect.Lists;
import java.util.List;
import me.ifamasssxd.cosmiccontests.contests.EntityKillContest;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;

public class PassiveMobContests
extends EntityKillContest {
    private List<EntityType> types = Lists.newArrayList(EntityType.SHEEP, EntityType.PIG, EntityType.MUSHROOM_COW, EntityType.COW, EntityType.CHICKEN, EntityType.WOLF, EntityType.HORSE, EntityType.IRON_GOLEM);

    public PassiveMobContests(String name, EntityType et, Material display) {
        super(name, et, display);
    }

    @Override
    public void onEntityKill(ContestUser user, EntityDeathEvent event) {
        EntityType type = event.getEntityType();
        if (this.types.contains((Object)type)) {
            user.incrementValue(Contests.PASSIVE_MOBS_KILLED, "mobsKilled", 1.0);
        }
    }

    @Override
    public String getFormattedData(ContestUser user) {
        return "%s" + user.getInt(Contests.PASSIVE_MOBS_KILLED, "mobsKilled");
    }
}


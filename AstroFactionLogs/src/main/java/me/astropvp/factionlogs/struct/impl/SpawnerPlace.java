package me.astropvp.factionlogs.struct.impl;

import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.EntityUtils;
import me.astropvp.factionlogs.utils.LocationSerializer;
import me.astropvp.factionlogs.utils.LocationUtils;
import me.astropvp.factionlogs.utils.TimeDateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import java.util.UUID;

@Getter
public class SpawnerPlace extends FactionLog {
    private final Integer amount;
    private final Location location;
    private final String entityType;
    private final String uuid;


    public SpawnerPlace(int amount, Location location, EntityType entityType, UUID uuid) {
        super(LogType.SPAWNER_PLACE);
        this.amount = amount;
        this.location = location;
        this.uuid = uuid.toString();
        this.entityType = entityType.toString();
    }

    public SpawnerPlace(int amount, Location location, EntityType entityType, UUID uuid, long time) {
        super(LogType.SPAWNER_PLACE);
        this.amount = amount;
        this.location = location;
        this.uuid = uuid.toString();
        this.entityType = entityType.toString();
        super.setTime(time);
    }

    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(UUID.fromString(getUuid())).getName() + " placed x" + getAmount() + " " + EntityUtils.getEntityName(EntityType.valueOf(getEntityType())) + " at " + LocationUtils.locToString(getLocation()) + ".";

    }
}

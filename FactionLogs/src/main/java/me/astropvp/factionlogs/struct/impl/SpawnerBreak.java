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
public class SpawnerBreak extends FactionLog {
    private final int amount;
    private final Location location;
    private final EntityType entityType;
    private final UUID uuid;

    public SpawnerBreak(int amount, Location location, EntityType entityType, UUID uuid) {
        super(LogType.SPAWNER_BREAK);
        this.amount = amount;
        this.location = location;
        this.entityType = entityType;
        this.uuid = uuid;

    }

    public SpawnerBreak(int amount, Location location, EntityType entityType, UUID uuid, long time) {
        super(LogType.SPAWNER_BREAK);
        this.amount = amount;
        this.location = location;
        this.entityType = entityType;
        this.uuid = uuid;
        super.setTime(time);

    }

    @Override
    public String serialize() {
        return (this.getTime() + ";-;" + amount + ";-;" + LocationSerializer.serialize(location) + ";-;" + entityType.name() + ";-;" + uuid);
    }

    public static FactionLog deserialize(String string) {
        String[] splitString = string.split(";-;");
        int amount = Integer.parseInt(splitString[1]);
        Location location = LocationSerializer.deserialize(splitString[2]);
        EntityType entityType = EntityType.valueOf(splitString[3]);
        UUID uuid = UUID.fromString(splitString[4]);
        return new SpawnerBreak(amount, location, entityType, uuid, Long.parseLong(splitString[0]));
    }

    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(getUuid()).getName() + " broke x" + getAmount() + " " + EntityUtils.getEntityName(getEntityType()) + " at " + LocationUtils.locToString(getLocation()) + ".";


    }
}

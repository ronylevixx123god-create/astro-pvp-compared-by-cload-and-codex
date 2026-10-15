package me.astropvp.factionlogs.struct.impl;

import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.LocationSerializer;
import me.astropvp.factionlogs.utils.LocationUtils;
import me.astropvp.factionlogs.utils.TimeDateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class ChunkBust extends FactionLog {
    private final Location location;
    private final String uuid;

    public ChunkBust(Location location, UUID uuid) {
        super(LogType.CHUNKBUST);
        this.location = location;
        this.uuid = uuid.toString();

    }

    public ChunkBust(Location location, UUID uuid, long time) {
        super(LogType.CHUNKBUST);
        this.location = location;
        this.uuid = uuid.toString();
        super.setTime(time);

    }

    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(UUID.fromString(getUuid())).getName() + " chunkbusted at " + LocationUtils.locToString(getLocation()) + "&7.";

    }
}

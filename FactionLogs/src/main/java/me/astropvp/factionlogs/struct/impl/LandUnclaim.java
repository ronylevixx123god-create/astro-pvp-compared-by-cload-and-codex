package me.astropvp.factionlogs.struct.impl;

import com.massivecraft.factions.FLocation;
import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.TimeDateUtils;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class LandUnclaim extends FactionLog {
    private final UUID uuid;
    private final FLocation fLocation;

    public LandUnclaim(UUID uuid, FLocation fLocation) {
        super(LogType.UNCLAIM);
        this.uuid = uuid;
        this.fLocation = fLocation;
    }

    public LandUnclaim(UUID uuid, FLocation fLocation, long time) {
        super(LogType.UNCLAIM);
        this.uuid = uuid;
        this.fLocation = fLocation;
        super.setTime(time);
    }

    @Override
    public String serialize() {
        return (this.getTime() + ";-;" + uuid + ";-;" + fLocation.toString());
    }

    public static FactionLog deserialize(String string) {
        String[] splitString = string.split(";-;");
        UUID uuid = UUID.fromString(splitString[1]);
        return new LandUnclaim(uuid, FLocation.fromString(splitString[2]), Long.parseLong(splitString[0]));
    }

    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(uuid).getName() + " unclaimed land at " + fLocation.toString();
    }
}

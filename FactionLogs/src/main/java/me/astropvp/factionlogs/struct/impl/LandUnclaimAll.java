package me.astropvp.factionlogs.struct.impl;

import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.TimeDateUtils;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class LandUnclaimAll extends FactionLog {
    private final UUID uuid;

    public LandUnclaimAll(UUID uuid) {
        super(LogType.UNCLAIMALL);
        this.uuid = uuid;

    }

    public LandUnclaimAll(UUID uuid, long time) {
        super(LogType.UNCLAIMALL);
        this.uuid = uuid;
        super.setTime(time);
    }

    @Override
    public String serialize() {
        return (super.getTime() + ";-;" + uuid);
    }

    public static FactionLog deserialize(String string) {
        String[] splitString = string.split(";-;");
        UUID uuid = UUID.fromString(splitString[1]);
        return new LandUnclaimAll(uuid, Long.parseLong(splitString[0]));
    }

    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(uuid).getName() + " unclaimed ALL land";

    }
}

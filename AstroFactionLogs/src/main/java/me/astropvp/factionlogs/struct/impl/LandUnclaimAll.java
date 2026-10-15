package me.astropvp.factionlogs.struct.impl;

import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.TimeDateUtils;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class LandUnclaimAll extends FactionLog {
    private final String uuid;

    public LandUnclaimAll(UUID uuid) {
        super(LogType.UNCLAIMALL);
        this.uuid = uuid.toString();

    }

    public LandUnclaimAll(UUID uuid, long time) {
        super(LogType.UNCLAIMALL);
        this.uuid = uuid.toString();
        super.setTime(time);
    }


    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + " unclaimed ALL land";

    }
}

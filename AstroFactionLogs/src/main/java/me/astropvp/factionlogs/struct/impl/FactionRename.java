package me.astropvp.factionlogs.struct.impl;

import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.TimeDateUtils;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class FactionRename extends FactionLog {
    private final String player;
    private final String oldTag;
    private final String newTag;

    public FactionRename(UUID playerUUID, String oldTag, String newTag) {
        super(LogType.FACTION_RENAME);
        this.player = playerUUID.toString();
        this.oldTag = oldTag;
        this.newTag = newTag;
    }

    public FactionRename(UUID playerUUID, String oldTag, String newTag, long time) {
        super(LogType.FACTION_RENAME);
        this.player = playerUUID.toString();
        this.oldTag = oldTag;
        this.newTag = newTag;
        super.setTime(time);
    }


    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(UUID.fromString(player)).getName() + " renamed the faction tag " + getOldTag() + " -> " + getNewTag() + ".";

    }
}

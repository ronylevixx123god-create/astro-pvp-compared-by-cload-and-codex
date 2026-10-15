package me.astropvp.factionlogs.struct.impl;

import com.massivecraft.factions.event.FPlayerJoinEvent;
import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.TimeDateUtils;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class PlayerJoin extends FactionLog {
    private final UUID uuid;
    private final FPlayerJoinEvent.PlayerJoinReason playerJoinReason;

    public PlayerJoin(UUID uuid, FPlayerJoinEvent.PlayerJoinReason playerJoinReason) {
        super(LogType.PLAYER_JOIN);
        this.uuid = uuid;
        this.playerJoinReason = playerJoinReason;

    }

    public PlayerJoin(UUID uuid, FPlayerJoinEvent.PlayerJoinReason playerJoinReason, long time) {
        super(LogType.PLAYER_JOIN);
        this.uuid = uuid;
        this.playerJoinReason = playerJoinReason;
        super.setTime(time);

    }

    @Override
    public String serialize() {
        return (this.getTime() + ";-;" + uuid + ";-;" + playerJoinReason.name());
    }

    public static FactionLog deserialize(String string) {
        String[] splitString = string.split(";-;");
        UUID uuid = UUID.fromString(splitString[1]);
        return new PlayerJoin(uuid, FPlayerJoinEvent.PlayerJoinReason.valueOf(splitString[2]), Long.parseLong(splitString[0]));
    }

    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(uuid).getName() + " joined the faction because of: " + playerJoinReason;

    }
}

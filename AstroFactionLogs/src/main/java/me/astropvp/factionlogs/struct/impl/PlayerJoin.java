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
    private final String uuid;
    private transient final FPlayerJoinEvent.PlayerJoinReason playerJoinReason;

    public PlayerJoin(UUID uuid, FPlayerJoinEvent.PlayerJoinReason playerJoinReason) {
        super(LogType.PLAYER_JOIN);
        this.uuid = uuid.toString();
        this.playerJoinReason = playerJoinReason;

    }

    public PlayerJoin(UUID uuid, FPlayerJoinEvent.PlayerJoinReason playerJoinReason, long time) {
        super(LogType.PLAYER_JOIN);
        this.uuid = uuid.toString();
        this.playerJoinReason = playerJoinReason;
        super.setTime(time);

    }


    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + " joined the faction because of: " + playerJoinReason;

    }
}

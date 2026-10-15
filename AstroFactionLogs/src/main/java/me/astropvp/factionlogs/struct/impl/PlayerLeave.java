package me.astropvp.factionlogs.struct.impl;

import com.massivecraft.factions.event.FPlayerLeaveEvent;
import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.TimeDateUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class PlayerLeave extends FactionLog {
    private final String uuid;
    private final transient FPlayerLeaveEvent.PlayerLeaveReason playerLeaveReason;

    public PlayerLeave(UUID uuid, FPlayerLeaveEvent.PlayerLeaveReason playerLeaveReason) {
        super(LogType.PLAYER_LEAVE);
        this.uuid = uuid.toString();
        this.playerLeaveReason = playerLeaveReason;
    }

    public PlayerLeave(UUID uuid, FPlayerLeaveEvent.PlayerLeaveReason playerLeaveReason, long time) {
        super(LogType.PLAYER_LEAVE);
        this.uuid = uuid.toString();
        this.playerLeaveReason = playerLeaveReason;
        super.setTime(time);
    }


    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + " left the faction because of: " + playerLeaveReason;

    }


}

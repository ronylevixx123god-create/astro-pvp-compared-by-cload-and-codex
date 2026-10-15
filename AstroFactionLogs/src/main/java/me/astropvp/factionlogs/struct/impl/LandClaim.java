package me.astropvp.factionlogs.struct.impl;

import com.massivecraft.factions.FLocation;
import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.LocationUtils;
import me.astropvp.factionlogs.utils.TimeDateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class LandClaim extends FactionLog {
    private final String uuid;
    private final Location fLocation;

    public LandClaim(UUID uuid, Location fLocation) {
        super(LogType.CLAIM);
        this.uuid = uuid.toString();
        this.fLocation = fLocation;
    }

    public LandClaim(UUID uuid, Location fLocation, long time) {
        super(LogType.CLAIM);
        this.uuid = uuid.toString();
        this.fLocation = fLocation;
        super.setTime(time);
    }


    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + " claimed land at " + LocationUtils.locToString(fLocation);
    }
}

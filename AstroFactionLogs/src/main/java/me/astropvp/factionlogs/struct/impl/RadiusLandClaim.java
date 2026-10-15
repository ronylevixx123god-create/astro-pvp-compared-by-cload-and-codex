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
public class RadiusLandClaim extends FactionLog {
    private final String uuid;
    private final Location fLocation;
    private final Integer radius;

    public RadiusLandClaim(UUID uuid, Location fLocation, int radius) {
        super(LogType.RADIUSCLAIM);
        this.uuid = uuid.toString();
        this.fLocation = fLocation;
        this.radius = radius;
    }

    public RadiusLandClaim(UUID uuid, Location fLocation,int radius, long time) {
        super(LogType.RADIUSCLAIM);
        this.uuid = uuid.toString();
        this.fLocation = fLocation;
        this.radius = radius;
        super.setTime(time);
    }

    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + " claimed land in a radius of " + radius +  " radius at " + LocationUtils.locToString(fLocation);

    }
}

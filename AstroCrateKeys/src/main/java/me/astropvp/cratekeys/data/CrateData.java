package me.astropvp.cratekeys.data;


import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.Location;

public class CrateData {
    private CrateType crate;

    private Location location;

    public CrateData(CrateType crate, Location location) {
        this.crate = crate;
        this.location = location;
    }

    public CrateType getCrateType() {
        return this.crate;
    }

    public Location getLocation() {
        return this.location;
    }
}

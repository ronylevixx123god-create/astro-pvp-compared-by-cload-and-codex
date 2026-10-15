/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class FireballAlleyRegionEvent extends RegionEvent {
    public List<Location> dispenserLocations = new ArrayList<Location>();

    public FireballAlleyRegionEvent(Dungeon d, String regionName) {
        super(d, regionName);
        this.updateDispenserLocations();
    }

    @Override
    public void run() {
        this.updateDispenserLocations();
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + (long)(this.dungeon.isHalfTime() ? 90000 : 150000), "Complete Fireball Alley");
        this.dungeon.broadcastMessage("Fireballs?! The traps and defenses of the Corrupt Cosmonaut has setup in this bunker are quite expensive. There is no time to waste! Get across this hallway!");
    }

    private void updateDispenserLocations() {
        this.dispenserLocations.clear();
        this.dispenserLocations.add(new Location(this.dungeon.world, 873.0, 215.0, 312.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 872.0, 215.0, 312.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 871.0, 215.0, 312.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 880.0, 215.0, 316.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 879.0, 215.0, 316.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 878.0, 215.0, 316.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 874.0, 215.0, 329.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 873.0, 215.0, 329.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 872.0, 215.0, 329.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 881.0, 215.0, 341.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 880.0, 215.0, 341.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 879.0, 215.0, 341.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 872.0, 215.0, 347.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 871.0, 215.0, 347.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 870.0, 215.0, 347.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 879.0, 215.0, 350.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 878.0, 215.0, 350.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 877.0, 215.0, 350.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 875.0, 215.0, 359.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 874.0, 215.0, 359.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 873.0, 215.0, 359.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 873.0, 215.0, 365.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 872.0, 215.0, 365.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 871.0, 215.0, 365.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 881.0, 215.0, 371.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 880.0, 215.0, 371.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 879.0, 215.0, 371.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 876.0, 215.0, 383.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 875.0, 215.0, 383.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 874.0, 215.0, 383.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 879.0, 215.0, 388.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 878.0, 215.0, 388.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 877.0, 215.0, 388.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 872.0, 215.0, 392.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 871.0, 215.0, 392.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 870.0, 215.0, 392.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 881.0, 215.0, 397.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 880.0, 215.0, 397.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 879.0, 215.0, 397.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 876.0, 215.0, 401.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 875.0, 215.0, 401.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 874.0, 215.0, 401.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 880.0, 215.0, 409.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 879.0, 215.0, 409.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 878.0, 215.0, 409.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 873.0, 215.0, 413.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 872.0, 215.0, 413.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 871.0, 215.0, 413.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 881.0, 215.0, 425.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 880.0, 215.0, 425.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 879.0, 215.0, 425.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 877.0, 215.0, 425.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 876.0, 215.0, 425.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 875.0, 215.0, 425.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 874.0, 215.0, 425.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 872.0, 215.0, 425.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 871.0, 215.0, 425.0));
        this.dispenserLocations.add(new Location(this.dungeon.world, 870.0, 215.0, 425.0));
    }
}


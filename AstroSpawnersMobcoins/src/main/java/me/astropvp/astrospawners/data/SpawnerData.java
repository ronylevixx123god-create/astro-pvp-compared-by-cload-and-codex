package me.astropvp.astrospawners.data;

import net.astropvp.storage.utils.KeyToSave;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;


public class SpawnerData implements KeyToSave {

    public int spawnerAmount;
    private final EntityType type;
    private Location location;

    public SpawnerData(EntityType type, int spawnerAmount, Location location) {
        this.spawnerAmount = spawnerAmount;
        this.type = type;
        this.location = location;
    }

    public EntityType getEntityType() {
        return this.type;
    }

    public int getSpawnerAmount() {
        return this.spawnerAmount;
    }

    public void setSpawnerAmount(int spawnerAmount) {
        this.spawnerAmount = spawnerAmount;
    }



    public static String locationToString(Location location) {
        return location.getWorld().getName() + "_" + location.getBlockX()+ "_" + location.getBlockY()+ "_" + location.getBlockZ();
    }


    public static Location locationFromString(String location) {
        String[] locationSplit = location.split("_");
        World world = Bukkit.getWorld(locationSplit[0]);
        double x = Double.parseDouble(locationSplit[1]);
        double y = Double.parseDouble(locationSplit[2]);
        double z = Double.parseDouble(locationSplit[3]);

        return new Location(world, x, y, z);
    }

    @Override
    public String getKey() {
        return locationToString(location);
    }

    @Override
    public void setKey(String key) {
        this.location = locationFromString(key);
    }
}

package net.crankedpvp.warzonechests.struct;

import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import net.crankedpvp.warzonechests.utils.LocationSerializerDeserializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WarzoneChestManager extends SaveLoadManager<WarzoneChest> {

    public WarzoneChestManager(JavaPlugin plugin) {
        super(plugin, StoragePlugin.getInstance().getGson(), "chests/", WarzoneChest.class);
    }

    public void resetWarzoneChestsForPlayer(Player player) {
        for (WarzoneChest warzoneChests : this.getCacheValues()) {
            warzoneChests.getPlayerChestLoot().invalidate(player.getUniqueId());
        }
    }

    public void resetWarzoneChests() {
        for (WarzoneChest warzoneChest : this.getCacheValues()) {
            warzoneChest.resetCache();
        }
    }

    public void placeWarzoneChest(Location location) {
        this.addToCache(new WarzoneChest(LocationSerializerDeserializer.serialize(location)));
    }

    /*
    @Override
    public String serialize(Location location) {
        return LocationSerializerDeserializer.serialize(location);
    }

    @Override
    public Location deserialize(String s) {
        return LocationSerializerDeserializer.deserialize(s);
    }

     */
}

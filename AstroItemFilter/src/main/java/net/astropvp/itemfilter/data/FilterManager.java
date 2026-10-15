package net.astropvp.itemfilter.data;

import lombok.Getter;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@Getter
public class FilterManager extends SaveLoadManager<FilterUser> {
    public FilterManager(JavaPlugin plugin) {
        super(plugin, StoragePlugin.getInstance().getGson(), "/data", FilterUser.class);
    }

    public FilterUser getFilterUser(UUID uuid) {
        if (!contains(uuid.toString())) {
            FilterUser filterUser = new FilterUser(uuid);
            addToCache(filterUser);
        }
        return getFromCache(uuid.toString());
    }
}

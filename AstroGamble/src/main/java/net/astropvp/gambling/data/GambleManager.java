package net.astropvp.gambling.data;



import net.astropvp.gambling.GamblingPlugin;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;

import java.util.UUID;

public class GambleManager extends SaveLoadManager<GambleUser> {
    public GambleManager(GamblingPlugin plugin) {
        super(plugin, StoragePlugin.getInstance().getGson(), "data/", GambleUser.class);
    }

    public GambleUser getGambleUser(UUID uuid) {
        if (!contains(uuid.toString())) {
            GambleUser gambleUser = new GambleUser(uuid);
            addToCache(gambleUser);
        }
        return getFromCache(uuid.toString());
    }
}

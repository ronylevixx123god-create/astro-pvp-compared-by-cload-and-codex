package net.astropvp.core.managers;

import lombok.Getter;
import net.astropvp.core.AstroCore;
import net.astropvp.core.struct.AstroPlayer;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.gson.Gson;
import net.astropvp.storage.utils.SaveLoadManager;
import net.astropvp.struct.PlayerCollect;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@Getter
public class AstroPlayerManager  extends SaveLoadManager<AstroPlayer> {
    public AstroPlayerManager() {
        super(AstroCore.getInstance(), AstroCore.getInstance().getGson(), "data/", AstroPlayer.class);
    }

    public AstroPlayer getAstroPlayer(UUID uuid) {
        if (getFromCache(uuid.toString()) == null) {
            addToCache(new AstroPlayer(uuid));
        }
        return getFromCache(uuid.toString());
    }
}

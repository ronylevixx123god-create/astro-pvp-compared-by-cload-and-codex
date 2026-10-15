package net.astropvp;

import lombok.Getter;
import net.astropvp.commands.CollectCommand;
import net.astropvp.commands.CollectSaveCommand;
import net.astropvp.listeners.PlayerListener;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.CollectManager;
import net.astropvp.struct.PlayerCollect;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Getter
public class CollectPlugin extends JavaPlugin {
    @Getter
    private static CollectPlugin instance;
    private CollectManager collectManager;

    public void onEnable() {
        instance = this;

        this.collectManager = new CollectManager(this, StoragePlugin.getInstance().getGson());
        this.collectManager.loadAll();

        this.registerCommands();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            System.out.println("[Collect] Started auto saving all collects");
            collectManager.purge();
            collectManager.saveAll();
            System.out.println("[Collect] Finished auto saving all collects");
            final OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
            for (OfflinePlayer player : offlinePlayers) {
                UUID uuid = player.getUniqueId();
                PlayerCollect playerCollect = getCollectManager().getFromCache(uuid.toString());
                if (playerCollect == null) continue;
                for (int j = 0; j < playerCollect.getCategories().size(); j++) {
                    CollectCategory category = playerCollect.getCategories().get(j);
                    if (category.isExpired()) {
                        getCollectManager().removeCategoryFromCache(uuid.toString(), category);
                        System.out.println("[Collect] Removed category for " + uuid + " because it expired!");
                    }
                }
            }
        }, 60L, 20 * 60L);

    }

    public void onDisable() {
        this.collectManager.purge();
        this.collectManager.saveAll();
        instance = null;
    }

    private void registerCommands() {
        getCommand("collect").setExecutor(new CollectCommand());
        getCommand("collectsave").setExecutor(new CollectSaveCommand(this));
        getCommand("collectload").setExecutor(new CollectSaveCommand(this));
    }
}

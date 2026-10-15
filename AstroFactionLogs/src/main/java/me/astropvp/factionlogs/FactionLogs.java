package me.astropvp.factionlogs;

import lombok.Getter;
import me.astropvp.factionlogs.commands.FactionLogsCommand;
import me.astropvp.factionlogs.data.FactionLogsData;
import me.astropvp.factionlogs.listeners.FactionListeners;
import me.astropvp.factionlogs.data.FactionLogsManager;
import me.astropvp.factionlogs.struct.typeadapters.FactionLogDeserializer;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


@Getter
public class FactionLogs extends JavaPlugin {

    public FactionLogsManager logManager;
    private static FactionLogs instance;
    private Gson gson;


    public void onEnable() {
        instance = this;

        this.gson = StoragePlugin.getInstance()
                .getGson()
                .newBuilder()
                .registerTypeAdapter(FactionLogsData.class, new FactionLogDeserializer()).setLenient()
                .create();

        this.logManager = new FactionLogsManager();
        this.logManager.loadAll();
        loadListeners();
        loadCommands();
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            System.out.println("[FactionLogs] Starting auto saving faction logs");
            this.logManager.saveAll();
            System.out.println("[FactionLogs] Finished auto saving faction logs");
        }, 100L, 20 * 60 * 15);
    }

    public void onDisable() {
        //this.logManager.purge();
        this.logManager.saveAll();
        instance = null;
    }


    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new FactionListeners(), this);
    }

    public void loadCommands() {
        Bukkit.getPluginManager().registerEvents(new FactionLogsCommand(), this);

    }

    public static FactionLogs getInstance() {
        return instance;
    }
}

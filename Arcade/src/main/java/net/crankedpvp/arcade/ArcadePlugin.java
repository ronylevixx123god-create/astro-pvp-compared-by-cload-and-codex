package net.crankedpvp.arcade;

import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import net.crankedpvp.arcade.commands.ArcadeAdminCommand;
import net.crankedpvp.arcade.commands.ArcadeCommand;
import net.crankedpvp.arcade.language.LanguageHandler;
import net.crankedpvp.arcade.listeners.ArcadeLogicListener;
import net.crankedpvp.arcade.listeners.SignListener;
import net.crankedpvp.arcade.struct.ArcadeManager;
import net.crankedpvp.arcade.tasks.ArcadeBorderPlayers;
import net.crankedpvp.arcade.tasks.ArcadeHiderTask;
import net.crankedpvp.arcade.tasks.ArcadeRegionChecker;
import net.crankedpvp.arcade.tasks.ArcadeTimerChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ArcadePlugin extends JavaPlugin {
    private ArcadeManager arcadeManager;
    private LanguageHandler languageHandler;
    private ArcadeRegionChecker arcadeRegionChecker;
    private ArcadeHiderTask arcadeHiderTask;
    private ArcadeTimerChecker arcadeTimerChecker;
    private ArcadeBorderPlayers arcadeBorderPlayers;

    private CommandManager commandManager;

    @Getter
    private static ArcadePlugin instance;
    public void onEnable() {
        instance = this;
        loadConfig();
        this.languageHandler = new LanguageHandler(this);
        languageHandler.load();


        this.commandManager = new CommandManager(this);
        this.commandManager.register(new ArcadeAdminCommand(this));
        this.commandManager.register(new ArcadeCommand());

        this.arcadeManager = new ArcadeManager();


        this.arcadeRegionChecker = new ArcadeRegionChecker();
        this.arcadeHiderTask = new ArcadeHiderTask();
        this.arcadeTimerChecker = new ArcadeTimerChecker();
        this.arcadeBorderPlayers = new ArcadeBorderPlayers();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, arcadeRegionChecker, 20L, 10L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, arcadeHiderTask, 20L, 5L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, arcadeTimerChecker, 20L, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, arcadeBorderPlayers, 1L, 1L);

        getServer().getPluginManager().registerEvents(new ArcadeLogicListener(this), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);

    }

    public void onDisable() {
        arcadeManager.onDisable();
        languageHandler.save();
        instance = null;
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}

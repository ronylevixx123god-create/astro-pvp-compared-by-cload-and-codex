package me.kingsavaqe.astrotournaments;

import lombok.Getter;
import me.kingsavaqe.astrotournaments.commands.TournamentCommand;
import me.kingsavaqe.astrotournaments.listeners.PlayerJoinListener;
import me.kingsavaqe.astrotournaments.listeners.PlayerQuitListener;
import me.kingsavaqe.astrotournaments.listeners.TournamentDeathListener;
import me.kingsavaqe.astrotournaments.listeners.TournamentLogicListener;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import me.kingsavaqe.astrotournaments.tasks.NoFlightTask;
import me.kingsavaqe.astrotournaments.utils.Queue;
import me.kingsavaqe.astrotournaments.utils.Utils;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AstroTournamentsPlugin extends JavaPlugin {

    public static AstroTournamentsPlugin instance;

    @Getter
    private TournamentManager tournamentManager;

    @Getter
    private ArenaRegister arenaRegister;

    public void onEnable() {

        instance = this;
        saveDefaultConfig();

        this.tournamentManager = new TournamentManager(this);
        this.tournamentManager.setPlayerQueue(new Queue<>());
        this.tournamentManager.setWaitingRoomLocation(Utils.getLocationFromString(this.getConfig().getString("Tournament.WaitingRoom.Location")));
        getTournamentManager().loadAll();
        getTournamentManager().loadBannedSkinsFromConfig();
        getTournamentManager().loadCurrentArmorCrystalFromConfig();

        this.arenaRegister = new ArenaRegister(this);
        this.arenaRegister.registerArenas();

        CommandManager commandManager = new CommandManager(this);
        commandManager.register(new TournamentCommand(this));

        this.registerListeners();
        this.registerTasks();


        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, ()-> {
            System.out.println("[Tournaments] Started auto saving tournament data");
            getTournamentManager().purge();
            getTournamentManager().saveAll();
            System.out.println("[Tournaments] Finished auto saving tournament data");

        },60L, 20L*60);
    }

    public void onDisable() {
        if(getTournamentManager().isActive()) {
            getTournamentManager().end();
            System.out.println("[Tournaments] Force ended an active tournament");
        }
        getTournamentManager().purge();

        getTournamentManager().saveAll();
        getTournamentManager().saveBannedSkinsToConfig();
        getTournamentManager().saveCurrentArmorCrystalToConfig();

    }

    public void registerTasks() {
        new NoFlightTask().runTaskTimer(this, 20, 5);

    }


    public void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new TournamentDeathListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new TournamentLogicListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);

    }
}

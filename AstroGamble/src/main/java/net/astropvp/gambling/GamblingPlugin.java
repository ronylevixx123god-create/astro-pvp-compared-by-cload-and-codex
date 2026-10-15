package net.astropvp.gambling;

import lombok.Getter;
import lombok.Setter;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;

import net.astropvp.gambling.crash.commands.CrashCommand;
import net.astropvp.gambling.crash.listeners.CrashListener;
import net.astropvp.gambling.crash.manager.CrashManager;
import net.astropvp.gambling.crash.tasks.CrashTask;
import net.astropvp.gambling.data.GambleManager;

import net.astropvp.gambling.mines.commands.MinesCommand;
import net.astropvp.gambling.tower.commands.TowerCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@Getter
public class GamblingPlugin extends JavaPlugin {

    @Getter
    private static GamblingPlugin instance;

    private GambleManager gambleManager;
    private CrashManager crashManager;

    private CommandManager commandManager;

    @Getter @Setter
    private long overallCrashLosings;
    @Getter @Setter
    private long overallCrashWinnings;
    @Getter @Setter
    private long overallTowerLosings;
    @Getter @Setter
    private long overallTowerWinnings;
    @Getter @Setter
    private long overallMinesLosings;
    @Getter @Setter
    private long overallMinesWinnings;
    @Getter @Setter
    private long overallBombsLosings;
    @Getter @Setter
    private long overallBombsWinnings;

    @Override
    public void onEnable() {
        instance = this;
        this.commandManager = new CommandManager(this);
        this.registerCommandParameters();
        this.commandManager.register(new CrashCommand(this));
        //this.commandManager.register(new BombsCommand(this));
        this.commandManager.register(new MinesCommand(this));
        this.commandManager.register(new GamblingCommand(this));
        this.commandManager.register(new TowerCommand(this));
        //this.commandManager.register(new RouletteCommand(this));
        this.gambleManager = new GambleManager(this);
        this.crashManager = new CrashManager(this);
        this.crashManager.setupLastBigCrashes();
        gambleManager.loadAll();

        getServer().getPluginManager().registerEvents(new CrashListener(this), this);

        this.crashManager.randomizeFinalCrashX();
        this.getServer().getScheduler().runTaskLater(this, () -> new CrashTask(this).initializeGame(), 60L);

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, ()-> {
            System.out.println("[Gamble] Started auto saving stats");
            gambleManager.saveAll();
            System.out.println("[Gamble] Finished auto saving stats");

        }, 60L, 10*20*60);

        this.setOverallCrashLosings(getConfig().getLong("CRASH-LOSINGS"));
        this.setOverallCrashWinnings(getConfig().getLong("CRASH-WINNINGS"));
        this.setOverallTowerLosings(getConfig().getLong("TOWER-LOSINGS"));
        this.setOverallTowerWinnings(getConfig().getLong("TOWER-WINNINGS"));
        this.setOverallBombsLosings(getConfig().getLong("BOMBS-LOSINGS"));
        this.setOverallBombsWinnings(getConfig().getLong("BOMBS-WINNINGS"));
        this.setOverallMinesLosings(getConfig().getLong("MINES-LOSINGS"));
        this.setOverallMinesWinnings(getConfig().getLong("MINES-WINNINGS"));
    }

    private void registerCommandParameters() {
        this.commandManager.getParameterHandler().register(OfflinePlayer.class, argument -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(argument.toString());
            if(offlinePlayer == null) return new TypeResult(argument);
            return new TypeResult(offlinePlayer, argument);
        });
    }

    @Override
    public void onDisable() {
        this.getConfig().set("CRASH-WINNINGS", this.overallCrashWinnings);
        this.getConfig().set("CRASH-LOSINGS", this.overallCrashLosings);
        this.getConfig().set("TOWER-WINNINGS", this.overallTowerWinnings);
        this.getConfig().set("TOWER-LOSINGS", this.overallTowerLosings);
        this.getConfig().set("BOMBS-WINNINGS", this.overallBombsWinnings);
        this.getConfig().set("BOMBS-LOSINGS", this.overallBombsLosings);
        this.getConfig().set("MINES-WINNINGS", this.overallMinesWinnings);
        this.getConfig().set("MINES-LOSINGS", this.overallMinesLosings);
        try {
            this.getConfig().save(this.getDataFolder() + "/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        gambleManager.saveAll();
        instance = null;

    }


}

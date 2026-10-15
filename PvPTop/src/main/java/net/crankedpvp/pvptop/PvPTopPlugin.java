package net.crankedpvp.pvptop;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;
import net.crankedpvp.pvptop.commands.PvPTopCommand;
import net.crankedpvp.pvptop.data.FactionData;
import net.crankedpvp.pvptop.data.PvPTopManager;
import net.crankedpvp.pvptop.inventory.ObtainPointsMethodsInventory;
import net.crankedpvp.pvptop.inventory.PvPTopInventory;
import net.crankedpvp.pvptop.listeners.FactionDisbandCheck;
import net.crankedpvp.pvptop.listeners.KothCaptureListener;
import net.crankedpvp.pvptop.listeners.PlayerDeathListener;
import net.crankedpvp.pvptop.tasks.UpdateLeaderboardTask;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class PvPTopPlugin extends JavaPlugin {

    @Getter
    private static PvPTopPlugin instance;

    @Getter
    private PvPTopManager pvpTopManager;

    @Getter
    private UpdateLeaderboardTask updateLeaderboardTask;

    @Getter
    private CommandManager commandManager;

    @Getter
    private PvPTopInventory pvPTopInventory;
    @Getter
    private ObtainPointsMethodsInventory obtainPointsMethodsInventory;

    @Override
    public void onEnable() {
        instance = this;

        this.pvpTopManager = new PvPTopManager(this);
        this.pvpTopManager.loadAll();

        this.commandManager = new CommandManager(this);
        this.registerCommandParameters();
        this.registerCommandCompletion();
        this.commandManager.register(new PvPTopCommand(this));

        this.pvPTopInventory = new PvPTopInventory(this, true);
        this.obtainPointsMethodsInventory = new ObtainPointsMethodsInventory();

        this.registerListeners();

        this.updateLeaderboardTask = new UpdateLeaderboardTask(this);
        this.updateLeaderboardTask.runTaskTimer(this, 10 * 20L, 30 * 20L);
    }

    private void registerCommandParameters() {
        this.commandManager.getParameterHandler().register(FactionData.Points.class, argument -> {
            FactionData.Points points = FactionData.Points.valueOf(String.valueOf(argument).toUpperCase());
            return new TypeResult(points, argument);
        });
    }


    private void registerCommandCompletion() {
        this.commandManager.getCompletionHandler().register("#factions", input -> Factions.getInstance().getAllFactions().stream().map(Faction::getTag).collect(Collectors.toList()));
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerDeathListener(this), this);
        pluginManager.registerEvents(new FactionDisbandCheck(), this);
        pluginManager.registerEvents(new KothCaptureListener(this), this);
    }

    @Override
    public void onDisable() {
        this.pvpTopManager.saveAll();
    }

    public void updateInventory() {
        this.pvPTopInventory = new PvPTopInventory(this, false);
    }
}

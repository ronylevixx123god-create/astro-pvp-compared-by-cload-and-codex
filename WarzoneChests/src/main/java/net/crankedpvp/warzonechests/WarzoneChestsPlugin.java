package net.crankedpvp.warzonechests;

import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import net.crankedpvp.warzonechests.commands.AdminCommand;
import net.crankedpvp.warzonechests.listener.WarzoneChestListeners;
import net.crankedpvp.warzonechests.listener.WarzoneChestSummonerListeners;
import net.crankedpvp.warzonechests.struct.WarzoneChestManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class WarzoneChestsPlugin extends JavaPlugin {

    private WarzoneChestManager warzoneChestManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        this.warzoneChestManager = new WarzoneChestManager(this);
        this.warzoneChestManager.loadAll();

        this.registerListeners();

       this.commandManager = new CommandManager(this);
       this.commandManager.register(new AdminCommand());
    }

    @Override
    public void onDisable() {
        this.warzoneChestManager.saveAll();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new WarzoneChestListeners(this), this);
        pm.registerEvents(new WarzoneChestSummonerListeners(this), this);
    }
}

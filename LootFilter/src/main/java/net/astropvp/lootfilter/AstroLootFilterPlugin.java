package net.astropvp.lootfilter;

import co.aikar.commands.CommandManager;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.astropvp.lootfilter.commands.LootFilterCommand;
import net.astropvp.lootfilter.commands.LootFrenzyCommand;
import net.astropvp.lootfilter.data.LootFilterManager;
import net.astropvp.lootfilter.listeners.FrenzyJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AstroLootFilterPlugin extends JavaPlugin {
    @Getter
    private static AstroLootFilterPlugin instance;
    public LootFilterManager lootFilterManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        this.loadConfig();
        this.lootFilterManager = new LootFilterManager();
        lootFilterManager.loadAll();
        this.commandManager = new PaperCommandManager(this);
        this.commandManager.registerCommand(new LootFilterCommand());
        this.commandManager.registerCommand(new LootFrenzyCommand());
        //this.getServer().getPluginManager().registerEvents(new FrenzyJoinListener(), this);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            System.out.println("[LootFilter] Started auto saving lootfilter data");
            lootFilterManager.purge();
            lootFilterManager.saveAll();
            System.out.println("[LootFilter] Finished auto saving lootfilter data");
        },60L, 10*20*60);
    }

    @Override
    public void onDisable() {
        lootFilterManager.purge();
        lootFilterManager.saveAll();
        instance = null;

    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}

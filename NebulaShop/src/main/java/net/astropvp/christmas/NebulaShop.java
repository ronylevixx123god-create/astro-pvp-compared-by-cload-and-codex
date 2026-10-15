package net.astropvp.christmas;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.Getter;
import me.matthawkk.plugin.BasePlugin;
import me.matthawkk.plugin.Plugins;
import net.astropvp.christmas.commands.NebulaCommand;
import net.astropvp.christmas.listeners.NebulaShopListener;
import net.astropvp.christmas.managers.NebulaShopManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class NebulaShop extends JavaPlugin {
    @Getter
    private static NebulaShop instance;

    @Getter
    private NebulaShopManager nebulaShopManager;


    public void onEnable() {
        instance = this;
        File file = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }
        this.nebulaShopManager = new NebulaShopManager();
        this.nebulaShopManager.loadAll();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, ()-> {
            nebulaShopManager.saveAll();
        }, 100L, 10*20*60);
        getCommand("nebula").setExecutor(new NebulaCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new NebulaShopListener(), this);
    }

    public void onDisable() {
        getNebulaShopManager().saveShop();
        nebulaShopManager.saveAll();
        instance = null;
    }
}

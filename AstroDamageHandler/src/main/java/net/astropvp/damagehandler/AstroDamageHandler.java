package net.astropvp.damagehandler;

import lombok.Getter;
import net.astropvp.damagehandler.commands.CommandDebugDamage;
import org.bukkit.plugin.java.JavaPlugin;

public class AstroDamageHandler extends JavaPlugin {
    @Getter
    private static AstroDamageHandler instance;
    private AstroDamageHandlerAPI damageHandler;



    public void onEnable() {
        instance = this;
        registerCommands();
        registerListeners();
        loadConfig();
        this.damageHandler = new AstroDamageHandlerAPI(this);
        System.out.println("[AstroDamageHandler] Plugin has been enabled!");
    }


    public void onDisable() {
        System.out.println("[AstroDamageHandler] Plugin has been disabled!");
        instance = null;
    }

    public void registerCommands() {
        getCommand("debugdamage").setExecutor(new CommandDebugDamage());
    }

    public void registerListeners() {
    }

    public AstroDamageHandlerAPI getDamageHandler() {
        return damageHandler;
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}

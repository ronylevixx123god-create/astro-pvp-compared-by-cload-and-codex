package me.kingsavaqe.astroenvoy;

import com.cosmicpvp.cosmicutils.serializer.location.LocationSerializerDeserializer;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.Getter;
import me.kingsavaqe.astroenvoy.commands.AdminEnvoyCommand;
import me.kingsavaqe.astroenvoy.commands.EnvoyCommand;
import me.kingsavaqe.astroenvoy.language.LanguageHandler;
import me.kingsavaqe.astroenvoy.listeners.AddCrateLocationListener;
import me.kingsavaqe.astroenvoy.listeners.EnvoySummonerListener;
import me.kingsavaqe.astroenvoy.listeners.OpenEnvoyCrateListener;
import me.kingsavaqe.astroenvoy.struct.EnvoyManager;
import me.mattstudios.mf.base.CommandManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EnvoyPlugin extends JavaPlugin {

    @Getter
    private static EnvoyPlugin instance;

    @Getter
    private EnvoyManager envoyManager;

    @Getter
    private CommandManager commandManager;

    @Getter
    private LanguageHandler languageHandler;


    @Override
    public void onEnable() {
        instance = this;
        this.languageHandler = new LanguageHandler(this);
        this.languageHandler.load();
        this.registerListeners(this.getServer().getPluginManager());
        this.commandManager = new CommandManager(this);
        this.commandManager.register(new EnvoyCommand(this));
        this.commandManager.register(new AdminEnvoyCommand(this));
        this.envoyManager = new EnvoyManager(this);
        this.registerLocationsFromConfig();
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            this.envoyManager.startEnvoyEvent();
        }, 20L * 60L * 30L, 20L * 60L * 30L);
        HologramsAPI.registerPlaceholder(EnvoyPlugin.getInstance(), "{envoy_time}", 1, this.getEnvoyManager());

    }

    private void registerListeners(PluginManager pluginManager) {
        pluginManager.registerEvents(new AddCrateLocationListener(this), this);
        pluginManager.registerEvents(new OpenEnvoyCrateListener(this), this);
        pluginManager.registerEvents(new EnvoySummonerListener(this), this);
    }


    private void registerLocationsFromConfig() {
        FileConfiguration yamlConfiguration = this.getConfig();
        if (yamlConfiguration.contains("locations")) {
            for (String string : yamlConfiguration.getStringList("locations")) {
                this.envoyManager.getLocations().add(LocationSerializerDeserializer.deserialize(string));
            }
        }
    }

    @Override
    public void onDisable() {
        this.envoyManager.handleCrateRemoving();
        this.envoyManager.setActive(false);
        this.envoyManager.setRemainingCrates(0);
    }
}

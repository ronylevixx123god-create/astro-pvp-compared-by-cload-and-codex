package net.crankedpvp.slotbot;

import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import net.crankedpvp.slotbot.commands.SlotBotCommand;
import net.crankedpvp.slotbot.listeners.SlotBotTempFix;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SlotBotPlugin extends JavaPlugin {

    @Getter
    private static SlotBotPlugin instance;

    @Override
    public void onEnable() {

        instance = this;
        CommandManager commandManager = new CommandManager(this);
        commandManager.register(new SlotBotCommand(this));

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new SlotBotTempFix(this), this);

    }

    @Override
    public void onDisable() {
        instance = null;
    }
}

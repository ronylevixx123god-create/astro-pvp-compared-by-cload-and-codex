package net.astropvp.vouchers;

import me.mattstudios.mf.base.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VouchersPlugin extends JavaPlugin {

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        this.commandManager = new CommandManager(this);
    }

    @Override
    public void onDisable() {

    }
}

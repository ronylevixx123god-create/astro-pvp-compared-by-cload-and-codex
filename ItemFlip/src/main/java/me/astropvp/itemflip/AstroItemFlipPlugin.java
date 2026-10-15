package me.astropvp.itemflip;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import lombok.Getter;
import me.astropvp.itemflip.commands.ItemFlipCmd;
import me.astropvp.itemflip.listeners.ItemFlipSanityListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AstroItemFlipPlugin
        extends JavaPlugin
{
    @Getter
    private InventoryManager inventoryManager;

    public void onEnable() {
        (this.inventoryManager = new InventoryManager((JavaPlugin)this)).init();
        PaperCommandManager commandManager;
        (commandManager = new PaperCommandManager((Plugin)this)).registerCommand((BaseCommand)new ItemFlipCmd(this, this.inventoryManager));
        this.getServer().getPluginManager().registerEvents((Listener)new ItemFlipSanityListener(), (Plugin)this);
    }

    public void onDisable() {
    }

}

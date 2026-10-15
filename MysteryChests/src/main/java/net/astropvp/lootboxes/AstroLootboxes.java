/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  co.aikar.commands.BaseCommand
 *  co.aikar.commands.CommandManager
 *  co.aikar.commands.PaperCommandManager
 *  com.decimatemc.decimatecommons.inventory.InventoryManager
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.astropvp.lootboxes;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandManager;
import co.aikar.commands.PaperCommandManager;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.api.PluginLootboxManager;
import net.astropvp.lootboxes.command.LootboxAdminCmd;
import net.astropvp.lootboxes.command.LootboxCmd;
import net.astropvp.lootboxes.config.RarityConfig;
import net.astropvp.lootboxes.gui.LootboxViewGui;
import net.astropvp.lootboxes.listener.LootboxListener;
import net.astropvp.lootboxes.lootbox.AstroLootboxManager;
import net.astropvp.lootboxes.lootbox.ExternalLootboxManager;
import net.astropvp.lootboxes.manager.ForeignLootboxManager;
import net.astropvp.lootboxes.manager.LootboxCreationManager;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AstroLootboxes
        extends JavaPlugin {
    private InventoryManager inventoryManager;
    private CommandManager commandManager;
    private LootboxManager lootboxManager;
    private LootboxCreationManager lootboxCreationManager;
    private ForeignLootboxManager foreignLootboxManager;
    private static AstroLootboxes INSTANCE;

    public static AstroLootboxes getInstance() {
        return INSTANCE;
    }

    public void onEnable() {
        INSTANCE = this;
        this.saveDefaultConfig();
        RarityConfig.initialize(this);
        this.inventoryManager = new InventoryManager((JavaPlugin) this);
        this.inventoryManager.init();
        this.lootboxManager = new AstroLootboxManager(this);
        this.lootboxCreationManager = new LootboxCreationManager(this.lootboxManager, this.inventoryManager, this);
        this.foreignLootboxManager = new ForeignLootboxManager(this, this.lootboxManager);
        this.commandManager = new PaperCommandManager((Plugin) this);
        this.commandManager.registerCommand((BaseCommand) new LootboxCmd(this, this.lootboxManager, this.foreignLootboxManager, this.lootboxCreationManager, this.inventoryManager, new LootboxViewGui(this.lootboxManager, this.inventoryManager)));
        this.commandManager.registerCommand((BaseCommand) new LootboxAdminCmd(this, this.lootboxManager));
        Bukkit.getPluginManager().registerEvents(new LootboxListener(this.lootboxManager, this, this.inventoryManager), this);
    }

    public void onDisable() {
        this.lootboxManager.forceSave(false);
        this.lootboxCreationManager.save();
        this.foreignLootboxManager.getPluginManagers().forEach(lootbox -> lootbox.forceSave(false));
    }

    public LootboxManager getLootboxManager() {
        return this.lootboxManager;
    }

    public static ForeignLootboxManager getForeignLootboxManager() {
        AstroLootboxes plugin = AstroLootboxes.ensureEnabled();
        return plugin.foreignLootboxManager;
    }

    public static PluginLootboxManager registerExternalManager(JavaPlugin owner, boolean handleable) {
        AstroLootboxes plugin = AstroLootboxes.ensureEnabled();
        ExternalLootboxManager manager = new ExternalLootboxManager(owner, handleable);
        plugin.foreignLootboxManager.registerManager(owner, manager);
        System.out.println("[DecimateLootboxes] Registered external lootbox manager from plugin: " + owner.getName());
        return manager;
    }

    public static AstroLootboxes ensureEnabled() {
        if (!Bukkit.getPluginManager().isPluginEnabled("AstroLootboxes")) {
            throw new IllegalStateException("Cannot instantiate external manager while DecimateLootboxes is not enabled!");
        }
        AstroLootboxes plugin = JavaPlugin.getPlugin(AstroLootboxes.class);
        if (plugin == null || !plugin.isEnabled()) {
            throw new IllegalStateException("Cannot instantiate external manager while DecimateLootboxes is not enabled!");
        }
        return plugin;
    }
}


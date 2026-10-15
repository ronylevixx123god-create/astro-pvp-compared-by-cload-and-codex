 package me.cosmicpvp.CosmicGlobalKits;

import lombok.Getter;
import lombok.Setter;
import me.cosmicpvp.CosmicGlobalKits.commands.CommandFallenHero;
import me.cosmicpvp.CosmicGlobalKits.commands.CommandGKit;
import me.cosmicpvp.CosmicGlobalKits.database.Database;
import me.cosmicpvp.CosmicGlobalKits.database.SQLManager;
import me.cosmicpvp.CosmicGlobalKits.database.SQLQueryCustomThread;
import me.cosmicpvp.CosmicGlobalKits.database.SQLQueryThread;
import me.cosmicpvp.CosmicGlobalKits.items.MysteryFallenHeroItem;
import me.cosmicpvp.CosmicGlobalKits.items.WildcardGkit;
import me.cosmicpvp.CosmicGlobalKits.listeners.*;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitUser;
import me.cosmicpvp.CosmicGlobalKits.utils.WorldGuardUtils;

import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CosmicGlobalKits extends JavaPlugin {

    @Getter private static CosmicGlobalKits plugin = null;

    @Getter @Setter public static String sql_table = "globalkits_userdata";
    @Getter @Setter private SQLQueryThread sql_worker;
    @Getter @Setter public static boolean kitGemsEnabled = true;
    @Getter @Setter private String serverName;
    @Getter private final Map<UUID, GlobalKitUser> globalKitUsers = new HashMap<UUID, GlobalKitUser>();
    @Getter private static final boolean debug = false;

    @Getter @Setter private Database sqlDatabase;
    @Getter @Setter private SQLQueryCustomThread sqlQueryThread = new SQLQueryCustomThread();

    public void onEnable() {
        plugin = this;
        WorldGuardUtils.hook();
        this.saveDefaultConfig();
        this.registerCommands();
        this.registerListeners();
        this.sqlDatabase = new Database("sqlconfig.yml");
        if (!this.sqlDatabase.isConnected()) {
            System.out.println("Unable to connect to database!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        kitGemsEnabled = this.getConfig().getBoolean("kitGemsEnabled");
        SQLManager.initializeSQL(this);
        SQLManager.sql_query.add("CREATE TABLE IF NOT EXISTS " + sql_table + "(player_uuid VARCHAR(64), player_name VARCHAR(16), global_kit TEXT, last_use LONG)");
        this.sql_worker = new SQLQueryThread();
        this.sql_worker.start();
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new WildcardGkit());
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new MysteryFallenHeroItem());


        if (!this.sqlDatabase.isConnected()) {
            System.out.println("Unable to connect to database!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.sqlQueryThread = new SQLQueryCustomThread();
        this.sqlQueryThread.start();
    }

    public void onDisable() {
        this.sql_worker.interrupt();
        this.sql_worker.doWork();
        this.sql_worker = null;

        if (this.sqlQueryThread != null) {
            this.sqlQueryThread.interrupt();
            this.sqlQueryThread.execute();
            this.sqlQueryThread = null;
        }
    }

    public GlobalKitUser getGlobalKitUser(UUID uuid) {
        return this.globalKitUsers.get(uuid);
    }

    private void registerCommands() {
        this.getCommand("gkit").setExecutor(new CommandGKit());
        this.getCommand("fallenhero").setExecutor(new CommandFallenHero());

    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
        this.getServer().getPluginManager().registerEvents(new FallenHeroListener(), this);
            this.getServer().getPluginManager().registerEvents(new PermissionListener(), this);
    }

    public static void log(String msg) {
        if (!debug) {
            return;
        }
        Bukkit.getLogger().info("[CosmicGlobalKits] " + msg);
    }

}


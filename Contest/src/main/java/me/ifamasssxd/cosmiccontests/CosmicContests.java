/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  lilypad.client.connect.api.Connect
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.ifamasssxd.cosmiccontests;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import lombok.SneakyThrows;
import me.ifamasssxd.cosmiccontests.commands.CommandContest;
import me.ifamasssxd.cosmiccontests.listeners.ArkkitListener;
import me.ifamasssxd.cosmiccontests.listeners.ContestListener;
import me.ifamasssxd.cosmiccontests.listeners.FactionsListener;
import me.ifamasssxd.cosmiccontests.listeners.InventoryListener;
import me.ifamasssxd.cosmiccontests.listeners.PlayerListener;
import me.ifamasssxd.cosmiccontests.listeners.mcMMOListener;
import me.ifamasssxd.cosmiccontests.sql.Database;
import me.ifamasssxd.cosmiccontests.struct.ContestManager;
import me.ifamasssxd.cosmiccontests.struct.PrizeManager;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import me.ifamasssxd.cosmiccontests.tasks.AnnounceTask;
import me.ifamasssxd.cosmiccontests.tasks.TimeSpentTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CosmicContests
extends JavaPlugin {
    private static CosmicContests instance;
    private ContestManager contestManager;
    PrizeManager prizeManager;
    private Database sqlDatabase;
    private boolean mcMMOEnabled = false;
    private boolean factionsEnabled = true;
    private String serverName;

    @SneakyThrows
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.factionsEnabled = Bukkit.getPluginManager().isPluginEnabled("Factions");
        this.mcMMOEnabled = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
        this.sqlDatabase = new Database();
        if (!this.setupSql()) {
            Bukkit.getLogger().info("Unable to load SQL, disabling plugin...");
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        this.contestManager = new ContestManager();
        this.contestManager.loadContests(this.getConfig());
        this.prizeManager = new PrizeManager();
        this.prizeManager.loadPrizes();
        this.registerArkkitListener();
        this.registerEvents();
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (ContestUser.getUser(pl.getUniqueId()) != null) continue;
            ContestUser user = new ContestUser(pl.getName(), pl.getUniqueId());
            ContestUser.getUsers().put(pl.getUniqueId(), user);
            user.loadData(false);
        }

        //These 2 used to be sync
        this.getServer().getScheduler().scheduleAsyncRepeatingTask((Plugin)this, (Runnable)new AnnounceTask(), 20L, 18000L);
        this.getServer().getScheduler().scheduleAsyncRepeatingTask((Plugin)this, (Runnable)new TimeSpentTask(), 20L, 20L);
    }

    private void registerArkkitListener() {
        try {
            Bukkit.getLogger().info(r);
            this.getServer().getPluginManager().registerEvents((Listener)new ArkkitListener(), (Plugin)this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents((Listener)new InventoryListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ContestListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerListener(), (Plugin)this);
        this.getCommand("challenge").setExecutor((CommandExecutor)new CommandContest());
        if (this.mcMMOEnabled) {
            this.getServer().getPluginManager().registerEvents((Listener)new mcMMOListener(), (Plugin)this);
        }
        if (this.factionsEnabled) {
            this.getServer().getPluginManager().registerEvents((Listener)new FactionsListener(), (Plugin)this);
        }
    }

    public void onDisable() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            ContestUser user = ContestUser.getUser(pl.getUniqueId());
            if (user == null) continue;
            user.saveData(false);
        }
        int toSave = 0;
        for (ContestUser user : ContestUser.getUsers().values()) {
            if (!user.isPendingChanges()) continue;
            ++toSave;
            user.saveData(false);
        }
        Bukkit.getLogger().info("Saved " + toSave + " unsaved changed to db.");
        instance = null;
    }

    private boolean setupSql() throws SQLException {
        boolean bl;
        block6: {
            PreparedStatement statement = CosmicContests.get().getSqlDatabase().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + this.serverName + "_contestUsers`(uuid varchar(64) PRIMARY KEY, name varchar(16), contestData longtext);");
            try {
                statement.executeUpdate();
                bl = true;
                if (Collections.singletonList(statement).get(0) == null) break block6;
            }
            catch (Throwable throwable) {
                try {
                    if (Collections.singletonList(statement).get(0) != null) {
                        statement.close();
                    }
                    throw throwable;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            statement.close();
        }
        return bl;
    }

    public static CosmicContests get() {
        return instance;
    }

    public ContestManager getContestManager() {
        return this.contestManager;
    }

    public PrizeManager getPrizeManager() {
        return this.prizeManager;
    }

    public Database getSqlDatabase() {
        return this.sqlDatabase;
    }

    public boolean isMcMMOEnabled() {
        return this.mcMMOEnabled;
    }

    public boolean isFactionsEnabled() {
        return this.factionsEnabled;
    }

    public String getServerName() {
        return this.serverName;
    }
}


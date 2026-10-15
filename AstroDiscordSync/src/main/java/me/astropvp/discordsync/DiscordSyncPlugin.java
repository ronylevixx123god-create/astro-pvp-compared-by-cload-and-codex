package me.astropvp.discordsync;

import me.astropvp.discordsync.commands.SyncCommand;
import me.astropvp.discordsync.commands.UnSyncCommand;
import me.astropvp.discordsync.commands.discord.WhitelistCommand;
import me.astropvp.discordsync.listeners.ChatListener;
import me.astropvp.discordsync.listeners.DiscordListener;
import me.astropvp.discordsync.managers.DatabaseManager;
import me.astropvp.discordsync.managers.SyncManager;
import me.mattstudios.mf.base.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class DiscordSyncPlugin extends JavaPlugin {
    private JDA jda;
    private SyncManager syncManager;
    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private DiscordListener discordListener;
    private ChatListener chatListener;

    public SyncManager getSyncManager() {
        return this.syncManager;
    }

    public ChatListener getChatListener() {
        return this.chatListener;
    }

    public JDA getJda() {
        return this.jda;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    // Shared with the asynchronous startup worker; shutdown must win a startup race.
    private volatile boolean stopping;

    @Override
    public void onEnable() {
        stopping = false;
        saveDefaultConfig();
        final String token = getConfig().getString("discord.bot-token", "").trim();
        if (token.isEmpty()) {
            getLogger().severe("discord.bot-token is not set in config.yml, disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        databaseManager = new DatabaseManager(this);
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            JDA startedJda = null;
            try {
                databaseManager.connect();
                databaseManager.createTable();
                databaseManager.fetchMap();
                startedJda = new JDABuilder().setToken(token)
                        .setActivity(Activity.playing("play.astropvp.net"))
                        .setStatus(OnlineStatus.ONLINE).build();
                synchronized (this) {
                    if (stopping) {
                        startedJda.shutdownNow();
                        return;
                    }
                    // Publish the client before waiting so disabling can cancel login too.
                    jda = startedJda;
                }
                startedJda.awaitReady();
                final JDA readyJda = startedJda;
                synchronized (this) {
                    if (stopping) {
                        readyJda.shutdownNow();
                        return;
                    }
                    Bukkit.getScheduler().runTask(this, () -> {
                        if (stopping || !isEnabled()) {
                            readyJda.shutdownNow();
                            return;
                        }
                        commandManager = new CommandManager(this);
                        syncManager = new SyncManager(this);
                        chatListener = new ChatListener(this);
                        getServer().getPluginManager().registerEvents(chatListener, this);
                        commandManager.register(new SyncCommand(this));
                        commandManager.register(new UnSyncCommand(this));
                        discordListener = new DiscordListener(this);
                        readyJda.addEventListener(discordListener, new WhitelistCommand(this));
                        databaseManager.autoReconnect(850);
                    });
                }
            } catch (Exception failure) {
                if (startedJda != null) {
                    startedJda.shutdownNow();
                }
                if (failure instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                // Do not log exception messages that may include tokens or JDBC URLs.
                getLogger().severe("Discord/database startup failed (" + failure.getClass().getSimpleName() + ").");
                synchronized (this) {
                    if (!stopping) {
                        Bukkit.getScheduler().runTask(this, () -> Bukkit.getPluginManager().disablePlugin(this));
                    }
                }
            }
        });
    }

    @Override
    public synchronized void onDisable() {
        stopping = true;
        Bukkit.getScheduler().cancelTasks(this);
        if (jda != null) {
            jda.shutdownNow();
            jda = null;
        }
        if (databaseManager != null) {
            try {
                databaseManager.close();
            } catch (SQLException failure) {
                getLogger().warning("Failed to close the Discord database connection.");
            }
        }
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public DiscordListener getDiscordListener() {
        return this.discordListener;
    }
}

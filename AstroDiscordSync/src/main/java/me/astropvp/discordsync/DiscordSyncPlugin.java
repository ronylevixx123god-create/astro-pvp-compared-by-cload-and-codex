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
import org.bukkit.scheduler.BukkitRunnable;

import javax.security.auth.login.LoginException;
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

    public void onEnable() {
        this.saveDefaultConfig();
        final String token = this.getConfig().getString("discord.bot-token", "");
        if (token.isEmpty()) {
            this.getLogger().severe("discord.bot-token is not set in config.yml, disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                this.jda = (new JDABuilder()).setToken(token).setActivity(Activity.playing("play.astropvp.net")).setStatus(OnlineStatus.ONLINE).build();
            } catch (LoginException var2) {
                Bukkit.getScheduler().runTask(this, () -> {
                    Bukkit.getPluginManager().disablePlugin(this);
                    throw new RuntimeException("Shutting Discord Link plugin down, failed to initiate bot!", var2);
                });
            }

            Bukkit.getScheduler().runTask(this, () -> {
                this.commandManager = new CommandManager(this);
                this.syncManager = new SyncManager(this);
                this.chatListener = new ChatListener(this);
                this.getServer().getPluginManager().registerEvents(this.chatListener, this);
                this.commandManager.register(new SyncCommand(this));
                this.commandManager.register(new UnSyncCommand(this));
                this.discordListener = new DiscordListener(this);
                this.jda.addEventListener(this.discordListener);
                this.jda.addEventListener(new WhitelistCommand());
            });
        });
        this.databaseManager = new DatabaseManager(this);
        (new BukkitRunnable() {
            public void run() {
                try {
                    DiscordSyncPlugin.this.databaseManager.connect();
                    DiscordSyncPlugin.this.databaseManager.createTable();
                    Bukkit.getScheduler().runTaskLater(DiscordSyncPlugin.this, () -> {
                        DiscordSyncPlugin.this.databaseManager.fetchMap();
                    }, 5L);
                    DiscordSyncPlugin.this.databaseManager.autoReconnect(850);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        }).runTaskAsynchronously(this);
    }

    public void onDisable() {
        if (this.jda != null) {
            this.jda.removeEventListener(this.discordListener);
        }

        if (this.databaseManager == null || this.databaseManager.getConnection() == null) {
            return;
        }

        try {
            this.databaseManager.getConnection().close();
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public DiscordListener getDiscordListener() {
        return this.discordListener;
    }
}

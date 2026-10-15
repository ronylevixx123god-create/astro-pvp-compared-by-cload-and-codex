/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  lilypad.client.connect.api.Connect
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.commands.CommandArkReboot
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.ifamasssxd.cosmicduel;

import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicduel.commands.*;
import me.ifamasssxd.cosmicduel.inventory.CustomInventory;
import me.ifamasssxd.cosmicduel.inventory.CustomInventoryListener;
import me.ifamasssxd.cosmicduel.listeners.*;
import me.ifamasssxd.cosmicduel.struct.*;
import me.ifamasssxd.cosmicduel.struct.enums.EndReason;
import me.ifamasssxd.cosmicduel.struct.newduel.DuelLogFormatter;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.ifamasssxd.cosmicduel.tasks.*;
import me.ifamasssxd.cosmicduel.utils.DuelToggles;
import me.ifamasssxd.cosmicduel.utils.WorldGuardUtils;
import org.arkhamnetwork.Arkkit.commands.CommandArkReboot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class CosmicDuel
extends JavaPlugin {
    private static CosmicDuel instance;
    private static Logger logger;
    private static FileHandler logHandler;
    public DuelToggles duelToggles;
    private DuelHistoryManager duelHistoryManager = new DuelHistoryManager();
    public static boolean queueEnabled;
    public static boolean enchantsEnabled;
    public static boolean envoysEnabled;
    private final String serverName = null;
    private TeamDataManager teamDataManager;

    public void onEnable() {
        instance = this;
        DuelPlayer.setDuelPlayers(new HashMap<UUID, DuelPlayer>());
        this.duelToggles = new DuelToggles();
        this.initPlugin();
        this.saveDefaultConfig();
        this.registerTasks();
        this.registerListeners();
        this.registerCommands();

        this.setupDuelLog();
        queueEnabled = this.getConfig().getBoolean("queueEnabled", false);
        this.teamDataManager = new TeamDataManager();
    }

    public void onDisable() {
        logHandler.flush();
        logHandler.close();
        this.duelToggles.save(false);

        try {
            CustomInventory.closeInventories();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        SpectateInventory.closeInventory();
        for (NewDuel duel : Lists.newArrayList(DuelAPI.duels)) {
            duel.onDuelEnd(EndReason.RELOAD, null);
        }
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.removeMetadata("in_duel", (Plugin)this);
            p.removeMetadata("spectator", (Plugin)this);
            if (p.getOpenInventory().getTopInventory() == null || !p.getOpenInventory().getTopInventory().getName().startsWith("Duel Items: ")) continue;
            p.closeInventory();
        }
        this.teamDataManager.terminate();
    }

    public boolean isShuttingDown() {
        return CommandArkReboot.restart_task_exists;
    }

    public TeamDataManager getTeamDataManager() {
        return this.teamDataManager;
    }

    public void initPlugin() {
        this.loadArenaConfigs();

        if (!new File("plugins/Enchantments.jar").exists()) {
            enchantsEnabled = false;
        }
        WorldGuardUtils.hook();
    }

    public void loadArenaConfigs() {
        for (String arenaName : this.getConfig().getConfigurationSection("Arenas").getKeys(false)) {
            String description = this.getConfig().getString("Arenas." + arenaName + ".Description");
            Arena arena = new Arena(arenaName, description, this.getConfig().getString("Arenas." + arenaName + ".Region"));
            if (this.getConfig().isSet("Arenas." + arenaName + ".Ranked")) {
                boolean ranked = this.getConfig().getBoolean("Arenas." + arenaName + ".Ranked", false);
                arena.setRankedArena(ranked);
            }
            arena.setSpawnLocations(this.getConfig().getStringList("Arenas." + arenaName + ".Spawns"));
            if (this.getConfig().contains("Arenas." + arenaName + ".Color")) {
                String color = ChatColor.translateAlternateColorCodes((char)'&', (String)this.getConfig().getString("Arenas." + arenaName + ".Color"));
                arena.setColor(color);
            }
            if (this.getConfig().contains("Arenas." + arenaName + ".Icon")) {
                try {
                    Material m = Material.valueOf((String)this.getConfig().getString("Arenas." + arenaName + ".Icon"));
                    arena.setIcon(m);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Arena.arenas.add(arena);
        }
    }

    public void registerTasks() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)get(), (Runnable)new DuelArenaTask(), 20L, 5L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)get(), (Runnable)new DuelTask(), 20L, 20L);

        //Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)get(), (Runnable)new QueueUpdateTask(), 20L, 700L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask((Plugin)get(), (Runnable)new DuelInviteTask(), 20L, 20L);
        getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new RebootTask(), 20L, 20L);
        //getServer().getScheduler()
                //.scheduleSyncRepeatingTask((Plugin)this, (Runnable)new QueueTask(this), (getConfig().getInt("queue-delay", 5) * 20), (getConfig().getInt("queue-delay", 5) * 20));
    }

    public void registerCommands() {
        this.getCommand("setduelworld").setExecutor(new CommandSetDuelWorld());
        this.getCommand("removeduelspawn").setExecutor(new CommandRemoveDuelSpawn());
        this.getCommand("setduelspawn").setExecutor(new CommandSetDuelSpawn());
        this.getCommand("spectate").setExecutor(new CommandSpectate());
        this.getCommand("duel").setExecutor(new CommandDuel());
        this.getCommand("viewdeathinventory").setExecutor(new CommandViewDeathInventory());
        this.getCommand("team").setExecutor(new CommandTeam());
    }

    public void registerListeners() {
        this.registerListener(new SpectateInventory());
        this.registerListener(new SpectatorListener());
        this.registerListener(new DuelInventoryListener());
        this.registerListener(new PlayerListener());
        this.registerListener(new DuelListener());
        this.registerListener(new CustomInventoryListener());
        this.registerListener(new PotionListener());
        this.registerListener(new SettingsListener());
        this.registerListener(new HitsListener());
        registerListener((Listener)new TeamDuelListener());
    }

    private void registerListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    private void setupDuelLog() {
        try {
            Calendar cal = Calendar.getInstance();
            String fileName = "duels-" + cal.get(2) + "-" + cal.get(5) + ".log";
            logHandler = new FileHandler("plugins/CosmicDuel/" + fileName);
            logger.addHandler(logHandler);
            DuelLogFormatter formatter = new DuelLogFormatter();
            logHandler.setFormatter(formatter);
            logger.setUseParentHandlers(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static CosmicDuel get() {
        return instance;
    }

    public static void logMessage(String msg) {
        logger.info(msg);
    }

    public DuelToggles getDuelToggles() {
        return this.duelToggles;
    }


    public DuelHistoryManager getDuelHistoryManager() {
        return this.duelHistoryManager;
    }

    public String getServerName() {
        return this.serverName;
    }

    static {
        logger = Logger.getLogger("CosmicDuels");
        queueEnabled = false;
        enchantsEnabled = true;
        envoysEnabled = true;
    }
}


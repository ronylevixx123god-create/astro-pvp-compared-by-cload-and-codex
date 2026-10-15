package me.ifamasssxd.cosmicbosses;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import me.ifamasssxd.cosmicbosses.bosses.GuardBoss;
import me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragon;
import me.ifamasssxd.cosmicbosses.commands.CommandBoss;
import me.ifamasssxd.cosmicbosses.commands.CommandBossRewards;
import me.ifamasssxd.cosmicbosses.commands.CommandSpawnBoss;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.entities.CustomEntity;
import me.ifamasssxd.cosmicbosses.listeners.*;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.MiniBoss;
import me.ifamasssxd.cosmicbosses.struct.RewardInventory;
import me.ifamasssxd.cosmicbosses.struct.blockmanager.BlockChangeManager;
import me.ifamasssxd.cosmicbosses.tasks.BleedTask;
import me.ifamasssxd.cosmicbosses.tasks.BossSpawnTask;
import me.ifamasssxd.cosmicbosses.tasks.BossTickTask;
import me.ifamasssxd.cosmicbosses.tasks.BossUpdateTask;
import me.ifamasssxd.cosmicbosses.utils.MapUtils;
import me.ifamasssxd.cosmicbosses.utils.ScoreboardUtils;
import me.ifamasssxd.cosmicbosses.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class CosmicBosses extends JavaPlugin {
    private static CosmicBosses instance;
    private static boolean mcMMOEnabled;
    private boolean eggEnabled;
    private boolean naturalSpawnEnabled;
    private boolean scoreboardEnabled;
    private BlockChangeManager blockChangeManager;
    private BossSpawnTask task;
    public static String battlefieldWorld;
    private HashMap<String, List<String[]>> pendingBosses;

    public CosmicBosses() {
        this.eggEnabled = false;
        this.naturalSpawnEnabled = false;
        this.scoreboardEnabled = true;
        this.pendingBosses = new HashMap<String, List<String[]>>();
    }

    public static CosmicBosses get() {
        return CosmicBosses.instance;
    }

    @SneakyThrows
    public void onEnable() {
        (CosmicBosses.instance = this).saveDefaultConfig();
        //CosmicBosses.mcMMOEnabled = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
        this.registerCommands();
        this.registerListeners();
        this.registerTasks();
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            PlayerListener.aSyncPlayerMap.put(p.getUniqueId(), p);
            ScoreboardUtils.clearBossScoreboard(p);
        }
        CustomEntity.registerEntities();
        StringUtils.getCenteredMessage(" Swag");
        MapUtils.sortByComparator(null, true, 0);
        this.eggEnabled = this.getConfig().getBoolean("eggEnabled", false);
        this.naturalSpawnEnabled = this.getConfig().getBoolean("naturalSpawn", false);
        this.scoreboardEnabled = this.getConfig().getBoolean("scoreboardEnabled", true);
        this.loadBosses();
        this.blockChangeManager = new BlockChangeManager();
        RewardInventory.loadBossRewards(this);
    }

    @SneakyThrows
    public void onDisable() {
        RewardInventory.saveBossRewards();
        this.blockChangeManager.restoreAllBlocks();
        this.saveBosses();
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            ScoreboardUtils.clearBossScoreboard(p);
        }
        CustomEntity.unregisterEntities();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents((Listener) new MinionListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new BossListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new PlayerListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new BlockListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new SkeletonHorseListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new WitherBossListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new DungeonBossListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new EggListener(), (Plugin) this);
    }

    private void registerCommands() {
        this.getCommand("boss").setExecutor((CommandExecutor) new CommandBoss());
        this.getCommand("spawnboss").setExecutor((CommandExecutor) new CommandSpawnBoss());
        this.getCommand("bossrewards").setExecutor((CommandExecutor) new CommandBossRewards());
    }

    private void registerTasks() {
        this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, (Runnable) new BossUpdateTask(), 20L, 5L);
        this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, (Runnable) new BossTickTask(), 1L, 1L);
        this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, (Runnable) new BleedTask(), 0L, 10L);

        //AUTO BOSS SPAWN
/*        this.task = new BossSpawnTask();
        final int delay = 72000; //72000
        this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, (Runnable) this.task, (long) delay, (long) delay);*/
    }

    public void loadBosses() {
        for (final String boss : this.getConfig().getStringList("bosses")) {
            final String[] args = boss.split(":");
            final World world = Bukkit.getWorld(args[1]);
            final Location loc = new Location(world, (double) Integer.parseInt(args[2]), (double) Integer.parseInt(args[3]), (double) Integer.parseInt(args[4]));
            final String chunkCoords = loc.getChunk().getX() + ":" + loc.getChunk().getZ();
            List<String[]> bosses = Lists.newArrayList();
            if (this.pendingBosses.containsKey(chunkCoords)) {
                bosses = this.pendingBosses.get(chunkCoords);
            }
            bosses.add(args);
            System.out.println("Loading boss at " + chunkCoords + " -> " + args[0]);
            this.pendingBosses.put(chunkCoords, bosses);
        }
        this.getConfig().set("bosses", (Object) null);
        this.saveConfig();
    }

    public void saveBosses() {
        final List<String> bossList = Lists.newArrayList();
        for (final Boss boss : Boss.bosses.values()) {
            if (boss.isBossSpawned()) {
                if (boss instanceof GuardBoss || boss instanceof DungeonYijki || boss instanceof MiniBoss || boss instanceof EndDragon) {
                    boss.onDeath(null);
                    boss.getEntity().remove();
                } else {
                    final Location loc = boss.getEntity().getLocation();
                    final Location l = boss.getLocation();
                    bossList.add(boss.getBossType().name() + ":" + loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ() + ":" + boss.getEntity().getHealth());
                    boss.onDeath(null);
                    boss.getEntity().remove();
                }
            }
        }
        this.reloadConfig();
        this.getConfig().set("bosses", (Object) bossList);
        this.saveConfig();
    }

    public static CosmicBosses getInstance() {
        return CosmicBosses.instance;
    }

    public static boolean isMcMMOEnabled() {
        return CosmicBosses.mcMMOEnabled;
    }

    public boolean isEggEnabled() {
        return this.eggEnabled;
    }

    public boolean isNaturalSpawnEnabled() {
        return this.naturalSpawnEnabled;//this.naturalSpawnEnabled;
    }

    public boolean isScoreboardEnabled() {
        return this.scoreboardEnabled;
    }


    public void setEggEnabled(final boolean eggEnabled) {
        this.eggEnabled = eggEnabled;
    }

    public void setNaturalSpawnEnabled(final boolean naturalSpawnEnabled) {
        this.naturalSpawnEnabled = naturalSpawnEnabled;
    }

    public void setScoreboardEnabled(final boolean scoreboardEnabled) {
        this.scoreboardEnabled = scoreboardEnabled;
    }


    public BlockChangeManager getBlockChangeManager() {
        return this.blockChangeManager;
    }

    public BossSpawnTask getTask() {
        return this.task;
    }

    public HashMap<String, List<String[]>> getPendingBosses() {
        return this.pendingBosses;
    }

    static {
        CosmicBosses.mcMMOEnabled = false;
        CosmicBosses.battlefieldWorld = "world_arena";
    }
}

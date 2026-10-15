/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.JSONUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldGuardUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  com.decimatemc.decimatecommons.inventory.InventoryManager
 *  com.decimatemc.decimatelootboxes.DecimateLootboxes
 *  com.decimatemc.decimatelootboxes.api.Lootbox
 *  com.decimatemc.decimatelootboxes.api.PluginLootboxManager
 *  com.decimatemc.decimateserversync.DecimateServerSync
 *  com.earth2me.essentials.IEssentials
 *  com.google.common.collect.Lists
 *  com.google.gson.reflect.TypeToken
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  fr.neatmonster.nocheatplus.checks.CheckType
 *  fr.neatmonster.nocheatplus.hooks.NCPHook
 *  fr.neatmonster.nocheatplus.hooks.NCPHookManager
 *  me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CosmicItem
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.compilex.CosmicDungeons;

import com.compilex.CosmicDungeons.cmds.*;
import com.compilex.CosmicDungeons.dungeons.libs.*;
import com.compilex.CosmicDungeons.items.HeroicDungeonScrollItem;
import com.compilex.CosmicDungeons.items.PortalProtectionTrinketItem;
import com.compilex.CosmicDungeons.listeners.*;
import com.compilex.CosmicDungeons.menus.DungeonMasterKeyMenu;
import com.compilex.CosmicDungeons.struct.PortalLog;
import com.compilex.CosmicDungeons.tasks.HolidayPortalTask;
import com.compilex.CosmicDungeons.utils.EntityHider;
import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.JSONUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import com.earth2me.essentials.IEssentials;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CosmicDungeonsPlugin extends JavaPlugin {
    private DungeonsAPI api;
    private static CosmicDungeonsPlugin _;
    private Economy economy;
    private EntityHider entityHider;
    private boolean disableDungeons = false;
    private boolean lowCostKeys = false;
    private boolean fundEnabled = false;
    private boolean disableCalled = false;
    private final List<DungeonType> disabledDungeons = Lists.newArrayList();
    private final Map<UUID, String> factionsInsideOnEnable = new HashMap<UUID, String>();
    private String serverName;
    private boolean usePhysicalKey = true;
    private final List<DungeonType> availableDungeonKeysTracked = Lists.newArrayList();
    private Map<DungeonType, List<PortalLog>> portalLogs = new HashMap<DungeonType, List<PortalLog>>();
    private final Type portalLogToken = new TypeToken<HashMap<DungeonType, List<PortalLog>>>(){}.getType();
    private File portalLogFile;
    public IEssentials ess3;
    private DungeonMenuListener dungeonMenuListener;
    private PlayerJoinQuitListener playerJoinQuitListener;
    private DungeonPortalCraftListener dungeonPortalCraftListener;
    private DungeonPortalListener dungeonPortalListener;
    private DungeonMasterNPCListener dungeonMasterNPCListener;
    private DungeonInOutListener dungeonInOutListener;
    private DungeonPlayerDeathListener dungeonPlayerDeathListener;
    private DungeonLootbagListener dungeonLootbagListener;
    private DungeonPlayerMoveListener dungeonPlayerMoveListener;
    private DungeonSanityListener dungeonSanityListener;

    public DungeonsAPI getApi() {
        return this.api;
    }

    public EntityHider getEntityHider() {
        return this.entityHider;
    }

    public static CosmicDungeonsPlugin get_() {
        return _;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public boolean isDisableDungeons() {
        return this.disableDungeons;
    }

    public boolean isLowCostKeys() {
        return this.lowCostKeys;
    }

    public void setDisableDungeons(boolean disableDungeons) {
        this.disableDungeons = disableDungeons;
    }

    public void setLowCostKeys(boolean lowCostKeys) {
        this.lowCostKeys = lowCostKeys;
    }

    public boolean isFundEnabled() {
        return this.fundEnabled;
    }

    public boolean isDisableCalled() {
        return this.disableCalled;
    }

    public List<DungeonType> getDisabledDungeons() {
        return this.disabledDungeons;
    }

    public Map<UUID, String> getFactionsInsideOnEnable() {
        return this.factionsInsideOnEnable;
    }

    public static CosmicDungeonsPlugin get() {
        return _;
    }

    public void setUsePhysicalKey(boolean usePhysicalKey) {
        this.usePhysicalKey = usePhysicalKey;
    }

    public boolean isUsePhysicalKey() {
        return this.usePhysicalKey;
    }

    public Map<DungeonType, List<PortalLog>> getPortalLogs() {
        return this.portalLogs;
    }

    public void onEnable() {
        _ = this;
        this.ess3 = (IEssentials)Bukkit.getPluginManager().getPlugin("Essentials");
        this.entityHider = new EntityHider(this, EntityHider.Policy.BLACKLIST);
        this.mkdirs();
        this.api = new DungeonsAPI();
        this.setupEconomy();
        this.loadDungeons();
        this.registerCommands();
        this.registerListeners();
        this.registerTasks();
        WorldGuardUtils.hook();
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            this.api.addPlayer(p);
        }
        try {
            Class.forName(WorldUtil.class.getName());
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (this.getConfig().getBoolean("dungeons.disabled." + DungeonType.DESTROYED_OUTPOST.name(), true) && this.getConfig().getBoolean("dungeons.disabled." + DungeonType.ABANDONED_SHIP.name(), false)) {
            this.disabledDungeons.add(DungeonType.ABANDONED_SHIP);
        }
        this.fundEnabled = new File("plugins/CosmicFund.jar").exists();
        this.lowCostKeys = this.getConfig().getBoolean("lowCostKeys");
        this.usePhysicalKey = this.getConfig().getBoolean("usePhysicalKeys", true);
        if (this.usePhysicalKey) {
            for (DungeonType type : DungeonType.values()) {
                if (DungeonMasterKeyMenu.getResetString(type) != null) continue;
                this.availableDungeonKeysTracked.add(type);
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicDungeonsPlugin.get(), () -> {
            for (FPlayer fplayer : FPlayers.getInstance().getAllFPlayers()) {
                try {
                    UUID uuid = UUID.fromString(fplayer.getAccountId());
                    this.factionsInsideOnEnable.put(uuid, fplayer.getFactionId());
                }
                catch (Exception e) {
                    Bukkit.getLogger().info("Unable to get UUID from: " + fplayer.getName() + " ID: " + fplayer.getAccountId());
                    e.printStackTrace();
                }
            }
            Bukkit.getLogger().info("[CosmicDungeons] Registered " + this.factionsInsideOnEnable.size() + " players inside of factions!");
        }, 100L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CosmicDungeonsPlugin.get(), () -> {
            for (DungeonType type : DungeonType.values()) {
                if (this.availableDungeonKeysTracked.contains(type) || DungeonMasterKeyMenu.getResetString(type) != null) continue;
                this.availableDungeonKeysTracked.add(type);
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(CC.GoldB + "(!) " + CC.GoldU + type.getDungeonName() + CC.Gold + " Dungeon Keys are now available for purchase from the Dungeon Master!");
                Bukkit.broadcastMessage("");
            }
        }, 200L, 200L);
        File orCreateFile = null;
        try {
            orCreateFile = JSONUtils.getOrCreateFile("portalLogs.json");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        this.portalLogFile = orCreateFile;
        try {
            this.portalLogs = (Map)JSONUtils.fromJson(orCreateFile, this.portalLogToken);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (this.portalLogs == null) {
            this.portalLogs = new HashMap<DungeonType, List<PortalLog>>();
        }
        if (this.portalLogs != null) {
            this.portalLogs.forEach((dType, logs) -> logs.removeIf(log -> System.currentTimeMillis() - log.getPlacetime() >= TimeUnit.DAYS.toMillis(10L)));
        }
        this.getLogger().info("Loaded " + this.portalLogs.size() + " Portal logs keys..");
    }

    public boolean isDevplanet() {
        return this.serverName != null && this.serverName.startsWith("devplanet");
    }

    public void onDisable() {
        this.disableCalled = true;
        for (DungeonPlayer player : this.api.getPlayers().values()) {
            player.save();
            if (!player.isOnline() || player.getCurrentDungeon() == null) continue;
            player.leaveDungeon();
        }
        for (Location l : this.dungeonPortalListener.portalBlocks) {
            l.getBlock().setType(Material.AIR);
        }
        if (this.portalLogs != null) {
            try {
                JSONUtils.saveJSONToFile(this.portalLogFile, this.portalLogs, this.portalLogToken);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    public DungeonMenuListener getDungeonMenuListener() {
        return this.dungeonMenuListener;
    }

    public PlayerJoinQuitListener getPlayerJoinQuitListener() {
        return this.playerJoinQuitListener;
    }

    public DungeonPortalCraftListener getDungeonPortalCraftListener() {
        return this.dungeonPortalCraftListener;
    }

    public DungeonPortalListener getDungeonPortalListener() {
        return this.dungeonPortalListener;
    }

    public DungeonMasterNPCListener getDungeonMasterNPCListener() {
        return this.dungeonMasterNPCListener;
    }

    public DungeonInOutListener getDungeonInOutListener() {
        return this.dungeonInOutListener;
    }

    public DungeonPlayerDeathListener getDungeonPlayerDeathListener() {
        return this.dungeonPlayerDeathListener;
    }

    public DungeonLootbagListener getDungeonLootbagListener() {
        return this.dungeonLootbagListener;
    }

    public DungeonPlayerMoveListener getDungeonPlayerMoveListener() {
        return this.dungeonPlayerMoveListener;
    }

    public DungeonSanityListener getDungeonSanityListener() {
        return this.dungeonSanityListener;
    }

    private void registerListeners() {
        this.dungeonMenuListener = new DungeonMenuListener();
        this.playerJoinQuitListener = new PlayerJoinQuitListener();
        this.dungeonMasterNPCListener = new DungeonMasterNPCListener();
        this.dungeonPortalListener = new DungeonPortalListener();
        this.dungeonPortalCraftListener = new DungeonPortalCraftListener();
        this.dungeonInOutListener = new DungeonInOutListener();
        this.dungeonPlayerDeathListener = new DungeonPlayerDeathListener();
        this.dungeonPlayerMoveListener = new DungeonPlayerMoveListener();
        this.dungeonLootbagListener = new DungeonLootbagListener();
        this.dungeonSanityListener = new DungeonSanityListener();
        this.getServer().getPluginManager().registerEvents(this.dungeonMenuListener, this);
        this.getServer().getPluginManager().registerEvents(this.playerJoinQuitListener, this);
        this.getServer().getPluginManager().registerEvents(this.dungeonMasterNPCListener, this);
        this.getServer().getPluginManager().registerEvents(this.dungeonPortalCraftListener, this);
        this.getServer().getPluginManager().registerEvents(this.dungeonPortalListener, this);
        this.getServer().getPluginManager().registerEvents(this.dungeonInOutListener, this);
        this.getServer().getPluginManager().registerEvents(this.dungeonPlayerDeathListener, this);
        this.getServer().getPluginManager().registerEvents(this.dungeonLootbagListener, this);
        this.getServer().getPluginManager().registerEvents(this.dungeonPlayerMoveListener, this);
        this.getServer().getPluginManager().registerEvents(this.dungeonSanityListener, this);
        this.getServer().getPluginManager().registerEvents(new DungeonChallengeChestListener(), this);


        if (Bukkit.getPluginManager().isPluginEnabled("CosmicSkyCustomItems")) {
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new PortalProtectionTrinketItem());
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new HeroicDungeonScrollItem());
        }
    }

    private void registerCommands() {
        this.getCommand("dungeon").setExecutor(new CommandDungeon());
        this.getCommand("dungeonmaster").setExecutor(new CommandDungeonMaster());
        this.getCommand("adungeon").setExecutor(new CommandAdminDungeon());
        this.getCommand("objective").setExecutor(new CommandObjective());
        this.getCommand("dungeongive").setExecutor(new CommandDungeonGive());
    }

    private void registerTasks() {
        if (this.getConfig().getBoolean("holiday.dailyPortals")) {
            Bukkit.getScheduler().runTaskTimer(this, new HolidayPortalTask(), 1200L, 1200L);
        }
        Bukkit.getScheduler().runTaskTimer(this, () -> CosmicGUI.activeGUIs.values(), 20L, 20L);
    }

    private void loadDungeons() {
        CosmicDungeonsPlugin.get_().getApi().addDungeon(new Dungeon(Bukkit.getWorld("dungeon_yijki_a"), DungeonType.ABANDONED_SHIP, this.getConfig().getString("dungeons.dungeon_yijki_a.name"), new LinkedList<String>(this.getConfig().getStringList("dungeons.dungeon_yijki_a.description"))));
        CosmicDungeonsPlugin.get_().getApi().addDungeon(new DestroyedOutpostDungeon(Bukkit.getWorld(DungeonType.DESTROYED_OUTPOST.getInternalName()), "&4&l&nDestroyed Outpost", new LinkedList<String>(Lists.newArrayList("", "&4&lLORE", CC.Gray + "After investigating the source of the", CC.Gray + "distress signal sent from the Abandoned Spaceship", CC.Gray + "and parsing over the recovered Cosmonaut Ship", CC.Gray + "master log files, a chilling series of events", CC.Gray + "has been uncovered.", "", CC.Gray + "With the teleportation matrix solved, the", CC.Gray + "Mother of Yijki\u2019s source location has been traced.", CC.Gray + "There is a rift into its dimension at the", CC.Gray + "core of this destroyed mining outpost.", "", CC.Gray + "You have been chosen to lead the assault", CC.Gray + "onto the hostile planet to rid the Cosmoverse", CC.Gray + "of this demonic entity of destruction."))));
        CosmicDungeonsPlugin.get_().getApi().addDungeon(new PlanetNulDungeon(Bukkit.getWorld(DungeonType.PLANET_NUL.getInternalName()), "&8&l&nPlanet Nul", new LinkedList<String>(Lists.newArrayList("", "&8&lLORE", CC.Gray + "Located on the edge of the Cosmoverse,", CC.Gray + "Planet Nul is a mysterious and dark planet", CC.Gray + "that, until recently, held no significance.", CC.Gray, CC.Gray + "However, new intel suggests one of the", CC.Gray + "Most infamous criminals in the universe,", CC.Gray + "known only as - " + CC.DarkGray + "The Corrupt Cosmonaut,", CC.Gray + "is hiding deep in an abandoned military bunker", CC.Gray + "that can be accessed from the planet's surface.", CC.Gray, CC.Gray + "An elite team has been assembled, and you have", CC.Gray + "been chosen, to launch a tactical assault on", CC.Gray + "the military bunker on Planet Nul with one", CC.Gray + "simple objective: " + CC.DarkGray + "Eliminate the Corrupt Cosmonaut."))));
    }

    private void mkdirs() {
        new File("plugins/CosmicDungeons/playerData").mkdirs();
    }

    public void logPortalPlace(DungeonType type, Player player, Block block) {
        List<PortalLog> logs = this.portalLogs.computeIfAbsent(type, e -> Lists.newLinkedList());
        logs.add(new PortalLog(player.getUniqueId(), player.getWorld().getName(), block.getX(), block.getY(), block.getZ(), System.currentTimeMillis()));
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            this.economy = (Economy)economyProvider.getProvider();
        }
        return this.economy != null;
    }
}


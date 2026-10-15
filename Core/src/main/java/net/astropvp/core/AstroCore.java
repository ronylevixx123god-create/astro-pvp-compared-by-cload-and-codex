package net.astropvp.core;

import co.aikar.commands.CommandManager;
import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.I18n;
import com.massivecraft.factions.Faction;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import net.astropvp.core.autobroadcast.tasks.AutoBroadcastTask;
import net.astropvp.core.autogiveall.commands.CommandForceGiveAll;
import net.astropvp.core.autogiveall.commands.CommandNextGiveAll;
import net.astropvp.core.autogiveall.struct.GiveAllManager;
import net.astropvp.core.betterpay.PayListener;
import net.astropvp.core.betterpay.PayToggle;
import net.astropvp.core.betterpay.commands.CommandTogglePay;
import net.astropvp.core.commands.*;
import net.astropvp.core.cosmiccoinflip.commands.CommandCoinFlip;
import net.astropvp.core.cosmiccoinflip.listeners.InventoryListener;
import net.astropvp.core.cosmiccoinflip.listeners.PlayerListener;
import net.astropvp.core.cosmiccoinflip.struct.CoinFlipManager;
import net.astropvp.core.cosmiccoinflip.struct.CoinFlipMatch;
import net.astropvp.core.cosmicjackpot.struct.JackpotManager;
import net.astropvp.core.cosmictoggles.commands.CommandToggles;
import net.astropvp.core.cosmictoggles.manager.TogglesManager;
import net.astropvp.core.giveaway.GiveAwayCommand;
import net.astropvp.core.giveaway.GiveAwayManager;
import net.astropvp.core.items.*;
import net.astropvp.core.items.coder.EnchantmentCoderManager;
import net.astropvp.core.items.coder.HeroicEnchantmentCoderItem;
import net.astropvp.core.killtracker.commands.CommandGiveSoulGem;
import net.astropvp.core.killtracker.commands.CommandWithdrawSouls;
import net.astropvp.core.killtracker.listeners.SoulGemListener;
import net.astropvp.core.listeners.*;
import net.astropvp.core.managers.AstroPlayerManager;
import net.astropvp.core.struct.Gift;
import net.astropvp.core.tasks.LastManStandingCapTask;
import net.astropvp.core.titles.cmds.CommandTitle;
import net.astropvp.core.titles.cmds.CommandTitleSet;
import net.astropvp.core.titles.listeners.PermissionListener;
import net.astropvp.core.titles.listeners.PlayerJoinQuitListener;
import net.astropvp.core.titles.listeners.TitleInventoryListener;
import net.astropvp.core.titles.sql.SQLConnection;
import net.astropvp.core.titles.sql.SQLHandler;
import net.astropvp.core.titles.sql.TitleDatabaseManager;
import net.astropvp.core.typeadapters.GiftAdapterSerializer;
import net.astropvp.core.typeadapters.GiftsListAdapterSerializer;
import net.astropvp.core.utils.CleanseScroll;
import net.astropvp.core.utils.CooldownUtils;
import net.astropvp.core.utils.ItemUtils;
import net.astropvp.core.utils.cache.CacheManager;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.gson.Gson;
import net.astropvp.storage.gson.reflect.TypeToken;
import net.crankedpvp.staff.commands.FreezeCommand;
import net.crankedpvp.staff.commands.GhostMuteCommand;
import net.crankedpvp.staff.commands.StaffChatCommand;
import net.crankedpvp.staff.commands.StaffModeCommand;
import net.crankedpvp.staff.listeners.*;
import net.crankedpvp.staff.managers.FreezeManager;
import net.crankedpvp.staff.managers.GhostMuteManager;
import net.crankedpvp.staff.managers.StaffChatManager;
import net.crankedpvp.staff.managers.StaffManager;
import net.crankedpvp.staff.tasks.FreezeTeleportTask;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;

@Getter
@Setter
public class AstroCore extends JavaPlugin {
    private static AstroCore instance;
    private CacheManager cacheManager;
    private static Economy econ = null;
    private static Field bukkitCommand;
    private final String consolePrefix = "[AstroCore]";
    private final Type giftListType = new TypeToken<List<Gift>>(){}.getType();
    private Gson gson;
    private me.mattstudios.mf.base.CommandManager commandManager2;

    public static AstroCore getInstance() {
        return instance;
    }
    private CommandManager commandManager;

    //private StaffModeManager staffModeManager;
    //private StaffModulesRegister staffModulesRegister;
    public static String LORE_SUFFIX = ":  ";
    private File spongeDirectory;
    private HolyWhiteScrollListener holyWhiteScrollListener;
    private HolyWhitescrollItem holyWhitescrollItem;
    private JackpotManager jackpotManager;
    public static Economy economy;
    private AstroPlayerManager astroPlayerManager;
    Essentials ess;
    private PayToggle payToggles;
    ProtocolManager protocolManager;
    public static Map<UUID, Player> grindingMessages;
    public TitleDatabaseManager dataManager;
    public static boolean hcf;
    private SoulItemListener soulItemListener;
    private EnchantmentCoderManager enchantmentCoderManager;
    private CoinFlipManager coinFlipManager;
    private TogglesManager toggleManager = new TogglesManager();
    private GiveAllManager giveAllManager;
    private CooldownUtils cooldownUtils;
    @Getter
    @Setter
    //private Freeze freezeCommand;
    private AutoBroadcastTask autoBroadcastTask;

    public static String prefix = "&a&lStaff &8➥ ";

    @Getter
    private StaffManager staffManager;

    @Getter
    private FreezeManager freezeManager;

    @Getter
    private GhostMuteManager ghostMuteManager;

    private FreezeTeleportTask teleportTask;

    @Getter
    private StaffChatManager staffChatManager;

    @Getter
    public GiveAwayManager giveAwayManager;

    @SneakyThrows
    public void onEnable() {
        instance = this;
        this.gson = StoragePlugin.getInstance()
                .getGson()
                .newBuilder()
                //.registerTypeAdapter(Gift.class, new GiftAdapterDeserializer()).setLenient()
                .registerTypeAdapter(Gift.class, new GiftAdapterSerializer()).setLenient()
                .registerTypeAdapter(this.giftListType, new GiftsListAdapterSerializer()).setLenient()
                //.registerTypeAdapter(this.giftListType, new GiftsListAdapterDeserializer()).setLenient()
                .create();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        setupEconomy();
        events();
        this.commandManager2 = new me.mattstudios.mf.base.CommandManager(this);
        this.enchantmentCoderManager = new EnchantmentCoderManager();
        this.astroPlayerManager = new AstroPlayerManager();

        //this.staffModeManager = new StaffModeManager();
        //this.staffModulesRegister = new StaffModulesRegister(this);
        //this.staffModulesRegister.initialize();
        this.cacheManager = new CacheManager();
        grindingMessages = new HashMap<>();
        this.claimsOnInfernal = new HashMap<>();
        this.claimsOnDesert = new HashMap<>();
        this.claimsOnFrozen = new HashMap<>();
        this.giveAwayManager = new GiveAwayManager(this);

        this.staffChatManager = new StaffChatManager();



        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "arkunload lava_sponge");
        WorldGuardPlugin.inst().getConfig().set("simulation.sponge.enable", false);
        for (World w : Bukkit.getWorlds()) {
            WorldGuardPlugin.inst().getGlobalStateManager().get(w).simulateSponge = false;
        }
        this.ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        this.payToggles = new PayToggle("toggles.yml");

       /* this.jackpotManager = new JackpotManager();
        try {
            this.jackpotManager.loadFileData(this);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        this.setupSQL();
        this.dataManager = new TitleDatabaseManager();
        this.dataManager.cacheAllEquippedTitles();
        this.coinFlipManager = new CoinFlipManager();
        this.cooldownUtils = new CooldownUtils("astroCore");
        try {
            this.coinFlipManager.loadCoinFlips();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.coinFlipManager.loadToggles();
        if (!this.getConfig().getBoolean("resetStats")) {
            this.getConfig().set("resetStats", true);
            this.saveConfig();
            try {
                this.coinFlipManager.clearStats();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.toggleManager.loadToggles();
        try {
            Class.forName("org.arkhamnetwork.Arkkit.patches.combatlog.CombatLoggerDeathEvent");
            getServer().getPluginManager().registerEvents(new HolyWhiteScrollCombatLoggerListener(), this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        this.staffManager = new StaffManager(this);
        this.freezeManager = new FreezeManager(this);
        this.ghostMuteManager = new GhostMuteManager(this);

        me.mattstudios.mf.base.CommandManager commandManager = new me.mattstudios.mf.base.CommandManager(this);
        commandManager.register(new StaffModeCommand(this));
        commandManager.register(new FreezeCommand(this));
        commandManager.register(new GhostMuteCommand(this));
        commandManager.register(new StaffChatCommand(this));

        this.teleportTask = new FreezeTeleportTask(this);
        this.teleportTask.runTaskTimer(this, 80L, 20L);


        registerCommands();
        registerListeners();
        registerCustomItems();
        registerTasks();
        protocolFix();


        Bukkit.getConsoleSender().sendMessage(" ******************************************** ");
        Bukkit.getConsoleSender().sendMessage(" ASTRO CORE PLUGIN SHOULD BE LOADED NOW");
        Bukkit.getConsoleSender().sendMessage(" ******************************************** ");
        astroPlayerManager.loadAll();
        enchantmentCoderManager.loadAll();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, ()-> {
            System.out.println("[EnchantmentCoder] Started auto saving coder data");
            enchantmentCoderManager.purge();
            enchantmentCoderManager.saveAll();
            System.out.println("[EnchantmentCoder] Finished auto saving coder data");

            Bukkit.getLogger().info("[AstroCore] Started Saving astrocore playerdata");
            enchantmentCoderManager.purge();
            astroPlayerManager.saveAll();
            Bukkit.getLogger().info("[AstroCore] Finished Saving astrocore playerdata");

        },60L, 15*20*60);
    }

    public HolyWhiteScrollListener getHolyWhiteScrollListener() {
        return this.holyWhiteScrollListener;
    }

    @SneakyThrows
    public void onDisable() {
        this.staffManager.getPlayersInStaffMode().forEach((player, staffMode) -> staffMode.removePlayerFromStaffMode());

        //this.staffModeManager.toggleOffAllStaffModes();
        this.payToggles.saveToggles();
        /*try {
            this.jackpotManager.saveFileData();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        this.dataManager.saveAllEquippedTitlesToSQL();

        Bukkit.getLogger().info("[AstroCore] Saving astrocore playerdata");


        for (CoinFlipMatch active : this.coinFlipManager.getCoinFlipMatches()) {
            if (active.getMatchState() == CoinFlipMatch.MatchState.WAITING) {
                if (active.getStarterPlayer() == null) continue;
                economy.depositPlayer(active.getStarterPlayer(), active.getWager());
                Bukkit.getLogger().info("Depositing $" + active.getWager() + " to " + active.getStarterPlayer().getName() + " due to reload...");
                continue;
            }
            if (active.getMatchState() == CoinFlipMatch.MatchState.DONE) continue;
            if (active.getStarterPlayer() != null) {
                economy.depositPlayer(active.getStarterPlayer(), active.getWager());
                Bukkit.getLogger().info("Depositing $" + active.getWager() + " to " + active.getStarterPlayer().getName() + " due to reload...");
            }
            if (active.getSecondPlayer() == null) continue;
            economy.depositPlayer(active.getSecondPlayer(), active.getWager());
            Bukkit.getLogger().info("Depositing $" + active.getWager() + " to " + active.getSecondPlayer().getName() + " due to reload...");
        }
        this.coinFlipManager.getCoinFlipMatches().clear();
        try {
            this.coinFlipManager.saveCoinFlipRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.coinFlipManager.saveToggles();

        this.toggleManager.saveToggles();

        Set infernalClaims = this.claimsOnInfernal.entrySet();
        for (Object infernalClaim : infernalClaims) {
            Map.Entry me = (Map.Entry) infernalClaim;
            Faction faction = (Faction) me.getKey();
            getConfig().set("infernal-claims." + faction.getTag(), me.getValue());
        }

        Set frozenClaims = this.claimsOnFrozen.entrySet();
        for (Object frozenClaim : frozenClaims) {
            Map.Entry me = (Map.Entry) frozenClaim;
            Faction faction = (Faction) me.getKey();
            getConfig().set("frozen-claims." + faction.getTag(), me.getValue());
        }

        Set desertClaims = this.claimsOnDesert.entrySet();
        for (Object desertClaim : desertClaims) {
            Map.Entry me = (Map.Entry) desertClaim;
            Faction faction = (Faction) me.getKey();
            getConfig().set("desert-claims." + faction.getTag(), me.getValue());
        }

        saveConfig();
        enchantmentCoderManager.purge();
        astroPlayerManager.purge();
        enchantmentCoderManager.saveAll();
        astroPlayerManager.saveAll();

        instance = null;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void events() {
        PluginManager pm = getServer().getPluginManager();

    }

    public void showError(Player sender, Throwable exception, String commandLabel) {
        sender.sendMessage(I18n.tl("errorWithMessage", exception.getMessage()));
    }

    private void setupSQL() {
        SQLHandler.initializeSQL(AstroCore.getInstance());
        SQLConnection.executeSQLQuery("CREATE TABLE IF NOT EXISTS `titledata` (`UUID` VARCHAR(36) NOT NULL, `ActiveTitle` VARCHAR(255), primary key(UUID))", false);
        SQLConnection.executeSQLQuery("DELETE FROM `titledata` WHERE ActiveTitle='none'", false);
    }

    public String getConsolePrefix() {
        return consolePrefix;
    }

    public Economy getEcon() {
        return econ;
    }


    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return;
        econ = rsp.getProvider();
    }


    public File getSpongeDirectory() {
        return this.spongeDirectory;
    }

    public TogglesManager getToggleManager() {
        return toggleManager;
    }


    public CoinFlipManager getCoinFlipManager() {
        return coinFlipManager;
    }

    public JackpotManager getJackpotManager() {
        return jackpotManager;
    }

    public PayToggle getPayToggles() {
        return this.payToggles;
    }

    public void registerCommands() {
        getCommand("withdrawsouls").setExecutor(new CommandWithdrawSouls());
        getCommand("givesoulgem").setExecutor(new CommandGiveSoulGem());
        getCommand("title").setExecutor(new CommandTitle());
        getCommand("titleset").setExecutor(new CommandTitleSet());
        getCommand("forcegiveall").setExecutor(new CommandForceGiveAll());
        getCommand("nextgiveall").setExecutor(new CommandNextGiveAll());
        getCommand("giveenchantmentcrystal").setExecutor(new GiveEnchantmentCrystalCommand());
        getCommand("bless").setExecutor(new Bless());
        getCommand("dupe").setExecutor(new CommandDupe());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("discord").setExecutor(new DiscordCommand());
        getCommand("pots").setExecutor(new PotionsCommand(this));
        getCommand("giveall").setExecutor(new GiveallCommand());
        getCommand("givechunkbuster").setExecutor(new ChunkBuster(this));
        getCommand("trash").setExecutor(new TrashCommand());
        getCommand("lastmanstanding").setExecutor(new LastManStandingCommand());
        getCommand("togglepay").setExecutor(new CommandTogglePay());
        getCommand("toggles").setExecutor(new CommandToggles());
        getCommand("armorcolor").setExecutor(new ArmorColorCommand());
        getCommand("spawnershop").setExecutor(new SpawnerShopCommand());
        getCommand("givebucket").setExecutor(new CommandGiveMagicBucket());
        getServer().getPluginManager().registerEvents(new KillTitleSound(), this);
        getCommand("givesoulpearl").setExecutor(new SoulPearlCommand());
        getCommand("coinflip").setExecutor(new CommandCoinFlip());
        getCommand("list").setExecutor(new ListCommand());
        getCommand("grinding").setExecutor(new ToggleGrindingMessages());
        //getCommand("jackpot").setExecutor(new CommandJackpot());
        getCommand("chatcolor").setExecutor(new ChatColorCommand());
        getCommand("helpenchants").setExecutor(new CommandHelpEnchants());
        getCommand("randomtp").setExecutor(new RandomTpCommand());
        getCommand("giveaway").setExecutor(new GiveAwayCommand());
        getCommand("togglekilltitle").setExecutor(new ToggleKillTitleCommand());
        getCommand("togglefactionland").setExecutor(new ToggleFactionLandTitle());
        getCommand("togglepotsdrop").setExecutor(new TogglePotsDropCommand());
        getCommand("togglebossmessages").setExecutor(new ToggleBossMessagesCommand());

        //freezeCommand = new Freeze();

        //, new StaffChatCommand(), new StaffModeCommand(), new FreezeCommand()
        Arrays.asList(new GiveThrowableCreeperEgg(), new Broadcast()).forEach(this::registerCommand);

        this.commandManager = new PaperCommandManager(this);
        this.commandManager.registerCommand(new Invsee());
        this.commandManager.registerCommand(new ReloadCommand());
        commandManager2.register(new GiftCommand(this));
        commandManager2.register(new GiftsCommand());
        commandManager2.register(new GlintCommand());
        //commandManager2.register(new MoreCommand());
        //commandManager2.register(new HasBeenDupedCommand());

    }

    public void registerTasks() {
        new LastManStandingCapTask().runTaskTimer(this, 20L, 20L);
        //Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new AutoGiveAllTask(), 20L, 20L);
        /*this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            try {
                this.jackpotManager.onJackpotTick();
            } catch (UserDoesNotExistException | NoLoanPermittedException e) {
                e.printStackTrace();
            }
        }, 20L, 20L);*/

        Collection<? extends Player> allPlayers = Bukkit.getOnlinePlayers();
        /*
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(getInstance(), () -> {
            for (Player p : allPlayers) {
                CosmicPlayer cosmicPlayer = CosmicPlayers.get().getCosmicPlayer(p);
                if (cosmicPlayer == null) continue;
                if (!cosmicPlayer.isOnCosmicClient() && !p.isOp()) {
                    p.sendMessage("");
                    p.sendMessage(CC.translate("&c&lHey &n" + p.getName() + "&c&l, &cwe have noticed that you are not on the official version of &nCosmicClient&c."));
                    p.sendMessage(CC.translate("&7If you wish to download it, follow the steps in the &f#cc-features&7 channel in our &f/discord&7."));
                    p.sendMessage("");
                }
            }
        }, 50L, 7200L);

         */



        autoBroadcastTask = new AutoBroadcastTask(this);
        autoBroadcastTask.setupMessages();
        autoBroadcastTask.runTaskTimerAsynchronously(this, 20L, 90*20);

        Bukkit.getScheduler().runTaskLater(this, () ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "arkload vault_gui"), 100L);
        Bukkit.getScheduler().runTaskLater(this, () ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "arkload death_messages"), 100L);
        Bukkit.getScheduler().runTaskLater(this, () ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "blockcommand remove ver"), 100L);

        Bukkit.getScheduler().runTaskLater(this, () ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "blockcommand remove alts"), 100L);
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new WarzoneClaimListener(), this);
        //getServer().getPluginManager().registerEvents(new StaffModeListeners(this), this);
        //getServer().getPluginManager().registerEvents(new EnderpearlFixes(), this);
        getServer().getPluginManager().registerEvents(new DamageFixes(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new MagicalLavaBucket(), this);
        getServer().getPluginManager().registerEvents(new MagicalWaterBucket(), this);
        //getServer().getPluginManager().registerEvents(new StaffListeners(), this);
        getServer().getPluginManager().registerEvents(new SoulGemListener(), this);
        getServer().getPluginManager().registerEvents(new ApplyEnchantCrystalListener(), this);
        getServer().getPluginManager().registerEvents(new TitleInventoryListener(this.dataManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this.dataManager), this);
        getServer().getPluginManager().registerEvents(new PermissionListener(this.dataManager), this);
        getServer().getPluginManager().registerEvents(new DungeonInventoryEvent(), this);
        getServer().getPluginManager().registerEvents(new ServerFixes(), this);
        getServer().getPluginManager().registerEvents(new MOTD(), this);
        getServer().getPluginManager().registerEvents(new CheckCreativeDupeListener(), this);
        getServer().getPluginManager().registerEvents(new ChunkBusterListener(), this);
        getServer().getPluginManager().registerEvents(new OmniGemFix(), this);
        getServer().getPluginManager().registerEvents(new CommandAliases(), this);
        getServer().getPluginManager().registerEvents(new FirstJoin(), this);
        getServer().getPluginManager().registerEvents(new CombatLogBlock(), this);
        getServer().getPluginManager().registerEvents(new LastManStandingListener(), this);
        getServer().getPluginManager().registerEvents(new PayListener(), this);
        //getServer().getPluginManager().registerEvents(new Freeze(), this);
        getServer().getPluginManager().registerEvents(new ArmorColorClickListener(), this);
        getServer().getPluginManager().registerEvents(new BlockCommand(), this);
        getServer().getPluginManager().registerEvents(new SoulItemListener(), this);
        getServer().getPluginManager().registerEvents(new HolyWhiteScrollListener(), this);
        getServer().getPluginManager().registerEvents(new ThrowableCreeperEggs(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentCoderInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentCoderItemListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new WarpCommand(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new WorldBorderPearlFix(), this);
        //getServer().getPluginManager().registerEvents(new JackpotListener(), this);
        getServer().getPluginManager().registerEvents(new GrindingMessagesToggleListener(), this);
        getServer().getPluginManager().registerEvents(new DupePatch(), this);
        getServer().getPluginManager().registerEvents(new KnockbackFixListener(), this);

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new VanishToggleListener(this), this);
        pluginManager.registerEvents(new RandomTeleportListener(this), this);
        pluginManager.registerEvents(new net.crankedpvp.staff.listeners.PlayerListener(this), this);
        pluginManager.registerEvents(new PlayerKickListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new FreezeListener(this), this);
        pluginManager.registerEvents(new StaffJoinListener(this), this);
        pluginManager.registerEvents(new RemoveKnockbackEnchantListener(), this);

    }

    public void registerCustomItems() {
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new HeroicUpgrade());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new RogueScratchCard());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new ApolloRank());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new NebulaRank());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new GalaxyRank());

        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new RevokeScratchCard());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new SlotbotScratchCard());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new HolyWhitescrollItem());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new HolyWaterItem());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new CleanseScroll());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new HeroicEnchantmentCoderItem());
        this.holyWhitescrollItem = new HolyWhitescrollItem();
    }

    public void protocolFix() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {

            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    if (event.getPlayer().isOp()) return;

                    try {
                        PacketContainer packet = event.getPacket();
                        String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();
                        if (message.startsWith("/?") && !message.contains("  ") || message.startsWith("/") && !message.contains(" ")
                                || message.startsWith("/help") && !message.contains("  ") || message.startsWith("/ver") && !message.contains("  ")
                                || message.startsWith("/bukkit") && !message.contains("  ")) {
                            event.setCancelled(true);
                        }
                    } catch (FieldAccessException e) {
                        AstroCore.this.getLogger().log(Level.SEVERE, "bruh", e);
                    }
                }
            }
        });
    }

    private void registerCommand(Command cmd) {
        this.registerCommand(cmd, this.getName());
    }

    public void registerCommand(Command cmd, String fallback) {
        try {
            if (bukkitCommand == null) {
                bukkitCommand = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommand.setAccessible(true);
            }
            CommandMap commandMap = (CommandMap) bukkitCommand.get(Bukkit.getServer());
            commandMap.register(cmd.getName(), fallback, cmd);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private HashMap<Faction, Integer> claimsOnInfernal;

    public int getClaimsOnInfernal(Faction faction) {
        if (!this.claimsOnInfernal.containsKey(faction)) {
            if (this.getConfig().contains("infernal-claims." + faction.getTag())) {
                this.claimsOnInfernal.put(faction, getConfig().getInt("infernal-claims." + faction));
            }
        }
        return this.claimsOnInfernal.get(faction);
    }

    public void setClaimsOnInfernal(Faction faction, int i) {
        this.claimsOnInfernal.put(faction, i);
    }

    private HashMap<Faction, Integer> claimsOnFrozen;

    public int getClaimsOnFrozen(Faction faction) {
        if (!this.claimsOnFrozen.containsKey(faction)) {
            if (this.getConfig().contains("frozen-claims." + faction.getTag())) {
                this.claimsOnFrozen.put(faction, getConfig().getInt("frozen-claims." + faction));
            }
        }
        return this.claimsOnFrozen.get(faction);
    }

    public void setClaimsOnFrozen(Faction faction, int i) {
        this.claimsOnFrozen.put(faction, i);
    }

    private HashMap<Faction, Integer> claimsOnDesert;

    public int getClaimsOnDesert(Faction faction) {
        if (!this.claimsOnDesert.containsKey(faction)) {
            if (this.getConfig().contains("desert-claims." + faction.getTag())) {
                this.claimsOnDesert.put(faction, getConfig().getInt("desert-claims." + faction));
            }
        }
        return this.claimsOnDesert.get(faction);
    }

    public void setClaimsOnDesert(Faction faction, int i) {
        this.claimsOnDesert.put(faction, i);
    }

    public HolyWhitescrollItem getHolyWhitescrollItem() {
        return this.holyWhitescrollItem;
    }

    public static ItemStack getSoulPearl() {
        return ItemUtils.addGlow(ItemUtils.buildItem(Material.ENDER_PEARL, (short) 0, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Soul Pearl", ChatColor.LIGHT_PURPLE + "A rare /dungeon trinket that", ChatColor.LIGHT_PURPLE + "functions like a normal enderpearl,", ChatColor.LIGHT_PURPLE + "but costs souls instead of being,", ChatColor.LIGHT_PURPLE + "consumed on use.", "", ChatColor.DARK_PURPLE + "" + ChatColor.UNDERLINE + "Soul Cost:" + ChatColor.LIGHT_PURPLE + " 5/pearl"));
    }

    public static ItemStack getHolyWhiteScroll() {
        return AstroCore.getInstance().getHolyWhitescrollItem().createItem(null);
    }

    @Deprecated
    public static ItemStack getOldHolyWhiteScroll() {
        return ItemUtils.addGlow(ItemUtils.buildItem(Material.PAPER, (short) 0, ChatColor.GOLD + "" + ChatColor.BOLD + "Holy White Scroll", ChatColor.YELLOW + "A legendary /dungeon reward that", ChatColor.YELLOW + "can be applied to armor/weapons,", ChatColor.YELLOW + "gives a 100% chance of not losing", ChatColor.YELLOW + "the blessed item on death.", "", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "REQ:" + ChatColor.YELLOW + " White Scroll"));
    }


}

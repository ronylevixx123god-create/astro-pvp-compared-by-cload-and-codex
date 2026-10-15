package org.arkhamnetwork.Arkkit.plugins;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.PlayerVaultGUI;
import org.arkhamnetwork.Arkkit.patches.*;
import org.arkhamnetwork.Arkkit.patches.chat_filter.ChatFilter;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.arkhamnetwork.Arkkit.patches.createtitle.CreateTitle;
import org.arkhamnetwork.Arkkit.patches.trading.BetterTrade;
import org.bukkit.plugin.Plugin;

public enum ArkPlugins {
    ANTI_DUPE(new AntiDupe(Arkkit.get()), "Blocks many common dupe exploits, adds /scanchests"),
    BOAT_BLOCKER(new BoatBlocker(Arkkit.get()), "Prevents placement of boats due to possible dupe exploits."),
    POTION_STACK_LIMITER(new PotionStackLimiter(Arkkit.get()), "Prevents users from using potions with a stack size > 1."),
    ASYNC_SPIGOT(new AsyncSpigot(Arkkit.get()), "Disables spigot's built in async method catcher."),
    TILE_ENTITY_CLEANER(new TileEntityCleaner(Arkkit.get()), "Removes empty tile entities (chests) as chunks load to decrease CPU tick iterations."),
    BULK_SAVING(new BulkSaving(Arkkit.get()), "Disables all incremental I/O saving, only saves on server shutdown, costs RAM, saves (massive) CPU."),
    NO_NATURAL_MOBS(new NoNaturalMobs(Arkkit.get()), "Excluse natural mob spawning from server tick(), saves CPU."),
    FILL_COMMAND(new FillCommand(Arkkit.get()), "Enables /fill which automatically fills dispensers with TNT from inventory."),
    COMMAND_BLOCKER(new CommandBlocker(Arkkit.get()), "Allows for dynamic command blocking / unblocking."),
    ESSENTIALS_WARP_ALIASES(new EssentialsWarpAliases(Arkkit.get()), "Enables per-warp /aliase net.astropvp.armorsets.commands. So /warp test will become /test, useful in prison server configurations."),
    CHAT_LIMITER(new ChatLimiter(Arkkit.get()), "Enables a chat limiter with configurable delays between chat messages, and a custom VIP delay and permission node."),
    ITEM_LINKING(new ItemLinking(Arkkit.get()), "Enables /senditem command and ability to link items in chat by typing [ITEM]. Requires permission node, itemlinking.senditem."),
    PLUGIN_HIDER(new PluginHider(Arkkit.get()), "Prevents players from obtaining the server's plugin list through means such as /p l /plugin /? /help:, etc."),
    NO_NEW_CHUNKS(new NoNewChunks(Arkkit.get()), "Prevents new chunks from generating in worlds specified in no_new_chunks.worlds section of configuration."),
    CHAT_FILTER(new ChatFilter(Arkkit.get()), "Enables a dynamic word filtering system via /censor."),
    FORCE_SPAWN(new ForceSpawn(Arkkit.get()), "Enables /forcespawn <radius>, sending all players around executor to world's /spawn. Great for moderation."),
    CREATE_TITLE(new CreateTitle(Arkkit.get()), "Enables /createtitle and /addcreatetitle net.astropvp.armorsets.commands to purchase and create custom player titles."),
    COMBAT_LOG(new CombatLog(Arkkit.get()), "Kills players who logout while within 10s of combat in pvp-enabled area."),
    NO_COMBAT_FLYING(new NoCombatFlying(Arkkit.get()), "Clips wings of players who engage in PvP or PvE combat for 10s."),
    PVP_POTION_EFFECTS(new PvPPotionEffects(Arkkit.get()), "Clear potion effects whenever a player goes between a pvp and non-pvp WG flagged area."),
    NO_CURSOR_DROP(new NoCursorDrop(Arkkit.get()), "Prevents users from losing item being held on cursor on inventory close."),
    NO_ENCHANTMENT_TABLE_BOOKS(new NoEnchantmentTableBooks(Arkkit.get()), "Prevents users from enchanting books on enchantment tables."),
    BETTER_TRADE(new BetterTrade(Arkkit.get()), "A much safer trade system made in 2018 using OOP."),
    ENCHANTMENT_ANVIL_FIX(new EnchantmentAnvilFix(Arkkit.get()), "Disables EnchantmentAPI anvil and enchantment table mechanics."),
    SMARTER_ESSENTIALS(new SmarterEssentials(Arkkit.get()), "Prevents /tpaccept exploitation into enemy faction bases."),
    CHUNK_LOAD_FLUSH(new ChunkLoadFlush(Arkkit.get()), "Removes entities in chunks when they are loaded."),
    ANTI_TILE_ENTITY_LAG(new AntiTileEntityLag(Arkkit.get()), "Scans and removes excessive tile entities."),
    AUTO_CRAFT_TNT(new AutoCraftTNT(Arkkit.get()), "Enables /tnt - Automatically crafts all tnt ingredients in inventory."),
    WILDERNESS_COMMAND(new WildernessCommand(Arkkit.get()), "Enables /wilderness - Teleports caster to random location in Faction wilderness."),
    EXP_BOTTLES(new ExpBottles(Arkkit.get()), "Enables ability to use /xpbottle to withdraw vanilla levels into exp bottles."),
    POTION_FIX(new PotionFix(Arkkit.get()), "Nerfs vanilla potion behavior to prevent crazy OP effects like STR III."),
    ANTI_LOOT_STEAL(new AntiLootSteal(Arkkit.get()), "Loot can only be picked up by the killer of the entity for a few seconds."),
    HOTBAR_ARMOR_SWAP(new HotbarArmorSwap(Arkkit.get()), "Enables right-click-to-equip mechanic for armor."),
    ESSENTIALS_KIT_VIEWER(new EssentialsKitViewer(Arkkit.get()), "Enables GUI for essential's /kit"),
    ESSENTIALS_ADD_HOMES(new EssentialsAddHomes(Arkkit.get()), "Enables /addhomes to add extra essentials /homes to players."),
    ITEM_ID(new ItemID(Arkkit.get()), "Enables Item ID tracking."),
    GOLDEN_APPLE_SICKNESS(new GoldenAppleSickness(Arkkit.get()), "Enables golden apple sickness."),
    CLEAR_INVENTORY_CONFIRM(new ClearInventoryConfirm(Arkkit.get()), "Adds confirmation support to /clearinventory net.astropvp.armorsets.commands."),
    //BALANCE_TOP(new BalanceTop(Arkkit.get()), "Enables compilex optimized /baltop."),
    ANTI_LAG(new AntiLag(Arkkit.get()), "Automatically reboots servers with >9H uptime and <17TPS"),
    SAFE_SHUTDOWN(new SafeShutdown(Arkkit.get()), "Safely unloads all loaded chunks on shutdown."),
    LAVA_SPONGE(new LavaSponge(Arkkit.get()), "Adds capability for sponges to absorb lava."),
    ITEM_NAMETAG(new ItemNametag(Arkkit.get()), "Enables item nametags to rename items."),
    TP_WARMUP(new TPWarmup(Arkkit.get()), "Enables TP warmup times based on player EXP levels."),
    EXP_ORB(new ExpOrbs(Arkkit.get()), "Convert xp into EXP orbs with /xporb"),
    BALANCE(new Balance(Arkkit.get()), "Custom /balance command."),
    RAID_SHOP(new RaidShop(Arkkit.get()), "Enables /raidshop"),
    TW_AUTO_CRAFT_TNT(new TWAutoCraftTNT(Arkkit.get()), "Enables TreasureWars /tnt"),
    HIT_ME(new HitMe(Arkkit.get()), "Enables /hitme command to deal self inflicted damage."),
    BUMP(new Bump(Arkkit.get()), "Enables /bump command to deal self inflicted damage in friendly areas."),
    ENDERPEARL_COOLDOWN(new EnderpearlCooldown(Arkkit.get()), "Enables enderpearl cooldowns."),
    NO_VEHICLE_TELEPORT(new NoVehicleTeleport(Arkkit.get()), "Disables teleportation events while in a vehicle."),
    ANTI_MINECART(new AntiMinecart(Arkkit.get()), "Prevents known minecart dupes."),
    NO_SAFEZONE_MOBS(new NoSafezoneMobs(Arkkit.get()), "Automatically despawns monsters in pvp disabled regions when hit."),
    HOME_GUI(new HomeGUI(Arkkit.get()), "Pretty looking net.astropvp.armorsets.gui for /home that adds customizable icons."),
    VAULT_GUI(new PlayerVaultGUI(Arkkit.get()), "GUI for /pv"),
    ANTI_BLOCK_GLITCH(new AntiBlockGlitch(Arkkit.get()), "Fixes block glitching and jumping."),
    NEAR_LEGACY(new NearLegacy(Arkkit.get()), "Custom near command."),

    VAULT_DISABLE(new VaultDisable(Arkkit.get()), "Disable adding items into PlayerVaults, (Only withdrawls allowed)"),
    GAMBLE(new Gamble(Arkkit.get()), "Enables players net.astropvp.gambling net.astropvp.armorsets.commands such as /roll."),
    //HIDE_LORE(new HideLore(Arkkit.get()), "Hides specific lore and item attributes via ProtocolLib."),
    SHOWCASE_EXPANDER(new ShowcaseExpander(Arkkit.get()), "Enables showcase expander items."),
    ANTI_EXPLOIT(new AntiExploit(Arkkit.get()), "Prevents a number of general server exploits."),
    SUICIDE(new Suicide(Arkkit.get()), "Enables /suicide command for players to use."),
    MCMMO_CREDITS(new McmmoCredits((Plugin)Arkkit.get()), "Enables ability to claim mcmmo credit vouchers."),
    REDEEM_MCMMO(new RedeemMCMMO((Plugin)Arkkit.get()), "Enables /redeem mcmmo functionality."),
    NO_MCMMO_LIMITS(new NoMCMMOLimits((Plugin)Arkkit.get()), "Disables mcMMO level caps without allow them to become more powerful past presets."),
    WITHDRAW_MCMMO(new WithdrawMCMMO((Plugin)Arkkit.get()), "Enables /withdrawmcmmo ability to withdraw raw mcmmo xp."),
    DEATH_MESSAGES(new DeathMessages(Arkkit.get()), "Enables nice pvp death messages that display a JSON value of the item used."),
    LORE_TAG(new LoreTag(Arkkit.get()), "Enables the lore tag item to add a custom line of lore text to an item"),
    COLOREDLORE_TAG(new ColoredLoreTag(Arkkit.get()), "Enables the colored lore tag item to add a custom line of lore text to an item"),

    BLOCK_VALUE(new BlockValue(Arkkit.get()), "Enables tracking of custom block values in claims."),
    EQUIPMENT_LOOTBOX(new EquipmentLootboxes(Arkkit.get()), "Equipement lootboxes that give a random Armor Set item."),
    NO_DUPE_EXPLOIT(new NoDupeExploit(Arkkit.get()), "Scans inventories for items with duplicated IID and removes them."),
    NO_SPAWN_HOMES(new NoSpawnHomes(Arkkit.get()), "Blocks players from setting homes or warps at /spawn."),
    FAT_BUCKETS(new FatBuckets(Arkkit.get()), "Enables Fat Buckets");
    private ArkPlugin ap = null;
    private String desc = null;

    ArkPlugins(ArkPlugin ap, String desc) {
        this.ap = ap;
        this.desc = desc;
    }

    public static ArkPlugins reflectArkPlugin(ArkPlugin ap) {
        for (ArkPlugins aps : ArkPlugins.values()) {
            if (!aps.getArkPlugin().getClass().getSimpleName().equals(ap.getClass().getSimpleName())) continue;
            return aps;
        }
        return null;
    }

    public static String getPatchList() {
        String s = "";
        for (ArkPlugins aps : ArkPlugins.values()) {
            s = s + aps.getConfigName() + ", ";
        }
        return s;
    }

    public static String getUnloadedPatches() {
        String s = "";
        for (ArkPlugins aps : ArkPlugins.values()) {
            if (aps.getArkPlugin().isRunning()) continue;
            s = s + aps.getConfigName() + ", ";
        }
        return s;
    }

    public static String getLoadedPatches() {
        String s = "";
        for (ArkPlugins aps : ArkPlugins.values()) {
            if (!aps.getArkPlugin().isRunning()) continue;
            s = s + aps.getConfigName() + ", ";
        }
        return s;
    }

    public ArkPlugin getArkPlugin() {
        return this.ap;
    }

    public String getDescription() {
        return this.desc;
    }

    public String getConfigName() {
        return this.name().toLowerCase();
    }
}


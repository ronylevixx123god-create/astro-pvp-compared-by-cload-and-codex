package me.ifamasssxd.cosmicconquest.struct;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Factions;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicconquest.CosmicConquests;
import me.ifamasssxd.cosmicconquest.listeners.ConquestListener;
import me.ifamasssxd.cosmicconquest.utils.TimeUtils;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ConquestManager
{
    private final int xCenter;
    private final int zCenter;
    private final int maxRange;
    private final int minRange;
    private final List<Material> disabledBlocks;
    private HashSet<String> claimedChunkCoords;
    private ConquerData lastConquer;
    private final Set<Entity> bosses;
    private final Map<Location, ChestData> conquestChests;
    private final Map<Location, List<ItemStack>> loot;
    private Location nextSpawnLocation;
    private ItemStack chickenHead;
    private int minCrystals;
    private int maxCrystals;
    private final Map<Location, List<FLocation>> warzoned;
    private final Set<FLocation> touched;
    private Location lastLocation;
    private double totalTime;
    private int totalAmount;
    
    public ConquestManager() {
        this.xCenter = 0;
        this.zCenter = 0;
        this.maxRange = 4500;
        this.minRange = 300;
        this.disabledBlocks = Lists.newArrayList(new Material[] { Material.BEDROCK });
        this.lastConquer = new ConquerData(System.currentTimeMillis(), null, null);
        this.bosses = new HashSet<Entity>();
        this.conquestChests = new ConcurrentHashMap<Location, ChestData>();
        this.loot = new HashMap<Location, List<ItemStack>>();
        this.minCrystals = 4;
        this.maxCrystals = 8;
        this.warzoned = new ConcurrentHashMap<Location, List<FLocation>>();
        this.touched = new HashSet<FLocation>();
        this.totalTime = 0.0;
        this.totalAmount = 0;
    }
    
    public void loadData() {
        this.claimedChunkCoords = this.getClaimedChunkCoords();
        for (final String materialName : CosmicConquests.get().getConfig().getStringList("disabled-blocks")) {
            if (Material.getMaterial(materialName) != null) {
                Bukkit.getLogger().info("[(EasterEvent)] Adding " + materialName + " to disabledBlocks!");
                this.disabledBlocks.add(Material.valueOf(materialName));
            }
        }
        this.minCrystals = CosmicConquests.get().getConfig().getInt("minCrystals");
        this.maxCrystals = CosmicConquests.get().getConfig().getInt("maxCrystals");
        if (CosmicConquests.get().getConfig().getBoolean("unclaimed")) {
            this.startTask(3);
        }
        else {
            this.startTask(this.getTaskDelay());
        }
        /*Bukkit.getScheduler().scheduleAsyncRepeatingTask(CosmicConquests.get(), () -> this.conquestChests.keySet().forEach(loc -> {
            try {
                de.inventivegames.particle.ParticleEffect.PORTAL.sendToPlayers(Bukkit.getOnlinePlayers(), loc.clone().add(0.5, 0.0, 0.5), 0.85f, 0.5f, 0.85f, 0.1f, 30);
                de.inventivegames.particle.ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), loc.clone().add(0.5, 0.0, 0.5), 0.85f, 0.5f, 0.85f, 0.1f, 30);
                de.inventivegames.particle.ParticleEffect.HAPPY_VILLAGER.sendToPlayers(Bukkit.getOnlinePlayers(), loc.clone().add(0.5, 0.0, 0.5), 0.85f, 0.5f, 0.85f, 0.1f, 30);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }), 100L, 100L);*/
        new BukkitRunnable() {
            public void run() {
                for (final Map.Entry<Location, ChestData> loc : ConquestManager.this.conquestChests.entrySet()) {
                    if (loc.getValue().getHits() <= 1) {
                        continue;
                    }
                    final Block block = loc.getKey().getBlock();
                    if (block.getType() == Material.CHEST) {
                        continue;
                    }
                    block.setType(Material.CHEST);
                    final Chest chest = (Chest)block.getState();
                    chest.getInventory().addItem(new ItemStack(Material.DIRT));
                }
            }
        }.runTaskTimer(CosmicConquests.get(), 20L, 100L);
        this.nextSpawnLocation = this.getRandomWildernessLocation(Bukkit.getWorlds().get(0), 10000);
        if (this.nextSpawnLocation != null) {
            this.claimNearbyWarzone(this.nextSpawnLocation);
        }
        this.chickenHead = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta im = (SkullMeta)this.chickenHead.getItemMeta();
        im.setOwner("MHF_Chicken");
        this.chickenHead.setItemMeta(im);
    }
    
    public void unclaimWarzone(final Location center) {
        Bukkit.getLogger().info("Unclaiming Warzone...");
        if (!this.warzoned.isEmpty()) {
            if (center == null) {
                this.warzoned.forEach((loc, list) -> this.unclaimWarzone(loc));
                return;
            }
            final List<FLocation> locs = this.warzoned.get(center);
            if (locs != null) {
                locs.forEach(fLocation -> {
                    if (FactionUtils.getFactionAt(fLocation).isWarZone()) {
                        Bukkit.getLogger().info("Unclaiming WarZone at " + fLocation.toString());
                        Board.getInstance().setFactionAt(Factions.getInstance().getNone(), fLocation);
                    }
                    return;
                });
                this.warzoned.remove(center);
            }
            else {
                Bukkit.getLogger().info("No warzone chunks found for " + center + "!");
                this.warzoned.keySet().forEach(loc -> Bukkit.getLogger().info("Key: " + loc));
            }
        }
    }
    
    private void claimNearbyWarzone(final Location location) {
        final Chunk chunk = location.getChunk();
        final int radius = 1;
        final List<FLocation> claims = Lists.newArrayList();
        for (int x = -radius; x < radius + 1; ++x) {
            for (int z = -radius; z < radius + 1; ++z) {
                final FLocation flocation = new FLocation(this.nextSpawnLocation.getWorld().getName(), chunk.getX() + x, chunk.getZ() + z);
                if (FactionUtils.getFactionAt(flocation).isNone()) {
                    Board.getInstance().setFactionAt(Factions.getInstance().getWarZone(), flocation);
                    claims.add(flocation);
                    this.touched.add(flocation);
                    Bukkit.getLogger().info("Claiming chunk at X: " + flocation.getX() + " Z: " + flocation.getZ() + " to warzone..");
                }
            }
        }
        if (claims.size() > 0) {
            this.warzoned.put(location.getBlock().getLocation(), claims);
        }
    }
    
    public void cleanup(final Location clean) {
        this.conquestChests.forEach((chest, timer) -> {
            if (chest.getBlock().getType() == Material.CHEST) {
                Chest ch = (Chest)chest.getBlock().getState();
                ch.getInventory().clear();
                ch.update();
                chest.getBlock().setType(Material.AIR);
            }
            this.unclaimWarzone(chest);
            return;
        });
        this.unclaimWarzone(clean);
        if (clean == null) {
            for (final FLocation loc : this.touched) {
                if (FactionUtils.getFactionAt(loc).isWarZone()) {
                    Board.getInstance().setFactionAt(Factions.getInstance().getNone(), loc);
                    Bukkit.getLogger().info("Unclaiming untracked WarZone chunk at " + loc.toString());
                }
            }
        }
        for (final Entity boss : this.bosses) {
            if (boss != null && boss.isValid()) {
                boss.remove();
            }
        }
        this.bosses.clear();
        this.loot.clear();
        this.conquestChests.clear();
    }
    
    private int getTaskDelay() {
        return (int)(ThreadLocalRandom.current().nextInt(3600) + 9000.0);
    }
    
    public void setMinMaxCrystals(final int min, final int max) {
        this.minCrystals = min;
        this.maxCrystals = max;
        CosmicConquests.get().getConfig().set("minCrystals", min);
        CosmicConquests.get().getConfig().set("maxCrystals", max);
        CosmicConquests.get().saveConfig();
    }
    
    public Location spawnConquestChest() {
        this.cleanup(this.lastLocation);
        Location wildernessLocation = this.nextSpawnLocation;
        if (wildernessLocation == null) {
            wildernessLocation = (this.nextSpawnLocation = this.getRandomWildernessLocation(Bukkit.getWorlds().get(0), 10000));
            if (this.nextSpawnLocation == null) {
                return null;
            }
        }
        if (!wildernessLocation.getChunk().isLoaded()) {
            wildernessLocation.getChunk().load();
        }
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l** &e&lASTRO CONQUEST &f&l**"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7A &eConquest Chest&7 has spawned at &f&l" + wildernessLocation.getBlockX() + "&fx &f&l" + wildernessLocation.getBlockY() + "&fy &f&l" + wildernessLocation.getBlockZ() + "&fz"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l* &e&lLOOT: &fConquest Lootbox"));
        Bukkit.broadcastMessage("");
        this.populateChests(wildernessLocation);
        CosmicConquests.get().getConfig().set("unclaimed", true);
        CosmicConquests.get().saveConfig();
        this.lastLocation = wildernessLocation.clone();
        this.lastConquer = new ConquerData(System.currentTimeMillis(), this.lastLocation, null);
        this.claimNearbyWarzone(this.nextSpawnLocation = this.getRandomWildernessLocation(Bukkit.getWorlds().get(0), 10000));
        return wildernessLocation;
    }
    
    public String getFormattedLocation(final ChatColor color, final Location location) {
        return color + ChatColor.BOLD.toString() + location.getBlockX() + color + "x " + color + ChatColor.BOLD + location.getBlockY() + color + "y " + color + ChatColor.BOLD + location.getBlockZ() + color + "z";
    }
    
    public void startTask(final int delay) {
        this.totalTime += delay;
        ++this.totalAmount;
        new BukkitRunnable() {
            int ticked = 0;
            
            public void run() {
                if (this.ticked >= delay) {
                    this.cancel();
                    ConquestManager.this.spawnConquestChest();
                    ConquestManager.this.startTask(ConquestManager.this.getTaskDelay());
                    return;
                }
                ++this.ticked;
                final int ticksLeft = delay - this.ticked;
                if (ticksLeft % 60 == 0 && ticksLeft > 1 && ticksLeft <= 300) {
                    Bukkit.broadcastMessage("");
                    final int minutes = ticksLeft / 60;
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l** &e&lASTRO CONQUEST &f&l**"));
                    Bukkit.broadcastMessage(ChatColor.GRAY + "A Conquest Chest will spawn in " + ChatColor.WHITE + ChatColor.UNDERLINE + minutes + " Minute" + ((minutes > 1) ? "s" : "") + ChatColor.GRAY + " at ");
                    Bukkit.broadcastMessage(ConquestManager.this.getFormattedLocation(ChatColor.WHITE, ConquestManager.this.nextSpawnLocation) + ChatColor.WHITE + ChatColor.BOLD + "!");
                    Bukkit.broadcastMessage("");
                }
                else if (ticksLeft > 1 && ticksLeft % 300 == 0 && !ConquestManager.this.conquestChests.isEmpty()) {
                    final Optional<Location> loc = ConquestManager.this.conquestChests.keySet().stream().findFirst();
                    if (loc != null && loc.isPresent()) {
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l** &e&lASTRO CONQUEST &f&l**"));
                        Bukkit.broadcastMessage(ChatColor.GRAY + "A Conquest Chest spawned at " + ConquestManager.this.getFormattedLocation(ChatColor.WHITE, loc.get()) + ChatColor.GRAY + "!");
                        Bukkit.broadcastMessage(ChatColor.WHITE.toString() + ChatColor.UNDERLINE + TimeUtils.formatDifference(this.ticked).trim() + ChatColor.GRAY + " ago!");
                        Bukkit.broadcastMessage("");
                    }
                }
            }
        }.runTaskTimer(CosmicConquests.get(), 20L, 20L);
        CosmicConquests.get().getConfig().set("average", this.totalTime / this.totalAmount);
        CosmicConquests.get().saveConfig();
    }
    
    public Location getRandomWildernessLocation(final World world, final int maxAttempts) {
        final Random r = new Random();
        final long start = System.currentTimeMillis();
        for (int counter = 0; counter < maxAttempts; ++counter) {
            final int xRange = this.minRange + r.nextInt(this.maxRange - this.minRange);
            final int zRange = this.minRange + r.nextInt(this.maxRange - this.minRange);
            int x = this.xCenter - xRange;
            int z = this.zCenter - zRange;
            if (ThreadLocalRandom.current().nextBoolean()) {
                x = Math.abs(x);
            }
            if (ThreadLocalRandom.current().nextBoolean()) {
                z = Math.abs(z);
            }
            if (!this.claimedChunkCoords.contains(x / 16 + "," + z / 16)) {
                final int chunkX = x / 16;
                final int chunkZ = z / 16;
                if (this.isOnlyWildernessNearby(world, chunkX, chunkZ, 1)) {
                    final int y = world.getHighestBlockYAt(x, z);
                    final Block block = world.getBlockAt(x, y - 1, z);
                    if (!this.disabledBlocks.contains(block.getType())) {
                        if (!this.disabledBlocks.contains(block.getLocation().add(0.0, 1.0, 0.0).getBlock().getType())) {
                            System.out.println("Returning chunk: " + x + " Y: " + y + " Z: " + z + " in " + counter + " attempts in " + (System.currentTimeMillis() - start) + " ms");
                            return new Location(world, x, y, z);
                        }
                    }
                }
            }
        }
        if (maxAttempts > 0) {
            Bukkit.getLogger().info("Unable to find wilderness location to spawn easter chest, tried " + maxAttempts + " times in " + (System.currentTimeMillis() - start) + " ms");
        }
        return null;
    }
    
    public boolean isOnlyWildernessNearby(final World world, final int chunkX, final int chunkZ, final int radius) {
        for (int x = -radius; x < radius + 1; ++x) {
            for (int z = -radius; z < radius + 1; ++z) {
                final FLocation flocation = new FLocation(world.getName(), chunkX + x, chunkZ + z);
                if (!FactionUtils.getFactionAt(flocation).isNone()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void addItem(final List<ItemStack> loot, final ItemBuilder itemBuilder, final int chance) {
        if (ThreadLocalRandom.current().nextInt(100) > chance) {
            return;
        }
        final ItemStack item = itemBuilder.build();
        if (item != null) {
            loot.add(item);
        }
    }
    public void populateChests(final Location location) {
        if (!location.getChunk().isLoaded()) {
            location.getChunk().load();
        }
        location.getBlock().setType(Material.CHEST);
        final List<ItemStack> loot = new ArrayList<>();
        loot.add(CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("ConquestLootbox").createClaimableItem(false));
        final Chest chest = (Chest) location.getBlock().getState();
        if (chest != null) {
            chest.getInventory().addItem(new ItemStack(Material.DIRT));
            chest.update();
            this.claimNearbyWarzone(location.getBlock().getLocation());
            final Location loc = chest.getBlock().getLocation();
            CosmicConquests.get().getManager().getConquestChests().put(loc, new ChestData(ConquestListener.maxHits, this.getBossSpawnList()));
            CosmicConquests.get().getManager().getLoot().put(loc, loot);
        }
    }
    
    private List<BossType> getBossSpawnList() {
        final List<BossType> list = Lists.newArrayList();
        list.add(BossType.ENFORCER);
        list.add(BossType.EXTERMINATOR);
        list.add(BossType.EXTERMINATOR);
        list.add(BossType.EXTERMINATOR);
        for (int rand = 2 + ThreadLocalRandom.current().nextInt(2), i = 0; i < rand; ++i) {
            list.add(BossType.MARKSMAN);
        }
        return list;
    }
    
    public ItemStack getEnchantBook() {
        final ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        final EnchantmentStorageMeta ebm = (EnchantmentStorageMeta)book.getItemMeta();
        ebm.addStoredEnchant(Enchantment.DEPTH_STRIDER, 1 + ThreadLocalRandom.current().nextInt(3), true);
        book.setItemMeta(ebm);
        return book;
    }
    
    private HashSet<String> getClaimedChunkCoords() {
        final String worldNeeded = Bukkit.getWorlds().get(0).getName();
        final HashSet<String> map = new HashSet<String>();
        for (final Map.Entry<String, Map<String, String>> data : FactionUtils.dumpAsSaveFormat().entrySet()) {
            if (data.getKey() != null) {
                if (!data.getKey().equals(worldNeeded)) {
                    continue;
                }
                map.addAll(data.getValue().keySet());
            }
        }
        Bukkit.getLogger().info("(CosmicConquests) Loaded " + map.size() + " claimed chunks into memory!");
        return map;
    }
    
    public ConquerData getLastConquer() {
        return this.lastConquer;
    }
    
    public Set<Entity> getBosses() {
        return this.bosses;
    }
    
    public Map<Location, ChestData> getConquestChests() {
        return this.conquestChests;
    }
    
    public Map<Location, List<ItemStack>> getLoot() {
        return this.loot;
    }
    
    public Location getNextSpawnLocation() {
        return this.nextSpawnLocation;
    }
    
    public ItemStack getChickenHead() {
        return this.chickenHead;
    }
    
    public Map<Location, List<FLocation>> getWarzoned() {
        return this.warzoned;
    }
}

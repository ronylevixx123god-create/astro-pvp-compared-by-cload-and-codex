/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.CosmicUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.EconomyUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.LocationUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NumberUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.PlayerUtil
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.WorldGuardUtils
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.Faction
 *  com.massivecraft.factions.P
 *  com.massivecraft.factions.integration.Econ
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.FLogType
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.ifamasssxd.cosmichcf.managers;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.utils.*;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.integration.Econ;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.HCFOutpost;
import me.ifamasssxd.cosmichcf.struct.Outpost;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class OutpostManager {
    int firstHour = 10;
    int secondHour = 19;
    private Map<Integer, Integer> hoursToSpawn = new LinkedHashMap<Integer, Integer>(){
        {
            this.put(11, 1);
            this.put(19, 2);
            this.put(23, 2);
        }
    };
    private Set<Outpost> outposts = new HashSet<Outpost>();
    private Map<String, Integer> outpostCaptureHistory = new HashMap<String, Integer>();
    private long delay = TimeUnit.HOURS.toMillis(6L);
    private long cosmicGalaxyDelay = TimeUnit.HOURS.toMillis(1L);
    private long lastSpawnedChest = 0L;
    private String lastSpawnedOutpost = null;
    private long outpostLogoutDelay = TimeUnit.MINUTES.toMillis(15L);
    private Map<UUID, Long> lastLogoutInOutpost = new HashMap<UUID, Long>();
    private long lastMoneyGiven = System.currentTimeMillis();
    private long MONEY_GIVE_DELAY = TimeUnit.MINUTES.toMillis(5L);

    public void sellStronghold(Player player) {
        if (!HCFOutpostAPI.isControllingOutpost(player, OutpostType.THE_GREAT_HALL) && !this.playerHasStrongholdPetAbility(player)) {
            player.sendMessage(CC.Red + "You must be controlling the " + CC.AquaB + "Frozen" + CC.Red + " Stronghold to sell items!");
            return;
        }
        HashMap<Material, Integer> sellCount = new HashMap<Material, Integer>();
        for (ItemStack is : player.getInventory()) {
            int price;
            Material material2;
            if (is == null || (material2 = is.getType()) == Material.AIR || (price = this.getPrice(material2)) <= 0) continue;
            Integer sellAmount = sellCount.getOrDefault((Object)material2, 0);
            sellAmount = sellAmount + is.getAmount();
            sellCount.put(material2, sellAmount);
            player.getInventory().removeItem(new ItemStack[]{is});
        }
        if (!sellCount.isEmpty()) {
            player.updateInventory();
            StringBuilder builder = new StringBuilder();
            StringBuilder paidBuilder = new StringBuilder();
            sellCount.forEach((material, amount) -> {
                int price = this.getPrice((Material)material);
                double moneyToGive = price * amount;
                String itemName = StringUtils.capitaliseAllWords((String)material.name().toLowerCase().replace("_", " "));
                player.sendMessage(CC.Green + "$" + NumberUtils.formatMoney((double)moneyToGive) + " has been added to your account.");
                player.sendMessage(CC.Green + "Sold for " + CC.Red + "$" + NumberUtils.formatMoney((double)moneyToGive) + CC.Green + " (" + amount + " " + itemName + " at $" + NumberUtils.formatMoney((double)price) + " each).");
                EconomyUtils.depositBalance((OfflinePlayer)player, (double)moneyToGive);
                builder.append(amount).append("x ").append(itemName).append(", ");
                if (paidBuilder.length() > 0) {
                    paidBuilder.append("$");
                }
                paidBuilder.append(NumberUtils.formatMoney((double)moneyToGive)).append(", ");
            });
            try {
                Faction faction = FactionUtils.getFaction((Player)player, (boolean)false);
                String front = builder.toString();
                String end = paidBuilder.toString();
                if (front.endsWith(", ")) {
                    front = front.substring(0, front.length() - 2);
                }
                if (end.endsWith(", ")) {
                    end = end.substring(0, end.length() - 2);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "No valid items found to sell!");
        }
    }

    private boolean playerHasStrongholdPetAbility(Player p) {
        return p.hasMetadata("strongholdPet") && ((MetadataValue)p.getMetadata("strongholdPet").get(0)).asLong() > System.currentTimeMillis();
    }

    public void onPlayerLeave(PlayerQuitEvent event) {
        Outpost outpost = this.getOutpostInside(event.getPlayer().getLocation());
        if (outpost != null) {
            this.lastLogoutInOutpost.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + this.outpostLogoutDelay);
        }
    }

    public void onPlayerJoin(PlayerJoinEvent event, Set<String> regionsInside) {
        Outpost outpost = this.getOutpostInside(regionsInside);
        Long expiration = this.lastLogoutInOutpost.remove(event.getPlayer().getUniqueId());
        if (outpost != null && (expiration == null || System.currentTimeMillis() >= expiration)) {
            event.getPlayer().teleport(((World)Bukkit.getWorlds().get(0)).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicKOTHPlugin.get_(), () -> {
                event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You were found logged out inside of a Stronghold!");
                event.getPlayer().sendMessage(CC.Gray + "You have been returned to spawn!");
            }, 80L);
            Bukkit.getLogger().info("Returning " + event.getPlayer().getName() + " due to them being inside " + outpost.getInternalOutpostName() + "!");
        }
    }

    private int getPrice(Material material) {
        if (material == Material.EMERALD_BLOCK) {
            return 2000;
        }
        if (material == Material.IRON_BLOCK) {
            return 300;
        }
        if (material == Material.GOLD_BLOCK) {
            return 500;
        }
        return -1;
    }

    public void loadOutposts(CosmicKOTHPlugin plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("outposts");
        if (section == null) {
            Bukkit.getLogger().info("No outposts found in config.yml!");
            return;
        }
        this.lastSpawnedChest = plugin.getConfig().getLong("last-spawned-chest");
        this.lastSpawnedOutpost = plugin.getConfig().getString("last-outpost");
        if (this.lastSpawnedChest <= 0L) {
            this.lastSpawnedChest = System.currentTimeMillis();
        }
        for (String outpostKey : section.getKeys(false)) {
            Faction lastCapturingFaction;
            List lore;
            String displayName = section.getString(outpostKey + ".displayName");
            String blockRegionName = section.getString(outpostKey + ".blockRegionName");
            String regionName = section.getString(outpostKey + ".regionName", "outpost");
            String lastCaptured = section.getString(outpostKey + ".lastCaptured");
            String teleportLocation = section.getString(outpostKey + ".location", null);
            double capPercent = section.getDouble(outpostKey + ".capturePercent", 0.0);
            String lastCapturing = section.getString(outpostKey + ".defendingFaction");
            long lastCapturedTime = section.getLong(outpostKey + ".lastCapturedTime");
            long lastTrophy = section.getLong(outpostKey + ".lastTrophySpawn");
            Location loc = LocationUtils.getLocationFromString((String)section.getString(outpostKey + ".trophyLocation", ""), (boolean)false);
            Location sellRoomLoc = LocationUtils.getLocationFromString((String)section.getString(outpostKey + ".sellRoomLocation", ""), (boolean)false);
            HCFOutpost outpost = new HCFOutpost(outpostKey, displayName, regionName, blockRegionName, lastCaptured, lastCapturedTime);
            this.outposts.add(outpost);
            ConfigurationSection section1 = plugin.getConfig().getConfigurationSection("perks." + outpostKey);
            if (section1 != null && (lore = section1.getStringList(plugin.getServerType())) != null) {
                outpost.getOutpostType().setRewardStrings(lore);
            }
            if (teleportLocation != null) {
                outpost.setTeleportLocation(LocationUtils.getLocationFromString((String)teleportLocation, (boolean)false));
            }
            if (sellRoomLoc != null) {
                outpost.setSellRoomLocation(sellRoomLoc);
            }
            if (lastCapturing != null && !lastCapturing.isEmpty() && (lastCapturingFaction = FactionUtils.getFactionById((String)lastCapturing)) != null && FactionUtils.isNormal((Faction)lastCapturingFaction)) {
                outpost.setCapPercentage(Math.min(100.0, capPercent));
                outpost.setDefendingFaction(lastCapturingFaction);
            }
            if (loc != null) {
                outpost.setTrophyChestLocation(loc);
            }
            outpost.setLastTrophySpawned(lastTrophy);
            Bukkit.getLogger().info("(CosmicHCFOutposts) Registering Outpost '" + outpostKey + "', display: " + displayName + " last captured: " + lastCaptured + " Cap Percentage: " + capPercent + " defendingFaction: " + (outpost.getDefendingFaction() != null ? outpost.getDefendingFaction().getTag() : "None") + "Last captured Tag: " + outpost.getLastFactionToCapTag() + " teleport: " + teleportLocation + " converted to: " + (outpost.getTeleportLocation() == null ? "Null" : LocationUtils.getStringFromLocation((Location)outpost.getTeleportLocation())));
        }
        if (this.lastSpawnedOutpost == null) {
            this.outposts.stream().findFirst().ifPresent(first -> {
                this.lastSpawnedOutpost = first.getInternalOutpostName();
            });
        }
        this.startOutpostTask();
    }

    private void saveLastSpawnedInfo(boolean save) {
        CosmicKOTHPlugin.get_().getConfig().set("last-spawned-chest", (Object)this.lastSpawnedChest);
        CosmicKOTHPlugin.get_().getConfig().set("last-outpost", (Object)this.lastSpawnedOutpost);
        if (save) {
            CosmicKOTHPlugin.get_().saveConfig();
        }
    }

    public Outpost getOutpostInside(Set<String> allRegionsInside) {
        for (Outpost outpost : this.outposts) {
            for (String region : allRegionsInside) {
                if (!region.contains(outpost.getRegionName()) && !region.contains(outpost.getBlockRegionNames()) && !region.contains(outpost.getInternalOutpostName())) continue;
                return outpost;
            }
        }
        return null;
    }

    public Outpost getOutpostInside(Location location) {
        return this.getOutpostInside(WorldGuardUtils.getRegionsIn((Location)location));
    }

    public void updatePassiveBonusEffects() {
        this.outposts.forEach(out -> {
            if (out.getOutpostType() == null) {
                return;
            }
            if (out.getLastFactionToCap() == null) {
                return;
            }
            if (out.getOutpostType() == OutpostType.COURTYARD) {
                Faction controlling = FactionUtils.getFactionById((String)out.getLastFactionToCap());
                if (controlling != null && out.getLastCapturedTime() != 0L && System.currentTimeMillis() - out.getLastCapturedTime() >= this.MONEY_GIVE_DELAY && System.currentTimeMillis() - this.lastMoneyGiven >= this.MONEY_GIVE_DELAY) {
                    this.lastMoneyGiven = System.currentTimeMillis();

                }
                return;
            }
            if (!out.getOutpostType().equals((Object)OutpostType.THE_GREAT_HALL)) {
                return;
            }
            Faction controlling = FactionUtils.getFactionById((String)out.getLastFactionToCap());
            if (controlling != null) {
                controlling.getFPlayers().stream().filter(fp -> fp.getPlayer() != null).forEach(fp -> this.applyPotionEffect(fp.getPlayer(), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 0)));
            }
        });
    }

    private void applyPotionEffect(Player player, PotionEffect effect) {
        int effectRequiredToNotOverride = effect.getDuration() / 4;
        for (PotionEffect effec : player.getActivePotionEffects()) {
            if (!effec.getType().getName().equals(effect.getType().getName())) continue;
            if (effec.getDuration() > effect.getDuration() && effec.getAmplifier() >= effect.getAmplifier()) {
                return;
            }
            if (effec.getDuration() < effectRequiredToNotOverride) continue;
            return;
        }
        player.addPotionEffect(effect, true);
    }

    public Outpost getOutpostFromBlockName(String name) {
        for (Outpost post : this.outposts) {
            if (!name.equalsIgnoreCase(post.getBlockRegionNames())) continue;
            return post;
        }
        return null;
    }

    public Outpost getOutpostFromName(String name) {
        for (Outpost post : this.outposts) {
            if (!name.equalsIgnoreCase(post.getInternalOutpostName())) continue;
            return post;
        }
        return null;
    }

    public void onDisable(JavaPlugin plugin) {
        this.outposts.forEach(outpost -> {
            outpost.restoreBrokenBlocks();
            outpost.restorePlacedBlocks();
        });
        if (this.saveOutpostData(plugin.getConfig())) {
            Bukkit.getLogger().info("(CosmicHCFOutposts) Config changes detected, calling .saveConfig()");
            plugin.saveConfig();
        }
    }

    public void onBlockBreak(final BlockBreakEvent event) {
        this.outposts.forEach(out -> out.onBlockBreak(event));
    }

    public void onBlockPlace(final BlockPlaceEvent event) {
        final String id = FactionUtils.getFactionId(event.getPlayer());
        this.outposts.forEach(out -> {
            if (out.getLastFactionToCap() != null && (out.getLastFactionToCap() == null || out.getLastFactionToCap().equals(id))) {
                out.onBlockPlace(event);
            }
        });
    }

    public void onBlockPlaceMONITOR(final BlockPlaceEvent event) {
        final String id = FactionUtils.getFactionId(event.getPlayer());
        this.outposts.forEach(out -> {
            if (out.getLastFactionToCap() != null && (out.getLastFactionToCap() == null || out.getLastFactionToCap().equals(id))) {
                out.onBlockPlaceMONITOR(event);
            }
        });
    }

    public boolean saveOutpostData(FileConfiguration config) {
        boolean changesDetected = false;
        for (Outpost out : this.outposts) {
            changesDetected |= this.saveOutpostData(out, config, false);
        }
        return changesDetected;
    }

    public boolean saveOutpostData(Outpost out, FileConfiguration config, boolean saveCoreData) {
        String base = "outposts." + out.getInternalOutpostName();
        boolean changes = false;
        if (saveCoreData) {
            changes = this.setConfigValueIfNeeded(config, base, "regionName", out.getRegionName());
            changes |= this.setConfigValueIfNeeded(config, base, "blockRegionName", out.getBlockRegionNames());
            changes |= this.setConfigValueIfNeeded(config, base, "displayName", out.getOutpostName());
            changes |= this.setConfigValueIfNeeded(config, base, "location", out.getTeleportLocation() == null ? null : LocationUtils.getStringFromLocation((Location)out.getTeleportLocation()));
            changes |= this.setConfigValueIfNeeded(config, base, "trophyLocation", out.getTrophyChestLocation() == null ? null : LocationUtils.getStringFromLocation((Location)out.getTrophyChestLocation()));
            changes |= this.setConfigValueIfNeeded(config, base, "sellRoomLocation", out.getSellRoomLocation() == null ? null : LocationUtils.getStringFromLocation((Location)out.getSellRoomLocation()));
        }
        changes |= this.setConfigValueIfNeeded(config, base, "defendingFaction", out.getDefendingFaction() == null ? null : out.getDefendingFaction().getId());
        changes |= this.setConfigValueIfNeeded(config, base, "lastCaptured", out.getLastFactionToCap());
        changes |= this.setConfigValueIfNeeded(config, base, "capturePercent", out.getCapPercentage());
        changes |= this.setConfigValueIfNeeded(config, base, "lastCapturedTime", out.getLastCapturedTime());
        return changes |= this.setConfigValueIfNeeded(config, base, "lastTrophySpawned", out.getLastTrophySpawned());
    }

    private boolean setConfigValueIfNeeded(FileConfiguration config, String base, String key, Object value) {
        Object object = config.get(base + "." + key);
        if (object != null && object.equals(value)) {
            return false;
        }
        if (value == null && object == null) {
            return false;
        }
        config.set(base + "." + key, value);
        return true;
    }

    public boolean doesControllAnotherOutpost(Faction faction) {
        for (Outpost outpost : this.outposts) {
            if (outpost.getLastFactionToCap() == null || !outpost.getLastFactionToCap().equals(faction.getId())) continue;
            return true;
        }
        return false;
    }

    public void startOutpostTask() {
        new BukkitRunnable(){

            public void run() {
                Map<Player, Set<String>> playersinRegions = OutpostManager.this.getAllPlayersInRegions();
                for (Outpost outpost : OutpostManager.this.outposts) {
                    Set playersInside = OutpostManager.this.getPlayersInside(playersinRegions, outpost.getRegionName());
                    outpost.onOutpostUpdate(playersInside, playersinRegions);
                    HashSet<Player> inside = new HashSet<Player>();
                    playersinRegions.forEach((pl, regions) -> {
                        if (outpost.contains(outpost.getInternalOutpostName(), (Set<String>)regions) || outpost.contains(outpost.getBlockRegionNames(), (Set<String>)regions) || outpost.contains(outpost.getRegionName(), (Set<String>)regions)) {
                            inside.add((Player)pl);
                        }
                    });
                    outpost.updateScoreboard(inside);
                }
                playersinRegions.forEach((pl, regions) -> {
                    for (Outpost outpost : OutpostManager.this.outposts) {
                        if (!outpost.isInsideSellRoom((Player)pl, (Set<String>)regions) || pl.isOp() || pl.hasPermission("arkkit.staff.hidden") || pl.isDead()) continue;
                        if (outpost.getLastFactionToCap() == null) {
                            outpost.teleportToMain((Player)pl);
                            pl.sendMessage(CC.RedB + "(!) " + CC.Red + "You are not allowed to be inside the Outpost Sell Room!");
                            continue;
                        }
                        FPlayer fplayer = FactionUtils.getFPlayer((Player)pl);
                        if (fplayer != null && fplayer.hasFaction() && fplayer.getFaction().getId().equals(outpost.getLastFactionToCap())) continue;
                        outpost.teleportToMain((Player)pl);
                        pl.sendMessage(CC.RedB + "(!) " + CC.Red + "You are not allowed to be inside the Outpost Sell Room!");
                    }
                });
                if (!CosmicUtils.get().isCosmicGalaxy()) {
                    OutpostManager.this.updatePassiveBonusEffects();
                }
            }
        }.runTaskTimer(CosmicKOTHPlugin.get_(), 20L, 20L);
        String spawnType = CosmicKOTHPlugin.get_().getConfig().getString("spawn-type");
        if (spawnType == null || spawnType.equals("timed")) {
            new BukkitRunnable(){

                public void run() {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(TimeZone.getTimeZone("CST"));
                    int hour = cal.get(11);
                    for (Outpost post : OutpostManager.this.outposts) {
                        Integer amountToSpawn = (Integer)OutpostManager.this.hoursToSpawn.get(hour);
                        if (amountToSpawn == null) continue;
                        long timeSinceSpawn = post.getLastTrophySpawned();
                        long timePassed = System.currentTimeMillis() - timeSinceSpawn;
                        if (timeSinceSpawn != 0L && timePassed < TimeUnit.HOURS.toMillis(1L)) continue;
                        post.spawnTrophyChest(amountToSpawn);
                    }
                }
            }.runTaskTimer(CosmicKOTHPlugin.get_(), 20L, 20L);
        } else if (spawnType.equals("delay") || CosmicUtils.get().isCosmicGalaxy()) {
            new BukkitRunnable(){

                public void run() {
                    long lastSpawned = System.currentTimeMillis() - OutpostManager.this.lastSpawnedChest;
                    if (lastSpawned >= (CosmicKOTHPlugin.get_().isCosmicGalaxyPlanet() ? OutpostManager.this.cosmicGalaxyDelay : OutpostManager.this.delay)) {
                        for (Outpost post : OutpostManager.this.outposts) {
                            if (OutpostManager.this.lastSpawnedOutpost != null && OutpostManager.this.lastSpawnedOutpost.equals(post.getInternalOutpostName())) continue;
                            OutpostManager.this.lastSpawnedOutpost = post.getInternalOutpostName();
                            OutpostManager.this.lastSpawnedChest = System.currentTimeMillis();
                            post.spawnTrophyChest(1);
                            OutpostManager.this.saveLastSpawnedInfo(true);
                            return;
                        }
                    }
                }
            }.runTaskTimer(CosmicKOTHPlugin.get_(), 600L, 20L);
        }
        Bukkit.getLogger().info("[CosmicHCFOutposts] Starting with spawn-type: " + spawnType);
    }

    private Set<Player> getPlayersInside(Map<Player, Set<String>> regions, String region) {
        HashSet<Player> retr = new HashSet<Player>();
        regions.forEach((pl, playerRegions) -> {
            if (playerRegions.contains(region)) {
                retr.add((Player)pl);
            }
        });
        return retr;
    }

    private Map<Player, Set<String>> getAllPlayersInRegions() {
        HashMap<Player, Set<String>> tracker = new HashMap<Player, Set<String>>();
        for (Player pl : PlayerUtil.getOnlinePlayers()) {
            Set regionsinside = WorldGuardUtils.getRegionsIn((Location)pl.getLocation());
            if (regionsinside.isEmpty()) continue;
            tracker.put(pl, regionsinside);
        }
        return tracker;
    }

    public Map<Integer, Integer> getHoursToSpawn() {
        return this.hoursToSpawn;
    }

    public Set<Outpost> getOutposts() {
        return this.outposts;
    }

    public Map<UUID, Long> getLastLogoutInOutpost() {
        return this.lastLogoutInOutpost;
    }
}


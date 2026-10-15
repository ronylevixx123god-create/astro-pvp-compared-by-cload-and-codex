package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonState;
import com.compilex.CosmicDungeons.events.DungeonStateChangeEvent;
import com.compilex.CosmicDungeons.struct.DungeonChallenge;
import com.compilex.CosmicDungeons.utils.ChatUtils;
import com.compilex.CosmicDungeons.utils.Logger;
import com.compilex.CosmicDungeons.utils.MonsterUtils;
import com.compilex.CosmicDungeons.utils.TimeUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.*;
import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import lombok.Getter;
import lombok.Setter;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.DamageTracker;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AbandonedShipEvents
extends DungeonEvents {
    private ParkourRestrictionTask parkourRestrictionTask;
    private final List<Block> blessLeverLocations = new ArrayList<Block>();
    private UUID singularDamager = null;
    @Getter @Setter
    private UUID firstBossUUID;
    @Getter @Setter
    private UUID secondBossUUID;

    public AbandonedShipEvents(Dungeon d) {
        super(d);
        this.blessLeverLocations.add(new Location(d.getWorld(), -86.0, 84.0, 169.0).getBlock());
        this.blessLeverLocations.add(new Location(d.getWorld(), -216.0, 78.0, 186.0).getBlock());
        this.loadRegionTriggerEvents();
        this.loadTasks();
        Logger.debug("regionEvents: " + this.regionEvents);
    }

    public void reset() {
        this.singularDamager = null;
    }

    @Override
    public void loadRegionTriggerEvents() {
        this.regionEvents.put("hanger", new PlayerEnterHanger(this.dungeon, "hanger"));
        this.regionEvents.put("parkour_a", new PlayerEnterParkourA(this.dungeon, "parkour_a"));
        this.regionEvents.put("maze_start", new PlayerEnterMaze(this.dungeon, "maze_start"));
        this.regionEvents.put("bridge", new PlayerEnterBridge(this.dungeon, "bridge"));
        this.regionEvents.put("parkour_b", new PlayerEnterParkourB(this.dungeon, "parkour_b"));
        this.regionEvents.put("teleportation_room", new PlayerEnterTeleportationRoom(this.dungeon, "teleportation_room"));
    }

    private void loadTasks() {
        this.parkourRestrictionTask = new ParkourRestrictionTask(this.dungeon, Lists.newArrayList("parkour_a", "parkour_b", "maze", "maze_start"));
        this.parkourRestrictionTask.runTaskTimer(CosmicDungeonsPlugin.get_(), 10L, 10L);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onYijkiBoss_Death(EntityDeathEvent e) {
        Boss boss = Boss.bosses.get(e.getEntity().getUniqueId());
        if (boss != null && boss instanceof DungeonYijki) {
            DungeonYijki dy = (DungeonYijki)boss;
            if (dy.getBossPhase() == DungeonYijki.BossPhase.PHASE_1) {
                for (int x = 21; x <= 24; ++x) {
                    for (int z = 230; z <= 235; ++z) {
                        for (int y = 89; y <= 92; ++y) {
                            Block b = e.getEntity().getWorld().getBlockAt(x, y, z);
                            WorldUtil.sendWorldEffect(b, 2001);
                            b.setType(Material.AIR);
                        }
                    }
                }
                BossUtils.createDungeonGhost(e.getEntity().getLocation().add(0.0, 10.0, 0.0), new Location(this.dungeon.getWorld(), 22.0, 95.0, 233.0), 4);
                this.dungeon.broadcastMessage(ChatColor.WHITE + "The Yijki is fleeing deeper into the collapsing ship! Locate and neutralize it before the ship falls apart!");
                DamageTracker tracker = boss.getDamageTracker();
                if (tracker.size() == 1) {
                    this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.GUARDIAN);
                    this.singularDamager = (tracker.entrySet().stream().findFirst().orElse(null)).getKey();
                    Bukkit.getLogger().info("Granting GUARDIAN due to " + this.singularDamager + " being only damager!");
                }
                if (this.checkForDamage(tracker)) {
                    this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.TAG_TEAM);
                    Bukkit.getLogger().info("Granting TAG_TEAM due to >5% damage from each.!");
                }
                this.checkForQuickKill(this.dungeon);
            } else if (dy.getBossPhase() == DungeonYijki.BossPhase.PHASE_2) {
                UUID singleDamager;
                this.dungeon.broadcastMessage(ChatColor.WHITE + "The Yijki is heading for the teleportation room! After him, quickly! The ship cannot hold together much longer!");
                this.dungeon.setObjectiveHologram(null);
                for (int x = -132; x < -126; ++x) {
                    for (int y2 = 79; y2 <= 85; ++y2) {
                        Block b2 = e.getEntity().getWorld().getBlockAt(x, y2, 163);
                        WorldUtil.sendWorldEffect(b2, 2001);
                        b2.setType(Material.AIR);
                    }
                }
                BossUtils.createDungeonGhost(e.getEntity().getLocation().add(0.0, 8.0, 0.0), new Location(this.dungeon.getWorld(), -132.0, 82.0, 163.0), 4);
                DamageTracker tracker = boss.getDamageTracker();
                if (this.checkForDamage(tracker)) {
                    if (this.dungeon.getDungeonChallengesCompleted().contains(DungeonChallenge.TAG_TEAM)) {
                        this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.WELL_OILED_MACHINE);
                    } else {
                        this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.TAG_TEAM);
                    }
                }
                this.checkForQuickKill(this.dungeon);
            }
        }
    }

    public void unregisterTasks() {
        if (this.parkourRestrictionTask != null) {
            Bukkit.getScheduler().cancelTask(this.parkourRestrictionTask.getTaskId());
            this.parkourRestrictionTask = null;
        }
    }

    private void checkForQuickKill(Dungeon dungeon) {
        if (System.currentTimeMillis() - dungeon.getBossSpawnTime() <= TimeUnit.SECONDS.toMillis(60L)) {
            dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.UP_CLOSE_AND_PERSONAL);
            Bukkit.getLogger().info("Granting UP_CLOSE_AND_PERSONAL due to killing Boss < 60s");
        }
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        if (this.dungeon.getPlayers().contains(event.getPlayer())) {
            DungeonsAPI.get().getPlayerDungeonData(event.getPlayer()).leaveDungeon(true);
            this.dungeon.getPlayers().remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onDungeonStateChange(DungeonStateChangeEvent e) {
        if (e.getDungeon().equals(this.dungeon)) {
            if (e.getOldState() == DungeonState.LOADING && e.getNewState() == DungeonState.IN_PROGRESS) {
                e.getDungeon().broadcastMessage(ChatColor.WHITE + "The dungeon portal has closed, it is time to begin!");
                if (e.getDungeon().isHeroic()) {
                    e.getDungeon().getPlayers().forEach(p -> {
                        ChatUtils.sendCenteredMessage(p, e.getDungeon().getDungeonName());
                        ChatUtils.sendCenteredMessage(p, CC.translate("&f&l* &7-50% Objective Time"));
                        ChatUtils.sendCenteredMessage(p, CC.translate("&f&l* &7Bosses take 75% less incoming damage"));
                        ChatUtils.sendCenteredMessage(p, CC.translate("&f&l* &7Bosses deal 2.5x outgoing damage"));
                        e.getDungeon().getRandomDungeonDebuffs().forEach(debuff -> ChatUtils.sendCenteredMessage(p, CC.translate("&f&l* &7" + debuff.getName())));
                    });
                }
                e.getDungeon().setObjectiveHologram(null);
                int z = 91;
                for (int x = 0; x >= -4; --x) {
                    for (int y = 79; y <= 83; ++y) {
                        Block b = e.getDungeon().getWorld().getBlockAt(x, y, 91);
                        WorldUtil.sendWorldEffect(b, 2001);
                        b.setType(Material.AIR);
                    }
                }
                e.getDungeon().broadcastMessage(ChatColor.WHITE + "The airlock leading to the Abandoned Spaceship has opened... proceed with caution.");
                e.getDungeon().updateTimeRestrictionTask(System.currentTimeMillis() + 300000L / (long)(e.getDungeon().isHalfTime() ? 2 : 1), "Enter Ship Hanger");
                for (Player p2 : this.dungeon.getPlayers()) {
                    DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p2);
                    dp.logDungeonAttempt(this.dungeon);
                }
            }
            if (e.getOldState() == DungeonState.IN_PROGRESS && e.getNewState() == DungeonState.COMPLETE) {
                double minutesCompletedIn = (double)(this.dungeon.getTimeSinceDungeonStart() / 1000L) / 60.0;
                if (minutesCompletedIn <= 10.0) {
                    this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.SPEED_RACER);
                }
                if (minutesCompletedIn <= 15.0) {
                    this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.NIMBLE_FEET);
                }
                if (this.dungeon.isOnARoll()) {
                    this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.ON_A_ROLL);
                }
                if (!this.dungeon.isPlayersDied()) {
                    this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.FLAWLESS);
                }
                int total = 0;
                HashSet<String> factionIdsToCredit = new HashSet<String>();
                final HashSet<UUID> availableLooters = new HashSet<UUID>();
                String playerList = "";
                List<Player> players = this.dungeon.getPlayers();
                for (Player p2 : players) {
                    DungeonPlayer dp2 = DungeonsAPI.get().getPlayerDungeonData(p2);
                    if (dp2 == null || dp2.getCurrentDungeon() == null || !dp2.getCurrentDungeon().equals(e.getDungeon())) continue;
                    ++total;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "contests add " + p2.getName() + " dungeonsCompleted 1");
                    Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(p2, "DUNGEON_COMPLETE", new Object[]{this.dungeon.dungeonType.name()}));
                    boolean inTeleportRoom = WorldGuardUtils.getRegionName(p2.getLocation()).equals("teleportation_room");
                    if (inTeleportRoom) {
                        dp2.logDungeonComplete(this.dungeon);
                    }
                    if (!dp2.getCooldowns().canPlayerDoDungeon(this.dungeon)) {
                        p2.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot receive rewards from the " + this.dungeon.getDungeonName() + ChatColor.RED + " dungeon for " + TimeUtils.formatDifference(dp2.getCooldowns().getCooldown(this.dungeon)) + ".");
                        continue;
                    }
                    availableLooters.add(p2.getUniqueId());
                    if (!inTeleportRoom) {
                        p2.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You were not standing in the final room of the dungeon, so you do not get a reward!");
                        continue;
                    }
                    p2.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "+1 Dungeon Loot Bag");

                    //ItemStack heroicBag = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("HeroicAbandonedShipLootbag").createClaimableItem(false);
                    //ItemStack abaBag = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("AbandonedShipLootbag").createClaimableItem(false);

                    LootFilterAPI.giveItem(p2.getPlayer(), LootTable.ABANDONED_SPACESHIP_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.ABANDONED_SPACESHIP_DUNGEON));
                    LootFilterAPI.giveItem(p2.getPlayer(), LootTable.ABANDONED_SPACESHIP_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.ABANDONED_SPACESHIP_DUNGEON));
                    LootFilterAPI.giveItem(p2.getPlayer(), LootTable.ABANDONED_SPACESHIP_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.ABANDONED_SPACESHIP_DUNGEON));
                    LootFilterAPI.giveItem(p2.getPlayer(), LootTable.ABANDONED_SPACESHIP_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.ABANDONED_SPACESHIP_DUNGEON));
                    LootFilterAPI.giveItem(p2.getPlayer(), LootTable.ABANDONED_SPACESHIP_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.ABANDONED_SPACESHIP_DUNGEON));

                    playerList = playerList + p2.getName() + ", ";
                    long bonusCD = dp2.getCooldowns().addDungeonCooldown(this.dungeon, 300000L);
                    Faction faction = FactionUtils.getFaction(p2);
                    if (faction == null || !faction.isNormal()) continue;
                    factionIdsToCredit.add(faction.getTag());
                }
                if (playerList.endsWith(", ")) {
                    playerList = playerList.substring(0, playerList.length() - 2);
                }
                this.dungeon.broadcastTitle("DUNGEON COMPLETE!", "+ Dungeon Loot", ChatColor.GREEN, ChatColor.GRAY);
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "*-*-* DUNGEON COMPLETE! *-*-*");
                Bukkit.broadcastMessage(ChatColor.GOLD + "The " + this.dungeon.getDungeonName() + ChatColor.GOLD + " has been cleared by: " + ChatColor.GRAY + playerList + ChatColor.GOLD + " in " + TimeUtils.formatDifference(this.dungeon.getTimeSinceDungeonStart()));
                if (this.dungeon.isHeroic()) {
                    Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&7with the following Heroic Dungeon debuffs:")));
                    Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&6&l* &7-50% Objective Time")));
                    Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&6&l* &7Bosses take 75% less incoming damage")));
                    Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&6&l* &7Bosses deal 2.5x outgoing damage")));
                    this.dungeon.getRandomDungeonDebuffs().forEach(debuff -> Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&6&l* &7" + debuff.getName()))));
                    this.dungeon.getTopHeroicDungeonTime().checkLatestTime((int)(this.dungeon.getTimeSinceDungeonStart() / 1000L), this.dungeon, players, availableLooters);
                } else {
                    this.dungeon.getTopDungeonTime().checkLatestTime((int)(this.dungeon.getTimeSinceDungeonStart() / 1000L), this.dungeon, players, availableLooters);
                }
                Bukkit.broadcastMessage("");
                factionIdsToCredit.forEach(tag -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fquests add DUNGEON_MASTER " + tag));
                if (this.dungeon.getDungeonChallengesCompleted().size() > 0) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "(!) " + ChatColor.GOLD + "Their team has completed " + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + this.dungeon.getDungeonChallengesCompleted().size() + ChatColor.GOLD + " Dungeon Challenges:");
                    this.dungeon.getDungeonChallengesCompleted().forEach(challenge -> Bukkit.broadcastMessage(" " + ChatColor.GOLD + ChatColor.BOLD + "* " + ChatColor.GREEN + challenge.getName()));
                    Bukkit.broadcastMessage(ChatColor.GRAY + "Loot the challenge chests for bonus dungeon items!");
                    Bukkit.broadcastMessage("");
                    this.dungeon.getPlayers().forEach(pl -> pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 8.0f, 0.6f));
                    if (this.dungeon.getDungeonChallengesCompleted().contains(DungeonChallenge.NIMBLE_FEET)) {
                        factionIdsToCredit.forEach(tag -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fquests addexact DUNGEON_RACER_I " + tag));
                    }
                    if (this.dungeon.getDungeonChallengesCompleted().contains(DungeonChallenge.SPEED_RACER)) {
                        factionIdsToCredit.forEach(tag -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fquests addexact DUNGEON_RACER_II " + tag));
                    }
                    final AtomicInteger index = new AtomicInteger(0);
                    final double percentageEligible = availableLooters.size() / total;
                    final boolean smallTeam = availableLooters.size() <= 4;
                    final PlayerEnterTeleportationRoom teleportationRoom = (PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room");
                    new BukkitRunnable(){

                        public void run() {
                            if (AbandonedShipEvents.this.dungeon.getDungeonChallengesCompleted().isEmpty() || index.get() >= teleportationRoom.buttonLocations.size()) {
                                this.cancel();
                                return;
                            }
                            DungeonChallenge nextToAnnounce = AbandonedShipEvents.this.dungeon.getDungeonChallengesCompleted().stream().findFirst().orElse(null);
                            if (nextToAnnounce == null) {
                                this.cancel();
                                return;
                            }
                            int itemsPerChest = smallTeam ? ThreadLocalRandom.current().nextInt(2) + 1 : (percentageEligible <= 0.25 ? 1 : (percentageEligible <= 0.75 ? 2 : 3));
                            AbandonedShipEvents.this.dungeon.getDungeonChallengesCompleted().remove(nextToAnnounce);
                            Location nextToSet = teleportationRoom.buttonLocations.get(index.getAndIncrement());
                            Block block = nextToSet.getBlock();
                            block.setType(Material.CHEST);
                            AbandonedShipEvents.this.fillChest(block, itemsPerChest, availableLooters);
                            block.getWorld().playEffect(nextToSet.clone().add(0.5, 0.5, 0.5), Effect.STEP_SOUND, Material.CHEST.getId());
                            AbandonedShipEvents.this.dungeon.broadcastTitle(nextToAnnounce.getName(), "", ChatColor.GREEN, ChatColor.GOLD);
                            RandomFireworks.getManager().launchRandomFirework(nextToSet.add(0.5, 1.0, 0.5));
                        }
                    }.runTaskTimer(CosmicDungeonsPlugin.get_(), 20L, 20L);
                }
            }
        }
    }

    private void fillChest(Block block, int lootToFill, Set<UUID> eligible) {
        List<ItemStack> potentialLoot = CosmicDungeonsPlugin.get_().getDungeonLootbagListener().getAllDungeonLoot(this.dungeon.getDungeonType());
        Collections.shuffle(potentialLoot);
        Chest chest = (Chest)block.getState();
        for (int i = 0; i < lootToFill && i < potentialLoot.size(); ++i) {
            ItemStack item = potentialLoot.get(i);
            if (item == null) continue;
            chest.getInventory().addItem(item);
        }
        chest.update();
        block.setMetadata("dungeonChallengeLootChest", new FixedMetadataValue(CosmicDungeonsPlugin.get_(), eligible));
    }

    private boolean checkForDamage(DamageTracker tracker) {
        AtomicInteger totalDamage = new AtomicInteger(0);
        tracker.values().forEach(totalDamage::addAndGet);
        Set<UUID> currentDungeonRunners = this.dungeon.getValidPlayerUUIDs();
        HashSet playersWhoDidDamage = new HashSet();
        double damage = totalDamage.get();
        for (Map.Entry values : tracker.entrySet()) {
            double percentDamageDealt = (double)((Integer)values.getValue()).intValue() / damage;
            if (!currentDungeonRunners.contains(values.getKey())) continue;
            if (percentDamageDealt >= 0.05) {
                playersWhoDidDamage.add(values.getKey());
                continue;
            }
            Logger.debug(values.getKey() + " only did " + values.getValue() + " damage (" + percentDamageDealt * 100.0 + "%)");
        }
        return currentDungeonRunners.size() == playersWhoDidDamage.size();
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteractBlessLever(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (this.exploredRegions.contains("hanger") && (e.getClickedBlock() == null || !this.blessLeverLocations.contains(e.getClickedBlock()))) {
            return;
        }
        if (DungeonsAPI.get().getPlayerDungeonData(e.getPlayer()).getCurrentDungeon() == null) {
            return;
        }
        Material m = e.getClickedBlock().getType();
        if (m == Material.LEVER || m == Material.STONE_BUTTON) {
            if (!this.exploredRegions.contains("hanger") && m != Material.LEVER) {
                return;
            }
            for (Player p : this.dungeon.getPlayers()) {
                PlayerPotionEffects.untrackPotionEffect(p, PotionEffectType.SLOW);
                PlayerPotionEffects.untrackPotionEffect(p, PotionEffectType.SLOW_DIGGING);
                p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "BLESSED!");
                p.sendMessage(ChatColor.GRAY + "The Gods have looked down and cast their blessing upon the weary Cosmonauts about to embark on this mission!");
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteractMazeLever(PlayerInteractEvent e) {
        if (!this.regionEvents.containsKey("maze_start")) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (DungeonsAPI.get().getPlayerDungeonData(e.getPlayer()).getCurrentDungeon() == null) {
            return;
        }
        if (!WorldGuardUtils.getRegionName(e.getPlayer().getLocation()).equals("maze")) {
            return;
        }
        Block mazeLever = ((PlayerEnterMaze)this.regionEvents.get("maze_start")).mazeLever;
        if (mazeLever != null) {
            if (e.getClickedBlock().equals(mazeLever)) {
                this.dungeon.getWorld().getBlockAt(-76, 79, 172).setType(Material.AIR);
                this.dungeon.getWorld().getBlockAt(-76, 80, 172).setType(Material.AIR);
                ((PlayerEnterMaze)this.regionEvents.get("maze_start")).mazeLever = null;
                this.dungeon.setObjectiveHologram(null);
                this.dungeon.broadcastMessage(ChatColor.GREEN + "As " + e.getPlayer().getName() + " pulls the lever, you hear the generator hum to life. The exit hatch is unlocked!");
            } else {
                Block lever = e.getClickedBlock();
                if (lever.getType() == Material.LEVER) {
                    this.dungeon.broadcastMessage(ChatColor.RED + e.getPlayer().getName() + " has pulled the lever at " + lever.getLocation().getBlockX() + "x, " + lever.getLocation().getBlockY() + "y, " + lever.getLocation().getBlockZ() + "z, but nothing seems to happen.");
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteractTeleportLever(PlayerInteractEvent e) {
        Block teleportLever;
        if (!this.regionEvents.containsKey("teleportation_room")) {
            return;
        }
        if (DungeonsAPI.get().getPlayerDungeonData(e.getPlayer()).getCurrentDungeon() == null) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!WorldGuardUtils.getRegionName(e.getPlayer().getLocation()).equals("teleportation_room")) {
            return;
        }
        LinkedList<Location> buttonLocations = ((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).buttonLocations;
        HashMap<Location, ChatColor> buttonColors = ((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).buttonColors;
        if (buttonLocations != null) {
            Logger.debug("" + buttonLocations);
            if (buttonLocations.contains(e.getClickedBlock().getLocation())) {
                ChatColor buttonColor = buttonColors.containsKey(e.getClickedBlock().getLocation()) ? buttonColors.get(e.getClickedBlock().getLocation()) : ChatColor.WHITE;
                ((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).currentButtons.add(e.getClickedBlock().getLocation());
                this.dungeon.broadcastMessage(buttonColor + e.getPlayer().getName() + " pressed the button at " + e.getClickedBlock().getX() + "x, " + e.getClickedBlock().getZ() + "z " + buttonColor + "(#" + ((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).currentButtons.size() + ")");
            }
        }
        if ((teleportLever = ((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).teleportLever) != null && teleportLever.equals(e.getClickedBlock())) {
            if (((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).currentButtons.size() == 0) {
                this.dungeon.broadcastMessage("Nothing happens.");
                return;
            }
            boolean devInstance = this.dungeon.getPlayers().stream().filter(pl -> pl.getName().equals("Bryside") && !pl.hasPotionEffect(PotionEffectType.INVISIBILITY) && pl.isSneaking()).findFirst().orElse(null) != null;
            this.dungeon.broadcastMessage("Loading teleportation matrix...");
            for (int x = 0; x < buttonLocations.size(); ++x) {
                if (x >= ((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).currentButtons.size()) {
                    this.dungeon.broadcastMessage(ChatColor.RED + "ERROR: Not enough input data!");
                    return;
                }
                if (!((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).currentButtons.get(x).equals(buttonLocations.get(x)) && !devInstance) {
                    this.dungeon.broadcastMessage("#" + (x + 1) + " - " + ChatColor.RED + "INVALID, reset!");
                    ((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).currentButtons.clear();
                    if (Math.random() < 0.33) {
                        this.dungeon.broadcastMessage("Space monsters are attacking the ship!");
                        for (int i = 5; i > 0; --i) {
                            try {
                                MonsterUtils.spawnSpacePirate(new Location(this.dungeon.getWorld(), -226.0, 75.0, 190.0));
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                    return;
                }
                this.dungeon.broadcastMessage("#" + (x + 1) + " - " + ChatColor.GREEN + "CORRECT");
            }
            this.dungeon.broadcastMessage("Teleportation matrix boot successful!");
            this.dungeon.broadcastMessage("You can now follow Yijki to whatever planet he teleported to. Take some time to resupply, there is no telling what awaits you in the next dungeon.");
            this.dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram(CosmicDungeonsPlugin.get_(), teleportLever.getLocation().add(0.0, 2.0, 0.5), new String[]{ChatColor.GREEN + "" + ChatColor.BOLD + "CONGRATULATIONS!", ChatColor.GRAY + "You have cleared the dungeon!", ChatColor.GRAY + "Exit through the teleporter."}));
            ((PlayerEnterTeleportationRoom)this.regionEvents.get("teleportation_room")).teleportLever = null;
            Block dungeon_portalBlockA = this.dungeon.getWorld().getBlockAt(-255, 76, 190);
            Block dungeon_portalBlockB = this.dungeon.getWorld().getBlockAt(-255, 77, 190);
            dungeon_portalBlockA.setMetadata("dungeonExitPortalBlock", new FixedMetadataValue(CosmicDungeonsPlugin.get_(), this.dungeon.getDungeonID()));
            dungeon_portalBlockB.setMetadata("dungeonExitPortalBlock", new FixedMetadataValue(CosmicDungeonsPlugin.get_(), this.dungeon.getDungeonID()));
            dungeon_portalBlockA.setTypeIdAndData(90, (byte)0, false);
            dungeon_portalBlockB.setTypeIdAndData(90, (byte)0, false);
            Hologram holo_a = HolographicDisplaysAPI.createHologram(CosmicDungeonsPlugin.get_(), dungeon_portalBlockB.getLocation().add(0.5, 1.25, 0.5), new String[]{ChatColor.RED + "" + ChatColor.BOLD + "Exit"});
            this.dungeon.broadcastMessage("");
            this.dungeon.setPortalPlaceCooldown(System.currentTimeMillis() + 120000L);
            this.dungeon.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "DUNGEON COMPLETE!");
            this.dungeon.broadcastMessage("");
            this.dungeon.setDungeonStatus(DungeonState.COMPLETE);
            this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 120000L / (long)(this.dungeon.isHalfTime() ? 2 : 1), "Leave the Dungeon");
        }
    }

    @EventHandler
    public void onPlayerMoveInMaze(PlayerMoveEvent e) {
        if (!e.getTo().getWorld().getName().startsWith("dungeon_")) {
            return;
        }
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }
        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(e.getPlayer());
        if (dp.getCurrentDungeon() != this.dungeon) {
            return;
        }
        if (!this.regionEvents.containsKey("maze_start")) {
            return;
        }
        String region = WorldGuardUtils.getRegionName(e.getPlayer().getLocation());
        if (region.equals("maze")) {
            Block b = e.getPlayer().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
            if (b.getType() != Material.QUARTZ_BLOCK && b.getType() != Material.STAINED_CLAY) {
                return;
            }
            if (b.hasMetadata("mazeBlockDurability")) {
                int durability = b.getMetadata("mazeBlockDurability").get(0).asInt() - 1;
                WorldUtil.sendWorldEffect(b, 2001);
                if (durability <= 0) {
                    b.setTypeIdAndData(Material.ENDER_PORTAL.getId(), (byte)0, false);
                    b.removeMetadata("mazeBlockDurability", CosmicDungeonsPlugin.get_());
                } else if (durability == 1 || b.getData() == 4) {
                    b.setTypeIdAndData(Material.STAINED_CLAY.getId(), (byte)1, false);
                } else if (durability == 2) {
                    b.setTypeIdAndData(Material.STAINED_CLAY.getId(), (byte)4, false);
                }
                b.setMetadata("mazeBlockDurability", new FixedMetadataValue(CosmicDungeonsPlugin.get_(), durability));
            } else {
                b.setMetadata("mazeBlockDurability", new FixedMetadataValue(CosmicDungeonsPlugin.get_(), 2));
                b.setTypeIdAndData(Material.STAINED_CLAY.getId(), (byte)4, true);
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerFallIntoMazeHole(PlayerPortalEvent e) {
        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(e.getPlayer());
        if (dp.getCurrentDungeon() == null || dp.getCurrentDungeon().getDungeonID() != this.dungeon.getDungeonID()) {
            return;
        }
        if (dp.getBukkitPlayer().isDead()) {
            return;
        }
        String region = WorldGuardUtils.getRegionName(e.getPlayer().getLocation());
        Logger.debug("Player " + dp.getBukkitPlayer().getName() + " portalEvent in region:" + region + ", currentDungeon:" + dp.getCurrentDungeon());
        if (region.equals("maze") || region.equals("parkour_b")) {
            e.setCancelled(true);
            e.getPlayer().setHealth(0.0);
            e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "The floor underneath you collapsed and you fell into the void of space!");
        }
    }

    @EventHandler
    public void onPlayerMoveInParkour(PlayerMoveEvent e) {
        if (!e.getTo().getWorld().getName().startsWith("dungeon_")) {
            return;
        }
        Block b;
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }
        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(e.getPlayer());
        if (dp.getCurrentDungeon() != this.dungeon) {
            return;
        }
        if (!this.regionEvents.containsKey("parkour_b")) {
            return;
        }
        String region = WorldGuardUtils.getRegionName(e.getPlayer().getLocation());
        if (region.equals("parkour_b") && (b = e.getPlayer().getLocation().subtract(0.0, 1.0, 0.0).getBlock()).getLocation().getY() < 74.0) {
            if (b.getType() != Material.STEP && b.getType() != Material.DOUBLE_STEP && b.getType() != Material.STAINED_CLAY) {
                return;
            }
            WorldUtil.sendWorldEffect(b, 2001);
            b.setTypeIdAndData(Material.ENDER_PORTAL.getId(), (byte)0, false);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getWorld().equals(this.dungeon.getWorld()) && !e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getWorld().equals(this.dungeon.getWorld()) && !e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("spacePirate")) {
            e.getDrops().clear();
            e.setDroppedExp(0);
            e.getEntity().remove();
        }
    }
}


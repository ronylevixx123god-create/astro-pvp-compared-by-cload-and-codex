/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.RandomFireworks
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TitleManager$TitleColor
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldGuardUtils
 *  com.massivecraft.factions.Faction
 *  me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.Chest
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.compilex.CosmicDungeons.dungeons.libs;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.events.DestroyedOutpostEvents;
import com.compilex.CosmicDungeons.struct.DungeonChallenge;
import com.compilex.CosmicDungeons.utils.ChatUtils;
import com.compilex.CosmicDungeons.utils.TimeUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.*;
import com.massivecraft.factions.Faction;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class DestroyedOutpostDungeon
        extends Dungeon {
    public MotherOfYijki boss;
    public boolean bossPendingRespawn = false;
    public boolean hasBeenDefeated = false;

    public boolean isBossPendingRespawn() {
        return this.bossPendingRespawn;
    }

    public void setBossPendingRespawn(boolean bossPendingRespawn) {
        this.bossPendingRespawn = bossPendingRespawn;
    }

    public DestroyedOutpostDungeon(World dungeonWorld, String dungeonName, LinkedList<String> dungeonDescription) {
        super(dungeonWorld, DungeonType.DESTROYED_OUTPOST, dungeonName, dungeonDescription);
        this.setSpawnLocation(new Location(dungeonWorld, 267.0, 208.0, -11.0));
        this.hasBeenDefeated = CosmicDungeonsPlugin.get().getConfig().getBoolean("destroyedOutpostDefeated", false);
    }

    @Override
    public void reset() {
        super.reset();
        if (this.boss != null && this.boss.isBossSpawned()) {
            this.boss.getEntity().remove();
        }
        this.boss = null;
    }

    public ItemStack createJunkReturningItem() {
        return new NBTWrapper(new ItemBuilder(Material.WATCH).name(CC.GoldB + "Clock of Returning " + CC.Gray + "(Right Click)").lore("", CC.Gray + "Capable of transporting you", CC.Gray + "back to the portal dimension.").build()).setString("cosmicType", "dungeonJunkOfReturning").build();
    }

    public ItemStack createReturningClock() {
        return new NBTWrapper(new ItemBuilder(Material.WATCH).name(CC.GoldB + "Clock of Returning " + CC.Gray + "(Right Click)").lore("", CC.Gray + "Capable of transporting you", CC.Gray + "back to the Mother of Yijki's", CC.Gray + "cave, from the Lava Realm.").build()).setString("cosmicType", "dungeonClockOfReturning").build();
    }

    public boolean isJunkOfReturning(ItemStack item) {
        return item != null && item.getType() == Material.WATCH && new NBTWrapper(item).getString("cosmicType").equals("dungeonJunkOfReturning");
    }

    public boolean isClockOfReturning(ItemStack item) {
        return item != null && item.getType() == Material.WATCH && new NBTWrapper(item).getString("cosmicType").equals("dungeonClockOfReturning");
    }

    private void fillChest(Block block, int lootToFill, Set<UUID> eligible) {
        List<ItemStack> potentialLoot = CosmicDungeonsPlugin.get_().getDungeonLootbagListener().getAllDungeonLoot(this.getDungeonType());
        Collections.shuffle(potentialLoot);
        Chest chest = (Chest) block.getState();
        for (int i = 0; i < lootToFill && i < potentialLoot.size(); ++i) {
            ItemStack item = potentialLoot.get(i);
            if (item == null) continue;
            chest.getInventory().addItem(item);
        }
        chest.update();
        block.setMetadata("dungeonChallengeLootChest", new FixedMetadataValue(CosmicDungeonsPlugin.get_(), eligible));
    }

    public void onDungeonComplete() {
        HashSet<String> factionIdsToCredit = new HashSet<String>();
        String playerList = "";
        boolean firstToComplete = !this.hasBeenDefeated;
        if (firstToComplete) {
            CosmicDungeonsPlugin.get().getConfig().set("destroyedOutpostDefeated", true);
            CosmicDungeonsPlugin.get().saveConfig();
            this.hasBeenDefeated = true;
            Bukkit.getLogger().info("[CosmicDungeons] Destroyed Outpost Dungeon defeated for the first time! Giving Dimensional Armor..");
        }
        final HashSet<UUID> validLooters = new HashSet<UUID>();
        List<Player> winners = this.getPlayers();
        this.setPortalPlaceCooldown(System.currentTimeMillis() + 120000L);
        int total = 0;
        for (Player p : winners) {
            DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p);
            if (dp == null || dp.getCurrentDungeon() == null || !dp.getCurrentDungeon().equals(this)) continue;
            ++total;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "contests add " + p.getName() + " dungeonsCompleted 1");
            Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(p, "DUNGEON_COMPLETE", new Object[]{this.dungeonType.name()}));
            if (!dp.getCooldowns().canPlayerDoDungeon(this)) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot receive rewards from the " + this.getDungeonName() + ChatColor.RED + " dungeon for " + TimeUtils.formatDifference(dp.getCooldowns().getCooldown(this)) + ".");
                continue;
            }
            if (!WorldGuardUtils.getRegionName(p.getLocation()).equals("victory_ship")) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You were not standing in the victory ship of the dungeon, so you do not get a reward!");
                continue;
            }
            validLooters.add(p.getUniqueId());


            LootFilterAPI.giveItem(p.getPlayer(), LootTable.DESTROYED_OUTPOST_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.DESTROYED_OUTPOST_DUNGEON));
            LootFilterAPI.giveItem(p.getPlayer(), LootTable.DESTROYED_OUTPOST_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.DESTROYED_OUTPOST_DUNGEON));
            LootFilterAPI.giveItem(p.getPlayer(), LootTable.DESTROYED_OUTPOST_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.DESTROYED_OUTPOST_DUNGEON));
            LootFilterAPI.giveItem(p.getPlayer(), LootTable.DESTROYED_OUTPOST_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.DESTROYED_OUTPOST_DUNGEON));
            LootFilterAPI.giveItem(p.getPlayer(), LootTable.DESTROYED_OUTPOST_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.DESTROYED_OUTPOST_DUNGEON));


            playerList = playerList + p.getName() + ", ";
            long bonusCD = dp.getCooldowns().addDungeonCooldown(this, 300000L);
            dp.logDungeonComplete(this);
            Faction faction = FactionUtils.getFaction(p);
            if (faction == null || !faction.isNormal()) continue;
            factionIdsToCredit.add(faction.getTag());
        }
        if (playerList.endsWith(", ")) {
            playerList = playerList.substring(0, playerList.length() - 2);
        }
        this.broadcastTitle("DUNGEON COMPLETE!", "+ Dungeon Loot", ChatColor.GREEN, ChatColor.GRAY);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "*-*-* DUNGEON COMPLETE! *-*-*");
        Bukkit.broadcastMessage(ChatColor.GOLD + "The " + this.getDungeonName() + ChatColor.GOLD + " has been cleared by: " + ChatColor.GRAY + playerList + ChatColor.GOLD + " in " + TimeUtils.formatDifference(this.getTimeSinceDungeonStart()));
        if (this.isHeroic()) {
            Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&7with the following Heroic Dungeon debuffs:")));
            Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&6&l* &7-50% Objective Time")));
            Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&6&l* &7Bosses take 75% less incoming damage")));
            Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&6&l* &7Bosses deal 2.5x outgoing damage")));
            this.getRandomDungeonDebuffs().forEach(debuff -> Bukkit.broadcastMessage(ChatUtils.getCenteredMessage(CC.translate("&6&l* &7" + debuff.getName()))));
            this.getTopHeroicDungeonTime().checkLatestTime((int) (this.getTimeSinceDungeonStart() / 1000L), this, winners, validLooters);
        } else {
            this.getTopDungeonTime().checkLatestTime((int) (this.getTimeSinceDungeonStart() / 1000L), this, winners, validLooters);
        }
        if (this.getDungeonChallengesCompleted().size() > 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "(!) " + ChatColor.GOLD + "Their team has completed " + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + this.getDungeonChallengesCompleted().size() + ChatColor.GOLD + " Dungeon Challenges:");
            this.getDungeonChallengesCompleted().forEach(challenge -> Bukkit.broadcastMessage(" " + ChatColor.GOLD + ChatColor.BOLD + "* " + ChatColor.GREEN + challenge.getName()));
            Bukkit.broadcastMessage(ChatColor.GRAY + "Loot the challenge chests for bonus dungeon items!");
            Bukkit.broadcastMessage("");
            this.getPlayers().forEach(pl -> pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 8.0f, 0.6f));
            if (this.getDungeonChallengesCompleted().contains(DungeonChallenge.NIMBLE_FEET)) {
                factionIdsToCredit.forEach(tag -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fquests addexact DUNGEON_RACER_I " + tag));
            }
            if (this.getDungeonChallengesCompleted().contains(DungeonChallenge.SPEED_RACER)) {
                factionIdsToCredit.forEach(tag -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fquests addexact DUNGEON_RACER_II " + tag));
            }
            final AtomicInteger index = new AtomicInteger(0);
            final double percentageEligible = validLooters.size() / total;
            final boolean smallTeam = validLooters.size() <= 4;
            final LinkedList<Location> endChestLocations = new LinkedList<Location>();
            endChestLocations.add(new Location(this.getWorld(), 73.0, 196.0, 29.0));
            endChestLocations.add(new Location(this.getWorld(), 78.0, 196.0, 44.0));
            endChestLocations.add(new Location(this.getWorld(), 83.0, 196.0, 44.0));
            endChestLocations.add(new Location(this.getWorld(), 83.0, 196.0, 34.0));
            endChestLocations.add(new Location(this.getWorld(), 83.0, 196.0, 39.0));
            endChestLocations.add(new Location(this.getWorld(), 78.0, 196.0, 44.0));
            endChestLocations.add(new Location(this.getWorld(), 78.0, 196.0, 44.0));
            endChestLocations.add(new Location(this.getWorld(), 73.0, 196.0, 44.0));
            new BukkitRunnable() {

                public void run() {
                    if (DestroyedOutpostDungeon.this.getDungeonChallengesCompleted().isEmpty() || index.get() >= endChestLocations.size()) {
                        this.cancel();
                        return;
                    }
                    DungeonChallenge nextToAnnounce = DestroyedOutpostDungeon.this.getDungeonChallengesCompleted().stream().findFirst().orElse(null);
                    if (nextToAnnounce == null) {
                        this.cancel();
                        return;
                    }
                    int itemsPerChest = smallTeam ? ThreadLocalRandom.current().nextInt(2) + 1 : (percentageEligible <= 0.25 ? 1 : (percentageEligible <= 0.75 ? 2 : 3));
                    DestroyedOutpostDungeon.this.getDungeonChallengesCompleted().remove(nextToAnnounce);
                    Location nextToSet = endChestLocations.get(index.getAndIncrement());
                    Block block = nextToSet.getBlock();
                    block.setType(Material.CHEST);
                    DestroyedOutpostDungeon.this.fillChest(block, itemsPerChest, validLooters);
                    block.getWorld().playEffect(nextToSet.clone().add(0.5, 0.5, 0.5), Effect.STEP_SOUND, Material.CHEST.getId());
                    DestroyedOutpostDungeon.this.broadcastTitle(nextToAnnounce.getName(), "", ChatColor.GREEN, ChatColor.GOLD);
                    RandomFireworks.getManager().launchRandomFirework(nextToSet.add(0.5, 1.0, 0.5));
                }
            }.runTaskTimer(CosmicDungeonsPlugin.get_(), 20L, 20L);
        }
        if (firstToComplete) {
            for (Player pl2 : Bukkit.getServer().getOnlinePlayers()) {
                pl2.sendMessage("");
                pl2.sendMessage(CC.Gold + "As a reward for being the 1st group to defeat the Destroyed Outpost this map, all members of the party have received a bonus piece of the " + CC.DarkPurple + "Dimensional Traveler Armor Set" + CC.Gold + "!");
            }
        }
        Bukkit.broadcastMessage("");
    }

    @Override
    public DestroyedOutpostEvents getDungeonEvents() {
        return (DestroyedOutpostEvents) super.getDungeonEvents();
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_7_R4.ItemStack
 *  net.minecraft.server.v1_7_R4.NBTTagCompound
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_7_R4.me.astropvp.cratekeys.inventory.CraftItemStack
 *  org.bukkit.entity.EntityType
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 */
package me.ifamasssxd.cosmiccontests.struct.contests;

import com.google.common.collect.Lists;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.contests.*;
import me.ifamasssxd.cosmiccontests.struct.ContestManager;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import me.ifamasssxd.cosmiccontests.utils.ItemBuilder;
import me.ifamasssxd.cosmiccontests.utils.TimeUtils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public enum Contests {
    BLOCKS_MINED(new BlocksMinedContest(BlockEventType.BREAK, Material.SMOOTH_BRICK)),
    BLOCKS_PLACED(new BlockPlaceContest()),
    EXP_GAINED(new CustomDataContest("EXP Gained", "expGained", Material.EXP_BOTTLE)),
    SPECIFIC_MINED(new SpecificOreMinedContest()),
    ORE_MINED(new OreMinedContest()),
    TREE_CUTTER(new TreeCutterContest()),
    PASSIVE_MOBS_KILLED(new PassiveMobContests("Passive Mobs Killed", EntityType.UNKNOWN, Material.FEATHER)),
    AGGRESSIVE_MOBS_KILLED(new AgressiveMobKillContest("Aggressive Mobs Killed", EntityType.UNKNOWN, Material.DIAMOND_SWORD)),
    OTHER_DEMENTIONAL_MOBS_KILLED(new OtherDemensionalKillContest("Other Dimensional Mobs Killed", EntityType.UNKNOWN, Material.NETHERRACK)),
    MOB_SPECIFIC(new MobSpecificKillContest("%s Mobs Killed", EntityType.UNKNOWN, Material.DIAMOND)),
    MOBS_KILLED(new EntityKillContest("Mobs Killed", EntityType.UNKNOWN, Material.BONE)),
    RANKED_DUEL(new CustomDataContest("Ranked Duels Won", "rankedduels", Material.DIAMOND_SWORD)),
    BOOKS_OPENED(new CustomDataContest("Custom Enchant Books Opened", "beopened", Material.BOOK)),
    ENVOYS_BROKEN(new CustomDataContest("Envoy Chests Broken", "envBroken", Material.CHEST)),
    FISH_CAUGHT(new CustomDataContest("Fish Caught", "fishCaught", Material.RAW_FISH)),
    MCMMO_SKILL(new mcMMOSkillContest(Material.GOLD_SWORD), true, false),
    MOST_COINFLIP_WON(new CustomDataContest("Coinflips Won", "cfWon", Material.EMERALD), true, false),
    CF_MONEY_LOSS(new CustomDataContest("Money Lost in Coinflips", "cfMoneyLoss", new MaterialData(Material.ANVIL, (byte) 2)), true, false),
    CF_MONEY_WON(new CustomDataContest("Money Won in Coinflips", "cfMoneyWon", Material.PAPER), true, false),
    BOUNTY_HUNTER_KILLS(new CustomDataContest("Bounty Hunter Kills", "bHunterKills", new MaterialData(Material.SKULL_ITEM, (byte) 3)), false, true),
    EXP_AT_ENCHANTER(new CustomDataContest("XP Spent at Enchanter", "enchanterXP", Material.EXP_BOTTLE)),
    ALCHEMIST_EXCHANGES(new CustomDataContest("Alchemist Exchanges", "alchemistExch", Material.DIAMOND_CHESTPLATE)),
    BOSSES_KILLED(new CustomDataContest("Bosses Killed", "bossKills", new MaterialData(Material.MONSTER_EGG, (byte)EntityType.ENDERMAN.getTypeId()))),
    UNIQUE_KILLS(new CustomDataContest("Unique Player Kills", "uniqueKills", Material.DIAMOND_SWORD)),
    DAMAGE_DEALT(new CustomDataContest("Damage Inflicted on Players", "playerDamage", new MaterialData(Material.SKULL_ITEM, (byte) 3))),
    SOUL_DEMONS_KILLED(new CustomDataContest("Soul Demon Kills", "demonKill", Material.GOLD_SWORD)),
    COSMIC_CONQUEST_CHEST(new CustomDataContest("Cosmic Conquest Chest Hits", "chestHits", Material.CHEST)),
    TIME_IN_WARZONE(new CustomDataContest("Time Spent in Main Warzone", "warzoneTime", Material.DIAMOND_AXE)),
    PLAYER_HEADS(new CustomDataContest("Unique Player Heads Collected", "headsCollected", new MaterialData(Material.SKULL_ITEM, (byte) 3))),
    END_KNOCKOFF(new CustomDataContest("Players Knocked Off Edge of End", "endKnockoff", Material.ENDER_STONE), TimeUnit.DAYS.toMillis(1L)),
    END_MONSTERS_KILLED(new EndMonstersContest("Ender Monsters Killed", EntityType.UNKNOWN, Material.ENDER_PORTAL_FRAME), TimeUnit.DAYS.toMillis(1L)),
    END_TIME_SPENT(new CustomDataContest("Time Spent in End", "endTimeSpent", Material.DRAGON_EGG), TimeUnit.DAYS.toMillis(1L)),
    KILLS_IN_END(new CustomDataContest("Kills in End", "endKills", Material.DIAMOND_SWORD), TimeUnit.DAYS.toMillis(1L)),
    LOTTERY_TICKETS_BOUGHT(new CustomDataContest("Jackpot Tickets Bought", "lotteryTickets", Material.PAPER)),
    LOTTERY_MONEY_WON(new CustomDataContest("Jackpot Money Won", "lotteryMoneyWin", Material.GOLD_INGOT), true, false),
    LOTTERY_MONEY_SPENT(new CustomDataContest("Money Put in Jackpot", "lotteryMoneySpent", Material.GOLD_INGOT), true, false),
    DUNGEONS_ENTERED(new CustomDataContest("Dungeons Entered", "dungeonsEntered", Material.OBSIDIAN), TimeUnit.DAYS.toMillis(14L)),
    DUNGEONS_COMPLETED(new CustomDataContest("Dungeons Completed", "dungeonsCompleted", Material.CHEST), TimeUnit.DAYS.toMillis(14L)),
    PORTALS_CRAFTED(new CustomDataContest("Dungeon Portals Crafted", "portalsCreated", Material.EYE_OF_ENDER), TimeUnit.DAYS.toMillis(14L));

    private Contest contest;
    private boolean useLargeStorage = false;
    private long challengeResetDelay = -1L;
    private boolean retired = false;
    private static SimpleDateFormat format;

    private Contests(Contest contest, boolean largeStorage, boolean retired) {
        this(contest, largeStorage, -1L, retired);
    }

    private Contests(Contest contest) {
        this(contest, false, -1L, false);
    }

    private Contests(Contest contest, long challengeReset) {
        this(contest, false, challengeReset, false);
    }

    public static Contests getContestFromInnerKey(String name) {
        for (Contests contest : Contests.values()) {
            if (!(contest.getContest() instanceof CustomDataContest ? name.equals(((CustomDataContest)contest.getContest()).getKey()) : contest.name().equals(name))) continue;
            return contest;
        }
        return null;
    }

    public static Contests getFromName(String name) {
        for (Contests contest : Contests.values()) {
            if (!contest.getContest().getName().equals(name) && !contest.name().equals(name)) continue;
            return contest;
        }
        return null;
    }

    public ItemStack getDisplayItem(ContestUser player) {
        ItemStack item;
        net.minecraft.server.v1_8_R3.ItemStack nmsItem;
        ArrayList lore = Lists.newArrayList();
        lore.add((Object)ChatColor.GRAY + format.format(CosmicContests.get().getContestManager().getLastReset()));
        lore.add((Object)ChatColor.GRAY + "ends in ... " + (Object)ChatColor.GREEN + TimeUtils.getNextResetTimer());
        lore.add("");
        ContestManager manager = CosmicContests.get().getContestManager();
        LinkedHashMap<Integer, ContestUser> topUsers = manager.getTopUsers().get((Object)this);
        if (topUsers != null && !topUsers.isEmpty()) {
            ContestUser user;
            for (int i = 1; i <= 3 && (user = topUsers.get(i)) != null; ++i) {
                lore.add((Object)ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + i + ". " + (user.getFactionTag() != null ? (Object)ChatColor.GRAY + "[" + user.getFactionTag() + "] " : "") + (Object)ChatColor.GREEN + user.getName() + (Object)ChatColor.GRAY + " - " + String.format(this.getContest().getFormattedData(user), new Object[]{ChatColor.GRAY}));
            }
            lore.add("");
        } else {
            lore.add((Object)ChatColor.RED + "No players involved in contest!");
        }
        lore.add((Object)ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "---------------------");
        lore.add("");
        if (player != null) {
            Integer position = player.getTopRankings().get((Object)this);
            ArrayList toAdd = Lists.newArrayList();
            for (String str : String.format(this.getContest().getFormattedLine(player), (Object)ChatColor.YELLOW + ChatColor.BOLD.toString()).split("\\\\")) {
                toAdd.add(str);
            }
            lore.addAll(toAdd);
            lore.add("");
            lore.add((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "Ranking");
            lore.add(" " + (Object)ChatColor.GRAY + (position == null ? "N/A" : this.getFormattedNumber(position)));
        }
        NBTTagCompound comp = (nmsItem = CraftItemStack.asNMSCopy(item = ItemBuilder.buildItem(this.getContest().getShowMaterial(), 1, this.getContest().getData(), (Object)ChatColor.GREEN + ChatColor.BOLD.toString() + "Most " + this.getContest().getName(), lore))).hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        comp.setString("contest", this.name());
        nmsItem.setTag(comp);
        return CraftItemStack.asCraftMirror(nmsItem);
    }

    private String getFormattedNumber(int i) {
        int j = i % 10;
        int k = i % 100;
        if (j == 1 && k != 11) {
            return i + "st";
        }
        if (j == 2 && k != 12) {
            return i + "nd";
        }
        if (j == 3 && k != 13) {
            return i + "rd";
        }
        return i + "th";
    }

    private Contests(Contest contest, boolean useLargeStorage, long challengeResetDelay, boolean retired) {
        this.contest = contest;
        this.useLargeStorage = useLargeStorage;
        this.challengeResetDelay = challengeResetDelay;
        this.retired = retired;
    }

    public Contest getContest() {
        return this.contest;
    }

    public boolean isUseLargeStorage() {
        return this.useLargeStorage;
    }

    public long getChallengeResetDelay() {
        return this.challengeResetDelay;
    }

    public boolean isRetired() {
        return this.retired;
    }

    static {
        format = new SimpleDateFormat("MMMM dd, YYYY");
    }
}


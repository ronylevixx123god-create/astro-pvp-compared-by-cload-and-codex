/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.google.common.collect.Lists
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  net.minecraft.util.io.netty.util.internal.ConcurrentSet
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.SkullMeta
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmiccontests.struct;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.netty.util.internal.ConcurrentSet;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.contests.EntityKillContest;
import me.ifamasssxd.cosmiccontests.contests.Rerollable;
import me.ifamasssxd.cosmiccontests.listeners.InventoryListener;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockContest;
import me.ifamasssxd.cosmiccontests.struct.contests.BlockEventType;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.contests.CustomDataContest;
import me.ifamasssxd.cosmiccontests.struct.contests.MobSpecificKillContest;
import me.ifamasssxd.cosmiccontests.struct.contests.mcMMOSkillContest;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import me.ifamasssxd.cosmiccontests.utils.InventoryUtils;
import me.ifamasssxd.cosmiccontests.utils.ItemBuilder;
import me.ifamasssxd.cosmiccontests.utils.StringUtils;
import me.ifamasssxd.cosmiccontests.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

public class ContestManager {
    private Set<Contests> activeContests = new ConcurrentSet();
    private long lastTopUserScan = 0L;
    private volatile Map<Contests, LinkedHashMap<Integer, ContestUser>> topUsers = new LinkedHashMap<Contests, LinkedHashMap<Integer, ContestUser>>();
    private static int MAX_CONTESTS = 5;
    private long lastReset;
    private long nextResetTime;
    private int resetTimeSpan;
    private long lastMapResetTime = -1L;
    private TimeUnit resetUnit = TimeUnit.HOURS;

    public void loadContests(FileConfiguration configuration) {
        this.lastReset = configuration.getLong("lastReset", -1L);
        if (this.lastReset == -1L) {
            Calendar cal = Calendar.getInstance();
            cal.add(6, -1);
            cal.set(11, 24);
            cal.setTimeZone(TimeZone.getTimeZone("CST"));
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            this.lastReset = cal.getTimeInMillis();
            Bukkit.getLogger().info("Setting last reset to 00:00 of today: " + this.lastReset);
        }
        this.loadActiveContests(configuration);
        Bukkit.getLogger().info("Loaded " + this.activeContests.size() + " Active Contests...");
        this.resetTimeSpan = configuration.getInt("resetTimer");
        this.nextResetTime = this.lastReset + this.resetUnit.toMillis(this.resetTimeSpan);
        if (this.shouldReset()) {
            this.reset();
        }
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement statement = CosmicContests.get().getSqlDatabase().getConnection().prepareStatement("SELECT name, uuid, contestData FROM `" + CosmicContests.get().getServerName() + "_contestUsers`;");
                try {
                    ResultSet rs = statement.executeQuery();
                    while (rs.next()) {
                        FPlayer player;
                        ContestUser user = new ContestUser(rs.getString("name"), UUID.fromString(rs.getString("uuid")));
                        ContestUser.getUsers().put(user.getUuid(), user);
                        try {
                            user.loadData(rs.getString("contestData"));
                        }
                        catch (Exception e) {
                            Bukkit.getLogger().info("Error loading data for " + user.getName() + " (" + user.getUuid() + "), exception:");
                            e.printStackTrace();
                        }
                        if (!CosmicContests.get().isFactionsEnabled() || (player = FPlayers.getInstance().getById(user.getUuid().toString())) == null || !player.hasFaction() || !player.getFaction().isNormal()) continue;
                        user.setFactionTag(player.getFaction().getTag());
                    }
                }
                finally {
                    if (Collections.singletonList(statement).get(0) != null) {
                        statement.close();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.lastMapResetTime = configuration.getLong("lastMapResetTime", 0L);
        if (this.lastMapResetTime <= 0L) {
            this.lastMapResetTime = System.currentTimeMillis();
            configuration.set("lastMapResetTime", (Object)this.lastMapResetTime);
            CosmicContests.get().saveConfig();
            Bukkit.getLogger().info("storing " + this.lastMapResetTime + " as last reset time, will use this to exclude certain contests.");
        }
        Bukkit.getScheduler().scheduleAsyncRepeatingTask((Plugin)CosmicContests.get(), () -> this.loadTopUsers(false), 20L, (long)(20 * CosmicContests.get().getConfig().getInt("top-refresh", 30)));
    }

    public void recalcLastReset() {
        Calendar cal = Calendar.getInstance();
        cal.add(6, -1);
        cal.set(11, 24);
        cal.setTimeZone(TimeZone.getTimeZone("CST"));
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        this.lastReset = cal.getTimeInMillis();
        this.nextResetTime = this.lastReset + this.resetUnit.toMillis(this.resetTimeSpan);
        Bukkit.getLogger().info("Setting last reset to: " + this.lastReset);
    }

    public void loadTopUsers(boolean force) {
        if (!force)
            if (this.lastTopUserScan != 0L && System.currentTimeMillis() - this.lastTopUserScan < TimeUnit.SECONDS.toMillis(60L))
                return;
        this.lastTopUserScan = System.currentTimeMillis();
        boolean first = true;
        Map<Contests, ContestUser> previousTop = new HashMap<>();
        this.activeContests.forEach(cont -> {
            HashMap<Integer, ContestUser> users = this.topUsers.get(cont);
            if (users != null) {
                ContestUser topUser = users.get(Integer.valueOf(1));
                if (topUser != null)
                    previousTop.put(cont, topUser);
            }
        });
        for (Iterator<Contests> iterator = this.activeContests.iterator(); iterator.hasNext(); ) {
            Contests current = iterator.next();
            LinkedList<ContestUser> userList = new LinkedList<>();
            LinkedHashMap<Integer, ContestUser> userMap = new LinkedHashMap<>();
            if (first) {
                ContestUser.getUsers().values().forEach(user -> {
                    userList.add(user);
                    user.getTopRankings().clear();
                });
                first = false;
            } else {
                ContestUser.getUsers().values().forEach(userList::add);
            }
            userList.sort((o1, o2) -> {
                if (o1 != null && o2 != null) {
                    if (current.getContest() instanceof BlockContest) {
                        BlockContest contest = (BlockContest)current.getContest();
                        int o1b = o1.getInt(current, (contest.getBlockEventType() == BlockEventType.BREAK) ? "broken" : "placed");
                        int o2b = o2.getInt(current, (contest.getBlockEventType() == BlockEventType.BREAK) ? "broken" : "placed");
                        return (o1b == o2b) ? 0 : ((o1b < o2b) ? 1 : -1);
                    }
                    if (current.getContest() instanceof me.ifamasssxd.cosmiccontests.contests.EntityKillContest || current.getContest() instanceof me.ifamasssxd.cosmiccontests.struct.contests.MobSpecificKillContest) {
                        int o1b = o1.getInt(current, "mobsKilled");
                        int o2b = o2.getInt(current, "mobsKilled");
                        return (o1b == o2b) ? 0 : ((o1b < o2b) ? 1 : -1);
                    }
                    if (current.getContest() instanceof CustomDataContest) {
                        CustomDataContest contest = (CustomDataContest)current.getContest();
                        if (contest instanceof me.ifamasssxd.cosmiccontests.struct.contests.mcMMOSkillContest) {
                            double d1 = o1.getDouble(current, contest.getKey());
                            double d2 = o2.getDouble(current, contest.getKey());
                            return (d1 == d2) ? 0 : ((d1 < d2) ? 1 : -1);
                        }
                        double o1b = o1.getDouble(current, contest.getKey());
                        double o2b = o2.getDouble(current, contest.getKey());
                        return (o1b == o2b) ? 0 : ((o1b < o2b) ? 1 : -1);
                    }
                }
                return -1;
            });
            int rank = 1;
            for (ContestUser user : userList) {
                userMap.put(Integer.valueOf(rank), user);
                if (rank == 1) {
                    ContestUser previous = previousTop.get(current);
                    if (previous != null && !previous.getName().equals(user.getName()))
                        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicContests.get(), () -> Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + user.getName() + ChatColor.GRAY + " has " + ChatColor.GRAY + "taken the lead" + ChatColor.GRAY + " in the " + ChatColor.GREEN + ChatColor.BOLD + "\"Most " + current.getContest().getName() + ChatColor.GREEN + "\" /challenge" + ChatColor.GRAY + " with " + ChatColor.GREEN + ChatColor.stripColor(current.getContest().getFormattedSuffix(user)).toLowerCase() + ChatColor.GRAY + ", the challenge ends in " + ChatColor.GRAY + ChatColor.UNDERLINE + TimeUtils.getNextResetTimer() + ChatColor.GREEN + "!"));
                }
                user.getTopRankings().put(current, Integer.valueOf(rank));
                rank++;
            }
            this.topUsers.put(current, userMap);
        }
        if (shouldReset())
            reset();
    }

    private boolean shouldReset() {
        return this.nextResetTime <= System.currentTimeMillis();
    }

    public void saveActiveContests() {
        ArrayList str = Lists.newArrayList();
        this.activeContests.forEach(cont -> {
            String extraData = "";
            if (cont.getContest() instanceof Rerollable) {
                extraData = ((Rerollable)((Object)cont.getContest())).getExtraData();
            }
            str.add(cont.name() + extraData);
        });
        CosmicContests.get().getConfig().set("lastReset", (Object)this.lastReset);
        CosmicContests.get().getConfig().set("active", (Object)str);
        CosmicContests.get().saveConfig();
    }

    public void loadActiveContests(FileConfiguration configuration) {
        for (String active : configuration.getStringList("active")) {
            Contests contest;
            String extraData = "";
            if (active.contains("&")) {
                extraData = active.split("&")[1];
                active = active.split("&")[0];
            }
            if ((contest = Contests.getFromName(active)) == null) continue;
            if (contest.getContest() instanceof Rerollable) {
                ((Rerollable)((Object)contest.getContest())).loadData(extraData);
            }
            this.activeContests.add(contest);
        }
        if (this.activeContests.isEmpty()) {
            this.rerollActiveContests();
            this.saveActiveContests();
        }
    }

    public void reset() {
        if (this.shouldReset()) {
            this.lastReset += this.resetUnit.toMillis(this.resetTimeSpan);
            long nextReset = this.lastReset + this.resetUnit.toMillis(this.resetTimeSpan);
            Bukkit.getLogger().info("Resetting contests... From " + this.lastReset + " To  " + nextReset);
            this.nextResetTime = nextReset;
            Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CosmicContests.get(), () -> {
                this.loadTopUsers(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicContests.get(), () -> {
                    for (Contests active : this.activeContests) {
                        HashMap users = this.topUsers.get((Object)active);
                        if (users == null) continue;
                        Bukkit.broadcastMessage((String)"");
                        Bukkit.broadcastMessage((String)StringUtils.centerText((Object)ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + active.getContest().getName()));
                        for (int i = 1; i <= 3; ++i) {
                            ContestUser user = (ContestUser)users.get(i);
                            if (user == null) continue;
                            Bukkit.broadcastMessage((String)StringUtils.centerText((Object)ChatColor.GREEN + ChatColor.BOLD.toString() + i + ". " + (Object)ChatColor.GREEN + user.getName() + " - " + (Object)ChatColor.GREEN + (Object)ChatColor.BOLD + String.format(active.getContest().getFormattedData(user), (Object)ChatColor.GREEN + ChatColor.BOLD.toString())));
                        }
                    }
                    this.clearUsersAndContests();
                    Bukkit.broadcastMessage((String)((Object)ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.GREEN + "New Challenges have started, use /challenges to view rankings!"));
                });
            });
        }
    }

    public void clearUsersAndContests() {
        this.topUsers.forEach((contests, topUsers) -> {
            ContestUser topUser;
            for (int i = 1; i <= 3 && (topUser = (ContestUser)topUsers.get(i)) != null; ++i) {
                if (i == 1) {
                    Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("fquests add DAILY_CHALLENGE_MASTER " + topUser.getFactionTag()));
                }
                try {
                    this.givePrize(topUser, (Contests)((Object)contests), i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(topUser.getUuid(), "DAILY_CHALLENGE_TOP_" + i, new Object[]{contests.name()}));
            }
        });
        int reset = 0;
        for (ContestUser user : ContestUser.getUsers().values()) {
            user.getContestData().clear();
            user.getTopRankings().clear();
            ++reset;
        }
        CosmicContests.get().getSqlDatabase().executeUpdate("UPDATE `" + CosmicContests.get().getServerName() + "_contestUsers` SET contestData = NULL;", true);
        Bukkit.getLogger().info("Cleared " + reset + " loaded contest me.astropvp.cratekeys.data.");
        Bukkit.getLogger().info("CLEARING ALL CONTEST DATA FROM TABLE...");
        this.lastTopUserScan = 0L;
        this.topUsers.clear();
        this.rerollActiveContests();
        this.saveActiveContests();
    }

    public void openContestInventory(Player player) {
        ContestUser user = ContestUser.getUser(player.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, (int)27, (String)"Daily Challenges");
        int slot = 0;
        for (int place = 1; place <= 3; ++place) {
            inv.setItem(slot, this.createPrizeDisplay(place));
            slot += 9;
        }
        slot = 11;
        for (Contests contest : this.getActiveContests()) {
            ItemStack item = contest.getDisplayItem(user);
            inv.setItem(slot++, item);
        }
        player.openInventory(inv);
        InventoryListener.contestViewers.add(player.getUniqueId());
    }

    private ItemStack createPrizeDisplay(int ranking) {
        ArrayList lore = Lists.newArrayList();
        if (ranking == 1) {
            lore.add((Object)ChatColor.RED + ChatColor.BOLD.toString() + " * " + (Object)ChatColor.RED + "1x " + (Object)ChatColor.RED + "Godly Items");
            lore.add((Object)ChatColor.GOLD + ChatColor.BOLD.toString() + " * " + (Object)ChatColor.GOLD + "8x " + (Object)ChatColor.GOLD + "Legendary Items");
            lore.add((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + " * " + (Object)ChatColor.YELLOW + "70,000 EXP");
        } else if (ranking == 2) {
            lore.add((Object)ChatColor.GOLD + ChatColor.BOLD.toString() + " * " + (Object)ChatColor.GOLD + "6x " + (Object)ChatColor.GOLD + "Legendary Items");
            lore.add((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + " * " + (Object)ChatColor.YELLOW + "30,000 EXP");
        } else if (ranking == 3) {
            lore.add((Object)ChatColor.GOLD + ChatColor.BOLD.toString() + " * " + (Object)ChatColor.GOLD + "3x " + (Object)ChatColor.GOLD + "Legendary Items");
            lore.add((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + " * " + (Object)ChatColor.YELLOW + "15,000 EXP");
        }
        ItemStack item = ItemBuilder.buildItem(ranking == 1 ? Material.EMERALD : (ranking == 2 ? Material.DIAMOND : Material.IRON_INGOT), (short)0, ChatColor.GREEN.toString() + (Object)ChatColor.BOLD + ChatColor.UNDERLINE.toString() + StringUtils.getFormattedNumber(ranking) + (Object)ChatColor.GREEN + " Place Prize", lore);
        item.setAmount(ranking);
        return item;
    }

    public void rerollActiveContests() {
        this.activeContests.clear();
        List<Contests> contests = Lists.newArrayList(Contests.values());
        Collections.shuffle(contests);
        boolean pickedEntity = false, pickedJackpot = false;
        for (Contests contest : contests) {
            if (contest.isRetired())
                continue;
            if (this.activeContests.size() >= MAX_CONTESTS)
                break;
            long time;
            if (contest.getChallengeResetDelay() != -1L && this.lastMapResetTime > 0L && (time = System.currentTimeMillis() - this.lastMapResetTime) < contest.getChallengeResetDelay()) {
                Bukkit.getLogger().info("Excluding " + contest.getContest().getName() + " due to it only being " + time + " ms since reset (" + ((contest
                        .getChallengeResetDelay() - time) / 1000L) + "s left)");
                continue;
            }
            if (pickedEntity && contest.getContest() instanceof me.ifamasssxd.cosmiccontests.contests.EntityKillContest)
                continue;
            if (contest == Contests.LOTTERY_MONEY_SPENT || contest == Contests.LOTTERY_MONEY_WON) {
                if (pickedJackpot)
                    continue;
                pickedJackpot = true;
            }
            if (contest.getContest() instanceof Rerollable)
                ((Rerollable)contest.getContest()).reroll();
            if (contest.getContest() instanceof me.ifamasssxd.cosmiccontests.contests.EntityKillContest)
                pickedEntity = true;
            this.activeContests.add(contest);
            Bukkit.getLogger().info("Choosing Contest: " + contest.getContest().getName());
        }
    }

    public Inventory createTop(Contests contest) {
        Map users = CosmicContests.get().getContestManager().getTopUsers().get((Object)contest);
        Inventory inventory = Bukkit.createInventory(null, (int)(users == null ? 9 : InventoryUtils.getSize(users.size())), (String)"Challenge Leaderboard");
        if (users == null) {
            return inventory;
        }
        for (int ranking = 1; ranking <= 50 && ranking <= inventory.getSize(); ++ranking) {
            ContestUser user = (ContestUser)users.get(ranking);
            if (user == null) continue;
            ItemStack item = new ItemStack(Material.SKULL_ITEM, ranking, (short) 3);
            SkullMeta sm = (SkullMeta)item.getItemMeta();
            if (user.getName() == null) continue;
            sm.setOwner(user.getName());
            sm.setDisplayName((Object)ChatColor.GREEN + ChatColor.BOLD.toString() + user.getName());
            ArrayList lore = Lists.newArrayList();
            lore.add("");
            lore.add((Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "Ranking");
            lore.add((Object)ChatColor.GRAY + "  " + StringUtils.getFormattedNumber(ranking));
            lore.add("");
            ArrayList toAdd = Lists.newArrayList();
            for (String line : String.format(contest.getContest().getFormattedLine(user), (Object)ChatColor.YELLOW + ChatColor.BOLD.toString()).split("\\\\")) {
                if (line.startsWith(" ")) {
                    line = " " + line;
                }
                toAdd.add(line);
            }
            lore.addAll(toAdd);
            sm.setLore((List)lore);
            item.setItemMeta((ItemMeta)sm);
            inventory.addItem(new ItemStack[]{item});
        }
        return inventory;
    }

    public void givePrize(ContestUser user, Contests contest, int ranking) throws IOException {
       return;
    }

    public Set<Contests> getActiveContests() {
        return this.activeContests;
    }

    public Map<Contests, LinkedHashMap<Integer, ContestUser>> getTopUsers() {
        return this.topUsers;
    }

    public long getLastReset() {
        return this.lastReset;
    }

    public long getNextResetTime() {
        return this.nextResetTime;
    }

    public void setLastMapResetTime(long lastMapResetTime) {
        this.lastMapResetTime = lastMapResetTime;
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_7_R4.ItemStack
 *  net.minecraft.server.v1_7_R4.NBTTagCompound
 *  net.minecraft.util.com.google.common.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.libs.com.google.gson.Gson
 *  org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder
 *  org.bukkit.craftbukkit.v1_7_R4.me.astropvp.cratekeys.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package net.astropvp.core.cosmiccoinflip.struct;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.google.common.collect.Lists;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.astropvp.core.cosmiccoinflip.listeners.PlayerListener;
import net.astropvp.core.cosmiccoinflip.utils.ItemBuilder;
import net.astropvp.core.cosmiccoinflip.utils.SlotUtils;
import net.astropvp.core.AstroCore;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CoinFlipManager {
    private Set<CoinFlipMatch> coinFlipMatches = new HashSet<CoinFlipMatch>();
    private Map<UUID, WinLossStats> winLossStats = new HashMap<UUID, WinLossStats>();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private File flipRecords;
    private DecimalFormat format = new DecimalFormat("#,###");
    private List<UUID> toggledNotifications = Lists.newArrayList();
    private Map<UUID, UUID> previousOpponent = new HashMap<UUID, UUID>();
    private int alertAmount = 1000000;
    private double taxCollected = 0.0;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public void loadToggles() {
        List<String> toggles = AstroCore.getInstance().getConfig().getStringList("toggles");
        for (String string : toggles) {
            try {
                this.toggledNotifications.add(UUID.fromString(string));
            }
            catch (Exception e) {
                Bukkit.getLogger().info("unable to load uuid in toggles: " + string);
                e.printStackTrace();
            }
        }
    }

    public void clearStats() throws IOException {
        this.winLossStats.clear();
        this.saveCoinFlipRecords();
        Bukkit.getLogger().info("Clearing all CoinFlip stats...");
    }

    public void saveToggles() {
        ArrayList toggles = Lists.newArrayList();
        this.toggledNotifications.forEach(id -> toggles.add(id.toString()));
        AstroCore.getInstance().getConfig().set("toggles", (Object)toggles);
        AstroCore.getInstance().saveConfig();
    }

    public Inventory getLobbyInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, (int)SlotUtils.fitSlots((int)this.coinFlipMatches.stream().filter(match -> match.getMatchState() == CoinFlipMatch.MatchState.WAITING).count() + 2), (String)"Coin Flip Matches");
        this.updateInventory(player, inventory);
        return inventory;
    }

    public void updateInventory(Player player, Inventory inventory) {
        inventory.clear();
        this.coinFlipMatches.forEach(match -> {
            String winLoss = null;
            if (match.getMatchState() != CoinFlipMatch.MatchState.WAITING) {
                return;
            }
            double balance = AstroCore.getInstance().getEcon().getBalance((OfflinePlayer)player);
            WinLossStats stats = AstroCore.getInstance().getCoinFlipManager().getWinLossStats().get(match.getStarterPlayer().getUniqueId());
            String string = stats == null ? "0 (0:0)" : (winLoss = (stats.getLosses() == 0 ? Integer.valueOf(stats.getWins()) : this.decimalFormat.format((double) stats.getWins() / Double.valueOf(stats.getLosses()))) + " (" + stats.getWins() + ":" + stats.getLosses() + ")");
            String dailyWinLoss = stats == null ? "0 (0:0)" : (stats.getDailyLosses() == 0 ? Integer.valueOf(stats.getDailyWins()) : this.decimalFormat.format((double) stats.getDailyWins() / Double.valueOf(stats.getDailyLosses()))) + " (" + stats.getDailyWins() + ":" + stats.getDailyLosses() + ")";
            ItemStack stack = ItemBuilder.createSkull(match.getStarterPlayer().getName(), FactionUtils.getFactionTag(player, match.getStarterPlayer()) + " " + ChatColor.BOLD.toString() + match.getStarterPlayer().getDisplayName(), "", (Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "Wager", (Object)ChatColor.GRAY + " " + (Object)ChatColor.GRAY + (Object)ChatColor.BOLD + "$" + (Object)ChatColor.GRAY + this.format.format(match.getWager()), (Object)ChatColor.GRAY + ChatColor.ITALIC.toString() + " -5% Tax " + (Object)ChatColor.GRAY + "($" + this.format.format(match.getTax()) + ")", "", (Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "Color Chosen", match.getFirstColor().getName(), "", (Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "Recent Win/Loss Ratio", (Object)ChatColor.GRAY + " " + dailyWinLoss, "", (Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "Total Win/Loss Ratio", (Object)ChatColor.GRAY + " " + winLoss, "", balance >= match.getWager() ? (Object)ChatColor.GRAY + "Click to accept this coin flip wager" : (Object)ChatColor.GRAY + "You can " + (Object)ChatColor.RED + (Object)ChatColor.BOLD + "NOT" + (Object)ChatColor.GRAY + " afford this wager!");
            net.minecraft.server.v1_8_R3.ItemStack item = CraftItemStack.asNMSCopy((ItemStack)stack);
            NBTTagCompound tag = item.getTag();
            if (tag == null) {
                tag = new NBTTagCompound();
                item.setTag(tag);
            }
            tag.setString("wager", match.getStarterPlayer().getName());
            inventory.addItem(new ItemStack[]{CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack)item)});
        });
        inventory.setItem(inventory.getSize() - 2, ItemBuilder.buildItem(Material.BOOK, ChatColor.GREEN.toString() + (Object)ChatColor.BOLD + "Coin Flip Help", "", (Object)ChatColor.YELLOW + "/coinflip", (Object)ChatColor.GRAY + "View all active Coin Flip matches.", "", (Object)ChatColor.YELLOW + "/coinflip <amount>", (Object)ChatColor.GRAY + "Start a Coin Flip match with the given wager.", "", (Object)ChatColor.YELLOW + "/coinflip cancel", (Object)ChatColor.GRAY + "Cancel the pending Coin Flip match.", "", (Object)ChatColor.YELLOW + "/coinflip toggle", (Object)ChatColor.GRAY + "Toggle Coin Flip win notifications.", "", (Object)ChatColor.YELLOW + "/coinflip stats", (Object)ChatColor.GRAY + "View your Coinflip stats with /coinflip stats"));
        inventory.setItem(inventory.getSize() - 1, ItemBuilder.buildItem(Material.CHEST, (Object)ChatColor.GREEN + ChatColor.BOLD.toString() + "Refresh", (Object)ChatColor.GRAY + "Click to refresh coinflip matches."));
    }

    public Inventory getCoinFlipSelectMenu(double wage, CoinColor picked) {
        Inventory inventory = Bukkit.createInventory(null, (int)SlotUtils.fitSlots(CoinColor.values().length), (String)"Choose a Color");
        for (CoinColor color : CoinColor.values()) {
            if (picked != null && color.equals((Object)picked)) continue;
            inventory.addItem(new ItemStack[]{ItemBuilder.buildItem(Material.WOOL, (short)color.getDyeColor().getWoolData(), color.getName(), (Object)ChatColor.GRAY + "Click to select this color", (Object)ChatColor.GRAY + "and start the coin clip for", (Object)ChatColor.GREEN + ChatColor.BOLD.toString() + "$" + (Object)ChatColor.GREEN + this.format.format(wage))});
        }
        return inventory;
    }

    public CoinFlipMatch getMatch(Player player) {
        for (CoinFlipMatch match : this.coinFlipMatches) {
            if (!match.getStarterPlayer().equals((Object)player) && (match.getSecondPlayer() == null || !match.getSecondPlayer().equals((Object)player))) continue;
            return match;
        }
        return null;
    }

    public String getWinLossRatio(UUID uuid, String delimeter) {
        WinLossStats stats = this.winLossStats.get(uuid);
        if (stats == null) {
            return "0" + delimeter + "0";
        }
        return stats.getDailyWins() + delimeter + stats.getDailyLosses();
    }

    public void addWin(UUID uuid, UUID loser, double winnings, double tax) {
        WinLossStats stats = this.winLossStats.computeIfAbsent(uuid, k -> new WinLossStats());
        stats.setWins(stats.getWins() + 1);
        stats.setWinnings((long)((double)stats.getWinnings() + (winnings /= 2.0)));
        stats.setDailyWinnings((long)((double)stats.getDailyWinnings() + winnings));
        stats.setDailyWins(stats.getDailyWins() + 1);
        stats.setTaxesPaid((long)((double)stats.getTaxesPaid() + tax));
        this.taxCollected += tax;
        AstroCore.getInstance().getConfig().set("taxCollected", (Object)this.taxCollected);
        AstroCore.getInstance().saveConfig();
        UUID lastOppon = this.previousOpponent.get(uuid);
        if (lastOppon != null && lastOppon.equals(loser)) {
            return;
        }
        if (PlayerListener.hasSameIP(uuid, loser)) {
            Bukkit.getLogger().info("Skipping coinflip WIN contest tracking for " + uuid + " vs " + loser + " due to same IP Address.");
            return;
        }
        this.previousOpponent.put(uuid, loser);
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("contest add " + uuid.toString() + " cfWon 1"));
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("contest add " + uuid.toString() + " cfMoneyWon " + Math.round(winnings)));
    }

    public void addLoss(UUID uuid, UUID winner, double lost) {
        WinLossStats stats = this.winLossStats.computeIfAbsent(uuid, k -> new WinLossStats());
        stats.setLosses(stats.getLosses() + 1);
        stats.setLosings((long)((double)stats.getLosings() + lost));
        stats.setDailyLosings((long)((double)stats.getDailyLosings() + lost));
        stats.setDailyLosses(stats.getDailyLosses() + 1);
        stats.setTaxesPaid((long)((double)stats.getTaxesPaid() + lost * CoinFlipMatch.TAX));
        Player loserPlayer = Bukkit.getPlayer((UUID)uuid);
        if (loserPlayer != null) {
            Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(loserPlayer, "CF_LOSE", new Object[]{lost, winner}));
        } else {
            Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(uuid, "CF_LOSE", new Object[]{lost, winner}));
        }
        UUID lastOppon = this.previousOpponent.get(uuid);
        if (lastOppon != null && lastOppon.equals(winner)) {
            return;
        }
        if (PlayerListener.hasSameIP(uuid, winner)) {
            Bukkit.getLogger().info("Skipping coinflip loss contest tracking for " + uuid + " due to same IP Address.");
            return;
        }
        this.previousOpponent.put(uuid, winner);
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("contest add " + uuid.toString() + " cfMoneyLoss " + Math.round(lost)));
    }

    public void loadCoinFlips() throws IOException {
        String line;
        this.flipRecords = new File(AstroCore.getInstance().getDataFolder(), "flipRecords.json");
        if (!this.flipRecords.exists()) {
            this.flipRecords.createNewFile();
        }
        StringBuilder jsonString = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(this.flipRecords));
        while ((line = reader.readLine()) != null) {
            jsonString.append(line);
        }
        this.winLossStats = (Map)this.gson.fromJson(jsonString.toString(), new TypeToken<HashMap<UUID, WinLossStats>>(){}.getType());
        if (this.winLossStats == null) {
            this.winLossStats = new HashMap<UUID, WinLossStats>();
        }
        this.winLossStats.values().forEach(WinLossStats::clearDailyStats);
        this.alertAmount = AstroCore.getInstance().getConfig().getInt("alertAmount");
        if (this.alertAmount == 0) {
            this.alertAmount = 1000000;
        }
        this.taxCollected = AstroCore.getInstance().getConfig().getDouble("taxCollected");
    }

    public void saveCoinFlipRecords() throws IOException {
        String map = this.gson.toJson(this.winLossStats);
        FileWriter writer = new FileWriter(this.flipRecords);
        try {
            writer.write(map);
            writer.flush();
        }
        finally {
            if (Collections.singletonList(writer).get(0) != null) {
                writer.close();
            }
        }
    }

    public Set<CoinFlipMatch> getCoinFlipMatches() {
        return this.coinFlipMatches;
    }

    public Map<UUID, WinLossStats> getWinLossStats() {
        return this.winLossStats;
    }

    public List<UUID> getToggledNotifications() {
        return this.toggledNotifications;
    }

    public void setAlertAmount(int alertAmount) {
        this.alertAmount = alertAmount;
    }

    public int getAlertAmount() {
        return this.alertAmount;
    }

    public double getTaxCollected() {
        return this.taxCollected;
    }

    public void setTaxCollected(double taxCollected) {
        this.taxCollected = taxCollected;
    }
}


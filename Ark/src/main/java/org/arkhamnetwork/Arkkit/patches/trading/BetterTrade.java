package org.arkhamnetwork.Arkkit.patches.trading;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BetterTrade extends ArkPlugin {
    private final Map<UUID, PlayerTrade> playerTradeMap = (new MapMaker()).concurrencyLevel(32).makeMap();
    private BukkitTask tradeTask;
    private Essentials ess;

    public BetterTrade(Plugin p) {
        super(p);
    }

    public void enable() throws IOException {
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            this.ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
        }

        this.registerListener(new BetterTradeListener(this));
        this.registerCommand(new BetterTradeCommand(this, "trade", "/<command>", "Trade items with other players!", Lists.newArrayList("trade", "trades", "exchange")));
        this.tradeTask = (new BukkitRunnable() {
            public void run() {
                BetterTrade.this.playerTradeMap.values().forEach(PlayerTrade::onTradeTick);
            }
        }).runTaskTimer(Arkkit.get(), 13L, 13L);
        super.enable();
    }

    public void disable() {
        if (this.playerTradeMap.size() > 0) {
            Bukkit.getLogger().info("Cancelling " + this.playerTradeMap.values() + " player trades.");
            this.playerTradeMap.values().forEach((trade) -> {
                trade.cancelTrade((Player)null);
            });
        }

        this.unregisterListeners();
        this.unregisterCommands();
        if (this.tradeTask != null) {
            this.tradeTask.cancel();
        }

    }

    public PlayerTrade getCurrentTrade(Player player) {
        return (PlayerTrade)this.playerTradeMap.get(player.getUniqueId());
    }

    public void unregisterTrade(PlayerTrade trade) {
        this.playerTradeMap.remove(trade.getPlayer1().getUniqueId());
        this.playerTradeMap.remove(trade.getPlayer2().getUniqueId());
    }

    public void startPlayerTrade(Player player, Player player2) {
        //this.clearTeleportRequests(player);
        //this.clearTeleportRequests(player2);
        player.closeInventory();
        player2.closeInventory();
        PlayerTrade trade = new PlayerTrade(player, player2);
        this.playerTradeMap.put(player.getUniqueId(), trade);
        this.playerTradeMap.put(player2.getUniqueId(), trade);
        trade.openTradeMenu();
        trade.playSound(Sound.WOOD_CLICK, 1.0F, 0.8F);
        player.sendMessage((String)(ChatColor.YELLOW + "Trading with " + ChatColor.BOLD + player2.getName() + ChatColor.YELLOW + "..."));
        player2.sendMessage((String)(ChatColor.YELLOW + "Trading with " + ChatColor.BOLD + player.getName() + ChatColor.YELLOW + "..."));
    }

    private void clearTeleportRequests(Player p) {
        if (this.ess != null) {
            try {
                User u = this.ess.getUser(p);
                if (u.getTeleportRequest() != null) {
                    u.requestTeleport((User)null, false);
                    Bukkit.getLogger().info("[Arkkit (Trade)] Cleared Teleport Request for " + p.getName());
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }

    public Map<UUID, PlayerTrade> getPlayerTradeMap() {
        return this.playerTradeMap;
    }
}

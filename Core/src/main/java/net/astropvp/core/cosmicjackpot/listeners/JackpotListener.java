/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.CosmicUtils
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.HCFOutpostUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.astropvp.core.cosmicjackpot.listeners;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.HCFOutpostUtils;
import net.astropvp.core.cosmicjackpot.struct.Jackpot;
import net.astropvp.core.cosmicjackpot.struct.JackpotHistory;
import net.astropvp.core.cosmicjackpot.struct.JackpotManager;
import net.astropvp.core.AstroCore;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class JackpotListener
implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.getPlayer().removeMetadata("jackpotTicket", AstroCore.getInstance());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase("Confirm Ticket Purchase")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() != Material.STAINED_GLASS_PANE) {
                return;
            }
            Player player = (Player)event.getWhoClicked();
            if (!player.hasMetadata("jackpotTicket") || player.getMetadata("jackpotTicket").size() <= 0) {
                player.closeInventory();
                return;
            }
            int ticketsToPurchase = player.getMetadata("jackpotTicket").get(0).asInt();
            player.removeMetadata("jackpotTicket", AstroCore.getInstance());
            short data = clicked.getData().getData();
            if (data == DyeColor.LIME.getWoolData()) {
                player.closeInventory();
                this.confirmPurchase(player, ticketsToPurchase);
            } else if (data == DyeColor.RED.getWoolData()) {
                player.closeInventory();
                player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Jackpot Ticket Purchase cancelled.");
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerTalk(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("jackpotTicket") && player.getMetadata("jackpotTicket").size() > 0) {
            int ticketsToPurchase = player.getMetadata("jackpotTicket").get(0).asInt();
            player.removeMetadata("jackpotTicket", AstroCore.getInstance());
            if (ticketsToPurchase < 0) {
                return;
            }
            event.setCancelled(true);
            if (!ChatColor.stripColor(event.getMessage()).equalsIgnoreCase("confirm")) {
                player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Jackpot Ticket Purchase cancelled.");
                return;
            }
            event.setMessage("");
            this.confirmPurchase(player, ticketsToPurchase);
        }
    }

    private void confirmPurchase(Player player, int ticketsToPurchase) {
        JackpotManager manager = AstroCore.getInstance().getJackpotManager();
        Jackpot current = manager.getCurrentJackpot();
        double price = ticketsToPurchase * current.getTicketPrice();
        if (Arkkit.economy.getBalance(player) < price) {
            player.sendMessage(ChatColor.RED + "You no longer have $" + manager.getMoneyFormat().format(price) + " required to purchase " + ticketsToPurchase + " tickets!");
            return;
        }
        int adjustedTickets = ticketsToPurchase;
        if (CosmicUtils.get().isCosmicGalaxy() && HCFOutpostUtils.doesControlOutpost(player, "frozen")) {
            adjustedTickets = (int)((double)adjustedTickets + (double)adjustedTickets * 0.1);
        }
        current.purchaseTicket(player, ticketsToPurchase, adjustedTickets);
        player.sendMessage(ChatColor.GREEN + "You have purchased " + ticketsToPurchase + " ticket" + (ticketsToPurchase > 1 ? "s" : "") + " for " + ChatColor.GREEN + ChatColor.BOLD.toString() + "$" + ChatColor.GREEN + manager.getMoneyFormat().format(price) + "!");
        if (adjustedTickets != ticketsToPurchase) {
            player.sendMessage(CC.Green + "You have received " + CC.GreenB + (adjustedTickets - ticketsToPurchase) + CC.Green + " bonus ticket(s)! " + CC.GreenB + "(SRC: " + CC.Green + "Frozen Stronghold" + CC.GreenB + ")");
            player.sendMessage("");
        }
        player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.GRAY + ChatColor.UNDERLINE.toString() + "/jackpot" + ChatColor.GRAY + " to view current Jackpot information!");
        player.sendMessage("");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.4f);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "contest add " + player.getUniqueId() + " lotteryTickets " + ticketsToPurchase);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "contest add " + player.getUniqueId() + " lotteryMoneySpent " + price);
        Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(player.getUniqueId(), "JACKPOT_BUY", ticketsToPurchase, price));
        JackpotHistory history = manager.getJackpotHistory().computeIfAbsent(player.getUniqueId(), e -> new JackpotHistory(player.getName()));
        history.setTicketsPurchased(history.getTicketsPurchased() + (double)ticketsToPurchase);
    }
}


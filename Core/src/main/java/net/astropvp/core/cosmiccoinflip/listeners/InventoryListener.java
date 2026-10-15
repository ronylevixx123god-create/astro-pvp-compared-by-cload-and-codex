/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.ItemStack
 *  net.minecraft.server.v1_7_R4.NBTTagCompound
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.craftbukkit.v1_7_R4.me.astropvp.cratekeys.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.astropvp.core.cosmiccoinflip.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.astropvp.core.cosmiccoinflip.struct.CoinColor;
import net.astropvp.core.cosmiccoinflip.struct.CoinFlipManager;
import net.astropvp.core.cosmiccoinflip.struct.CoinFlipMatch;
import net.astropvp.core.AstroCore;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class InventoryListener
implements Listener {
    private final Map<UUID, CoinFlipMatch> choosingColors = new HashMap<UUID, CoinFlipMatch>();

    @EventHandler
    public void onPLayerClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        this.choosingColors.remove(player.getUniqueId());
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player clicked;
        if (event.getRightClicked() instanceof Player && (clicked = (Player)event.getRightClicked()).getName().endsWith("Coin Flipper") && clicked.hasMetadata("NPC")) {
            event.setCancelled(true);
            event.getPlayer().chat("/cf");
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (event.getInventory().getName().startsWith("Coin Flip Match") && !event.getInventory().getName().equals("Coin Flip Matches")) {
            event.setCancelled(true);
            return;
        }
        if (event.getInventory().getName().equals("Choose a Color")) {
            double amount;
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            CoinFlipManager manager = AstroCore.getInstance().getCoinFlipManager();
            CoinFlipMatch joining = this.choosingColors.get(player.getUniqueId());
            boolean joiningMatch = joining != null;
            CoinFlipMatch activeMatch = manager.getMatch(player);
            if (activeMatch != null && !joiningMatch) {
                player.sendMessage(ChatColor.RED + "You already have a pending coin flip!");
                return;
            }
            if (!(player.hasMetadata("coinFlipWage") && player.getMetadata("coinFlipWage").size() > 0 || joiningMatch)) {
                return;
            }
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() != Material.WOOL) {
                return;
            }
            CoinColor chosen = CoinColor.getFromDyeColor(item.getDurability());
            if (chosen == null) {
                return;
            }
            double d = amount = joiningMatch ? joining.getWager() : player.getMetadata("coinFlipWage").get(0).asDouble();
            if (AstroCore.getInstance().getEcon().has(player, amount)) {
                if (joiningMatch) {
                    if (joining.getMatchState() != CoinFlipMatch.MatchState.WAITING) {
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Coin Flip match no longer valid!");
                        return;
                    }
                    if (!manager.getCoinFlipMatches().contains(joining)) {
                        player.sendMessage(ChatColor.RED + "Coin flip match no longer valid.");
                        player.closeInventory();
                        return;
                    }
                    if (chosen == joining.getFirstColor()) {
                        player.sendMessage(ChatColor.RED + "Color already taken!");
                        return;
                    }
                    joining.setSecondColor(chosen);
                    joining.onAcceptMatch(player);
                    return;
                }
                CoinFlipMatch newMatch = new CoinFlipMatch(player, amount, chosen);
                player.closeInventory();
                manager.getCoinFlipMatches().add(newMatch);
                player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "Coin Flip queued, waiting for opponent to /coinflip.");
                player.sendMessage(ChatColor.GRAY + "To cancel your Coin Flip match, use /coinflip cancel");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have $" + amount + " anymore!");
            }
            player.removeMetadata("coinFlipWage", AstroCore.getInstance());
        } else if (event.getInventory().getName().equals("Coin Flip Matches")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            if (event.getRawSlot() <= event.getInventory().getSize() - 1 && event.getCurrentItem() != null) {
                String name;
                Player pl;
                NBTTagCompound tag;
                ItemStack item = event.getCurrentItem();
                if (item == null || item.getType() == Material.AIR) {
                    return;
                }
                if (item.getType() == Material.CHEST) {
                    Long cd;
                    Long l = cd = player.hasMetadata("cf_refresh") && player.getMetadata("cf_refresh").size() > 0 ? Long.valueOf(player.getMetadata("cf_refresh").get(0).asLong()) : null;
                    if (cd != null && cd > System.currentTimeMillis()) {
                        player.sendMessage(ChatColor.RED + "Please wait 3 seconds in between refreshes!");
                        return;
                    }
                    player.closeInventory();
                    player.setMetadata("cf_refresh", new FixedMetadataValue(AstroCore.getInstance(), System.currentTimeMillis() + 3000L));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(AstroCore.getInstance(), () -> player.openInventory(AstroCore.getInstance().getCoinFlipManager().getLobbyInventory(player)), 1L);
                    return;
                }
                net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                if (nmsItem.hasTag() && (tag = nmsItem.getTag()) != null && tag.hasKey("wager") && (pl = Bukkit.getPlayer(name = tag.getString("wager"))) != null) {
                    CoinFlipManager manager = AstroCore.getInstance().getCoinFlipManager();
                    CoinFlipMatch match = manager.getMatch(pl);
                    if (match == null) {
                        event.getInventory().removeItem(item);
                    } else if (match.getMatchState() == CoinFlipMatch.MatchState.WAITING) {
                        if (manager.getMatch(player) != null && !player.getName().equals("iFamasssxD")) {
                            player.sendMessage(ChatColor.RED + "You are already in a Coin Flip Match!");
                            player.closeInventory();
                            return;
                        }
                        if (AstroCore.getInstance().getEcon().has(player, match.getWager())) {
                            player.closeInventory();
                            this.choosingColors.put(player.getUniqueId(), match);
                            player.openInventory(manager.getCoinFlipSelectMenu(match.getWager(), match.getFirstColor()));
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have enough money to accept that wager!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Match no longer valid...");
                        event.getInventory().removeItem(item);
                    }
                }
            }
        }
    }
}


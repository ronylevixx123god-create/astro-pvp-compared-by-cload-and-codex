// =============================================== //
// Recompile disabled. Please run Recaf with a JDK //
// =============================================== //

// Decompiled with: FernFlower
// Class Version: 8
package org.arkhamnetwork.Arkkit.patches.trading;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerTrade {
    private Player player1;
    private Player player2;
    private TradeMenu player1Menu;
    private TradeMenu player2Menu;
    private Map<UUID, LinkedList<ItemStack>> currentlyTradedItems = new HashMap();
    private int tradeCountdown = 10;
    private boolean tradeSuccess = false;
    private boolean cancelled = false;
    private boolean player1Accepted = false;
    private boolean player2Accepted = false;
    private long lastWindowUpdate;

    PlayerTrade(Player player, Player player2) {
        this.player1 = player;
        this.player2 = player2;
    }

    void onTradeTick() {
        if (this.lastWindowUpdate == 0L || this.lastWindowUpdate <= System.currentTimeMillis()) {
            this.lastWindowUpdate = System.currentTimeMillis() + 100L;
            if (this.player1.isValid() && this.player2.isValid()) {
                if (this.player2Accepted && this.player1Accepted) {
                    if (this.tradeCountdown <= 1) {
                        this.completeTrade();
                    } else {
                        --this.tradeCountdown;
                        this.updateTradeMenus();
                    }
                } else if (this.tradeCountdown < 10) {
                    this.tradeCountdown = 10;
                    this.updateTradeMenus();
                }

            } else {
                this.cancelTrade((Player)null);
                Bukkit.getLogger().info("Unable to finish trade due to player being invalid: " + this.toString());
            }
        }
    }

    void cancelTrade(Player cancelled) {
        if (!this.cancelled && !this.tradeSuccess) {
            this.cancelled = true;
            this.returnItems(this.player1);
            this.returnItems(this.player2);
            this.player1.closeInventory();
            this.player2.closeInventory();
            this.sendMessage(CC.YellowB + "Trade cancelled" + (cancelled != null ? " " + CC.Yellow + "by " + cancelled.getName() + "!" : "."));
            this.unregister();
        }
    }

    private void completeTrade() {
        if (!this.cancelled && !this.tradeSuccess) {
            this.tradeSuccess = true;
            int p1Sent = this.giveTradeItems(this.player1, this.player2);
            int p2Sent = this.giveTradeItems(this.player2, this.player1);
            this.player1.sendMessage((String)(CC.GreenB + "Trade Accepted (" + CC.Green + "Received " + CC.GreenB + p2Sent + CC.Green + " item" + (p2Sent > 1 ? "s" : "") + ", gave " + CC.GreenB + p1Sent + CC.Green + " item" + (p1Sent > 1 ? "s" : "") + CC.GreenB + ")"));
            this.player2.sendMessage((String)(CC.GreenB + "Trade Accepted (" + CC.Green + "Received " + CC.GreenB + p1Sent + CC.Green + " item" + (p1Sent > 1 ? "s" : "") + ", gave " + CC.GreenB + p2Sent + CC.Green + " item" + (p2Sent > 1 ? "s" : "") + CC.GreenB + ")"));
            this.unregister();
            this.player1.closeInventory();
            this.player2.closeInventory();
            this.playSound(Sound.BLAZE_HIT, 1.0F, 1.5F);
            Bukkit.getLogger().info("[CosmicTrade] " + this.player1.getName() + " has traded " + this.player2.getName() + " successfully!");
        }
    }

    private int giveTradeItems(Player playersTradeItems, Player playerToReceiveItems) {
        List<ItemStack> items = (List)this.currentlyTradedItems.get(playersTradeItems.getUniqueId());
        if (items != null && !items.isEmpty()) {
            Iterator var4 = items.iterator();

            while(var4.hasNext()) {
                ItemStack toReceive = (ItemStack)var4.next();
                if (!PlayerUtil.giveOrDropProtectedItem(playerToReceiveItems, toReceive, -1)) {
                    playerToReceiveItems.sendMessage((String)(CC.Red + "(!) " + CC.Red + "Trade item " + (toReceive.hasItemMeta() ? toReceive.getItemMeta().getDisplayName() : StringUtils.capitaliseAllWords(toReceive.getType().name().replace("_", " ").toLowerCase())) + CC.Yellow + " has been dropped at your feet!"));
                }
            }

            Bukkit.getLogger().info("[CosmicTrade] Giving " + playerToReceiveItems.getName() + " trade items of " + playersTradeItems.getName() + " : " + Arrays.toString(items.toArray(new ItemStack[items.size()])));
            int size = items.size();
            playerToReceiveItems.updateInventory();
            items.clear();
            return size;
        } else {
            return 0;
        }
    }

    private void returnItems(Player player) {
        List<ItemStack> itemsToReturn = (List)this.currentlyTradedItems.get(player.getUniqueId());
        if (itemsToReturn != null) {
            Iterator var3 = itemsToReturn.iterator();

            while(var3.hasNext()) {
                ItemStack toRetr = (ItemStack)var3.next();
                if (!PlayerUtil.giveOrDropProtectedItem(player, toRetr, -1)) {
                    player.sendMessage((String)(CC.Gray + "Overflow trade item dropped at your feet!"));
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                }
            }

            if (itemsToReturn.size() > 0) {
                Bukkit.getLogger().info("Returning " + Arrays.toString(itemsToReturn.toArray(new ItemStack[itemsToReturn.size()])));
            }

            itemsToReturn.clear();
        }
    }

    void addItemToTradeList(Player player, ItemStack item) {
        LinkedList<ItemStack> list = (LinkedList)this.currentlyTradedItems.computeIfAbsent(player.getUniqueId(), (l) -> {
            return new LinkedList();
        });
        list.add(item);
        this.onTradeUpdate(player);
    }

    void returnItemFromTradeList(Player player, ItemStack item) {
        List<ItemStack> itemsStillTrading = (List)this.currentlyTradedItems.get(player.getUniqueId());
        if (itemsStillTrading != null && itemsStillTrading.contains(item)) {
            itemsStillTrading.remove(item);
            int emptySlot = player.getInventory().firstEmpty();
            if (emptySlot == -1) {
                PlayerUtil.giveOrDropProtectedItem(player, item, -1);
                Bukkit.getLogger().info("[CosmicTrade] " + player.getName() + " is being dropped " + item);
            } else {
                player.getInventory().setItem(emptySlot, item);
            }

            this.onTradeUpdate(player);
        } else {
            Bukkit.getLogger().info("[CosmicTrade] Not removing " + item + " for " + player.getName() + " due to list not having any trade items!");
        }
    }

    boolean canTradeItem(Player player, ItemStack item) {
        if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
            long id = ItemID.getItemID(item);
            if (id != -1L) {
                if (ItemID.blacklist_iid.contains(id)) {
                    player.sendMessage((String)(CC.RedB + "(!) " + CC.Red + "That item is blacklisted and cannot be traded!"));
                    player.getInventory().removeItem(new ItemStack[]{item});
                    Bukkit.getLogger().info("[CosmicTrade] " + player.getName() + " tried to trade blacklisted item: " + id + " Item: " + item);
                    return false;
                }

                if (item.getAmount() > 1) {
                    player.sendMessage((String)(CC.RedB + "(!) " + CC.Red + "You cannot trade this item while it is stacked!"));
                    return false;
                }
            }
        }

        List<ItemStack> alreadyTrading = (List)this.getCurrentlyTradedItems().get(player.getUniqueId());
        int limit = TradeMenu.leftTradingSlots.size();
        if (alreadyTrading != null && alreadyTrading.size() >= limit) {
            player.sendMessage((String)(CC.RedB + "(!) " + CC.Red + "You can only trade " + limit + " items at once!"));
            player.sendMessage((String)(CC.Gray + "Remove some items from the trade window and try again."));
            return false;
        } else {
            return true;
        }
    }

    Player getOppositePlayer(Player player) {
        if (this.player1.equals(player)) {
            return this.player2;
        } else {
            return this.player2.equals(player) ? this.player1 : null;
        }
    }

    boolean hasAccepted(Player player) {
        if (this.player1.equals(player)) {
            return this.player1Accepted;
        } else {
            return this.player2.equals(player) && this.player2Accepted;
        }
    }

    boolean toggleAccepted(Player player) {
        boolean retr = false;
        if (this.tradeCountdown < 10) {
            this.player1Accepted = false;
            this.player2Accepted = false;
            this.playSound(Sound.NOTE_PIANO, 1.1F, 1.0F);
        } else {
            boolean player2Accepted;
            if (this.player1.equals(player)) {
                player2Accepted = !this.player1Accepted;
                this.player1Accepted = player2Accepted;
                retr = player2Accepted;
            } else if (this.player2.equals(player)) {
                player2Accepted = !this.player2Accepted;
                this.player2Accepted = player2Accepted;
                retr = player2Accepted;
            }
        }

        this.updateOppositeButtons(player);
        return retr;
    }

    private void updateOppositeButtons(Player player) {
        Player opp = this.getOppositePlayer(player);
        if (opp.equals(this.player2)) {
            this.player2Menu.updateToggleButtons();
        } else if (opp.equals(this.player1)) {
            this.player1Menu.updateToggleButtons();
        }

        opp.updateInventory();
    }

    private void updateOppositeMenu(Player player) {
        Player opp = this.getOppositePlayer(player);
        if (opp.equals(this.player2)) {
            this.player2Menu.redraw();
        } else if (opp.equals(this.player1)) {
            this.player1Menu.redraw();
        }

    }

    private void onTradeUpdate(Player whoUpdated) {
        if (this.player1Accepted) {
            this.player1.sendMessage((String)(CC.Yellow + "Trade modified, re-accept the trade."));
            this.player1.playSound(this.player1.getLocation(), Sound.NOTE_PIANO, 3.0F, 1.05F);
        }

        if (this.player2Accepted) {
            this.player2.sendMessage((String)(CC.Yellow + "Trade modified, re-accept the trade."));
            this.player2.playSound(this.player2.getLocation(), Sound.NOTE_PIANO, 3.0F, 1.05F);
        }

        this.player1Accepted = false;
        this.player2Accepted = false;
        this.updateOppositeMenu(whoUpdated);
    }

    void openTradeMenu() {
        if (this.player1Menu == null) {
            (this.player1Menu = new TradeMenu(this.player1, this.getTradeInventoryTitle(this.player2), this)).openGUI(Arkkit.get());
        }

        if (this.player2Menu == null) {
            (this.player2Menu = new TradeMenu(this.player2, this.getTradeInventoryTitle(this.player1), this)).openGUI(Arkkit.get());
        }

    }

    private void updateTradeMenus() {
        if (!this.cancelled && !this.tradeSuccess) {
            this.player1Menu.redraw();
            this.player2Menu.redraw();
        }

    }

    public void playSound(Sound sound, float pitch, float vol) {
        this.player1.playSound(this.player1.getLocation(), sound, pitch, vol);
        this.player2.playSound(this.player2.getLocation(), sound, pitch, vol);
    }

    private void sendMessage(String msg) {
        this.player1.sendMessage((String)msg);
        this.player2.sendMessage((String)msg);
    }

    private String getTradeInventoryTitle(Player toShowNameOnRight) {
        String lPName = "You";

        String title;
        for(title = "  You"; title.length() + toShowNameOnRight.getName().length() <= 30; title = title + " ") {
        }

        title = title + toShowNameOnRight.getName();
        return title.length() <= 32 ? title : title.substring(0, 31);
    }

    private void unregister() {
        ((BetterTrade)ArkPlugins.BETTER_TRADE.getArkPlugin()).unregisterTrade(this);
    }

    public String toString() {
        return "player1=" + (this.player1 != null ? this.player1.getName() + " valid=" + this.player1.isValid() : "none") + ", player2=" + (this.player2 != null ? this.player2.getName() + " valid=" + this.player2.isValid() : "Null?") + ", Traded items=" + this.currentlyTradedItems.toString() + ", p1Accept=" + this.player1Accepted + " p2Accept=" + this.player2Accepted;
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public TradeMenu getPlayer1Menu() {
        return this.player1Menu;
    }

    public TradeMenu getPlayer2Menu() {
        return this.player2Menu;
    }

    public Map<UUID, LinkedList<ItemStack>> getCurrentlyTradedItems() {
        return this.currentlyTradedItems;
    }

    public int getTradeCountdown() {
        return this.tradeCountdown;
    }

    public boolean isTradeSuccess() {
        return this.tradeSuccess;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public boolean isPlayer1Accepted() {
        return this.player1Accepted;
    }

    public boolean isPlayer2Accepted() {
        return this.player2Accepted;
    }

    public long getLastWindowUpdate() {
        return this.lastWindowUpdate;
    }

    public void setTradeCountdown(int tradeCountdown) {
        this.tradeCountdown = tradeCountdown;
    }
}
 
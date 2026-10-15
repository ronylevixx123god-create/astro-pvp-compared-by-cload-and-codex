package org.arkhamnetwork.Arkkit.patches.trading;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TradeMenu extends CosmicGUI {
    public static List<Integer> leftTradingSlots = Lists.newArrayList(1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39, 45, 46, 47, 48);
    private static final List<Integer> rightTradingSlots = Lists.newArrayList(5, 6, 7, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44, 50, 51, 52, 53);
    private final PlayerTrade trade;
    private final InventoryItem dividerItem;
    private long lastClick;
    private long lastItemModified;

    public PlayerTrade getTrade() {
        return this.trade;
    }

    public TradeMenu(Player player, String title, PlayerTrade trade) {
        super(player, title, 54);
        this.dividerItem = new InventoryItem((new ItemBuilder(Material.THIN_GLASS, " ", "")).build());
        this.lastClick = 0L;
        this.trade = trade;
        this.setOnClose(() -> {
            this.trade.cancelTrade(this.player);
        });
    }

    public void redraw() {
        this.inventory.clear();
        this.getInventoryItems().clear();
        this.updateToggleButtons();
        this.drawItemsPlayerIsTrading(this.player, leftTradingSlots);
        this.drawItemsPlayerIsTrading(this.trade.getOppositePlayer(this.player), rightTradingSlots);
        this.setDividers();
        this.player.updateInventory();
    }

    public void updateToggleButtons() {
        this.setItem(0, this.getAcceptedPane(this.player).click(() -> {
            if (this.checkCanClick(false)) {
                this.trade.toggleAccepted(this.player);
                this.redraw();
            }
        }));
        Player opposite = this.trade.getOppositePlayer(this.player);
        this.setItem(8, this.getAcceptedPane(opposite));
    }

    private boolean checkCanClick(boolean modified) {
        if ((modified ? this.lastItemModified : this.lastClick) != 0L && (modified ? this.lastItemModified : this.lastClick) > System.currentTimeMillis()) {
            this.player.sendMessage(CC.Red + "You are clicking too fast! Please slow it down.");
            return false;
        } else {
            if (modified) {
                this.lastItemModified = System.currentTimeMillis() + 100L;
            } else {
                this.lastClick = System.currentTimeMillis() + 500L;
            }

            return true;
        }
    }

    public void onUnknownItemClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null && item.getType() != Material.AIR) {
            if (event.getInventory() != null && event.getInventory().getTitle().equals(this.title) && event.getWhoClicked().equals(this.player) && event.getClickedInventory() != null && event.getClickedInventory().getTitle().equals("container.inventory") && event.getClickedInventory().equals(this.player.getOpenInventory().getBottomInventory())) {
                if (!this.trade.canTradeItem(this.player, item)) {
                    return;
                }

                if (!this.checkCanClick(true)) {
                    return;
                }

                this.player.getInventory().setItem(event.getSlot(), null);
                this.trade.addItemToTradeList(this.player, item);
                this.redraw();
            }

        }
    }

    private void drawItemsPlayerIsTrading(Player player, List<Integer> slotsToUse) {
        List<ItemStack> itemsInTrade = this.trade.getCurrentlyTradedItems().get(player.getUniqueId());
        if (itemsInTrade != null && !itemsInTrade.isEmpty()) {
            int itemIndex = 0;

            Integer slot;
            InventoryItem invItem;
            for(Iterator var5 = slotsToUse.iterator(); var5.hasNext(); this.setItem(slot, invItem)) {
                slot = (Integer)var5.next();
                if (itemIndex >= itemsInTrade.size()) {
                    break;
                }

                ItemStack tradeItem = itemsInTrade.get(itemIndex++);
                invItem = new InventoryItem(tradeItem);
                if (player.equals(this.player)) {
                    invItem.click(() -> {
                        if (this.checkCanClick(true)) {
                            this.trade.returnItemFromTradeList(this.player, tradeItem);
                            this.redraw();
                        }
                    });
                }
            }

        }
    }

    public void setDividers() {
        int slot = 4;

        for(int i = 0; i < 6; ++i) {
            int newSlot = i * 9 + slot;
            if (newSlot >= this.inventory.getSize()) {
                break;
            }

            this.setItem(newSlot, this.dividerItem);
        }

    }

    public InventoryItem getAcceptedPane(Player playerFor) {
        boolean hasAccepted = this.trade.hasAccepted(playerFor);
        return new InventoryItem((new ItemBuilder(Material.STAINED_GLASS_PANE, hasAccepted ? CC.Green + "Trade ACCEPTING..." : CC.Yellow + "Click to ACCEPT Trade", hasAccepted ? CC.Gray + "Modify the trade to cancel." : "")).data(hasAccepted ? (short)DyeColor.LIME.getWoolData() : (short)DyeColor.RED.getWoolData()).amount(this.trade.getTradeCountdown()).build());
    }
}

package me.astropvp.itemflip.inventory;

import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryListener;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import me.astropvp.itemflip.struct.ItemFlipColor;
import me.astropvp.itemflip.struct.ItemFlipMatch;
import me.astropvp.itemflip.struct.ItemFlipPlayer;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.UUID;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.Player;


public class StakesGui implements InventoryProvider {
    private final SmartInventory inventory;
    private ItemFlipMatch match;
    private final Player player;

    public StakesGui(InventoryManager inventoryManager, ItemFlipMatch match, Player player) {
        this.player = player;
        this.match = match;

            this.inventory = SmartInventory.builder().type(InventoryType.CHEST).size(6, 9).provider((InventoryProvider)this).manager(inventoryManager).title(this.getInventoryTitle(match.getOppositePlayer(player))).id(UUID.randomUUID().toString()).listenForPlayerInventoryClick(true).listener(new InventoryListener(InventoryClickEvent.class,  event -> {
            if (((InventoryClickEvent)event).getWhoClicked() instanceof Player) {
                if (((InventoryClickEvent)event).getClickedInventory() != null) {
                    if (((InventoryClickEvent)event).getCurrentItem() != null && ((InventoryClickEvent)event).getCurrentItem().getType() != Material.AIR) {
                        Player p = (Player)((InventoryClickEvent)event).getWhoClicked();
                        ItemFlipPlayer ifPlayer = match.getIfPlayer(p);
                        if (((InventoryClickEvent)event).getClickedInventory().getType() != InventoryType.PLAYER) {
                            if (ItemFlipMatch.PLAYER_ONE_ITEM_SLOTS.contains(((InventoryClickEvent)event).getRawSlot())) {
                                int index = ItemFlipMatch.PLAYER_ONE_ITEM_SLOTS.indexOf(((InventoryClickEvent)event).getRawSlot());
                                if (ifPlayer.getWageredItems().get(index) != null) {
                                    ItemStack item = (ItemStack)ifPlayer.getWageredItems().get(index);
                                    ifPlayer.getWageredItems().remove(index);
                                    player.getInventory().addItem(new ItemStack[]{item});
                                    ((InventoryClickEvent)event).setCancelled(true);
                                    this.match.redrawAllPlayers();
                                    this.match.getCountdownRunnable().doStop();
                                }
                            }

                        } else {
                            if (ifPlayer.getWageredItems().size() >= 15) {
                                CommonsUtil.sendMessage(p, "&c&l(!) &cYou can only place 15 items into the item flip stakes window!");
                            } else {
                                ifPlayer.getWageredItems().add(((InventoryClickEvent)event).getCurrentItem());
                                ((InventoryClickEvent)event).setCurrentItem((ItemStack)null);
                                ((InventoryClickEvent)event).setCancelled(true);
                                this.match.redrawAllPlayers();
                                this.match.getCountdownRunnable().doStop();
                            }

                            ((InventoryClickEvent)event).setCancelled(true);
                        }
                    }
                }
            }
        })).listener(new InventoryListener(InventoryCloseEvent.class, (event) -> {
            if (!this.match.isCancelled() && !this.match.isInCoinFlip()) {
                CommonsUtil.sendMessage(match.getPlayerOne(), "&cItem Coin Flip by " + match.getPlayerOne().getName() + " &c&l- CANCELLED");
                CommonsUtil.sendMessage(match.getPlayerTwo(), "&cItem Coin Flip by " + match.getPlayerOne().getName() + " &c&l- CANCELLED");
                match.getIfPlayerOne().getWageredItems().forEach((i) -> {
                    match.getPlayerOne().getInventory().addItem(new ItemStack[]{i});
                });
                match.getIfPlayerTwo().getWageredItems().forEach((i) -> {
                    match.getPlayerTwo().getInventory().addItem(new ItemStack[]{i});
                });
                match.cancel();
            }
        })).build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemStack(Material.AIR)));
        this.addPlayerItems(contents, player, ItemFlipMatch.PLAYER_ONE_ITEM_SLOTS);
        this.addPlayerItems(contents, this.match.getOppositePlayer(player), ItemFlipMatch.PLAYER_TWO_ITEM_SLOTS);
        this.addFillerPanes(contents);
        this.addColors(contents);
        this.addConfirmPanes(contents);
    }

    private void addPlayerItems(InventoryContents contents, Player player, List<Integer> slotArray) {
        ItemFlipPlayer ifPlayer = this.match.getIfPlayer(player);
        LinkedList<ItemStack> items = ifPlayer.getWageredItems();

        for(int i = 0; i < items.size(); ++i) {
            ItemStack item = (ItemStack)items.get(i);
            Integer position = (Integer)slotArray.get(i);
            contents.set(position, ClickableItem.empty(item));
        }

    }

    private void addConfirmPanes(InventoryContents contents) {
        boolean playerConfirmed = this.match.getIfPlayer(this.player).isConfirmed();
        boolean oppositePlayerConfirmed = this.match.getIfPlayer(this.match.getOppositePlayer(this.player)).isConfirmed();
        contents.set(0, 0, ClickableItem.of(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, playerConfirmed ? "&aStakes ACCEPTED" : "&eClick to ACCEPT Stakes", this.match.getCountdownTimeLeft(), playerConfirmed ? 5 : 14, new String[]{playerConfirmed ? "&7Modify the stakes to cancel." : ""}), (event) -> {
            if (this.match.getIfPlayer(this.player).isConfirmed()) {
                this.match.getCountdownRunnable().doStop();
            }

            this.match.getIfPlayer(this.player).setConfirmed(!this.match.getIfPlayer(this.player).isConfirmed());
            this.match.redrawAllPlayers();
        }));
        contents.set(0, 8, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, oppositePlayerConfirmed ? "&aStakes ACCEPTED" : "&eClick to ACCEPT Stakes", this.match.getCountdownTimeLeft(), oppositePlayerConfirmed ? 5 : 14, new String[]{oppositePlayerConfirmed ? "&7Modify the stakes to cancel." : ""})));
    }

    private void addColors(InventoryContents contents) {
        for(int i = 0; i < ItemFlipColor.values().length; ++i) {
            ItemFlipColor color = ItemFlipColor.values()[i];
            ItemStack item = CommonsUtil.createItem(Material.WOOL, color.getColoredName(), 1, color.getWoolId(), new String[0]);
            if (this.match.isColorTaken(color)) {
                item = CommonsUtil.addGlow(item);
                ItemMeta meta = item.getItemMeta();
                meta.setLore(Collections.singletonList(CommonsUtil.color("&e" + this.match.getPlayerByColor(color).getName())));
                item.setItemMeta(meta);
            }

            contents.set(5, i, ClickableItem.of(item, (event) -> {
                if (this.match.isColorTaken(color)) {
                    CommonsUtil.sendMessage(this.player, "&c&l(!) &cThis color is already taken!");
                } else {
                    this.match.getIfPlayer(this.player).setChosenColor(color);
                    this.match.redrawAllPlayers();
                }
            }));
        }

    }

    private void addFillerPanes(InventoryContents contents) {
        contents.fillColumn(4, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&7", 1, 7, new String[]{""})));
        contents.fillRow(4, ClickableItem.empty(CommonsUtil.createItem(Material.STAINED_GLASS_PANE, "&7", 1, 7, new String[]{""})));
        contents.set(5, 4, ClickableItem.empty(new ItemStack(Material.AIR)));
    }

    private String getInventoryTitle(Player toShowNameOnRight) {
        String title;
        for(title = "  You"; title.length() + toShowNameOnRight.getName().length() <= 30; title = title + " ") {
        }

        title = title + toShowNameOnRight.getName();
        return title.length() <= 32 ? title : title.substring(0, 31);
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}

package net.astropvp.gambling.tower.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import de.tr7zw.nbtapi.NBTItem;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Setter;

import net.astropvp.gambling.GamblingPlugin;
import  net.astropvp.gambling.data.GambleUser;
import  net.astropvp.gambling.tower.enums.DifficultyType;
import net.astropvp.gambling.utils.ItemBuilder;
import net.astropvp.gambling.utils.TitleUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TowerGameInventory {

    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final GambleUser gambleUser;

    private int currentCashout = 0;

    @Setter
    private boolean hasLost = false;

    private int row = 6;

    private final Gui towerGameInventory;
    public Gui getUI() {
        return this.towerGameInventory;
    }


    public TowerGameInventory(Player player, DifficultyType difficultyType, double bet) {
        EconomyUtils.withdrawBalance(player, bet);
        this.gambleUser = GamblingPlugin.getInstance().getGambleManager().getGambleUser(player.getUniqueId());

        this.towerGameInventory = new Gui(6, "Tower Game: " + difficultyType.name());
        this.towerGameInventory.setDefaultClickAction(event -> event.setCancelled(true));
        this.towerGameInventory.getFiller().fill(new GuiItem(createFillerItem()));
        this.towerGameInventory.setCloseGuiAction(inventoryCloseEvent -> {
            if(!hasLost) {
                EconomyUtils.depositBalance(player, bet);
                player.sendMessage(CC.translate("&aYour money has been returned to you as you closed the GUI"));
            }
        });

        switch(difficultyType) {
            case EASY:
                startEasyTowerGame(player, bet);
                break;
            case MEDIUM:
                startMediumTowerGame(player, bet);
                break;
            case HARD:
                startHardTowerGame(player, bet);
                break;
        }

    }

    private void startMediumTowerGame(Player player, double bet) {
        setCashoutButton(player, bet, DifficultyType.MEDIUM);
        for(int row = 1; row < 7; row++) {
            ItemStack itemStack = new ItemBuilder(Material.ENDER_CHEST, "&f&l???").colorize().build();
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setInteger("row", row);
            for(int column : new int[] {4, 6}) {
                this.towerGameInventory.setItem(row, column, new GuiItem(nbtItem.getItem(), event -> {
                    NBTItem nbtItem1 = new NBTItem(event.getCurrentItem());
                    if(this.row == nbtItem1.getInteger("row")) {
                        if(Math.random() > 0.5D) {
                            this.row--;
                            currentCashout++;
                            ItemStack correctItemStack = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5, "&a&lCORRECT").colorize().build();
                            this.towerGameInventory.updateItem(event.getSlot(), new GuiItem(correctItemStack));
                            setCashoutButton(player, bet, DifficultyType.MEDIUM);
                        } else {
                            setHasLost(true);
                            this.gambleUser.addTowerLoss(bet);
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 3.0F, 2.0F);
                            TitleUtils.sendTitle(player, CC.translate("&c&lTOWER CRASHED"), CC.translate("&fYour tower crashed..."), 1, 3, 1);
                        }
                    } else {
                        player.sendMessage(CC.translate("&cError: &eYou are not on this row in the tower."));
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
                    }
                }));
            }
        }
    }

    private void startEasyTowerGame(Player player, double bet) {
        setCashoutButton(player, bet, DifficultyType.EASY);
        for(int row = 1; row < 7; row++) {
            ItemStack itemStack = new ItemBuilder(Material.ENDER_CHEST, "&f&l???").colorize().build();
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setInteger("row", row);
            for(int column : new int[] {3, 5, 7}) {
                this.towerGameInventory.setItem(row, column, new GuiItem(nbtItem.getItem(), event -> {
                    NBTItem nbtItem1 = new NBTItem(event.getCurrentItem());
                    if(this.row == nbtItem1.getInteger("row")) {
                        if(Math.random() > 0.33D) {
                            this.row--;
                            currentCashout++;
                            ItemStack correctItemStack = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5, "&a&lCORRECT").colorize().build();
                            this.towerGameInventory.updateItem(event.getSlot(), new GuiItem(correctItemStack));
                            setCashoutButton(player, bet, DifficultyType.EASY);
                        } else {
                            setHasLost(true);
                            this.gambleUser.addTowerLoss(bet);
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 3.0F, 2.0F);
                            TitleUtils.sendTitle(player, CC.translate("&c&lTOWER CRASHED"), CC.translate("&fYour tower crashed..."), 1, 3, 1);
                        }
                    } else {
                        player.sendMessage(CC.translate("&cError: &eYou are not on this row in the tower."));
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
                    }
                }));
            }
        }
    }

    private void startHardTowerGame(Player player, double bet) {
        setCashoutButton(player, bet, DifficultyType.HARD);
        for(int row = 1; row < 7; row++) {
            ItemStack itemStack = new ItemBuilder(Material.ENDER_CHEST, "&f&l???").colorize().build();
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setInteger("row", row);
            for(int column : new int[] {3, 5, 7}) {
                this.towerGameInventory.setItem(row, column, new GuiItem(nbtItem.getItem(), event -> {
                    NBTItem nbtItem1 = new NBTItem(event.getCurrentItem());
                    if(this.row == nbtItem1.getInteger("row")) {
                        if(Math.random() > 0.66D) {
                            this.row--;
                            currentCashout++;
                            ItemStack correctItemStack = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5, "&a&lCORRECT").colorize().build();
                            this.towerGameInventory.updateItem(event.getSlot(), new GuiItem(correctItemStack));
                            setCashoutButton(player, bet, DifficultyType.HARD);
                        } else {
                            setHasLost(true);
                            this.gambleUser.addTowerLoss(bet);
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 3.0F, 2.0F);
                            TitleUtils.sendTitle(player, CC.translate("&c&lTOWER CRASHED"), CC.translate("&fYour tower crashed..."), 1, 3, 1);
                        }
                    } else {
                        player.sendMessage(CC.translate("&cError: &eYou are not on this row in the tower."));
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
                    }
                }));
            }
        }
    }

    private void setCashoutButton(Player player, double bet, DifficultyType difficultyType) {
        ItemBuilder cashoutButton = new ItemBuilder(Material.DIAMOND, "&a&lCASHOUT",
                "&7Click this button to cashout");
        List<String> lore = new ArrayList<>();
        lore.add("&fBet: &a$" + moneyFormat.format(bet));
        lore.add("&fMultiplier: &a" + difficultyType.getMultiplier()[currentCashout] + "x");
        lore.add("&fCashout: &a$" + moneyFormat.format(bet * difficultyType.getMultiplier()[currentCashout]));
        cashoutButton.setLore(lore);
        cashoutButton.setGlowing();
        this.towerGameInventory.updateItem(3, 9, new GuiItem(cashoutButton.colorize().build(), event -> {
            setHasLost(true);
            double moneyGain = bet * difficultyType.getMultiplier()[currentCashout];
            EconomyUtils.depositBalance(player, moneyGain);
            this.gambleUser.addTowerWin(moneyGain);
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 5.0F, 2.0F);
            TitleUtils.sendTitle(player, CC.translate("&a&lCASHOUT"), CC.translate("&7You cashed out for " + moneyFormat.format(moneyGain)), 1, 3, 1);
        }));
        player.updateInventory();
    }


    private ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
}

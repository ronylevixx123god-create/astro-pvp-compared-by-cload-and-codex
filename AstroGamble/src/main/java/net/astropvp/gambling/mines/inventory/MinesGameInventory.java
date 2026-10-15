package net.astropvp.gambling.mines.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.google.common.util.concurrent.AtomicDouble;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Setter;

import net.astropvp.gambling.GamblingPlugin;
import  net.astropvp.gambling.data.GambleUser;
import  net.astropvp.gambling.mines.enums.DifficultyType;
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

public class MinesGameInventory {

    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final GambleUser gambleUser;

    @Setter
    private boolean hasLost = false;

    private int currentCashout = 0;

    private final Gui mineInventory;
    public Gui getUI() {
        return this.mineInventory;
    }


    public MinesGameInventory(Player player, DifficultyType difficultyType, double bet) {
        EconomyUtils.withdrawBalance(player, bet);
        this.gambleUser = GamblingPlugin.getInstance().getGambleManager().getGambleUser(player.getUniqueId());

        this.mineInventory = new Gui(6, "Mine Game: " + difficultyType.name());
        this.mineInventory.setDefaultClickAction(event -> event.setCancelled(true));
        this.mineInventory.getFiller().fillBorder(new GuiItem(createFillerItem()));
        this.mineInventory.setCloseGuiAction(inventoryCloseEvent -> {
            if(!hasLost) {
                EconomyUtils.depositBalance(player, bet);
                player.sendMessage(CC.translate("&aYour money has been returned to you as you closed the GUI"));
            }
        });


        ItemBuilder unknownBuilder = new ItemBuilder(Material.ENDER_CHEST, "&f&l???");
        ItemBuilder safeBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short)5, "&a&lSAFE",
                "&7This tile was safe!").setGlowing();


        AtomicDouble chance = new AtomicDouble(getMinesAmount(difficultyType) / 28.0);
        for(int i = 0; i < 45; i++) {
            this.mineInventory.addItem(new GuiItem(unknownBuilder.colorize().build(), event -> {
                if(Math.random() < chance.get()) {
                    setHasLost(true);
                    this.gambleUser.addMinesLoss(bet);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.EXPLODE, 5.0F, 2.0F);
                    TitleUtils.sendTitle(player, CC.translate("&c&lBOOM!"), CC.translate("&7You clicked a mine!"), 1, 3, 1);
                    setHasLost(true);
                } else {
                    currentCashout++;
                    chance.set(getMinesAmount(difficultyType) / 28.0);
                    event.setCurrentItem(safeBuilder.colorize().build());
                    setMiddleButton(player, bet, difficultyType);
                }
            }));
        }
        setMiddleButton(player, bet, difficultyType);
    }

    private int getMinesAmount(DifficultyType difficultyType) {
        int mines = difficultyType.getMines();
        mines = mines + currentCashout;
        return mines;
    }

    private void setMiddleButton(Player player, double bet, DifficultyType difficultyType) {
        ItemBuilder middleButton = new ItemBuilder(Material.DIAMOND, "&a&lCASHOUT",
                "&7Click this button to cashout");
        List<String> lore = new ArrayList<>();
        lore.add("&fBet: &a$" + moneyFormat.format(bet));
        lore.add("&fMultiplier: &a" + difficultyType.getMultiplier()[currentCashout] + "x");
        lore.add("&fCashout: &a$" + moneyFormat.format(bet * difficultyType.getMultiplier()[currentCashout]));
        middleButton.setLore(lore);
        middleButton.setGlowing();
        this.mineInventory.updateItem(1, 5, new GuiItem(middleButton.colorize().build(), event -> {
            setHasLost(true);
            double moneyGain = bet * difficultyType.getMultiplier()[currentCashout];
            EconomyUtils.depositBalance(player, moneyGain);
            this.gambleUser.addMinesWin(moneyGain);
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 5.0F, 2.0F);
            TitleUtils.sendTitle(player, CC.translate("&a&lCASHOUT"), CC.translate("&7You cashed out for $" + moneyFormat.format(moneyGain)), 1, 3, 1);
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

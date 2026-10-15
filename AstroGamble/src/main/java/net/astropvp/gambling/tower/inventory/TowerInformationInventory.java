package net.astropvp.gambling.tower.inventory;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;

import net.astropvp.gambling.GamblingPlugin;
import  net.astropvp.gambling.data.GambleUser;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TowerInformationInventory {

    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");


    private final Gui towerInformationInventory;

    public Gui getUI() {
        return this.towerInformationInventory;
    }


    public TowerInformationInventory(Player player) {
        this.towerInformationInventory = new Gui(GuiType.HOPPER, "Astro Towers");
        this.towerInformationInventory.setDefaultClickAction(event -> event.setCancelled(true));

        ItemStack info = new ItemBuilder(Material.BOOK_AND_QUILL,
                "&cTowers Gambling",
                "&fUse &c/tower <amount> &fto enter the game"
        ).colorize().build();

        GambleUser gambleUser = GamblingPlugin.getInstance().getGambleManager().getGambleUser(player.getUniqueId());
        List<String> playerStatsLore = new ArrayList<>();
        playerStatsLore.add("&fWins: &a" + gambleUser.getTowerWins());
        playerStatsLore.add("&fWinnings: &a$" + moneyFormat.format(gambleUser.getTowerWinnings()));
        playerStatsLore.add("&fLosses: &c" + gambleUser.getTowerLosses());
        playerStatsLore.add("&fLosings: &c$" + moneyFormat.format(gambleUser.getTowerLosings()));
        playerStatsLore.add("&fRatio: &7[" + gambleUser.getTowerWinLossRatio(":") + "]");
        ItemBuilder playerStatsItemBuilder = new ItemBuilder(Material.PAPER,
                "&cTower Stats").setLore(playerStatsLore);

        this.towerInformationInventory.setItem(3, new GuiItem(playerStatsItemBuilder.setGlowing().colorize().build()));
        this.towerInformationInventory.setItem(2, new GuiItem(info));

        this.towerInformationInventory.setItem(4, new GuiItem(createExitItem(), event -> player.closeInventory()));
        this.towerInformationInventory.setItem(0, new GuiItem(createExitItem(), event -> player.closeInventory()));
    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit tower menu").colorize().build();
    }

}

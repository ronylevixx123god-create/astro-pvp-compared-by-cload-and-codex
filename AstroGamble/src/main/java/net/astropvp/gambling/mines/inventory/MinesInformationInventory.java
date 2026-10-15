package net.astropvp.gambling.mines.inventory;

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

public class MinesInformationInventory {


    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");


    private final Gui mineInventory;

    public Gui getUI() {
        return this.mineInventory;
    }


    public MinesInformationInventory(Player player) {
        this.mineInventory = new Gui(GuiType.HOPPER, "Astro Mines");
        this.mineInventory.setDefaultClickAction(event -> event.setCancelled(true));

        ItemStack info = new ItemBuilder(Material.BOOK_AND_QUILL,
                "&cMines Gambling",
                "&fUse &c/mines <amount> &fto enter the game"
        ).colorize().build();

        GambleUser gambleUser = GamblingPlugin.getInstance().getGambleManager().getGambleUser(player.getUniqueId());
        List<String> playerStatsLore = new ArrayList<>();
        playerStatsLore.add("&fWins: &a" + gambleUser.getMinesWins());
        playerStatsLore.add("&fWinnings: &a$" + moneyFormat.format(gambleUser.getMinesWinnings()));
        playerStatsLore.add("&fLosses: &c" + gambleUser.getMinesLosses());
        playerStatsLore.add("&fLosings: &c$" + moneyFormat.format(gambleUser.getMinesLosings()));
        playerStatsLore.add("&fRatio: &7[" + gambleUser.getMinesWinLossRatio(":") + "]");
        ItemBuilder playerStatsItemBuilder = new ItemBuilder(Material.PAPER,
                "&cMines Stats").setLore(playerStatsLore);

        this.mineInventory.setItem(3, new GuiItem(playerStatsItemBuilder.setGlowing().colorize().build()));
        this.mineInventory.setItem(2, new GuiItem(info));

        this.mineInventory.setItem(4, new GuiItem(createExitItem(), event -> player.closeInventory()));
        this.mineInventory.setItem(0, new GuiItem(createExitItem(), event -> player.closeInventory()));
    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit mines menu").colorize().build();
    }


}

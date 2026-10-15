package net.astropvp.gambling.bombs.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Setter;
import net.astropvp.gambling.GamblingPlugin;


import  net.astropvp.gambling.bombs.enums.DifficultyType;
import  net.astropvp.gambling.data.GambleUser;
import net.astropvp.gambling.utils.ItemBuilder;
import net.astropvp.gambling.utils.TitleUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BombsGameInventory {

    private final GambleUser gambleUser;

    @Setter
    private boolean hasLost = false;

    private Gui bombInventory;
    public Gui getUI() {
        return this.bombInventory;
    }


    public BombsGameInventory(Player player, DifficultyType difficultyType, double bet) {

        EconomyUtils.withdrawBalance(player, bet);
        this.gambleUser = GamblingPlugin.getInstance().getGambleManager().getGambleUser(player.getUniqueId());


        this.bombInventory = new Gui(GuiType.CHEST, difficultyType.name());
        this.bombInventory.setCloseGuiAction(inventoryCloseEvent -> {
            if(!hasLost) {
                EconomyUtils.depositBalance(player, bet);
                player.sendMessage(CC.translate("&aYour money has been returned to you as you closed the GUI"));
            }
        });

        switch(difficultyType) {
            case EASY:
                openEasyMinesGame(player, bet);
                break;
            case MEDIUM:
                openMediumMinesGame(player, bet);
                break;
            case HARD:
                openHardMinesGame(player, bet);
                break;
        }

        this.bombInventory.setDefaultClickAction(event -> event.setCancelled(true));

    }

    private void openEasyMinesGame(Player player, double bet) {
        this.bombInventory = new Gui(GuiType.DISPENSER, "Easy Bomb Game");
        ItemBuilder unknownBuilder = new ItemBuilder(Material.ENDER_CHEST, "&f&l???");
        for(int i = 0; i < 9; i++) {
            this.bombInventory.setItem(i, new GuiItem(unknownBuilder.colorize().build(), event -> {
                if(Math.random() >= 0.44) {
                    setHasLost(true);
                    this.gambleUser.addBombsLoss(bet);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.EXPLODE, 5.0F, 2.0F);
                    TitleUtils.sendTitle(player, CC.translate("&c&lBOOM!"), CC.translate("&7You clicked a bomb!"), 1, 3, 1);
                } else {
                    setHasLost(true);
                    this.gambleUser.addBombsWin(bet*2);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
                    TitleUtils.sendTitle(player, CC.translate("&a&lSAFE!"), CC.translate("&7You're safe from exploding..."), 1, 3, 1);
                    EconomyUtils.depositBalance(player, bet*2);
                }
            }));
        }
    }

    private void openMediumMinesGame(Player player, double bet) {
        this.bombInventory = new Gui(6, "Medium Bomb Game");
        this.bombInventory.getFiller().fillBorder(new GuiItem(createFillerItem()));
        ItemBuilder unknownBuilder = new ItemBuilder(Material.ENDER_CHEST, "&f&l???");
        for(int i = 0; i < 28; i++) {
            this.bombInventory.addItem(new GuiItem(unknownBuilder.colorize().build(), event -> {
                if(Math.random() >= 0.29) {
                    setHasLost(true);
                    this.gambleUser.addBombsLoss(bet);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.EXPLODE, 5.0F, 2.0F);
                    TitleUtils.sendTitle(player, CC.translate("&c&lBOOM!"), CC.translate("&7You clicked a bomb!"), 1, 3, 1);
                } else {
                    setHasLost(true);
                    this.gambleUser.addBombsWin(bet*3);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
                    TitleUtils.sendTitle(player, CC.translate("&a&lSAFE!"), CC.translate("&7You're safe from exploding..."), 1, 3, 1);
                    EconomyUtils.depositBalance(player, bet*3);
                }
            }));
        }
    }

    private void openHardMinesGame(Player player, double bet) {
        this.bombInventory = new Gui(6, "Medium Bomb Game");
        ItemBuilder unknownBuilder = new ItemBuilder(Material.ENDER_CHEST, "&f&l???");
        for(int i = 0; i < 54; i++) {
            this.bombInventory.addItem(new GuiItem(unknownBuilder.colorize().build(), event -> {
                if(Math.random() >= 0.15) {
                    setHasLost(true);
                    this.gambleUser.addBombsLoss(bet);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.EXPLODE, 5.0F, 2.0F);
                    TitleUtils.sendTitle(player, CC.translate("&c&lBOOM!"), CC.translate("&7You clicked a bomb!"), 1, 3, 1);
                } else {
                    setHasLost(true);
                    this.gambleUser.addBombsWin(bet*5);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
                    TitleUtils.sendTitle(player, CC.translate("&a&lSAFE!"), CC.translate("&7You're safe from exploding..."), 1, 3, 1);
                    EconomyUtils.depositBalance(player, bet*5);
                }
            }));
        }
    }


    private ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&6Astro&fPvP"));
        item.setItemMeta(meta);
        return item;
    }
}

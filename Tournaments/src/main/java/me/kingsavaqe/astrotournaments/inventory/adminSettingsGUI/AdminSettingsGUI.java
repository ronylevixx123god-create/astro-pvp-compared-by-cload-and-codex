package me.kingsavaqe.astrotournaments.inventory.adminSettingsGUI;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.inventory.TournamentGUI;
import me.kingsavaqe.astrotournaments.utils.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminSettingsGUI {
    private final Gui adminSettingsGUI;

    public Gui getUI() {
        return this.adminSettingsGUI;
    }


    public AdminSettingsGUI(AstroTournamentsPlugin astroTournamentsPlugin) {
        this.adminSettingsGUI = new Gui(3, "Admin Tournament Settings");
        this.adminSettingsGUI.setDefaultClickAction(event -> event.setCancelled(true));


        this.adminSettingsGUI.setItem(1, 1, new GuiItem(new ItemBuilder(Material.LEATHER_HELMET)
                .setGlowing()
                .setName("&6&lBanned Helmet Skins")
                .setLore("&7Click here to ban helmet skins")
                .colorize()

                .build(), event -> {
            new BannedSkinsGUI(ItemSkinType.HELMET).getBannedSkinSettingsGUI().open(event.getWhoClicked());
        }));
        this.adminSettingsGUI.setItem(1, 2, new GuiItem(new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setGlowing()
                .setName("&6&lBanned Chestplate Skins")
                .setLore("&7Click here to ban chestplate skins")
                .colorize()

                .build(), event -> {
            new BannedSkinsGUI(ItemSkinType.AMULET).getBannedSkinSettingsGUI().open(event.getWhoClicked());
        }));
        this.adminSettingsGUI.setItem(2, 2, new GuiItem(new ItemBuilder(Material.GOLD_CHESTPLATE)
                .setGlowing()
                .setName("&6&lBanned Backpack Skins")
                .setLore("&7Click here to ban backpack skins")
                .colorize()

                .build(), event -> {
            new BannedSkinsGUI(ItemSkinType.BACKPACK).getBannedSkinSettingsGUI().open(event.getWhoClicked());
        }));
        this.adminSettingsGUI.setItem(1, 3, new GuiItem(new ItemBuilder(Material.LEATHER_LEGGINGS)
                .setGlowing()
                .setName("&6&lBanned Leggings Skins")
                .setLore("&7Click here to ban leggings skins")
                .colorize()
                .build(), event -> {
            new BannedSkinsGUI(ItemSkinType.BELT).getBannedSkinSettingsGUI().open(event.getWhoClicked());
        }));
        this.adminSettingsGUI.setItem(1, 4, new GuiItem(new ItemBuilder(Material.LEATHER_BOOTS)
                .setGlowing()
                .setName("&6&lBanned Boots Skins")
                .setLore("&7Click here to ban boots skins")
                .colorize()

                .build(), event -> {
            new BannedSkinsGUI(ItemSkinType.BOOTS).getBannedSkinSettingsGUI().open(event.getWhoClicked());
        }));
        this.adminSettingsGUI.setItem(1, 5, new GuiItem(new ItemBuilder(Material.GOLD_SWORD)
                .setGlowing()
                .setName("&6&lBanned Sword Skins")
                .setLore("&7Click here to ban sword skins")
                .colorize()
                .build(), event -> {
            new BannedSkinsGUI(ItemSkinType.SWORD).getBannedSkinSettingsGUI().open(event.getWhoClicked());
        }));

        this.adminSettingsGUI.setItem(3, 1, new GuiItem(new ItemBuilder(Material.NETHER_STAR)
                .setGlowing()
                .setName("&b&lCurrent Crystal")
                .setLore("&7Click here to edit the current crystal")
                .colorize()

                .build(), event -> {
            new ArmorCrystalsGUI().getArmorCrystalGUI().open(event.getWhoClicked());
        }));

        this.adminSettingsGUI.setItem(3, 8, new GuiItem(createBackItem(), event -> {
            new TournamentGUI(AstroTournamentsPlugin.instance, (Player) event.getWhoClicked()).getUI().open(event.getWhoClicked());
        }));
        this.adminSettingsGUI.setItem(3, 9, new GuiItem(createExitItem(), event -> {
            event.getWhoClicked().closeInventory();
        }));
    }


    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit tournament menu", "&7You can execute /tournament leave", "&7to leave the tournament event").colorize().build();
    }

    private ItemStack createBackItem() {
        return new ItemBuilder(Material.ARROW, "&cGo back", "&7Click to go back to the main menu").colorize().build();
    }
}

package me.kingsavaqe.astrotournaments.inventory.settingsGUI;

import dev.triumphteam.gui.guis.Gui;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.inventory.TournamentGUI;
import me.kingsavaqe.astrotournaments.objects.TournamentPlayer;
import me.kingsavaqe.astrotournaments.utils.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlayerSettingsGUI {
    private final Gui tournamentPlayerSettings;

    public Gui getUI() {
        return this.tournamentPlayerSettings;
    }


    public PlayerSettingsGUI(AstroTournamentsPlugin astroTournamentsPlugin, TournamentPlayer tournamentPlayer) {
        this.tournamentPlayerSettings = new Gui(3, "Tournament Player Settings");
        this.tournamentPlayerSettings.setDefaultClickAction(event -> event.setCancelled(true));



        this.tournamentPlayerSettings.setItem(1, 1, new GuiItem(tournamentPlayer.getHelmet().clone(), event -> {
            new SkinSettingsGUI(astroTournamentsPlugin.getTournamentManager().getTournamentPlayer(tournamentPlayer.getPlayer().getUniqueId()), ItemSkinType.HELMET).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
        }));
        this.tournamentPlayerSettings.setItem(1, 2, new GuiItem(tournamentPlayer.getChestplate().clone(), event -> {
            new SkinSettingsGUI(astroTournamentsPlugin.getTournamentManager().getTournamentPlayer(tournamentPlayer.getPlayer().getUniqueId()), ItemSkinType.AMULET).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
        }));
        this.tournamentPlayerSettings.setItem(2, 2, new GuiItem(tournamentPlayer.getChestplate().clone(), event -> {
            new SkinSettingsGUI(astroTournamentsPlugin.getTournamentManager().getTournamentPlayer(tournamentPlayer.getPlayer().getUniqueId()), ItemSkinType.BACKPACK).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
        }));
        this.tournamentPlayerSettings.setItem(1, 3, new GuiItem(tournamentPlayer.getLeggings().clone(), event -> {
            new SkinSettingsGUI(astroTournamentsPlugin.getTournamentManager().getTournamentPlayer(tournamentPlayer.getPlayer().getUniqueId()), ItemSkinType.BELT).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
        }));
        this.tournamentPlayerSettings.setItem(1, 4, new GuiItem(tournamentPlayer.getBoots().clone(), event -> {
            new SkinSettingsGUI(astroTournamentsPlugin.getTournamentManager().getTournamentPlayer(tournamentPlayer.getPlayer().getUniqueId()), ItemSkinType.BOOTS).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
        }));
        this.tournamentPlayerSettings.setItem(1, 5, new GuiItem(tournamentPlayer.getSword().clone(), event -> {
            new SkinSettingsGUI(astroTournamentsPlugin.getTournamentManager().getTournamentPlayer(tournamentPlayer.getPlayer().getUniqueId()), ItemSkinType.SWORD).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
        }));

        this.tournamentPlayerSettings.setItem(3, 8, new GuiItem(createBackItem(), event -> {
            new TournamentGUI(AstroTournamentsPlugin.instance, tournamentPlayer.getPlayer()).getUI().open(tournamentPlayer.getPlayer());
        }));
        this.tournamentPlayerSettings.setItem(3, 9, new GuiItem(createExitItem(), event -> {
            tournamentPlayer.getPlayer().closeInventory();
        }));

        ItemStack glass = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName("&7").colorize().build();
        this.tournamentPlayerSettings.getFiller().fill(new GuiItem(glass));
    }



    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit tournament menu", "&7You can execute /tournament leave", "&7to leave the tournament event").colorize().build();
    }

    private ItemStack createBackItem() {
        return new ItemBuilder(Material.ARROW, "&cGo back", "&7Click to go back to the main menu").colorize().build();
    }
}

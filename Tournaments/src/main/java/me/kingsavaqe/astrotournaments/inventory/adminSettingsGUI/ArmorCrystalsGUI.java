package me.kingsavaqe.astrotournaments.inventory.adminSettingsGUI;

import lombok.Getter;
import me.astropvp.astropvpskins.enums.SkinType;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import me.kingsavaqe.astrotournaments.utils.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ArmorCrystalsGUI {
    @Getter
    private final Gui armorCrystalGUI;
    private final TournamentManager tournamentManager;

    public ArmorCrystalsGUI() {
        this.tournamentManager = AstroTournamentsPlugin.instance.getTournamentManager();
        this.armorCrystalGUI = new Gui(5, "Armor Crystals Settings GUI");
        this.armorCrystalGUI.setDefaultClickAction(event -> event.setCancelled(true));

        for (ArmorSetType armorSetType : ArmorSetType.values()) {
            if (tournamentManager.getCurrentArmorCrystal().contains(armorSetType)) {
                this.armorCrystalGUI.addItem(new GuiItem(createActiveCrystal(AstroArmorSetsAPI.createCrystal(Arrays.asList(armorSetType), 100)), event -> {
                    tournamentManager.addArmorCrystalToCurrentCrystal(armorSetType, (Player) event.getWhoClicked());
                    new ArmorCrystalsGUI().getArmorCrystalGUI().open(event.getWhoClicked());
                }));
            } else {
                this.armorCrystalGUI.addItem(new GuiItem(AstroArmorSetsAPI.createCrystal(Arrays.asList(armorSetType), 100), event -> {
                    tournamentManager.addArmorCrystalToCurrentCrystal(armorSetType, (Player) event.getWhoClicked());
                    new ArmorCrystalsGUI().getArmorCrystalGUI().open(event.getWhoClicked());
                }));
            }
        }

        this.armorCrystalGUI.setItem(4, 5, new GuiItem(AstroArmorSetsAPI.createCrystal(tournamentManager.getCurrentArmorCrystal(), 100)));

        this.armorCrystalGUI.setItem(5, 9, new GuiItem(createExitItem(), event -> {
            event.getWhoClicked().closeInventory();
        }));

        this.armorCrystalGUI.setItem(5, 8, new GuiItem(createBackItem(), event -> {
            new AdminSettingsGUI(AstroTournamentsPlugin.instance).getUI().open(event.getWhoClicked());
        }));

    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit tournament menu", "&7You can execute /tournament leave", "&7to leave the tournament event").colorize().build();
    }

    private ItemStack createBackItem() {
        return new ItemBuilder(Material.ARROW, "&cGo back", "&7Click to go back to the admin settings").colorize().build();
    }


    public ItemStack createActiveCrystal(ItemStack crystal) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GREEN.getData());
        ItemMeta meta = crystal.getItemMeta().clone();
        ItemMeta newMeta = item.getItemMeta();
        newMeta.setDisplayName(meta.getDisplayName());
        newMeta.setLore(meta.getLore());
        item.setItemMeta(newMeta);
        ItemBuilder itemBuilder = new ItemBuilder(item);
        itemBuilder.setGlowing();
        return itemBuilder.build();
    }
}

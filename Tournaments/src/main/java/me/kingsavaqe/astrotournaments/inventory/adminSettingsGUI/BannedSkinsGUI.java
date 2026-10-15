package me.kingsavaqe.astrotournaments.inventory.adminSettingsGUI;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.Gui;
import lombok.Getter;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.enums.SkinType;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import me.kingsavaqe.astrotournaments.utils.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BannedSkinsGUI {
    @Getter
    private final Gui bannedSkinSettingsGUI;
    private final TournamentManager tournamentManager;

    public BannedSkinsGUI( ItemSkinType itemSkinType) {
        this.tournamentManager = AstroTournamentsPlugin.instance.getTournamentManager();
        this.bannedSkinSettingsGUI = new Gui(5, "Banned " + itemSkinType.name() + " Skins");
        this.bannedSkinSettingsGUI.setDefaultClickAction(event -> event.setCancelled(true));

        for (SkinType skinType : SkinType.values()) {
            if (!skinType.isEnabled()) continue;
            if (skinType.getItemSkinType() != itemSkinType) continue;
            if (tournamentManager.isBannedItemSkin(skinType)) {
                this.bannedSkinSettingsGUI.addItem(new GuiItem(createBannedSkinItem(skinType), event -> {
                    tournamentManager.banItemSkin(skinType, (Player) event.getWhoClicked());
                    new BannedSkinsGUI(itemSkinType).getBannedSkinSettingsGUI().open(event.getWhoClicked());

                }));
            } else {
                this.bannedSkinSettingsGUI.addItem(new GuiItem(skinType.getItem(), event -> {
                    tournamentManager.banItemSkin(skinType, (Player) event.getWhoClicked());
                    new BannedSkinsGUI(itemSkinType).getBannedSkinSettingsGUI().open(event.getWhoClicked());
                }));
            }


        }
        this.bannedSkinSettingsGUI.setItem(5, 9, new GuiItem(createExitItem(), event -> {
            event.getWhoClicked().closeInventory();
        }));

        this.bannedSkinSettingsGUI.setItem(5, 8, new GuiItem(createBackItem(), event -> {
            new AdminSettingsGUI(AstroTournamentsPlugin.instance).getUI().open(event.getWhoClicked());
        }));

    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit tournament menu", "&7You can execute /tournament leave", "&7to leave the tournament event").colorize().build();
    }

    private ItemStack createBackItem() {
        return new ItemBuilder(Material.ARROW, "&cGo back", "&7Click to go back to the admin settings").colorize().build();
    }


    public ItemStack createBannedSkinItem(SkinType skinType) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
        ItemMeta meta = skinType.getItem().getItemMeta().clone();
        ItemMeta newMeta = item.getItemMeta();
        newMeta.setDisplayName(meta.getDisplayName());
        newMeta.setLore(meta.getLore());
        item.setItemMeta(newMeta);
        ItemBuilder itemBuilder = new ItemBuilder(item);
        itemBuilder.setGlowing();
        return itemBuilder.build();
    }
}

package me.kingsavaqe.astrotournaments.inventory.settingsGUI;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.Gui;
import lombok.Getter;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.enums.SkinType;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import me.kingsavaqe.astrotournaments.objects.TournamentPlayer;
import me.kingsavaqe.astrotournaments.utils.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkinSettingsGUI {
    @Getter
    private final Gui skinSettingsGUI;


    public SkinSettingsGUI(TournamentPlayer tournamentPlayer, ItemSkinType itemSkinType) {
        this.skinSettingsGUI = new Gui(5, "Tournament " + itemSkinType.name() + " Skin Settings");
        this.skinSettingsGUI.setDefaultClickAction(event -> event.setCancelled(true));
        TournamentManager tournamentManager = AstroTournamentsPlugin.instance.getTournamentManager();

        for (SkinType skinType : SkinType.values()) {
            if (!skinType.isEnabled()) continue;
            if (skinType.getItemSkinType() != itemSkinType) continue;
            switch (itemSkinType) {
                case HELMET:
                    if (tournamentManager.isBannedItemSkin(skinType)) {
                        this.skinSettingsGUI.addItem(new GuiItem(createBannedSkinItem(skinType)));
                    } else if (tournamentPlayer.getHelmetSkin() == skinType) {
                        this.skinSettingsGUI.addItem(new GuiItem(createActiveSkinItem(skinType)));
                    } else {
                        this.skinSettingsGUI.addItem(new GuiItem(skinType.getItem(), event -> {
                            tournamentPlayer.setHelmetSkin(skinType);
                            tournamentPlayer.getPlayer().sendMessage(CC.translate("&a&l(!)&a Set your tournament helmet skin to: " + skinType.getItem().getItemMeta().getDisplayName()));
                            new SkinSettingsGUI(tournamentPlayer, itemSkinType).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
                        }));
                    }
                    break;
                case AMULET:
                    if (tournamentManager.isBannedItemSkin(skinType)) {
                        this.skinSettingsGUI.addItem(new GuiItem(createBannedSkinItem(skinType)));
                    } else if (tournamentPlayer.getChestplateSkin() == skinType) {
                        this.skinSettingsGUI.addItem(new GuiItem(createActiveSkinItem(skinType)));
                    } else {
                        this.skinSettingsGUI.addItem(new GuiItem(skinType.getItem(), event -> {
                            tournamentPlayer.setChestplateSkin(skinType);
                            tournamentPlayer.getPlayer().sendMessage(CC.translate("&a&l(!)&a Set your tournament chestplate skin to: " + skinType.getItem().getItemMeta().getDisplayName()));
                            new SkinSettingsGUI(tournamentPlayer, itemSkinType).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
                        }));
                    }
                    break;
                case BACKPACK:
                    if (tournamentManager.isBannedItemSkin(skinType)) {
                        this.skinSettingsGUI.addItem(new GuiItem(createBannedSkinItem(skinType)));
                    } else if (tournamentPlayer.getBackpackSkin() == skinType) {
                        this.skinSettingsGUI.addItem(new GuiItem(createActiveSkinItem(skinType)));
                    } else {
                        this.skinSettingsGUI.addItem(new GuiItem(skinType.getItem(), event -> {
                            tournamentPlayer.setBackpackSkin(skinType);
                            tournamentPlayer.getPlayer().sendMessage(CC.translate("&a&l(!)&a Set your tournament backpack skin to: " + skinType.getItem().getItemMeta().getDisplayName()));
                            new SkinSettingsGUI(tournamentPlayer, itemSkinType).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
                        }));
                    }
                    break;
                case BELT:
                    if (tournamentManager.isBannedItemSkin(skinType)) {
                        this.skinSettingsGUI.addItem(new GuiItem(createBannedSkinItem(skinType)));
                    } else if (tournamentPlayer.getLeggingsSkin() == skinType) {
                        this.skinSettingsGUI.addItem(new GuiItem(createActiveSkinItem(skinType)));
                    } else {
                        this.skinSettingsGUI.addItem(new GuiItem(skinType.getItem(), event -> {
                            tournamentPlayer.setLeggingsSkin(skinType);
                            tournamentPlayer.getPlayer().sendMessage(CC.translate("&a&l(!)&a Set your tournament leggings skin to: " + skinType.getItem().getItemMeta().getDisplayName()));
                            new SkinSettingsGUI(tournamentPlayer, itemSkinType).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
                        }));
                    }
                    break;
                case BOOTS:
                    if (tournamentManager.isBannedItemSkin(skinType)) {
                        this.skinSettingsGUI.addItem(new GuiItem(createBannedSkinItem(skinType)));
                    } else if (tournamentPlayer.getBootsSkin() == skinType) {
                        this.skinSettingsGUI.addItem(new GuiItem(createActiveSkinItem(skinType)));
                    } else {
                        this.skinSettingsGUI.addItem(new GuiItem(skinType.getItem(), event -> {
                            tournamentPlayer.setBootsSkin(skinType);
                            tournamentPlayer.getPlayer().sendMessage(CC.translate("&a&l(!)&a Set your tournament boots skin to: " + skinType.getItem().getItemMeta().getDisplayName()));
                            new SkinSettingsGUI(tournamentPlayer, itemSkinType).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
                        }));
                    }
                    break;
                case SWORD:
                    if (tournamentManager.isBannedItemSkin(skinType)) {
                        this.skinSettingsGUI.addItem(new GuiItem(createBannedSkinItem(skinType)));
                    } else if (tournamentPlayer.getSwordSkin() == skinType) {
                        this.skinSettingsGUI.addItem(new GuiItem(createActiveSkinItem(skinType)));
                    } else {
                        this.skinSettingsGUI.addItem(new GuiItem(skinType.getItem(), event -> {
                            tournamentPlayer.setSwordSkin(skinType);
                            tournamentPlayer.getPlayer().sendMessage(CC.translate("&a&l(!)&a Set your tournament sword skin to: " + skinType.getItem().getItemMeta().getDisplayName()));
                            new SkinSettingsGUI(tournamentPlayer, itemSkinType).getSkinSettingsGUI().open(tournamentPlayer.getPlayer());
                        }));
                    }
                    break;
            }


        }
        this.skinSettingsGUI.setItem(5, 9, new GuiItem(createExitItem(), event -> {
            tournamentPlayer.getPlayer().closeInventory();
        }));

        this.skinSettingsGUI.setItem(5, 8, new GuiItem(createBackItem(), event -> {
            new PlayerSettingsGUI(AstroTournamentsPlugin.instance,tournamentPlayer).getUI().open(tournamentPlayer.getPlayer());
        }));

        ItemStack glass = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName("&7").colorize().build();
        this.skinSettingsGUI.getFiller().fill(new GuiItem(glass));

    }

    private ItemStack createExitItem() {
        return new ItemBuilder(Material.FENCE_GATE, "&cExit tournament menu", "&7You can execute /tournament leave", "&7to leave the tournament event").colorize().build();
    }

    private ItemStack createBackItem() {
        return new ItemBuilder(Material.ARROW, "&cGo back", "&7Click to go back to the settings").colorize().build();
    }


    public ItemStack createActiveSkinItem(SkinType skinType) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getData());
        ItemMeta meta = skinType.getItem().getItemMeta().clone();
        ItemMeta newMeta = item.getItemMeta();
        newMeta.setDisplayName(meta.getDisplayName());
        newMeta.setLore(meta.getLore());
        item.setItemMeta(newMeta);
        ItemBuilder itemBuilder = new ItemBuilder(item);
        itemBuilder.setGlowing();
        return itemBuilder.build();
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

package me.ifamasssxd.cosmicmysterychest.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.data.LootboxFilterData;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import me.ifamasssxd.cosmicmysterychest.utils.FilterUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class FilterGUI {

    private Player player;

    private LootboxFilterData filter;

    private  PaginatedGui lootboxMenu;


    public PaginatedGui getFilterGUI() {
        return this.lootboxMenu;
    }

    public FilterGUI(Player player, MysteryChest chest) {
        this.player = player;
            this.filter = CosmicMysteryChest.get().getFilterManager().getLootFilterData(player.getUniqueId());
            this.lootboxMenu = new PaginatedGui(6, 45, "Loot Filter");

        lootboxMenu.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        lootboxMenu.getFiller().fillBottom(new GuiItem(createFillerItem()));

        lootboxMenu.setItem(6, 1, new GuiItem(createPreviousItem(), event -> {
            lootboxMenu.previous();
            event.setCancelled(true);
        }));

        lootboxMenu.setItem(6, 9, new GuiItem(createNextItem(), event -> {
            lootboxMenu.next();
            event.setCancelled(true);
        }));

        for (ItemStack item : FilterUtils.getItems(chest)) {
            lootboxMenu.addItem(createGuiItem(chest, item));
        }
    }

    public GuiItem createGuiItem(MysteryChest chest, ItemStack item) {
        return new GuiItem(filter.isFiltered(chest.getInternalName(), item) ? createFilteredItem(item.clone()) :  createNonFilteredItem(item.clone()), event -> {
            if (event.isRightClick() && event.isShiftClick() && event.getWhoClicked().isOp()) {
                chest.removeItem(item);
                FilterGUI filterGUI = new FilterGUI(player, chest);
                filterGUI.getFilterGUI().open(player);
                player.sendMessage(CC.translate("&c&l(!)&c Removed " + ItemUtils.getDisplayName(item) + "&c from " + chest.getDisplayName()));
                return;
            }
            filter.filterItem(chest.getInternalName(),item);

            lootboxMenu.updateItem(event.getSlot(), createGuiItem(chest, item));
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        });
    }

    public ItemStack createFilteredItem(ItemStack itemStack) {
        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData()));
        builder.addLoreLines(itemStack.getItemMeta().getLore());
        builder.name(itemStack.getItemMeta().getDisplayName());
        builder.addLoreLine("");
        builder.addLoreLine(CC.translate("&7Left-Click to &c&lUNFILTER &7this item."));
        return builder.build();
    }

    public ItemStack createNonFilteredItem(ItemStack itemStack) {
        ItemBuilder builder = new ItemBuilder(itemStack);
        builder.addLoreLine("");
        builder.addLoreLine(CC.translate("&7Left-Click to &a&lFILTER &7this item."));
        return builder.build();
    }

    public ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&f"));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createPreviousItem() {
        return new ItemBuilder(Material.ARROW, CC.translate("&c<- Go Back"), CC.translate("&7Right-Click&7 to go to the previous page.")).build();
    }

    public ItemStack createNextItem() {
        return new ItemBuilder(Material.ARROW, CC.translate("&aNext Page ->"), CC.translate("&7Right-Click&7 to go to the next page.")).build();

    }

}

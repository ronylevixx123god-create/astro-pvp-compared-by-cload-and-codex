package net.astropvp.lootfilter.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import net.astropvp.lootfilter.AstroLootFilterPlugin;
import net.astropvp.lootfilter.data.LootFilterData;
import net.astropvp.lootfilter.utils.ItemBuilder;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class LootFilterSubGUI {
    private final PaginatedGui lootFilterSubGUI;
    private LootFilterData data;

    public LootFilterSubGUI(UUID uuid, LootTable lootTable) {
        this.lootFilterSubGUI = new PaginatedGui(6, 54, lootTable.getFancy() + " Filter");
        this.lootFilterSubGUI.setDefaultClickAction(event -> event.setCancelled(true));
        this.data = AstroLootFilterPlugin.getInstance().getLootFilterManager().getLootFilterData(uuid);


        lootFilterSubGUI.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        lootFilterSubGUI.getFiller().fillBottom(new GuiItem(createFillerItem()));



        for (ItemStack item : LootTableAPI.getAllLootFromLootTable(lootTable)) {
            lootFilterSubGUI.addItem(getFilterItem(item.clone(), lootTable, data));
        }

        this.lootFilterSubGUI.setItem(6, 9, new GuiItem(createBackItem(), event -> {
            new LootFilterMainGUI(uuid).getLootFilterMainGUI().open(Bukkit.getPlayer(uuid));
        }));

        lootFilterSubGUI.getFiller().fillBottom(new GuiItem(createFillerItem()));

        ItemStack prevPage = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prevPage.getItemMeta();
        prevMeta.setDisplayName(CC.translate("&c<- Previous Page"));
        List<String> prevLore = new ArrayList<>();
        prevLore.add(CC.translate("&7Right-Click&7 to go to the previous page."));
        prevMeta.setLore(prevLore);
        prevPage.setItemMeta(prevMeta);

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextPage.getItemMeta();
        nextMeta.setDisplayName(CC.translate("&aNext Page ->"));
        List<String> nextLore = new ArrayList<>();
        nextLore.add(CC.translate("&7Right-Click&7 to go to the next page."));
        nextMeta.setLore(nextLore);
        nextPage.setItemMeta(nextMeta);

        lootFilterSubGUI.setItem(6, 5, new GuiItem(prevPage, event -> {
            lootFilterSubGUI.previous();
            event.setCancelled(true);
        }));

        lootFilterSubGUI.setItem(6, 6, new GuiItem(nextPage, event -> {
            lootFilterSubGUI.next();
            event.setCancelled(true);
        }));
    }

    public GuiItem getFilterItem(ItemStack item, LootTable lootTable, LootFilterData data) {

        return new GuiItem(data.isFiltered(lootTable, item) ? createFilteredItem(item.clone()) : createUnFilteredItem(item.clone()), event -> {
            data.filterItem(lootTable, item);

            lootFilterSubGUI.updateItem(event.getSlot(), getFilterItem(item, lootTable, data));
            data.getPlayer().playSound(data.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        });


    }

    public ItemStack createFilteredItem(ItemStack filterItem) {
        com.cosmicpvp.cosmicutils.utils.ItemBuilder builder = new com.cosmicpvp.cosmicutils.utils.ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData()));
        builder.addLoreLines(filterItem.getItemMeta().getLore());
        builder.name(filterItem.getItemMeta().getDisplayName());
        builder.addLoreLine("");
        builder.addLoreLine(CC.translate("&7Left-Click to &c&lUNFILTER &7this item."));
        return builder.build();
    }

    public ItemStack createUnFilteredItem(ItemStack filterItem) {
        com.cosmicpvp.cosmicutils.utils.ItemBuilder builder = new com.cosmicpvp.cosmicutils.utils.ItemBuilder(filterItem);
        builder.addLoreLine("");
        builder.addLoreLine(CC.translate("&7Left-Click to &a&lFILTER &7this item."));
        return builder.build();
    }

    private ItemStack createBackItem() {
        return new ItemBuilder(Material.ARROW, CC.translate("&cGo back"), CC.translate("&7Click to go back to the main menu")).build();
    }

    public ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&f"));
        item.setItemMeta(meta);
        return item;
    }
}

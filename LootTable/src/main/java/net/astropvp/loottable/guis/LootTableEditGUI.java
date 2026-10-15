package net.astropvp.loottable.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;

import net.astropvp.loottable.LootTablePlugin;
import net.astropvp.loottable.struct.ECO_UPDATE;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class LootTableEditGUI {
    private PaginatedGui lootEditGUI;

    public LootTableEditGUI(LootTable lootTableType, ECO_UPDATE ecoUpdate) {
        this.lootEditGUI = new PaginatedGui(6, 54, lootTableType.getFancy() + " Loot - " + ecoUpdate.getFancy());
        this.lootEditGUI.setItem(6, 1, new GuiItem(createPreviousItem(), event -> {
            LootTableMainGUI lootTableMainGUI = new LootTableMainGUI((Player) event.getWhoClicked());
            lootTableMainGUI.getMainGUI().open(event.getWhoClicked());
        }));

        lootEditGUI.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });



        for (Map.Entry<ItemStack, Double> entry : LootTablePlugin.getInstance().getLootTableManager().getLootTable(lootTableType).getLootFromEcoUpdate(ecoUpdate).entrySet()) {
            ItemStack item = entry.getKey();
            double chance = entry.getValue();
            if (ItemUtils.isAirOrNull(item)) continue;
            lootEditGUI.addItem(new GuiItem(new ItemBuilder(item.clone()).addLoreLine("").addLoreLine(CC.translate(lootTableType.getColor() + "Chance: &f" + chance + "%")).addLoreLine("").addLoreLine(CC.translate("&cRight-Click &7items in gui to remove")).build(), event -> {
                if (event.isRightClick()) {
                    event.getWhoClicked().sendMessage(CC.translate("&c&l(!)&c Removed " + ItemUtils.getDisplayName(item) + "&c from the " + lootTableType.getFancy() + " - " + lootTableType.getFancy()));
                    lootEditGUI.removeItem(event.getSlot());
                    LootTablePlugin.getInstance().getLootTableManager().getLootTable(lootTableType).removeItem(ecoUpdate, item);
                    LootTableEditGUI editGUI = new LootTableEditGUI(lootTableType, ecoUpdate);
                    editGUI.getLootEditGUI().open(event.getWhoClicked());
                } else {
                    event.getWhoClicked().getInventory().addItem(item);
                }
            }));
        }

        lootEditGUI.getFiller().fillBottom(new GuiItem(createFillerItem()));

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

        lootEditGUI.setItem(6, 5, new GuiItem(prevPage, event -> {
            lootEditGUI.previous();
            event.setCancelled(true);
        }));

        lootEditGUI.setItem(6, 6, new GuiItem(nextPage, event -> {
            lootEditGUI.next();
            event.setCancelled(true);
        }));
    }

    public ItemStack createPreviousItem() {
        return new ItemBuilder(Material.ARROW, CC.translate("&c<- Go Back"), CC.translate("&7Right-Click&7 to go back.")).build();
    }

    public ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&f"));
        item.setItemMeta(meta);
        return item;
    }
}

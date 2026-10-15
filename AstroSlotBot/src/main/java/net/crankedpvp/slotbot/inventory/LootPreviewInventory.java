package net.crankedpvp.slotbot.inventory;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.crankedpvp.slotbot.SlotBotAPI;
import net.crankedpvp.slotbot.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class LootPreviewInventory {

    private final PaginatedGui slotBotPreviewInventory;

    public PaginatedGui getUI() {
        return this.slotBotPreviewInventory;
    }


    public LootPreviewInventory(Player player) {

        this.slotBotPreviewInventory = new PaginatedGui(6, 28, "Roll Bot Loot");
        this.slotBotPreviewInventory.setDefaultClickAction(event -> event.setCancelled(true));
        this.slotBotPreviewInventory.getFiller().fillBorder(new GuiItem(createFillerItem()));
        ItemStack prevPage = new ItemBuilder(Material.ARROW, "&c<- Previous Page", "&7Right-Click&7 to go to the previous page.").colorize().build();
        ItemStack nextPage = new ItemBuilder(Material.ARROW, "&aNext Page ->", "&7Right-Click&7 to go to the next page.").colorize().build();
        this.slotBotPreviewInventory.setItem(6, 1, new GuiItem(prevPage, event -> {
            this.slotBotPreviewInventory.previous();
            event.setCancelled(true);
        }));
        this.slotBotPreviewInventory.setItem(6, 9, new GuiItem(nextPage, event -> {
            this.slotBotPreviewInventory.next();
            event.setCancelled(true);
        }));


        this.slotBotPreviewInventory.updateItem(6, 5, new GuiItem(new ItemBuilder(Material.BED, "&cReturn to Roll Bot").colorize().build(), event -> {
            player.closeInventory();
            SlotBotInventory slotBotInventory = new SlotBotInventory(player);
            slotBotInventory.getSlotBotInventory().open(player);
        }));

        SlotBotAPI.getRewards().stream()
                .filter(Objects::nonNull)
                .filter(ItemStack::hasItemMeta)
                .forEach(itemStack -> this.slotBotPreviewInventory.addItem(new GuiItem(itemStack, event -> {
                    if(player.hasPermission("cranked.admin")) player.getInventory().addItem(itemStack);
                })));
    ;
    }

    private ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&6Astro&fPvP"));
        item.setItemMeta(meta);
        return item;
    }
}

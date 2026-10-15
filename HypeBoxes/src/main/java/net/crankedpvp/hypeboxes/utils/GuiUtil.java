package net.crankedpvp.hypeboxes.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiUtil {

    public static Gui setupGui(String title, boolean exitButton) {
        Gui gui = new Gui(6, title);
        gui.getFiller().fillBorder(new GuiItem(createFillerItem()));
        if (exitButton)
            gui.setItem(6, 5, new GuiItem(createExitItem()));
        return gui;
    }

    public static Gui setupGui(String title) {
        return setupGui(title, true);
    }

    public static PaginatedGui setupGui(PaginatedGui gui) {
        if(gui.getRows() != 6) return gui;
        gui.getFiller().fillBorder(new GuiItem(createFillerItem()));
        gui.setItem(6, 5, new GuiItem(createExitItem(), event -> gui.close(event.getWhoClicked())));
        gui.setItem(6, 1, new GuiItem(createPreviousItem(), event -> gui.previous()));
        gui.setItem(6, 9, new GuiItem(createNextItem(), event -> gui.next()));
        return gui;
    }

    public static ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(" "));
        item.setItemMeta(meta);
        return item;
    }

    public static GuiItem createFillerGuiItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(" "));
        item.setItemMeta(meta);
        return new GuiItem(item);
    }

    public static ItemStack createExitItem() {
        return (new ItemBuilder(Material.REDSTONE, "&cExit Menu")).colorize().build();
    }

    public static ItemStack createPreviousItem() {
        return new ItemBuilder(Material.ARROW, "&c<- Previous Page", "&7Right-Click&7 to go to the previous page.").colorize().build();
    }

    public static ItemStack createNextItem() {
        return new ItemBuilder(Material.ARROW, "&aNext Page ->", "&7Right-Click&7 to go to the next page.").colorize().build();
    }
}

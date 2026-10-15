package net.astropvp.christmas.inventories;

import com.cosmicpvp.cosmicutils.utils.CC;

import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import net.astropvp.christmas.ChristmasPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class ChristmasInventory {
    @Getter
    private Gui gui;
    public ChristmasInventory() {
        this.gui = new Gui(1, "Santa Daily Reward");
        this.gui.setDefaultClickAction(event -> event.setCancelled(true));
        this.gui.setItem(3, getCurrentItem());
        this.gui.setItem(5, getInformationBook());
    }

    private GuiItem getCurrentItem() {
        ItemStack shopItem = ChristmasPlugin.getInstance().getChristmasManager().getCurrentItem().clone();
        ItemStack item = new ItemBuilder(shopItem).build();
        return new GuiItem(item.clone(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (!ChristmasPlugin.getInstance().getChristmasManager().canBuy(player)) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You may only get this item once per restock.");
                return;
            }
            if (player.getInventory().firstEmpty() < 0) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must have an empty slot to get this item.");
                return;
            }
            ChristmasPlugin.getInstance().getChristmasManager().setAlreadyBought(player);
            player.getInventory().addItem(ChristmasPlugin.getInstance().getChristmasManager().getCurrentItem().clone());
            Bukkit.broadcastMessage(CC.translate("&2&l<&a&lSanta&2&l> &f" + player.getName() + " &ahas just claimed the " + getCurrentItem().getItemStack().getItemMeta().getDisplayName() + " &afrom the daily &f/santa&a present!"));
        }){
        };
    }

    private GuiItem getInformationBook() {
        ItemStack item = new ItemBuilder(Material.COOKIE).name(CC.DarkGreenB + "Christmas Daily Present").lore(CC.translate((String)"&7This is a place where you you can"), CC.translate((String)"&7claim your daily reward from"), CC.translate((String)"&7Santa Claus. He will give you each item"), CC.translate((String)"&7only once and if you miss it, that"), CC.translate((String)"&7is your own fault, Santa Claus"), CC.translate((String)"&7has got a busy schedule...")).build();
        return new GuiItem(item){};
    }
}

package net.astropvp.core.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Lists;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarpGUI {
    private PaginatedGui expShopUI;



    public PaginatedGui getLootGUI() {
        return this.expShopUI;
    }





    public WarpGUI(Player player) {


        this.expShopUI = new PaginatedGui(1, 9, "Warp GUI");
        expShopUI.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });




        ItemStack desert = new ItemBuilder(Material.SAND, CC.translate("&6&lDesert Warp"), CC.translate("&7Click to teleport to this warp")).build();
        ItemStack crates = new ItemBuilder(Material.CHEST, CC.translate("&b&lCrates Warp"), CC.translate("&7Click to teleport to this warp")).build();
        ItemStack space = new ItemBuilder(Material.PRISMARINE, CC.translate("&d&lSpace Warp"), CC.translate("&7Click to teleport to this warp")).build();
        ItemStack pluto = new ItemBuilder(Material.QUARTZ, CC.translate("&a&lPluto Outpost"), CC.translate("&7Click to teleport to this warp")).build();
        ItemStack mars = new ItemBuilder(Material.NETHER_STALK, CC.translate("&c&lMars Outpost"), CC.translate("&7Click to teleport to this warp")).build();
        ItemStack flat = new ItemBuilder(Material.RED_SANDSTONE, CC.translate("&d&lFlat World"), CC.translate("&7Click to teleport to this warp")).build();
        ItemStack battlefield = new ItemBuilder(Material.DIAMOND_SWORD, CC.translate("&4&lWarp Battlefield"), CC.translate("&7Click to teleport to this warp")).build();

        ItemStack nebula;

        if (player.hasPermission("astrocore.nebula")) {
            nebula = new ItemBuilder(Material.NETHER_STAR, CC.translate("&5&lNebula Lounge"), CC.translate("&7Click to teleport to this warp")).build();

        } else {
            nebula = new ItemBuilder(Material.BARRIER, CC.translate("&5&lNebula Lounge"), CC.translate("&cThis warp requires nebula rank. &7(/buy)")).build();

        }



        expShopUI.setItem(1, 1, new GuiItem((desert), event -> player.performCommand("warp desert")));
        expShopUI.setItem(1, 2, new GuiItem((crates), event -> player.performCommand("warp crates")));
        expShopUI.setItem(1, 3, new GuiItem((nebula), event -> player.performCommand("warp nebula")));
        expShopUI.setItem(1, 4, new GuiItem((space), event -> player.performCommand("warp space")));
        expShopUI.setItem(1, 5, new GuiItem((pluto), event -> player.performCommand("warp outpost-pluto")));
        expShopUI.setItem(1, 6, new GuiItem((mars), event -> player.performCommand("warp outpost-mars")));
        expShopUI.setItem(1, 7, new GuiItem((flat), event -> player.performCommand("warp flat")));
        expShopUI.setItem(1, 8, new GuiItem((battlefield), event -> player.performCommand("warp battlefield")));




    }


}

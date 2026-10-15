package net.astropvp.christmas.inventories;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import me.matthawkk.chat.messages.PrefixedMessage;
import me.matthawkk.gui.BaseGUI;
import me.matthawkk.gui.GUIItem;
import me.matthawkk.item.ItemBuilder;
import net.astropvp.christmas.NebulaShop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class NebulaShopInventory extends BaseGUI {
    public Player player;
    public NebulaShopInventory(Player player) {
        super("Nebula Shop", 9);
        this.player = player;
        setItem(3, getCurrentItem());
        setItem(5, getInformationBook());
    }


    private GUIItem getCurrentItem() {
        ItemStack shopItem = NebulaShop.getInstance().getNebulaShopManager().getCurrentItem().clone();
        double price = NebulaShop.getInstance().getNebulaShopManager().getCurrentPrice();
        if (player.hasPermission("astrocore.galaxy")) {
            price = 0;
        }
        ItemStack item = new ItemBuilder(shopItem).addLore("").addLore(CC.LightPurpleB + " * PRICE: " + CC.White + "$" + NumberUtils.formatMoney(price)).build();
        double finalPrice = price;
        return new GUIItem(item.clone()) {
            public void onClick(Player player1, ClickType clickType, InventoryClickEvent inventoryClickEvent) {
                if (!NebulaShop.getInstance().getNebulaShopManager().canBuy(player1)) {
                    player1.sendMessage(CC.RedB + "(!) " + CC.Red + "You may only get this item once per restock and 1 per ip.");
                    return;
                }
                if (player1.getInventory().firstEmpty() < 0) {
                    player1.sendMessage(CC.RedB + "(!) " + CC.Red + "You must have an empty slot to get this item.");
                    return;
                }


                if(!EconomyUtils.hasBalance(player1, finalPrice)) {
                    player1.sendMessage(CC.RedB + "(!) " + CC.Red + "You require $" + NumberUtils.formatMoney(finalPrice) + " to purchase this item.");
                    return;
                }
                NebulaShop.getInstance().getNebulaShopManager().setAlreadyBought(player1);
                EconomyUtils.withdrawBalance(player1, finalPrice);
                NebulaShop.getInstance().getNebulaShopManager().setAlreadyBought(player1);
                player1.getInventory().addItem(NebulaShop.getInstance().getNebulaShopManager().getCurrentItem());
                Bukkit.broadcastMessage(CC.translate("&5&l<&d&lNebula&5&l> ") +  CC.DarkPurple + player1.getName() + CC.LightPurple + " has purchased " + NebulaShop.getInstance().getNebulaShopManager().getCurrentItem().getItemMeta().getDisplayName() + CC.LightPurple + " from the " + CC.LightPurpleU + "Nebula Shop!");
            }
        };
    }

    private GUIItem getInformationBook() {
        ItemStack item = new ItemBuilder(Material.BOOK).name(CC.LightPurpleB + "Nebula Shop").lore("The Nebula shop is a secret",
                CC.Gray + "market, only accessible by the",
                CC.translate( "&d&l<&5&l&o&nN&d&l&o&ne&5&l&n&ob&d&l&n&ou&5&l&n&ol&d&l&n&oa&5&l> ") + CC.Gray + "rank holders",
                "",
                CC.Gray + "The items in the Nebula shop are",
                CC.Gray + "only available for " + CC.GrayU + "limited time",
                CC.Gray + "before being replaced with new items.",
                "",
                CC.RedB + "NOTE: " + CC.Red + "You can only purchase items",
                CC.Red + "on from the Nebula Shop 1 time per restock"
                ).build();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.LightPurpleB + "Nebula Shop");
        List<String> lore = new ArrayList<>();
        lore.add(CC.Gray + "The Nebula shop is a secret");
        lore.add(CC.Gray + "market, only accessible by the");
        lore.add(CC.translate( "&d&l<&5&l&o&nN&d&l&o&ne&5&l&n&ob&d&l&n&ou&5&l&n&ol&d&l&n&oa&5&l> ") + CC.Gray + "rank holders");
        lore.add("");
        lore.add(CC.Gray + "The items in the Nebula shop are");
        lore.add(CC.Gray + "only available for " + CC.GrayU + "limited time");
        lore.add(CC.Gray + "before being replaced with new items.");
        lore.add("");
        lore.add(CC.RedB + "NOTE: " + CC.Red + "You can only purchase items");
        lore.add(CC.Red + "on from the Nebula Shop 1 time per restock");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return new GUIItem(item) {};

    }
}

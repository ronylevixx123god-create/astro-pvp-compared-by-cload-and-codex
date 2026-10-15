package net.astropvp.giftcardshop.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;

import net.astropvp.giftcardshop.GiftcardShopPlugin;
import net.astropvp.giftcardshop.struct.Giftcard;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.*;

@Getter
public class GiftcardMenu {
    private PaginatedGui giftcardsMenu;

    public GiftcardMenu(UUID uuid) {
        this.giftcardsMenu = new PaginatedGui(6,56, "Your Giftcards - " + new Date().toString());


        for (Giftcard giftcard : GiftcardShopPlugin.getInstance().getGiftcards().get(uuid)) {
            String code = giftcard.getCode();
            double amount = giftcard.getAmount();
            ItemStack item = new ItemBuilder(Material.PAPER, CC.translate("&a&lGiftcard"))
                    .addLoreLine("")
                    .addLoreLine(CC.translate("&f&l● &aCode: &f") + code)
                    .addLoreLine(CC.translate("&f&l● &aCreated: &f") + giftcard.getCreationDate())
                    .addLoreLine(CC.translate("&f&l● &aInitial Amount: &f$") + NumberUtils.formatMoney(amount))
                    .addLoreLine("")
                    .addLoreLine(CC.translate("&7You can use this giftcard on &fbuy.astropvp.net&7."))
                    .addLoreLine("")
                    .addLoreLine(CC.translate("&7* You can check the remaining balance at"))
                    .addLoreLine(CC.translate("&fbuy.astropvp.net/#giftcardbalance&7!"))
                    .build();

            this.giftcardsMenu.addItem(new GuiItem(item));
        }
        this.giftcardsMenu.setDefaultClickAction(event -> event.setCancelled(true));
        this.giftcardsMenu.getFiller().fillBorder(new GuiItem(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7)).name(CC.translate("&7")).build()));

        //ItemStack sortButton = new ItemBuilder(Material.REDSTONE_TORCH_ON, CC.translate("&c&lSort By Date")).addLoreLine(CC.translate("&7Click this button to sort giftcards")).addLoreLine(CC.translate("&7from latest to oledest creation date.")).build();

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

        this.giftcardsMenu.setItem(6, 4, new GuiItem(prevPage, event -> {
            this.giftcardsMenu.previous();
            event.setCancelled(true);
        }));


        this.giftcardsMenu.setItem(6, 6, new GuiItem(nextPage, event -> {
            this.giftcardsMenu.next();
            event.setCancelled(true);
        }));


    }
}

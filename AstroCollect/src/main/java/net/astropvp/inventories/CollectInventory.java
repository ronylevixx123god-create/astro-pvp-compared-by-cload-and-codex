package net.astropvp.inventories;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Lists;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;

import net.astropvp.CollectPlugin;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.PlayerCollect;
import net.astropvp.utils.TimeUtil;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CollectInventory {
    private final PaginatedGui collectUI;

    public PaginatedGui getCollectUI() {
        return this.collectUI;
    }

    public CollectInventory(Player player) {
        this.collectUI = new PaginatedGui(6, 45, "Collect Menu");
        collectUI.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });
        collectUI.getFiller().fillBottom(new GuiItem(createFillerItem()));
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

        collectUI.setItem(6, 1, new GuiItem(prevPage, event -> {
            collectUI.previous();
            event.setCancelled(true);
        }));

        collectUI.setItem(6, 9, new GuiItem(nextPage, event -> {
            collectUI.next();
            event.setCancelled(true);
        }));

        PlayerCollect collect = CollectPlugin.getInstance().getCollectManager().getOrCreate(player.getUniqueId().toString());
        List<CollectCategory> categories = collect.getCategories();
        for (CollectCategory cat : categories) {
            collectUI.addItem(new GuiItem(getCategoryItem(player, cat), event -> {
                if (event.getClick() == ClickType.SHIFT_RIGHT) {
                    CollectPlugin.getInstance().getCollectManager().removeCategoryFromCache(player.getUniqueId().toString(), cat);
                    player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1f, 1f);
                    CollectInventory collectInventory = new CollectInventory(player);
                    collectInventory.getCollectUI().open(player);
                    player.sendMessage(CC.translate("&cRemoved the collect inventory."));
                } else {
                    new CategoryInventory(player, cat).getCollectUI().open(player);
                }
            }));
        }
    }

    private ItemStack getCategoryItem(Player player, CollectCategory cat) {
        return new ItemBuilder(cat.getType().getMaterial(), CC.translate(cat.getName()), CC.translate(""), CC.translate("&aLeft-Click&7 to open inventory"), CC.translate("&cSHIFT Right-Click&7 to remove inventory"), "", CC.translate("&6Expires in&7: &f") +
                ((cat.getTimeCollected() + 86400000) - System.currentTimeMillis() <= 0 ? "Now" : TimeUtil.fancy((cat.getTimeCollected() + 86400000) - System.currentTimeMillis()))).build();
    }


    public ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&f"));
        item.setItemMeta(meta);
        return item;
    }
}
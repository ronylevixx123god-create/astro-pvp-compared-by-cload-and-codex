package net.astropvp.inventories;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;

import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.astropvp.CollectPlugin;
import net.astropvp.struct.CollectCategory;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CategoryInventory {
    private final PaginatedGui categoryInventory;

    public PaginatedGui getCollectUI() {
        return this.categoryInventory;
    }

    public CategoryInventory(Player player, CollectCategory category) {
        this.categoryInventory = new PaginatedGui(6, 45, category.getName());
        categoryInventory.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });
        categoryInventory.getFiller().fillBottom(new GuiItem(createFillerItem()));
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
        ItemStack removeCategory = new ItemStack(Material.BARRIER);
        ItemMeta meta = removeCategory.getItemMeta();
        meta.setDisplayName(CC.RedB + "REMOVE CATEGORY");
        List<String> lore = new ArrayList<>();
        lore.add(CC.Gray + "Use this button to remove this category");
        lore.add("");
        lore.add(CC.translate("&c&lWARNING: &7This cannot be undone."));
        meta.setLore(lore);
        removeCategory.setItemMeta(meta);

        ItemBuilder builder = new ItemBuilder(Material.REDSTONE_TORCH_ON, CC.translate("&c&lSort Items"));
        builder.addLoreLine(CC.translate("&7Click this button to sort all the items."));
        ItemStack sortCategory = builder.build();

        categoryInventory.setItem(6, 1, new GuiItem(prevPage, event -> {
            int pageNum = categoryInventory.getCurrentPageNum();
            PaginatedGui inventory= new CategoryInventory(player, category).getCollectUI();
            inventory.open(player, pageNum);
            inventory.previous();
            event.setCancelled(true);
        }));

        categoryInventory.setItem(6, 9, new GuiItem(nextPage, event -> {
            int pageNum = categoryInventory.getCurrentPageNum();
            PaginatedGui inventory= new CategoryInventory(player, category).getCollectUI();
            inventory.open(player, pageNum);
            inventory.next();

            event.setCancelled(true);
        }));

        categoryInventory.setItem(6, 3, new GuiItem(removeCategory, event -> {
            CollectPlugin.getInstance().getCollectManager().removeCategoryFromCache(player.getUniqueId().toString(), category);
            new CollectInventory(player).getCollectUI().open(player);
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1f, 1f);
            player.sendMessage(CC.translate("&cRemoved the collect inventory."));

        }));

        categoryInventory.setItem(6, 4, new GuiItem(new ItemBuilder(Material.BED, CC.translate("&c&lMain Menu"), CC.translate("&7Click to go to the main menu")).build(), event -> {
                    CollectInventory collectInventory = new CollectInventory(player);
                    collectInventory.getCollectUI().open(player);
                })
        );

        categoryInventory.setItem(6, 6, new GuiItem(sortCategory, event -> {
            player.playSound(player.getLocation(), Sound.BAT_LOOP, 1f, 1f);
            category.sort();
            new CategoryInventory(player, category).getCollectUI().open(player);
            event.setCancelled(true);
        }));

        categoryInventory.setItem(6, 7, new GuiItem(createCollectAllItem(), event -> {
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);
            for (ItemStack item : category.getItems().clone()) {
                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(CC.translate("&c&l(!)&c You do not have enough inventory slots to claim any more items"));
                    event.setCancelled(true);
                    return;
                }


                player.getInventory().addItem(item);
                categoryInventory.getInventory().remove(event.getCurrentItem());
                category.setItems(category.removeItem(item));


                if (category.onlyContainsAir()) {
                    categoryInventory.close(player);
                    CollectPlugin.getInstance().getCollectManager().removeCategoryFromCache(player.getUniqueId().toString(), category);
                    new CollectInventory(player).getCollectUI().open(player);
                } else {
                    category.sort();
                    new CategoryInventory(player, category).getCollectUI().open(player);
                    event.setCancelled(true);
                }
                event.setCancelled(true);
            }
        }));

        for (ItemStack item : category.getItems().clone()) {
            categoryInventory.addItem(new GuiItem(item.clone(), event -> {


                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(CC.translate("&c&l(!)&c Please clear some inventory space and try again!"));
                    event.setCancelled(true);
                    return;
                }


                player.getInventory().addItem(item);
                categoryInventory.getInventory().remove(event.getCurrentItem());
                category.setItems(category.removeItem(item));


                if (category.onlyContainsAir()) {
                    categoryInventory.close(player);
                    CollectPlugin.getInstance().getCollectManager().removeCategoryFromCache(player.getUniqueId().toString(), category);
                    player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1f, 1f);
                    new CollectInventory(player).getCollectUI().open(player);
                }
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.9F);

            }));
        }
    }

    public ItemStack createCollectAllItem() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.CHEST);
        itemBuilder.name(CC.translate("&d&lCollect All"));
        itemBuilder.lore(
                CC.translate("&7Click to collect all of the items"),
                CC.translate("&7in this collect category")
        );
        return itemBuilder.build();
    }

    public ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&f"));
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isInvEmpty(Inventory inv) {
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }
}
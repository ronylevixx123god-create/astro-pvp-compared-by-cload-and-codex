package me.astropvp.astropvpskins.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.inventories.ItemSkinSubInventory;
import me.astropvp.astropvpskins.inventories.ItemSkinsInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemSkinInventoryClickListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void click(InventoryClickEvent e) {
        String title = e.getInventory().getTitle();
        ItemStack item = e.getCurrentItem();
        Player player = (Player)e.getWhoClicked();
        if (item == null)
            return;
        if (title.equals(CC.translate("&8Astro Item Skins"))) {
            e.setCancelled(true);
            Material material = item.getType();
            ItemSkinType type = (material == Material.DIAMOND_HELMET) ? ItemSkinType.HELMET : ((material == Material.DIAMOND_CHESTPLATE) ? ItemSkinType.CHESTPLATE : ((material == Material.DIAMOND_LEGGINGS) ? ItemSkinType.LEGGINGS : ((material == Material.DIAMOND_BOOTS) ? ItemSkinType.BOOTS : ((material == Material.DIAMOND_AXE) ? ItemSkinType.AXE : ((material == Material.DIAMOND_SWORD) ? ItemSkinType.SWORD : ((material == Material.BOW) ? ItemSkinType.BOW : ((material == Material.IRON_LEGGINGS) ? ItemSkinType.BELT : ((material == Material.IRON_CHESTPLATE) ? ItemSkinType.AMULET : ((material == Material.DIAMOND_PICKAXE) ? ItemSkinType.PICKAXE : ((material == Material.GOLD_CHESTPLATE) ? ItemSkinType.BACKPACK : null))))))))));
            if (type == null)
                return;
            if (e.getCurrentItem().getType() == Material.DIAMOND_LEGGINGS || e.getCurrentItem().getType() == Material.DIAMOND_CHESTPLATE) {
                e.setCancelled(true);
                return;
            }
            ItemSkinSubInventory.openInventory(player, type);
        } else if (title.startsWith(CC.translate("&8Item Skins:"))) {
            if (item.equals(ItemSkinSubInventory.createBackButton()))
                ItemSkinsInventory.openInventory(player);
            if (AstroItemSkinsAPI.isItemSkin(item) && player.isOp()) {
                player.getInventory().addItem(item);
                player.sendMessage(CC.translate("&aGiven you the &f" + AstroItemSkinsAPI.getItemSkin(item).get(0).name() + "&a."));
            }
            e.setCancelled(true);
        }
    }
}

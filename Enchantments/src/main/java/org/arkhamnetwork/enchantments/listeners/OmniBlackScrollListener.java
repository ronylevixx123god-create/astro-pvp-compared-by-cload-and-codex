package org.arkhamnetwork.enchantments.listeners;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ParticleEffect;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import java.util.Random;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.items.OmniBlackscroll;
import org.arkhamnetwork.enchantments.items.OmniBlackscrollItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class OmniBlackScrollListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equalsIgnoreCase(CC.translate("&8Select an enchantment to remove.")))
            return;
        event.setCancelled(true);
        Player player = (Player)event.getWhoClicked();
        if (event.getRawSlot() <= event.getInventory().getSize()) {
            if (!OmniBlackscroll.getModifiedItems().containsKey(player.getUniqueId())) {
                player.sendMessage(CC.translate("&c&l(!) &cSorry, an error occured, please report this in Bug-Reports."));
                player.closeInventory();
                return;
            }
            if (event.getCurrentItem() == null || !event.getCurrentItem().getType().equals(Material.BOOK))
                return;
            OmniBlackscrollItem data = (OmniBlackscrollItem)OmniBlackscroll.getModifiedItems().get(player.getUniqueId());
            ItemStack item = data.getItem();
            String enchantmentName = OmniBlackscroll.getEnchantmentName(event.getCurrentItem());
            CustomEnchantment enchantment = EnchantmentAPI.getEnchantment(enchantmentName);
            final int level = EnchantmentAPI.getEnchantments(item).get(enchantment);
            if (level > enchantment.getMaxLevel()) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot blackscroll an admin item enchant!");
                event.setCancelled(true);
                return;
            }

            enchantment.removeFromItem(item);
            player.getInventory().setItem(data.getSlot(), item);
            player.sendMessage(CC.translate("&a&l(!) &aYou have removed the &f&l" + enchantment.name() + "&a enchantment from " + data.getItem().getItemMeta().getDisplayName() + "&a!"));
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.5F, 0.5F);
            Location l = player.getLocation();
            Random random = new Random();
            player.removeMetadata("omniBlackScrollEdit", (Plugin)Enchantments.getInstance());
            player.closeInventory();
            PlayerUtil.giveOrDropProtectedItem(player, EnchantmentBook.getEnchantmentBook(enchantment, enchantment.getMaxLevel(), 100, 0), 60);
            player.updateInventory();
        }
    }

    @EventHandler
    public void onExit(InventoryCloseEvent event) {
        if (!event.getInventory().getName().equalsIgnoreCase(CC.translate("&8Select an enchantment to remove.")))
            return;
        Player player = (Player)event.getPlayer();
        OmniBlackscroll.getModifiedItems().remove(player.getUniqueId());
        if (player.hasMetadata("omniBlackScrollEdit")) {
            player.sendMessage(CC.translate("&c&l(!) &cYou have exited the inventory without using the Omni Black Scroll!"));
            ItemStack item = CosmicSkyCustomItems.get().getItemManager().getCustomItem("omni_black_scroll").createItem(player, null);
            PlayerUtil.giveOrDropProtectedItem(player, item, 60);
            player.removeMetadata("omniBlackScrollEdit", (Plugin)Enchantments.getInstance());
        }
    }
}

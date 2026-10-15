package net.astropvp.core.listeners;

import net.astropvp.core.AstroCore;
import net.astropvp.core.items.coder.EnchantmentCoderInventory;
import net.astropvp.core.items.coder.EnchantmentCoderManager;
import org.bukkit.event.Listener;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.CustomEnchantment;
import java.util.List;
import java.util.UUID;
import me.ifamasssxd.cosmicduel.utils.PlayerUtils;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EnchantmentCoderInventoryListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!inventory.getTitle().equals(CC.translate("&8Heroic Enchantment Coder")))
            return;
        event.setCancelled(true);
        if (inventory != event.getClickedInventory())
            return;
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR || item.getType() == Material.STAINED_GLASS_PANE)
            return;
        if (event.getClick() != ClickType.LEFT)
            return;
        Material type = item.getType();
        if (type == null)
            return;
        Player player = (Player)event.getWhoClicked();
        player.closeInventory();
        Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), () -> player.openInventory(EnchantmentCoderInventory.getInstance().getChildInventory(player.getUniqueId(), type)), 1L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChildInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!inventory.getTitle().contains(CC.translate("&8Heroic Enchantment Coder - ")))
            return;
        event.setCancelled(true);
        if (event.getClick() != ClickType.LEFT)
            return;
        ItemStack item = event.getCurrentItem();
        Player player = (Player)event.getWhoClicked();
        if (item != null && item.getType() == Material.ARROW && inventory == event.getClickedInventory()) {
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater((Plugin)Enchantments.getInstance(), () -> player.openInventory(EnchantmentCoderInventory.getInstance().getInventory()), 1L);
        }
        if (!EnchantmentBook.isEnchantmentBook(item))
            return;
        Material type = Material.valueOf(inventory.getTitle().substring(inventory.getTitle().indexOf("-") + 2, inventory.getTitle().length()).toUpperCase());
        EnchantmentCoderManager manager = AstroCore.getInstance().getEnchantmentCoderManager();
        UUID uuid = player.getUniqueId();
        List<CustomEnchantment> enchantments = manager.getEnchantmentsForUser(uuid, type);
        CustomEnchantment enchantment = EnchantmentBook.getEnchantment(item);
        if (inventory != event.getClickedInventory()) {
            if (enchantments.size() >= 14) {
                player.sendMessage(CC.translate("&c&l(!) &cThere cannot be more than 14 enchantments!"));
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                event.setCancelled(true);
                return;
            }
            if (EnchantmentBook.getEnchantmentBookSuccessRate(item) < 100 || EnchantmentBook.getEnchantmentLevel(item) != enchantment.getMaxLevel()) {
                player.sendMessage(CC.translate("&c&l(!) &cBook must be 100% success rate!"));
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                event.setCancelled(true);
                return;
            }
            if (EnchantmentBook.getEnchantment(item).name().equalsIgnoreCase("rogue")) {
                player.sendMessage(CC.translate("&c&l(!) &cYou cannot use this book in enchant encoders!"));
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                event.setCancelled(true);
                return;
            }
            if (EnchantmentBook.getEnchantment(item).name().equalsIgnoreCase("revoke")) {
                player.sendMessage(CC.translate("&c&l(!) &cYou cannot use this book in enchant encoders!"));
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                event.setCancelled(true);
                return;
            }
            if (!isMaterialApplicable(type, enchantment)) {
                player.sendMessage(CC.translate("&c&l(!) &cBook is not applicable to the item!"));
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                event.setCancelled(true);
                return;
            }
            if (item.getAmount() > 1) {
                player.sendMessage(CC.translate("&c&l(!) &cYou cannot add stacked books!"));
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                event.setCancelled(true);
                return;
            }
            enchantments.add(enchantment);
            manager.add(uuid, type, enchantments);
            event.setCurrentItem(null);
            EnchantmentCoderInventory.getInstance().refreshChildInventory(uuid, type, inventory);
            player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
            return;
        }
        player.playSound(player.getLocation(), Sound.CLICK, 0.5F, 0.5F);
        enchantments.remove(enchantment);
        manager.add(uuid, type, enchantments);
        PlayerUtils.addItem(player, EnchantmentBook.getEnchantmentBook(enchantment, enchantment.getMaxLevel(), 100, 0));
        event.setCurrentItem(null);
        EnchantmentCoderInventory.getInstance().refreshChildInventory(uuid, type, inventory);
    }

    private boolean isMaterialApplicable(Material type, CustomEnchantment enchantment) {
        Material[] allowedMaterials = enchantment.getNaturalMaterials();
        for (Material mat : allowedMaterials) {
            if (mat == type)
                return true;
        }
        return false;
    }
}

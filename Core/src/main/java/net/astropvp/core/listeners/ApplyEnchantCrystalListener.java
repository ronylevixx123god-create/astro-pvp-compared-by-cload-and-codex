package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.utils.EnchantmentCrystalUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ApplyEnchantCrystalListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e)  {
        Player player = (Player) e.getWhoClicked();
        ItemStack cursor = e.getCursor();
        ItemStack clicked = e.getCurrentItem();
        if(clicked == null || clicked.getType() == Material.AIR || e.getClickedInventory() != e.getView().getBottomInventory())
            return;
        if(EnchantmentCrystalUtils.isEnchantmentCrystal(cursor)) {
            Enchantment enchantment = EnchantmentCrystalUtils.getEnchantmentTypeFromCrystal(cursor);
            if(!(enchantment.canEnchantItem(clicked)))
                return;
            if(EnchantmentCrystalUtils.hasEnchantment(clicked, enchantment)) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "This item already has " + enchantment.getName() + " applied.");
                return;
            }
            clicked.addEnchantment(enchantment, enchantment.getMaxLevel());
            e.setCursor(null);
            e.setCancelled(true);
            player.playSound(e.getWhoClicked().getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
        }
    }
}

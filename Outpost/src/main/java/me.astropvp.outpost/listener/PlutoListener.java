package me.astropvp.outpost.listener;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;

public class PlutoListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onEnchantProc(CustomEnchantProcOnEntityEvent e) {
        if (e.getPlayer().getWorld().getName().equals("world_outpost_pluto")) {
            e.setCancelled(true);

        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPotionSplashEvent( PotionSplashEvent e) {
        if (e.getEntity().getWorld().getName().equals("world_outpost_pluto")) {

            Player player = (Player) e.getEntity().getShooter();

            if (e.getPotion().getType() == EntityType.SPLASH_POTION) {
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR, 1));
                player.sendMessage(CC.translate("&c&l(!)&c Splash potions are disabled in the pluto outpost!"));
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10,10);
                e.setCancelled(true);
            }
        }
    }
}

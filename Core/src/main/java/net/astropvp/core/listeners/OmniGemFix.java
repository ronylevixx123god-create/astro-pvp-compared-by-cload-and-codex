package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OmniGemFix implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void dupeFix(PlayerInteractEvent e) {
        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR)
            return;
        ItemStack item = e.getPlayer().getItemInHand();
        if (item.getType() != Material.DIAMOND)
            return;
        if (item.getAmount() > 1 && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (!item.getItemMeta().getDisplayName().contentEquals(CC.GoldB + "Omni Gem (" + CC.LightPurple + "/vkit" + CC.GoldB + ")")) {
                return;
            }
            e.setCancelled(true);
            Player plr = e.getPlayer();
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou &ncannot&r &cuse stacked gems!"));
            plr.closeInventory();
            plr.getItemInHand().setAmount(plr.getItemInHand().getAmount() - (plr.getItemInHand().getAmount() - 1));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void dupeFix2(PlayerInteractEvent e) {
        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR)
            return;
        ItemStack item = e.getPlayer().getItemInHand();
        if (item.getType() != Material.DIAMOND)
            return;
        if (item.getAmount() > 1 && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (!item.getItemMeta().getDisplayName().contentEquals(CC.GoldB + "Omni Gem (" + CC.Gold + "/gkit" + CC.GoldB + ")")) {
                return;
            }
            e.setCancelled(true);
            Player plr = e.getPlayer();
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou &ncannot&r &cuse stacked gems!"));
            plr.closeInventory();
            plr.getItemInHand().setAmount(plr.getItemInHand().getAmount() - (plr.getItemInHand().getAmount() - 1));
            e.setCancelled(true);
        }

    }
}

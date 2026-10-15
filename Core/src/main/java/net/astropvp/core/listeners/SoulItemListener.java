package net.astropvp.core.listeners;

import net.astropvp.core.killtracker.KillTrackerAPI;
import net.astropvp.core.AstroCore;
import org.arkhamnetwork.Arkkit.patches.EnderpearlCooldown;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;


public class SoulItemListener
implements Listener {
    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerUseSoulPearl(PlayerInteractEvent e) {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && this.isSoulPearl(e.getItem())) {
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            Player p = e.getPlayer();
            if (!EnderpearlCooldown.isEnderpearlCooldownOver((Player)p) || !this.canThrowPearl(e.getItem(), p)) {
                return;
            }
            if (!KillTrackerAPI.hasSoulAmount((Player)p, (int)5)) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + " *** NOT ENOUGH SOULS " + (Object)ChatColor.RED + "(REQ: 5)" + (Object)ChatColor.BOLD + " ***");
                p.updateInventory();
                return;
            }
            KillTrackerAPI.removeSoulsFromGems((Player)p, (int)5);
            p.launchProjectile(EnderPearl.class);
            p.updateInventory();
        }
    }

    private boolean isSoulPearl(ItemStack i) {
        return i != null && i.getType() == Material.ENDER_PEARL && i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().equals(AstroCore.getInstance().getSoulPearl().getItemMeta().getLore());
    }

    private boolean canThrowPearl(ItemStack item, Player pl) {
        if (pl.hasMetadata("lastEnderpearlThrow")) {
            long lastEnderpearlThrow = ((MetadataValue)pl.getMetadata("lastEnderpearlThrow").get(0)).asLong();
            long cd = 400L;
            long timeSinceThrow = System.currentTimeMillis() - lastEnderpearlThrow;
            if (timeSinceThrow < 400L) {
                return false;
            }
        }
        return true;
    }


}


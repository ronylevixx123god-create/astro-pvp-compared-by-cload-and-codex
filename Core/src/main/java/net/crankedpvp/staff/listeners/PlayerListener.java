package net.crankedpvp.staff.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerListener implements Listener {

    private final AstroCore plugin;
    public PlayerListener(AstroCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void stopInventoryClick(InventoryClickEvent event) {
        if(event.getWhoClicked() == null) return;
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if(!this.plugin.getStaffManager().isPlayerInStaffMode(player)) return;
        event.setCancelled(true);
        player.sendMessage(CC.translate(AstroCore.prefix + "&cYou cannot pursue this action while in staff mode"));
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getItem() == null) return;
        Player player = event.getPlayer();
        if(!this.plugin.getStaffManager().isPlayerInStaffMode(player)) return;
        event.setCancelled(true);
        player.sendMessage(CC.translate(AstroCore.prefix + "&cYou cannot pickup items while in staff mode"));
    }


    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getItemDrop() == null) return;
        Player player = event.getPlayer();
        if(!this.plugin.getStaffManager().isPlayerInStaffMode(player)) return;
        event.setCancelled(true);
        player.sendMessage(CC.translate(AstroCore.prefix + "&cYou cannot drop items while in staff mode"));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        if(event.getEntity() == null) return;
        Player player = event.getEntity();
        if(!this.plugin.getStaffManager().isPlayerInStaffMode(player)) return;
        event.getDrops().clear();
        this.plugin.getStaffManager().getStaffModeFromPlayer(player).removePlayerFromStaffMode();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void breakBlockEvent(BlockBreakEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getBlock() == null) return;
        Player player = event.getPlayer();
        if(!this.plugin.getStaffManager().isPlayerInStaffMode(player)) return;
        if(event.getBlock().getType() == Material.BEDROCK) {
            event.setCancelled(true);
            player.sendMessage(CC.translate(AstroCore.prefix + "&cDon't break bedrock dickhead..."));
        }
    }
}

package net.astropvp.core.conaxgames.staff.staffmode.staffmodules;

/*
import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import net.astropvp.core.conaxgames.staff.staffmode.StaffModeManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffModeListeners implements Listener {

    private final StaffModeManager staffModeManager;

    public StaffModeListeners(AstroCore plugin) {
        this.staffModeManager = plugin.getStaffModeManager();
    }

    @EventHandler
    public void stopInventoryClick(InventoryClickEvent event) {
        if(event.getWhoClicked() == null) return;
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!staffModeManager.isStaffModeToggled(player)) return;
        event.setCancelled(true);
        player.sendMessage(CC.translate("&cYou cannot pursue this action while in staff mode"));
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getItem() == null) return;
        Player player = event.getPlayer();
        if (!staffModeManager.isStaffModeToggled(event.getPlayer())) return;
        event.setCancelled(true);
        player.sendMessage(CC.translate("&cYou cannot pickup items while in staff mode"));
    }

    @EventHandler
    public void invClick(InventoryClickEvent event) {
        if(event.getWhoClicked() == null) return;
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (player.isOp()) return;
        if (event.getAction() == InventoryAction.CLONE_STACK) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou cannot pursue this action while in staff mode"));
        }
    }


    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getItemDrop() == null) return;
        Player player = event.getPlayer();
        if (!staffModeManager.isStaffModeToggled(event.getPlayer())) return;
        event.setCancelled(true);
        player.sendMessage(CC.translate( "&cYou cannot drop items while in staff mode"));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        if(event.getEntity() == null) return;
        Player player = event.getEntity();
        if (!staffModeManager.isStaffModeToggled(player)) return;
        event.getDrops().clear();
        this.staffModeManager.toggleStaffModeOff(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void breakBlockEvent(BlockBreakEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getBlock() == null) return;
        Player player = event.getPlayer();
        if (!staffModeManager.isStaffModeToggled(event.getPlayer())) return;
        if(event.getBlock().getType() == Material.BEDROCK) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cDon't break bedrock..."));
        }
    }

    @EventHandler
    public void onPlayerLogOutWhileInStaffMode(PlayerQuitEvent event) {
        if (!staffModeManager.isStaffModeToggled(event.getPlayer())) return;
        this.staffModeManager.toggleStaffModeOff(event.getPlayer());
    }

    @EventHandler
    public void onPlayerPickUpItemInStaffMode(PlayerPickupItemEvent event) {
        if (!staffModeManager.isStaffModeToggled(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickUpArrowInStaffMode(PlayerPickupArrowEvent event) {
        if (!staffModeManager.isStaffModeToggled(event.getPlayer())) return;
        event.setCancelled(true);
    }
}


 */
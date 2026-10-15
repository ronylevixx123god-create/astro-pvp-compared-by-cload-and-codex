package net.astropvp.christmas.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.matthawkk.chat.messages.PrefixedMessage;
import net.astropvp.christmas.NebulaShop;
import net.astropvp.christmas.inventories.NebulaShopInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NebulaShopListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!e.getPlayer().getWorld().getName().equals("nebula"))
            return;
        if (e.getPlayer().getLocation().getBlockY() < 135) {
            e.getPlayer().teleport(NebulaShop.getInstance().getNebulaShopManager().getShopLocation());
        }
    }


    @EventHandler
    public void teleportCancel(PlayerTeleportEvent e) {
        if (e.getPlayer() == null) return;
        if (!e.getTo().getWorld().getName().equals("nebula")) return;
        if (e.getPlayer().hasMetadata("NPC")) return;
        if (!e.getPlayer().hasPermission("astrocore.nebula")) {
            e.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot teleport to the nebula lounge."));
            e.getPlayer().sendMessage(CC.translate("&cThis lounge requires the nebula rank!"));
            e.setCancelled(true);
        }



    }
}

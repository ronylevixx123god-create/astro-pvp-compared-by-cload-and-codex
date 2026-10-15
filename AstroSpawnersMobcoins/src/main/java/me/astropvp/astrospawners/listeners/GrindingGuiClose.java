package me.astropvp.astrospawners.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.guis.AntiAFKGrindingGUI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GrindingGuiClose implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (AstroSpawners.getInstance().getWaitingForAfkGrindingVerification().contains(e.getPlayer().getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(AstroSpawners.getInstance(), () -> {
                AstroSpawners.getInstance().getAfkGrinders().put(e.getPlayer().getUniqueId(), 1);
                AstroSpawners.getInstance().getWaitingForAfkGrindingVerification().remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage(CC.translate("&a&l(!)&a You have completed the anti afk grinding captcha! You can now enjoy grinding again."));
                e.getPlayer().closeInventory();
            }, 2L);
        }

    }


    @EventHandler
    public void cancelInteract(PlayerInteractEvent e) {
        if (AstroSpawners.getInstance().getWaitingForAfkGrindingVerification().contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        AstroSpawners.getInstance().getWaitingForAfkGrindingVerification().remove(e.getPlayer().getUniqueId());
        AstroSpawners.getInstance().getAfkGrinders().remove(e.getPlayer().getUniqueId());
    }
}

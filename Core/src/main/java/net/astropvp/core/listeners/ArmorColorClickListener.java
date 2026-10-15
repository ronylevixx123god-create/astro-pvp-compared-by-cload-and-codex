package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.guis.ArmorColorGUI;
import net.astropvp.core.guis.ChatColorGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ArmorColorClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (p.getOpenInventory().getTitle().equals(CC.translate("&8Select a Color..."))) {
            e.setCancelled(true);
            if (e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {


                ArmorColorGUI collectorInventory = (ArmorColorGUI) e.getClickedInventory().getHolder();
                if (collectorInventory != null) {
                    collectorInventory.click(e);
                }
            }
        } if (p.getOpenInventory().getTitle().equals(CC.translate("&7Select a Color..."))) {
            e.setCancelled(true);
            if (e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {


                ChatColorGUI collectorInventory = (ChatColorGUI) e.getClickedInventory().getHolder();
                if (collectorInventory != null) {
                    collectorInventory.click(e);
                }
            }
        }
    }
}

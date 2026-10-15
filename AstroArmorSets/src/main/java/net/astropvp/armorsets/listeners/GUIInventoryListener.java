package net.astropvp.armorsets.listeners;

import net.astropvp.armorsets.gui.SetGUI;
import net.astropvp.armorsets.struct.ArmorSetType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();

        if (inventory == null) return;
        if (inventory.getName() == null) return;
        if(inventory.getName().equals("Armor Sets")) {
            event.setCancelled(true);
            ItemStack item = inventory.getItem(event.getSlot());
            if (item == null) return;
            if(item.getType() == Material.BARRIER) p.closeInventory();
            if(item.getType() == Material.BED) {
                SetGUI gui = new SetGUI(p, "Armor Sets");
                gui.openBaseGUI();
            }

            for(ArmorSetType set : ArmorSetType.values()) {
                if(set.getDisplay().equals(item.getItemMeta().getDisplayName())) {
                    SetGUI gui = new SetGUI(p, "Armor Sets");
                    gui.openSubGUI(set);
                }
            }
        }
    }

}

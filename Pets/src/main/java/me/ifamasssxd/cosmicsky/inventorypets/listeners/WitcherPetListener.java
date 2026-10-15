package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.pets.InventoryWitcherPet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class WitcherPetListener implements Listener {
    @EventHandler
    public void onHit(PlayerVelocityEvent e) {
        if(InventoryWitcherPet.no_kb.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (e.getPlayer().hasMetadata("witcher_ability")) {
            e.getPlayer().removeMetadata("witcher_ability", CosmicSkyInventoryPets.get());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {

        if (e.getPlayer().hasMetadata("witcher_ability")) {
            e.getPlayer().removeMetadata("witcher_ability", CosmicSkyInventoryPets.get());
        }
    }
}

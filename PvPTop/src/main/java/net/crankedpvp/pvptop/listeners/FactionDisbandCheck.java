package net.crankedpvp.pvptop.listeners;

import com.massivecraft.factions.event.FactionDisbandEvent;
import net.crankedpvp.pvptop.PvPTopPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FactionDisbandCheck implements Listener {

    @EventHandler
    public void facDisband(FactionDisbandEvent event) {
        PvPTopPlugin.getInstance().getPvpTopManager().removeFromCache(event.getFaction().getId());
    }

}

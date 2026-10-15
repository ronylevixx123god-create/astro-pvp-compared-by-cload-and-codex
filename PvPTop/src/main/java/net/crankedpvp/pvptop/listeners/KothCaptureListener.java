package net.crankedpvp.pvptop.listeners;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.golfing8.kore.event.KothCaptureEvent;
import com.massivecraft.factions.Faction;
import net.crankedpvp.pvptop.PvPTopPlugin;
import net.crankedpvp.pvptop.data.FactionData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KothCaptureListener implements Listener {

    private final PvPTopPlugin plugin;
    public KothCaptureListener(PvPTopPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onKothCapture(KothCaptureEvent event) {
        if(event.getPlayerCapper() == null) return;
        Faction faction = FactionUtils.getFaction(event.getPlayerCapper());
        FactionData factionData = this.plugin.getPvpTopManager().getOrCreate(faction);
        if(factionData == null) return;
        factionData.addPoint(FactionData.Points.KOTH_CAPTURE);
    }
}

package me.astropvp.factionlogs.listeners;

import com.massivecraft.factions.event.*;
import me.astropvp.factionlogs.struct.impl.*;
import me.astropvp.factionlogs.utils.FactionsLogsAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FactionListeners implements Listener {
    @EventHandler
    public void onLandClaim(LandClaimEvent event) {
        if (event.isCancelled()) return;
        FactionsLogsAPI.log(event.getFactionId(), new LandClaim(event.getfPlayer().getPlayer().getUniqueId(), event.getLocation()));
    }

    @EventHandler
    public void onLandUnClaim(LandUnclaimEvent event) {
        if (event.isCancelled()) return;
        FactionsLogsAPI.log(event.getFactionId(), new LandUnclaim(event.getfPlayer().getPlayer().getUniqueId(), event.getLocation()));
    }

    @EventHandler
    public void onLandUnClaimAll(LandUnclaimAllEvent event) {
        if (event.isCancelled()) return;
        FactionsLogsAPI.log(event.getFactionId(), new LandUnclaimAll(event.getfPlayer().getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onRadiusLandClaim(LandClaimRadiusEvent event) {
        if (event.isCancelled()) return;
        FactionsLogsAPI.log(event.getFactionId(), new RadiusLandClaim(event.getfPlayer().getPlayer().getUniqueId(), event.getLocation(), event.getRadius()));
    }


    @EventHandler
    public void onPlayerJoinFaction(FPlayerJoinEvent event) {
        if (event.isCancelled()) return;
        FactionsLogsAPI.log(event.getFaction().getId(), new PlayerJoin(event.getfPlayer().getPlayer().getUniqueId(), event.getReason()));
    }

    @EventHandler
    public void onPlayerLeaveFaction(FPlayerLeaveEvent event) {
        if (event.isCancelled()) return;
        FactionsLogsAPI.log(event.getFaction().getId(), new PlayerLeave(event.getfPlayer().getPlayer().getUniqueId(), event.getReason()));
    }

    @EventHandler
    public void onRelationChange(FactionRelationEvent event) {
        FactionsLogsAPI.log(event.getFaction().getId(), new RelationEdits(event.getFaction(), event.getTargetFaction(), event.getOldRelation(), event.getRelation()));
        FactionsLogsAPI.log(event.getTargetFaction().getId(), new RelationEdits(event.getFaction(), event.getTargetFaction(), event.getOldRelation(), event.getRelation()));
    }

    @EventHandler
    public void onFactionRename(FactionRenameEvent event) {
        if (event.isCancelled()) return;
        FactionsLogsAPI.log(event.getFaction().getId(), new FactionRename(event.getPlayer().getUniqueId(), event.getOldFactionTag(), event.getFactionTag()));
    }
}

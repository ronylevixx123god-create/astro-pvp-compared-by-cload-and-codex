/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.events.PlayerEnterRegionEvent;
import com.compilex.CosmicDungeons.utils.Logger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public abstract class DungeonEvents
implements Listener {
    public Dungeon dungeon;
    protected Set<String> exploredRegions = new HashSet<String>();
    public Random random;
    protected Map<String, RegionEvent> regionEvents = new HashMap<String, RegionEvent>();

    public DungeonEvents(Dungeon d) {
        this.dungeon = d;
        this.random = d.getRandom();
    }

    public abstract void loadRegionTriggerEvents();

    public void onPlayerEnterRegion(Player player, String regionName) {
    }

    @EventHandler
    public void onPlayerEnterRegionEvent(PlayerEnterRegionEvent e) {
        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(e.getPlayer());
        Dungeon d = dp.getCurrentDungeon();
        Logger.debug("Player " + e.getPlayer().getName() + " has entered region: " + e.getRegionName() + ", dungeon: " + d);
        if (d != null && d.equals(this.dungeon)) {
            String region = e.getRegionName();
            this.onPlayerEnterRegion(e.getPlayer(), region);
            if (!this.exploredRegions.contains(region)) {
                RegionEvent event = this.regionEvents.get(region);
                if (event != null) {
                    event.run();
                }
                this.exploredRegions.add(region);
            }
        }
    }
}


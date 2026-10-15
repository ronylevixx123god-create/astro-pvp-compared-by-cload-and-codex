/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 */
package com.compilex.CosmicDungeons.dungeons.libs;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.utils.Logger;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CooldownData
extends HashMap<Dungeon, Long> {
    private Player cooldownPlayer;
    private YamlConfiguration playerConfig;

    public void setCooldownPlayer(Player cooldownPlayer) {
        this.cooldownPlayer = cooldownPlayer;
    }

    public CooldownData(Player p, YamlConfiguration config) {
        this.cooldownPlayer = p;
        this.playerConfig = config;
        for (Dungeon d : DungeonsAPI.get().getDungeons()) {
            if (!this.playerConfig.contains("dungeons.cooldowns." + d.getWorldName())) continue;
            this.put(d, this.playerConfig.getLong("dungeons.cooldowns." + d.getWorldName()));
        }
    }

    public long addDungeonCooldown(Dungeon d, long cooldownDuration) {
        int level = 1;
        long toReduce = TimeUnit.HOURS.toMillis(level == 1 ? 4L : (level == 2 ? 8L : (level == 3 ? 12L : (level == 4 ? 18L : (level == 5 ? 24L : (level == 6 ? 36L : 0L))))));
        long l = System.currentTimeMillis() + (cooldownDuration -= toReduce);
        Long previous = this.put(d, l);
        if (previous != null && previous > l) {
            this.put(d, previous);
        }
        this.playerConfig.set("dungeons.cooldowns." + d.getWorldName(), (Object)l);
        Logger.debug("set cooldown of " + this.cooldownPlayer.getName() + " for " + d.toString() + " to +" + cooldownDuration);
        return toReduce;
    }

    public void setDungeonCooldown(Dungeon d, long cooldownDuration) {
        long l = System.currentTimeMillis() + cooldownDuration;
        this.put(d, l);
        this.playerConfig.set("dungeons.cooldowns." + d.getWorldName(), (Object)l);
        Logger.debug("set cooldown of " + this.cooldownPlayer.getName() + " for " + d.toString() + " to +" + cooldownDuration);
    }

    public void removeDungeonCooldown(Dungeon d) {
        this.remove(d);
        this.playerConfig.set("dungeons.cooldowns." + d.getWorldName(), (Object)0);
        Logger.debug("removed cooldown of " + this.cooldownPlayer.getName() + " for " + d.toString());
    }

    public boolean canPlayerDoDungeon(Dungeon d) {
        Long stored = (Long)this.get(d);
        return stored == null || stored < System.currentTimeMillis();
    }

    public long getCooldown(Dungeon d) {
        Long stored = (Long)this.get(d);
        if (stored != null) {
            return Math.max(0L, stored - System.currentTimeMillis());
        }
        return 0L;
    }

    public boolean doINeedToBeSaved() {
        return this.size() > 0;
    }
}


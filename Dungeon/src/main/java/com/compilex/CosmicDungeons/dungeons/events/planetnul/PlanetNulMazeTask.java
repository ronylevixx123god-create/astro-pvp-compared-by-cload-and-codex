/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TitleManager
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TitleManager$TitleColor
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.dungeons.libs.PlanetNulDungeon;
import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.ArrayList;
import java.util.List;

import com.cosmicpvp.cosmicutils.utils.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

public class PlanetNulMazeTask
extends BukkitRunnable {
    private final PlanetNulDungeon dungeon;
    private final List<Player> affectedPlayers;
    private int loops = 0;

    public PlanetNulMazeTask(PlanetNulDungeon dungeon) {
        this.dungeon = dungeon;
        this.affectedPlayers = new ArrayList<Player>();
    }

    private float clamp(float min, float max, float value) {
        return value < min ? min : (Math.min(value, max));
    }

    public void run() {
        ++this.loops;
        this.dungeon.getDungeonEvents().chestLocations.stream().filter(l -> !this.dungeon.getDungeonEvents().announcedChestLocations.contains(l)).forEach(l -> {
            try {
                Location location = new Location(l.getWorld(), l.getBlockX() + 0.5, l.getBlockY() + 0.5, l.getBlockZ() + 0.5);
                de.inventivegames.particle.ParticleEffect.ENCHANTMENT_TABLE.sendToPlayers(this.dungeon.getPlayers(), location, 0.2f, 0.2f, 0.2f, 0.0f, 24);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        for (Player p2 : this.dungeon.getPlayers()) {
            DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p2);
            if (dp.getCurrentDungeon() == null || !dp.getCurrentDungeon().equals(this.dungeon))
                continue;
            if (p2.getLocation().add(0.0, -1.0, 0.0).getBlock().getType() == Material.MYCEL || p2.getLocation().add(0.0, -1.0, 0.0).getBlock().getType() == Material.AIR && p2.getLocation().add(0.0, -2.0, 0.0).getBlock().getType() == Material.MYCEL || p2.getLocation().add(0.0, -1.0, 0.0).getBlock().getType() == Material.AIR && p2.getLocation().add(0.0, -2.0, 0.0).getBlock().getType() == Material.AIR && p2.getLocation().add(0.0, -3.0, 0.0).getBlock().getType() == Material.MYCEL || p2.getLocation().add(0.0, -1.0, 0.0).getBlock().getType() == Material.AIR && p2.getLocation().add(0.0, -2.0, 0.0).getBlock().getType() == Material.AIR && p2.getLocation().add(0.0, -3.0, 0.0).getBlock().getType() == Material.AIR && p2.getLocation().add(0.0, -4.0, 0.0).getBlock().getType() == Material.MYCEL) {
                if (this.affectedPlayers.contains(p2))
                    continue;
                this.affectedPlayers.add(p2);
                p2.sendTitle(new Title(CC.Red + "Poisonous Gas", CC.Gray + "Will this be your last breath?", 20, 65, 20));
                continue;
            }
            this.affectedPlayers.remove(p2);
        }
        ArrayList<Player> playersToRemove = new ArrayList<>();
        this.affectedPlayers.forEach(p -> {
            if (p.getWorld() != this.dungeon.world) {
                playersToRemove.add(p);
            }
        });
        this.affectedPlayers.removeAll(playersToRemove);
        if (this.loops == 2) {
            this.affectedPlayers.forEach(p -> p.playSound(p.getLocation(), Sound.NOTE_BASS_DRUM, 1.0f, 0.5f));
        }
        if (this.loops >= 4) {
            this.loops = 0;
            this.affectedPlayers.forEach(p -> p.damage((double)this.clamp(2.0f, 8.0f, this.dungeon.getTimeSinceDungeonStart() / 60000L)));
        }
    }
}


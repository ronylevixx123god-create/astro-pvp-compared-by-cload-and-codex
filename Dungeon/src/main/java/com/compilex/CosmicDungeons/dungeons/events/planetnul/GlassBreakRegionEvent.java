/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.gmail.filoghost.holograms.api.HolographicDisplaysAPI
 *  org.bukkit.Location
 *  org.bukkit.plugin.Plugin
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.GlassBreakRegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.utils.WorldUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import java.util.ArrayList;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GlassBreakRegionEvent extends RegionEvent {
    public GlassBreakRegionEvent(Dungeon d, String regionName) {
        super(d, regionName);
    }

    static final class GlassBreakRegionEvent$run$1 implements Function1<Block, Unit> {
        public static final GlassBreakRegionEvent$run$1 INSTANCE = new GlassBreakRegionEvent$run$1();

        private boolean playedSound = false;

        public Unit invoke(@NotNull Block block) {
            Intrinsics.checkParameterIsNotNull(block, "block");
            if (block.getType() == Material.STAINED_GLASS) {
                block.setType(Material.AIR);
                if (!this.playedSound)
                    block.getWorld().playEffect(block.getLocation().add(0.5D, 0.5D, 0.5D), Effect.STEP_SOUND, Material.STAINED_GLASS);
                this.playedSound = true;
            }
            return Unit.INSTANCE;
        }
    }

    private void fillGlass() {
        ArrayList<Location> locations = new ArrayList<Location>();
        locations.add(new Location(this.dungeon.world, 35.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 39.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 35.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 39.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 35.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 39.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 192.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 192.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 192.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 193.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 193.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 193.0, -72.0));
        locations.forEach(l -> l.getBlock().setTypeIdAndData(95, (byte)1, true));
    }

    @Override
    public void run() {
        this.fillGlass();
        WorldUtils.Companion.scanBlocks(new Location(this.dungeon.world, 1.0, 222.0, 85.0), 70, GlassBreakRegionEvent$run$1.INSTANCE);
        WorldUtils.Companion.scanBlocks(new Location(this.dungeon.world, -6.0, 224.0, 2.0), 5, GlassBreakRegionEvent$run$1.INSTANCE);
        this.dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get(), (Location)new Location(this.dungeon.world, 37.5, 206.5, -18.0), (String[])new String[]{CC.RedB + "LOCKED", CC.Gray + "Requires a key", CC.Gray + "to unlock door."}));
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + (this.dungeon.isHalfTime() ? 390000L : 600000L), "Locate Vault Airlock");
    }
}


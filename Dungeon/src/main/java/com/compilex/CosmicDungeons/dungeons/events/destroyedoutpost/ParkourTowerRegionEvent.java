package com.compilex.CosmicDungeons.dungeons.events.destroyedoutpost;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.utils.WorldUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\000\036\n\002\030\002\n\002\030\002\n\000\n\002\030\002\n\000\n\002\020\016\n\002\b\002\n\002\020\002\n\000\030\0002\0020\001B\025\022\006\020\002\032\0020\003\022\006\020\004\032\0020\005\006\002\020\006J\b\020\007\032\0020\bH\026\006\t"}, d2 = {"Lcom/compilex/CosmicDungeons/dungeons/events/destroyedoutpost/ParkourTowerRegionEvent;", "Lcom/compilex/CosmicDungeons/dungeons/events/RegionEvent;", "d", "Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;", "regionName", "", "(Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;Ljava/lang/String;)V", "run", "", "CosmicDungeons"})
public final class ParkourTowerRegionEvent extends RegionEvent {
    public ParkourTowerRegionEvent(@NotNull Dungeon d, @NotNull String regionName) {
        super(d, regionName);
    }

    @Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 3, d1 = {"\000\016\n\000\n\002\020\002\n\000\n\002\030\002\n\000\020\000\032\0020\0012\006\020\002\032\0020\003H\n\006\002\b\004"}, d2 = {"<anonymous>", "", "block", "Lorg/bukkit/block/Block;", "invoke"})
    static final class ParkourTowerRegionEvent$run$1 implements Function1<Block, Unit> {
        public static final ParkourTowerRegionEvent$run$1 INSTANCE = new ParkourTowerRegionEvent$run$1();

        public Unit invoke(@NotNull Block block) {
            Intrinsics.checkParameterIsNotNull(block, "block");
            if (block.getType() == Material.STAINED_GLASS) {
                block.setType(Material.AIR);
                block.getWorld().playEffect(block.getLocation().add(0.5D, 0.5D, 0.5D), Effect.STEP_SOUND, Material.STAINED_GLASS);
            }
            return Unit.INSTANCE;
        }
    }

    public void run() {
        Intrinsics.checkExpressionValueIsNotNull(this.dungeon.world, "dungeon.world");
        Intrinsics.checkExpressionValueIsNotNull(this.dungeon.world, "dungeon.world");
        Bukkit.getLogger().info("World: " + this.dungeon.world.getName() + " players: " + this.dungeon.world.getPlayers().size());
        WorldUtils.Companion.scanBlocks(new Location(this.dungeon.world, 286.0D, 206.0D, -11.0D), 3, ParkourTowerRegionEvent$run$1.INSTANCE);
        this.dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get(), new Location(this.dungeon.world, 287.0D, 208.0D, -12.0D), CC.AquaB + "Parkour Start"));
    }
}

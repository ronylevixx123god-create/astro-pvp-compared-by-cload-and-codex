package com.compilex.CosmicDungeons.dungeons.events.destroyedoutpost;

import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.utils.WorldUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

@Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\000\036\n\002\030\002\n\002\030\002\n\000\n\002\030\002\n\000\n\002\020\016\n\002\b\002\n\002\020\002\n\000\030\0002\0020\001B\025\022\006\020\002\032\0020\003\022\006\020\004\032\0020\005\006\002\020\006J\b\020\007\032\0020\bH\026\006\t"}, d2 = {"Lcom/compilex/CosmicDungeons/dungeons/events/destroyedoutpost/AirLockBRegionEvent;", "Lcom/compilex/CosmicDungeons/dungeons/events/RegionEvent;", "d", "Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;", "regionName", "", "(Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;Ljava/lang/String;)V", "run", "", "CosmicDungeons"})
public final class AirLockBRegionEvent extends RegionEvent {
    public AirLockBRegionEvent(@NotNull Dungeon d, @NotNull String regionName) {
        super(d, regionName);
    }

    @Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 3, d1 = {"\000\016\n\000\n\002\020\002\n\000\n\002\030\002\n\000\020\000\032\0020\0012\006\020\002\032\0020\003H\n\006\002\b\004"}, d2 = {"<anonymous>", "", "block", "Lorg/bukkit/block/Block;", "invoke"})
    static final class AirLockBRegionEvent$run$1 implements Function1<Block, Unit> {
        public static final AirLockBRegionEvent$run$1 INSTANCE = new AirLockBRegionEvent$run$1();

        public final Unit invoke(@NotNull Block block) {
            Intrinsics.checkParameterIsNotNull(block, "block");
            Material type = block.getType();
            if (type == Material.STAINED_GLASS || type == Material.WOOL) {
                block.setType(Material.AIR);
                block.getWorld().playEffect(block.getLocation().add(0.5D, 0.5D, 0.5D), Effect.STEP_SOUND, type);
            }
            return Unit.INSTANCE;
        }
    }

    public void run() {
        WorldUtils.Companion.scanBlocks(new Location(this.dungeon.world, 216.0D, 109.0D, -15.0D), 8, AirLockBRegionEvent$run$1.INSTANCE);
        this.dungeon.broadcastMessage(CC.White + "Yijki is responsible for what has become of his hellish world... you must locate the portal the miners uncovered that leads to Yijki's dimension... perhaps you should look for additional clues in the buildings around the dig site.");
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 900000L / (this.dungeon.isHalfTime() ? 2L : 1L), "Locate Yijki's Portal");
    }
}


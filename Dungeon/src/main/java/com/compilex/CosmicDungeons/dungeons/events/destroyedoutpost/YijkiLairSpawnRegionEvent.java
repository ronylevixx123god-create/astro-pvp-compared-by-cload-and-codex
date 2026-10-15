package com.compilex.CosmicDungeons.dungeons.events.destroyedoutpost;

import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.cosmicpvp.cosmicutils.utils.CC;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\000\036\n\002\030\002\n\002\030\002\n\000\n\002\030\002\n\000\n\002\020\016\n\002\b\002\n\002\020\002\n\000\030\0002\0020\001B\025\022\006\020\002\032\0020\003\022\006\020\004\032\0020\005\006\002\020\006J\b\020\007\032\0020\bH\026\006\t"}, d2 = {"Lcom/compilex/CosmicDungeons/dungeons/events/destroyedoutpost/YijkiLairSpawnRegionEvent;", "Lcom/compilex/CosmicDungeons/dungeons/events/RegionEvent;", "d", "Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;", "regionName", "", "(Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;Ljava/lang/String;)V", "run", "", "CosmicDungeons"})
public final class YijkiLairSpawnRegionEvent extends RegionEvent {
    public YijkiLairSpawnRegionEvent(@NotNull Dungeon d, @NotNull String regionName) {
        super(d, regionName);
    }

    public void run() {
        this.dungeon.broadcastMessage(CC.White + "The time has come. You have arrived at Yijki's home dimension. If the Goddess is slain here, she will die once and for all. ");
        this.dungeon.broadcastMessage("When you are ready, step off the spawn platform and Yijki will sense your presence.");
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 1200000L / (this.dungeon.isHalfTime() ? 2L : 1L), "Kill Yijki");
    }
}

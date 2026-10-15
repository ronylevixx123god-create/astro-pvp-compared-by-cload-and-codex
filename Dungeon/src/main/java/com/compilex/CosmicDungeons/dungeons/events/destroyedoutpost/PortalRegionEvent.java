package com.compilex.CosmicDungeons.dungeons.events.destroyedoutpost;

import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.cosmicpvp.cosmicutils.utils.CC;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\000\036\n\002\030\002\n\002\030\002\n\000\n\002\030\002\n\000\n\002\020\016\n\002\b\002\n\002\020\002\n\000\030\0002\0020\001B\025\022\006\020\002\032\0020\003\022\006\020\004\032\0020\005\006\002\020\006J\b\020\007\032\0020\bH\026\006\t"}, d2 = {"Lcom/compilex/CosmicDungeons/dungeons/events/destroyedoutpost/PortalRegionEvent;", "Lcom/compilex/CosmicDungeons/dungeons/events/RegionEvent;", "dungeon", "Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;", "regionName", "", "(Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;Ljava/lang/String;)V", "run", "", "CosmicDungeons"})
public final class PortalRegionEvent extends RegionEvent {
    public PortalRegionEvent(@NotNull Dungeon dungeon, @NotNull String regionName) {
        super(dungeon, regionName);
    }

    public void run() {
        this.dungeon.broadcastMessage(CC.White + "You have entered into an interdimensional rift; you are now in a place that only exists between multiple dimensions. You must navigate through these series of portals to arrive at the Mother of Yijki's home dimension where she can be killed once and for all.");
        this.dungeon.broadcastMessage(CC.Gray + "Be cautious, this reality is extremely unstable. Choosing the wrong portal could result in catastrophic events.");
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 300000L / (this.dungeon.isHalfTime() ? 2L : 1L), "Travel to Yijki's Dimension");
    }
}

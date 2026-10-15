package com.compilex.CosmicDungeons.dungeons.events.destroyedoutpost;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.DestroyedOutpostDungeon;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import me.ifamasssxd.cosmicbosses.dungeonboss.destroyedoutpost.MotherOfYijki;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.utils.BossUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\000\036\n\002\030\002\n\002\030\002\n\000\n\002\030\002\n\000\n\002\020\016\n\002\b\002\n\002\020\002\n\000\030\0002\0020\001B\025\022\006\020\002\032\0020\003\022\006\020\004\032\0020\005\006\002\020\006J\b\020\007\032\0020\bH\026\006\t"}, d2 = {"Lcom/compilex/CosmicDungeons/dungeons/events/destroyedoutpost/YijkiLairAreaRegionEvent;", "Lcom/compilex/CosmicDungeons/dungeons/events/RegionEvent;", "d", "Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;", "regionName", "", "(Lcom/compilex/CosmicDungeons/dungeons/libs/Dungeon;Ljava/lang/String;)V", "run", "", "CosmicDungeons"})
public final class YijkiLairAreaRegionEvent extends RegionEvent {
    public YijkiLairAreaRegionEvent(@NotNull Dungeon d, @NotNull String regionName) {
        super(d, regionName);
    }

    public void run() {
        if (this.dungeon == null)
            throw new TypeCastException("null cannot be cast to non-null type com.compilex.CosmicDungeons.dungeons.libs.DestroyedOutpostDungeon");
        ((DestroyedOutpostDungeon)this.dungeon).boss = (MotherOfYijki)Boss.bosses.get(BossUtils.spawnDungeonBoss(new Location(this.dungeon.world, -96.0D, 78.0D, -643.0D), 3, BossType.MOTHER_OF_YIJKI).getUniqueId());
        Intrinsics.checkExpressionValueIsNotNull(this.dungeon, "this.dungeon");
        for (Player player : this.dungeon.getPlayers()) {
            Location location = player.getLocation();
            Intrinsics.checkExpressionValueIsNotNull(location, "location");
            double x = location.getX();
            double z = location.getZ();
            if (x > 400.0D && x < 540.0D && z > -2050.0D && z < -623.0D) {
                player.sendMessage(CC.RedB + "The Mother of Yijki has been summoned, you have been forcibly teleported into its dimension!");
                if (this.dungeon == null)
                    throw new TypeCastException("null cannot be cast to non-null type com.compilex.CosmicDungeons.dungeons.libs.DestroyedOutpostDungeon");
                player.setMetadata("ignore_combat_tag", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (((DestroyedOutpostDungeon)this.dungeon).getDungeonEvents()).mainYijkiCave));
                if (this.dungeon == null)
                    throw new TypeCastException("null cannot be cast to non-null type com.compilex.CosmicDungeons.dungeons.libs.DestroyedOutpostDungeon");
                player.teleport((((DestroyedOutpostDungeon)this.dungeon).getDungeonEvents()).mainYijkiCave, PlayerTeleportEvent.TeleportCause.UNKNOWN);
                player.removeMetadata("ignore_combat_tag", (Plugin)CosmicDungeonsPlugin.get());
                continue;
            }
            if (WorldGuardUtils.inRegion(location, "yijki_realm") || WorldGuardUtils.inRegion(location, "yijki_portal_exit"))
                continue;
            player.sendMessage(CC.RedB + "You were not protected by the Interdimensional Portals at the time the Mother of Yijki was spawned, so you were killed instantly by her planet ending ability.");
            Intrinsics.checkExpressionValueIsNotNull(player, "player");
            player.damage(player.getMaxHealth() + 99.0D);
        }
    }
}

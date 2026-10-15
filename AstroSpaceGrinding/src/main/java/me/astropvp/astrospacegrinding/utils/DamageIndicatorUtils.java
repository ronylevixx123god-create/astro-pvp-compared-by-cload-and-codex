package me.astropvp.astrospacegrinding.utils;

import com.astropvp.cc.packet.type.out.PacketOutHitSplat;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class DamageIndicatorUtils {
    public static void sendDamageIndicator(LivingEntity entity, String text, String type) {
        Location place = entity.getLocation();
        place = place.add(ThreadLocalRandom.current().nextBoolean() ? -0.25D : 0.25D, ThreadLocalRandom.current().nextBoolean() ? -0.25D : 0.25D, ThreadLocalRandom.current().nextBoolean() ? -0.25D : 0.25D);
        PacketOutHitSplat hitSplat = new PacketOutHitSplat(new PacketOutHitSplat.HitSplatData(entity.getEntityId(), text, type, place.getX(), place.getY(), place.getZ()));
        for (CosmicPlayer cosmicPlayer : CosmicPlayers.get().getAll()) {
            if (cosmicPlayer.getPlayer().getWorld() != entity.getWorld())
                continue;
            if (cosmicPlayer.getPlayer().getLocation().distance(entity.getLocation()) > 80.0D)
                continue;
            cosmicPlayer.sendPacket(hitSplat);
        }
    }
}

package org.arkhamnetwork.enchantments.util;

import com.astropvp.cc.packet.type.out.PacketOutHitSplat;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class DamageIndicatorUtils {
    public static void sendDamageIndicator(Player player, String text, String type) {
        Location place = player.getLocation();
        place = place.add(ThreadLocalRandom.current().nextBoolean() ? -0.25D : 0.25D, ThreadLocalRandom.current().nextBoolean() ? -0.25D : 0.25D, ThreadLocalRandom.current().nextBoolean() ? -0.25D : 0.25D);
        PacketOutHitSplat hitSplat = new PacketOutHitSplat(new PacketOutHitSplat.HitSplatData(player.getEntityId(), text, type, place.getX(), place.getY(), place.getZ()));
        for (CosmicPlayer cosmicPlayer : CosmicPlayers.get().getAll()) {
            if (cosmicPlayer.getPlayer().getWorld() != player.getWorld())
                continue;
            if (cosmicPlayer.getPlayer().getLocation().distance(player.getLocation()) > 80.0D)
                continue;
            if (cosmicPlayer.getPlayer() == player)
                continue;
            cosmicPlayer.sendPacket(hitSplat);
        }
    }
}

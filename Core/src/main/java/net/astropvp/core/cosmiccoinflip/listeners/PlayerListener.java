/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package net.astropvp.core.cosmiccoinflip.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.astropvp.core.cosmiccoinflip.struct.CoinFlipManager;
import net.astropvp.core.cosmiccoinflip.struct.CoinFlipMatch;
import net.astropvp.core.AstroCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener
implements Listener {
    public static Map<UUID, String> ipAddresses = new HashMap<UUID, String>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ipAddresses.put(event.getPlayer().getUniqueId(), event.getPlayer().getAddress().getHostString());
    }

    @EventHandler
    public void onPlayerleave(PlayerQuitEvent event) {
        CoinFlipManager coinFlipManager = AstroCore.getInstance().getCoinFlipManager();
        CoinFlipMatch match = coinFlipManager.getMatch(event.getPlayer());
        if (match != null) {
            match.onPlayerLogout(event.getPlayer());
        }
    }

    public static boolean hasSameIP(UUID uuid, UUID other) {
        String first = ipAddresses.get(uuid);
        String second = ipAddresses.get(other);
        return first != null && second != null && first.equals(second);
    }
}


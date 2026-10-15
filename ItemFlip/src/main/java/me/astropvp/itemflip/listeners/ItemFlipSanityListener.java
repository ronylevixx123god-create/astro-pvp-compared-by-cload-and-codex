package me.astropvp.itemflip.listeners;


import me.astropvp.itemflip.inventory.CoinFlipGui;
import me.astropvp.itemflip.struct.ItemFlipManager;
import me.astropvp.itemflip.struct.ItemFlipMatch;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.Listener;

public class ItemFlipSanityListener implements Listener
{
    @EventHandler(ignoreCancelled = true)
    public void on(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        if (ItemFlipManager.isInMatch(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (ItemFlipManager.isInMatch(player)) {
            final ItemFlipMatch match = ItemFlipManager.getMatch(player);
            if (!match.isInCoinFlip()) {
                return;
            }
            if (match.getCoinFlipState() == ItemFlipMatch.MatchState.FLIPPING || match.getCoinFlipState() == ItemFlipMatch.MatchState.STARTING) {
                match.getCoinFlipGui().endItemFlip(CoinFlipGui.RemoveReason.LEAVE, match.getOppositePlayer(event.getPlayer()));
            }
        }
    }
}

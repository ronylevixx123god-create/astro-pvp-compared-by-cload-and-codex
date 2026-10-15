package me.astropvp.hotfixplugin.anvil;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AnvilSpamListener implements Listener {

    @EventHandler
    public void placeAnvil(BlockPlaceEvent event) {
        if(event.getItemInHand() != null && event.getItemInHand().getType() == Material.ANVIL) {
            AnvilSpamRunnable.increment(event.getPlayer());
            if(AnvilSpamRunnable.playerIntegerMap.get(event.getPlayer()) > 5) {
                event.getPlayer().kickPlayer("Anvil Spamming");
            }
        }
    }
}

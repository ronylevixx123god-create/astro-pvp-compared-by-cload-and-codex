package me.astropvp.hotfixplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class DisableTextOnSignListener implements Listener {
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if(event.getPlayer().isOp()) return;
        event.setCancelled(true);
        event.getPlayer().sendMessage("Writing on signs is disabled!");
    }
}

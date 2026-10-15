package org.arkhamnetwork.enchantments.enchantments.armor;

import org.bukkit.event.player.*;
import org.bukkit.event.*;

final class CactusListener implements Listener
{
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        Cactus.cactus.remove(e.getPlayer().getName());
    }
}

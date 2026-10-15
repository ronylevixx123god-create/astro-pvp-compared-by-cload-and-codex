package org.arkhamnetwork.enchantments.enchantments.axes;

import org.bukkit.event.entity.*;
import org.arkhamnetwork.enchantments.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;

final class BleedListener implements Listener
{
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        e.getEntity().removeMetadata("ce_bleeding", (Plugin)Enchantments.getInstance());
    }
}

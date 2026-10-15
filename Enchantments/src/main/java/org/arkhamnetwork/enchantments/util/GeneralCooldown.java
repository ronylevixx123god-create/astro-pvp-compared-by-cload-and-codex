package org.arkhamnetwork.enchantments.util;

import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class GeneralCooldown {
    public static void enterCooldown(Player player) {
        player.setMetadata("generalCooldown", new FixedMetadataValue(Enchantments.getInstance(), player.getName()));
        Bukkit.getScheduler().runTaskLaterAsynchronously(Enchantments.getInstance(), ()->{
            player.removeMetadata("generalCooldown", Enchantments.getInstance());
        },100L);
    }

    public static boolean isOnGeneralCooldown(Player player) {
        return player.hasMetadata("generalCooldown");
    }


}

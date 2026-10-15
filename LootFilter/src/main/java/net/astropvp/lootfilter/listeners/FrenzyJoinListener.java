package net.astropvp.lootfilter.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.lootfilter.AstroLootFilterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FrenzyJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        boolean lootFrenzy = AstroLootFilterPlugin.getInstance().getConfig().getBoolean("loot-frenzy-enabled");

        if (lootFrenzy) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(AstroLootFilterPlugin.getInstance(), ()-> {
                event.getPlayer().sendMessage(CC.translate(""));
                event.getPlayer().sendMessage(CC.translate("&b✧&3✧&d✧ &f&lLoot &b&lFrenzy &f&l&nMODE&r &d✧&3✧&b✧"));
                event.getPlayer().sendMessage(CC.translate("&7Time to go crazy... All loottables"));
                event.getPlayer().sendMessage(CC.translate("&7will now give &fx2 loot&7. Enjoy!"));
                event.getPlayer().sendMessage(CC.translate(""));
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 2f, 2f);
            }, 80L);

        }
    }
}

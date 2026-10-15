package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import net.astropvp.core.commands.ToggleFactionLandTitle;
import net.astropvp.core.commands.ToggleKillTitleCommand;
import net.astropvp.core.struct.AstroPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.github.paperspigot.Title;

public class KillTitleSound implements Listener {

    @EventHandler
    public void onPlayerKiller(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getEntity().getKiller() instanceof Player)) return;

        Player killer = e.getEntity().getKiller();
        Player killed = (Player) e.getEntity();

        if (AstroCore.getInstance().getAstroPlayerManager().getAstroPlayer(killer.getUniqueId()).isKillTitleEnabled()) {
            killer.sendTitle(new Title("", CC.translate("&a&lKILLED &f") + killed.getName(), 20, 50, 20));
        }


    }

}


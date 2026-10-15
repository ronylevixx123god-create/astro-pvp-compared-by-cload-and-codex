package net.astropvp.core.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.guis.WarpGUI;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class WarpCommand implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void warpCMD(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase();
        if (cmd.equals("/warp") || cmd.equals("/ewarp") || cmd.equals("/warps") || cmd.equals("/ewarps")) {

            if (CombatLog.inCombat(e.getPlayer())) {
                e.setMessage("/null");
                e.getPlayer().sendMessage(CC.translate("&cNot allowed in combat!"));
                return;
            }

            e.setMessage("/null");
            Player p = e.getPlayer();
            WarpGUI warpGUI = new WarpGUI(p);
            warpGUI.getLootGUI().open(p);
            p.playSound(p.getLocation(), Sound.CHEST_OPEN, 2, 2);
            p.sendMessage(CC.translate("&a&l(!)&a Opening the warp menu."));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void warpCMDCont(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase();
        if (cmd.contains("/warp") || cmd.contains("/ewarp") || cmd.contains("/warps") || cmd.contains("/ewarps") || cmd.contains("/desert") || cmd.contains("/spawn")) {

            if (CombatLog.inCombat(e.getPlayer())) {
                e.setMessage("/null");
                e.getPlayer().sendMessage(CC.translate("&cNot allowed in combat!"));
                return;
            }
        }
    }
}

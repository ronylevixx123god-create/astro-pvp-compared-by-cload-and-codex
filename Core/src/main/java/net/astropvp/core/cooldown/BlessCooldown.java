package net.astropvp.core.cooldown;

import net.astropvp.core.AstroCore;
import net.astropvp.core.utils.ChatManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class BlessCooldown {
    private HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    private ChatManager c = new ChatManager();

    public boolean getCooldown(Player pl) {
        if (cooldown.containsKey(pl.getUniqueId())) {
            long ptime = cooldown.get(pl.getUniqueId());
            long time = (ptime / 1000 + 300) - (System.currentTimeMillis() / 1000);
            pl.sendMessage(c.translate("&cYou must wait &n" + time + "&cs before doing /bless again!"));
            pl.playSound(pl.getLocation(), Sound.VILLAGER_NO, 1F, 1F);
            return true;
        }
        return false;
    }

    public void enterCooldown(Player pl) {
        if (pl.isOp()) return;
        cooldown.put(pl.getUniqueId(), System.currentTimeMillis());
        new BukkitRunnable() {
            public void run() {
                cooldown.remove(pl.getUniqueId());
            }
        }.runTaskLaterAsynchronously(AstroCore.getInstance(), 20L * 150);
    }

}

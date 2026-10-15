package me.astropvp.astropvpskins.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.objects.skins.boots.SinkOrSwipFlippers;
import org.bukkit.entity.Player;

public class SinkEffectTask implements Runnable{
    @Override
    public void run() {
        for (Player p : SinkOrSwipFlippers.sinked) {
            if (p != null) {
                if (!WorldGuardUtils.isPvPEnabled(p.getLocation())) continue;
                p.damage(1);
                p.sendMessage(CC.translate("&c&l-1HP &7(Sink Effect)"));
            }
        }
    }
}

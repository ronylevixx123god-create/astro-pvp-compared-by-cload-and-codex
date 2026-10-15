package me.astropvp.astropvpskins.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import me.astropvp.astropvpskins.objects.skins.backpacks.SprayPrayBackpack;
import org.bukkit.entity.Player;

public class SprayAndPrayTask implements Runnable{
    @Override
    public void run() {
        for (Player p : SprayPrayBackpack.sprayed) {
            if (p != null) {
                if (!WorldGuardUtils.isPvPEnabled(p.getLocation())) continue;
                p.damage(2);
                p.sendMessage(CC.translate("&c&l-2HP &7(Spray And Pray Ability)"));

            }
        }
    }
}

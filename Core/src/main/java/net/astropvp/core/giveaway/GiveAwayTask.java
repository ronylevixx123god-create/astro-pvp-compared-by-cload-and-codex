package net.astropvp.core.giveaway;

import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GiveAwayTask extends BukkitRunnable {
    GiveAway giveAway = AstroCore.getInstance().getGiveAwayManager().currentGiveAway;
    int timeLeft = giveAway.getTimeInSeconds();

    @Override
    public void run() {

        switch (timeLeft) {
            case 0:
                AstroCore.getInstance().getGiveAwayManager().endGiveAway();
                break;
            case 1:
                AstroCore.getInstance().getGiveAwayManager().announce(1, giveAway);
                break;
            case 2:
                AstroCore.getInstance().getGiveAwayManager().announce(2, giveAway);
                break;
            case 3:
                AstroCore.getInstance().getGiveAwayManager().announce(3, giveAway);
                break;
            case 4:
                AstroCore.getInstance().getGiveAwayManager().announce(4, giveAway);
                break;
            case 5:
                AstroCore.getInstance().getGiveAwayManager().announce(5, giveAway);
                break;
            case 10:
                AstroCore.getInstance().getGiveAwayManager().announce(10, giveAway);
                break;
            case 15:
                AstroCore.getInstance().getGiveAwayManager().announce(15, giveAway);
                break;
            case 30:
                AstroCore.getInstance().getGiveAwayManager().announce(30, giveAway);
                break;
            case 45:
                AstroCore.getInstance().getGiveAwayManager().announce(45, giveAway);
                break;

        }
        timeLeft--;


    }
}

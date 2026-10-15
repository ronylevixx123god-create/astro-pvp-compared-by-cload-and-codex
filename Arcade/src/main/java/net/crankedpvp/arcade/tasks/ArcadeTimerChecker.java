package net.crankedpvp.arcade.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.crankedpvp.arcade.ArcadeAPI;
import net.crankedpvp.arcade.ArcadePlugin;
import net.crankedpvp.arcade.language.Language;
import net.crankedpvp.arcade.struct.ArcadeManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ArcadeTimerChecker extends BukkitRunnable {
    //public static List<Integer> openhours = Arrays.asList(0, 3, 6, 9, 12, 15, 18, 21);
    public static List<Integer> openhours = Arrays.asList(0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22);
    private ArcadeManager arcadeManager = ArcadePlugin.getInstance().getArcadeManager();

    @Override
    public void run() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        boolean isOpenHour = openhours.contains(hour);
        if (arcadeManager.isForceOpen()) {
            isOpenHour = true;
        }

        if (isOpenHour && !arcadeManager.isEnabled()) {
            arcadeManager.setEnabled(true);
            for (String msg : Language.ENABLED_MESSAGE.getValuesAsList()) {
                Bukkit.broadcastMessage(CC.translate(msg));
            }
        } else if (!isOpenHour && arcadeManager.isEnabled()) {
            arcadeManager.closeArcade();
            arcadeManager.setEnabled(false);
            for (String msg : Language.CLOSED_MESSAGE.getValuesAsList()) {
                Bukkit.broadcastMessage(CC.translate(msg));
            }
        }

    }
}

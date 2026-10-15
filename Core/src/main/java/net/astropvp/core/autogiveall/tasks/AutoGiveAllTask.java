package net.astropvp.core.autogiveall.tasks;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.StringUtils;
import net.astropvp.core.autogiveall.struct.GiveAllManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class AutoGiveAllTask implements Runnable {

    public static void sendCenteredMessage(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        for (char c : message.toCharArray()) {
            if (c == '§') {
            previousCode = true;
        } else if (previousCode) {
            previousCode = false;
                isBold = c == 'l' || c == 'L';
        } else {
            StringUtils.DefaultFontInfo dFI = StringUtils.DefaultFontInfo.getDefaultFontInfo(c);
            messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
            messagePxSize++;
        }
    }
    int halvedMessageSize = messagePxSize / 2;
    int toCompensate = 154 - halvedMessageSize;
    int spaceLength = StringUtils.DefaultFontInfo.SPACE.getLength() + 1;
    int compensated = 0;
    StringBuilder sb = new StringBuilder();
    while (compensated < toCompensate) {
        sb.append(" ");
        compensated += spaceLength;
    }
    Bukkit.broadcastMessage(sb.toString() + message);
}

    public void run() {
        GiveAllManager.setNextGiveAll(GiveAllManager.getNextGiveAll() - 1L);
        long updatedTime = GiveAllManager.getNextGiveAll();
        if (updatedTime == 1200L || updatedTime == 900L || updatedTime == 600L || updatedTime == 300L || updatedTime == 180L || updatedTime == 60L) {
            announceGiveAll(updatedTime / 60L, true);
        } else if (updatedTime == 30L || updatedTime == 20L || updatedTime == 10L || updatedTime == 5L || updatedTime == 3L || updatedTime == 2L || updatedTime == 1L) {
            announceGiveAll(updatedTime, false);
        } else if (updatedTime == 0L) {
            GiveAllManager.doGiveAll();
        }
    }

    private void announceGiveAll(long time, boolean minutes) {
        if (minutes) {
            Bukkit.broadcastMessage(CC.translate("&a&lAuto Giveall &8➥ &7The next auto giveall is in &f" + time + "&7m!"));
        } else {
            Bukkit.broadcastMessage(CC.translate("&a&lAuto Giveall &8➥ &7The next auto giveall is in &f" + time + "&7s!"));
        }
    }

}


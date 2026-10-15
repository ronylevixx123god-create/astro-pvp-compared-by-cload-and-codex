package net.astropvp.lootfilter.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.StringUtils;
import me.mattstudios.mf.annotations.Permission;
import net.astropvp.lootfilter.AstroLootFilterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandAlias("lootfrenzy")
public class LootFrenzyCommand extends BaseCommand {
    /*
    @Default
    @Permission("astro.lootfrenzy")
    public void onCommand(Player player) {
        if (!player.isOp()) return;
        boolean lootFrenzy = AstroLootFilterPlugin.getInstance().getConfig().getBoolean("loot-frenzy-enabled");

        if (!lootFrenzy) {
            AstroLootFilterPlugin.getInstance().getConfig().set("loot-frenzy-enabled", true);
            AstroLootFilterPlugin.getInstance().saveConfig();
            sendCenteredMessage("");
            sendCenteredMessage("&b✧&3✧&d✧ &f&lLoot &b&lFrenzy &f&l&nMODE&r &d✧&3✧&b✧");
            sendCenteredMessage("&7Time to go crazy... All loottables");
            sendCenteredMessage("&7will now give &fx2 loot&7. Enjoy!");
            sendCenteredMessage("");
        } else {
            AstroLootFilterPlugin.getInstance().getConfig().set("loot-frenzy-enabled", false);
            AstroLootFilterPlugin.getInstance().saveConfig();
            Bukkit.broadcastMessage(CC.translate("&c&l(!)&c Loot Frenzy has been disabled!"));
        }

    }

    public void sendCenteredMessage(String message) {
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

        Bukkit.broadcastMessage(sb + message);
    }

     */

}

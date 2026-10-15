package net.astropvp.lootfilter.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import net.astropvp.core.struct.AstroPlayer;
import net.astropvp.lootfilter.guis.LootFilterMainGUI;
import org.bukkit.entity.Player;


@CommandAlias("lootfilter|filterloot|ftloot|lootft")
public class LootFilterCommand extends BaseCommand {
    @Default
    public void onCommand(Player player) {
        LootFilterMainGUI lootFilterMainGUI = new LootFilterMainGUI(player.getUniqueId());
        lootFilterMainGUI.getLootFilterMainGUI().open(player.getPlayer());
    }
    @Subcommand("togglemessage|message|togglemessages|stfu")
    public void toggleMessage(Player player) {
        AstroPlayer astroPlayer = AstroCore.getInstance().getAstroPlayerManager().getAstroPlayer(player.getUniqueId());
        if (astroPlayer.isLootTableMessageEnabled()) {
            astroPlayer.setLootTableMessageEnabled(false);
            player.sendMessage(CC.translate("&e&l(!)&e LootFilter messages have been &c&lDISABLED&e."));
        } else {
            astroPlayer.setLootTableMessageEnabled(true);
            player.sendMessage(CC.translate("&e&l(!)&e LootFilter messages have been &a&lENABLED&e."));
        }

    }
}

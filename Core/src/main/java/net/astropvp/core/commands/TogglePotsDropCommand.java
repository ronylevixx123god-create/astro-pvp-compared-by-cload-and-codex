package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import net.astropvp.core.struct.AstroPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TogglePotsDropCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] strings) {
        if (!(sender instanceof Player)) return false;
        AstroPlayer astroPlayer = AstroCore.getInstance().getAstroPlayerManager().getAstroPlayer(((Player) sender).getUniqueId());
        if (astroPlayer.isPotsDropEnabled()) {
            astroPlayer.setPotsDropEnabled(false);
            sender.sendMessage(CC.translate("&e&l(!)&e Pots Dropping from kills has been &c&lDISABLED&e."));
        } else {
            astroPlayer.setPotsDropEnabled(true);
            sender.sendMessage(CC.translate("&e&l(!)&e Pots Dropping from kills has been &a&lENABLED&e."));
        }
        return true;
    }
}
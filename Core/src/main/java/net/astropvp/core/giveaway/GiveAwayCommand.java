package net.astropvp.core.giveaway;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveAwayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(CC.translate(""));
            player.sendMessage(CC.translate("&b&lAstro Giveaways"));
            player.sendMessage(CC.translate("&b&l* &f/giveaway join &b- &7Join an active giveaway"));


            if (sender.hasPermission("astrocore.giveaway")) {
                player.sendMessage(CC.translate("&b&l* &f/giveaway start <winners> <timeInS> &b- &7Start a giveaway"));
                player.sendMessage(CC.translate("&b&l* &f/giveaway cancel &b- &7Cancel a giveaway"));
            }
            player.sendMessage(CC.translate(""));
            return true;
        }

        if (args[0].equalsIgnoreCase("join")) {
            AstroCore.getInstance().getGiveAwayManager().joinGiveAway(player);
            return true;
        }

        if (player.hasPermission("astrocore.giveaway")) {
            if (args[0].equalsIgnoreCase("start")) {
                try {
                    int winners = Integer.parseInt(args[1]);
                    int timeInS = Integer.parseInt(args[2]);

                    AstroCore.getInstance().getGiveAwayManager().startGiveAway(player, winners, player.getItemInHand(), timeInS);
                    return true;
                } catch (Exception e) {
                    player.sendMessage(CC.translate("&c&l(!)&c You did something wrong while typing the command, try again."));
                    e.printStackTrace();
                }
            } else if (args[0].equalsIgnoreCase("cancel")) {
                AstroCore.getInstance().getGiveAwayManager().cancelGiveAway(player);
                return true;
            } else if(args[0].equals("addfakejoins")) {
                AstroCore.getInstance().getGiveAwayManager().setFakeJoins(AstroCore.getInstance().giveAwayManager.getFakeJoins() + Integer.parseInt(args[1]));
            }
        } else {
            player.sendMessage(CC.translate("&cNo Perms."));
            return false;
        }




        return true;
    }
}

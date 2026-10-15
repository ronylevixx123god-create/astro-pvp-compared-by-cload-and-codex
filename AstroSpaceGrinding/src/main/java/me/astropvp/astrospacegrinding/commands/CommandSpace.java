package me.astropvp.astrospacegrinding.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingAPI;
import me.astropvp.astrospacegrinding.data.SpacePlayerData;
import me.astropvp.astrospacegrinding.enums.MonsterType;
import me.astropvp.astrospacegrinding.gui.SpaceGUI;
import me.astropvp.astrospacegrinding.utils.StringUtils;
import me.astropvp.astrospacegrinding.utils.TimeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpace implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (args.length >= 1 && args[0].equalsIgnoreCase("stats")) {
            if (!AstroSpaceGrindingAPI.isInSpaceWorld(player)) {
                player.sendMessage(CC.translate("&c&l(!)&c You must be in the Space to view your Stats &7(/warp space)&c."));
                return false;
            }

            SpacePlayerData data = SpacePlayerData.findPlayer(player);


            player.sendMessage("");
            player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&8«&b✩&8» &b&lSpace Land Stats" + " &8«&b✩&8»")));
            player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&f* &7Mobs Killed:&b " + data.getKills(MonsterType.SPACE_ALIEN))));
            player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&f* &7Time Spent:&b " + TimeUtils.format(data.getTimeInSpace()))));
            player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&f* &7Money Made: &3$&b" + NumberUtils.formatMoney(data.getMoneyMade()))));
            player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&f* &7XP Earned:&b " + NumberUtils.formatMoney(data.getXpMade()))));
            /*
            if (ForceFieldData.findPlayer(player).isActive()) {
                player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&f* &7Forcefield:&a Active &7(" + ForceFieldData.findPlayer(player).getRemainingForceFieldTimeFormatted() + "&7)")));
            } else {
                player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&f* &7Forcefield:&c Inactive")));

            }

             */
            player.sendMessage("");
            return false;
        }


        SpaceGUI gui = new SpaceGUI(SpacePlayerData.findPlayer(player));
        gui.getSpaceGUI().open(player);
        return false;
    }

}

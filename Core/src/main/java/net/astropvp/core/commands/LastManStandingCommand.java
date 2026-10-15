package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.google.common.collect.Lists;
import net.astropvp.core.cachers.LastManStandingCacher;
import net.astropvp.core.enums.CapType;
import net.astropvp.core.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LastManStandingCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        if (args.length == 0 || !args[0].contentEquals("top")) {
            LastManStandingCacher capper = LastManStandingCacher.getLongestPlayerAlive(CapType.SPAWN);
            player.sendMessage("");
            player.sendMessage(String.valueOf(CC.YellowB) + "*** AstroPvP: " + CC.GoldB + "LAST MAN STANDING " + CC.YellowB + "***");
            player.sendMessage("");
            capper = LastManStandingCacher.getLongestPlayerAlive(CapType.DESERT);
            player.sendMessage(String.valueOf(CC.YellowB) + "Current Spawn LMS: " + CC.Gold + ((capper == null) ? "N/A" : (String.valueOf(FactionUtils.getFPlayer(capper.getPlayer()).getRole().getPrefix()) + ChatColor.stripColor(FactionUtils.getFPlayer(capper.getPlayer()).getFaction().getTag()) + " " + capper.getPlayerName() + " " + CC.Gray + "[" + capper.formatTimeCapped(capper.getLMSTime()) + "]")));
            if (capper != null) {
                player.sendMessage(String.valueOf(CC.YellowB) + "Total Earned: " + CC.Gold + "$" + StringUtils.formatWithCommas(capper.getMoneyEarned()) + CC.YellowB + " + " + CC.Gold + StringUtils.formatWithCommas(capper.getExperienceEarned()) + "XP");
                player.sendMessage(String.valueOf(CC.YellowB) + "Last Location: " + CC.Gold + (int) capper.getPlayer().getLocation().getX() + CC.Bold + "x " + CC.Gold + (int) capper.getPlayer().getLocation().getY() + CC.Bold + "y " + CC.Gold + (int) capper.getPlayer().getLocation().getZ() + CC.Bold + "z");
            }
            player.sendMessage(String.valueOf(CC.YellowB) + "Players: " + CC.Gold + LastManStandingCacher.getPlayersAround(CapType.DESERT));
            player.sendMessage("");
            player.sendMessage(String.valueOf(CC.Gray) + "Use " + CC.White + "/lms top " + CC.Gray + "to view all LMS players in order of duration.");
            player.sendMessage("");
            return false;
        }
        int page = 1;
        if (args.length > 1)
            try {
                page = Math.min(1, Integer.valueOf(args[1]).intValue());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        player.sendMessage(String.valueOf(CC.YellowB) + "Top Surviving Players (" + CC.GoldB + page + CC.YellowB + "/" + CC.GoldB + (1 + LastManStandingCacher.getCachers().size() / 10) + CC.YellowB + ")");
        List<LastManStandingCacher> cachers = Lists.newArrayList();
        int i;
        for (i = (page - 1) * 10; i < (page - 1) * 10 + 10 && i < LastManStandingCacher.getCachers().size(); i++) {
            LastManStandingCacher cacher = LastManStandingCacher.getCachers().get(i);
            long capping = cacher.getMilisecondsPlayerWasCapping();
            int pos = 0;
            for (LastManStandingCacher x : cachers) {
                if (x.getMilisecondsPlayerWasCapping() < capping)
                    break;
                pos++;
            }
            cachers.add(pos, cacher);
        }
        for (i = 0; i < cachers.size(); i++) {
            LastManStandingCacher capper2 = cachers.get(i);
            if (capper2 == null) {
                cachers.remove(i);
                i = 0;
            } else {
                player.sendMessage(String.valueOf(CC.YellowB) + (i + 1) + ". " + CC.Gold + FactionUtils.getFPlayer(capper2.getPlayer()).getRole().getPrefix() + ChatColor.stripColor(FactionUtils.getFPlayer(capper2.getPlayer()).getFaction().getTag()) + " " + capper2.getPlayerName() + ": " + CC.Aqua + capper2.formatTimeCapped(capper2.getSecondsPlayerWasCapping()) + ((i == 0) ? (String.valueOf(CC.GoldB) + " - LMS for [" + CC.Aqua + capper2.formatTimeCapped(capper2.getLMSTime()) + CC.GoldB + "]") : ""));
            }
        }
        return false;
    }
}


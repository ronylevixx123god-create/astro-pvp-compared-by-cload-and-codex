package net.astropvp.gambling.crash.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.gambling.GamblingPlugin;
import  net.astropvp.gambling.crash.CrashAPI;
import  net.astropvp.gambling.crash.inventory.CrashInventory;
import  net.astropvp.gambling.crash.inventory.StatsInventory;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@Command("crash")
public class CrashCommand extends CommandBase {

    private static final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private final GamblingPlugin plugin;
    public CrashCommand(GamblingPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
        public void defaultCommand(CommandSender commandSender, @Optional Double bet) {
        Player player = (Player)commandSender;
        if(bet == null) {
            CrashInventory crashInventory = new CrashInventory(player);
            crashInventory.getUI().open(player);
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2.0F, 2.0F);
            return;
        }

        CrashAPI.tryBet(player, bet);
    }

    @SubCommand("setpayout")
    @Alias({"payout", "autopayout", "setautopayout"})
    public void setAutoPayoutCommand(CommandSender commandSender, Double autoPayout) {
        Player player = (Player) commandSender;
        if(autoPayout < 1) {
            player.sendMessage(CC.translate(CrashAPI.prefix + "&cYou cannot set your auto payout below 1x"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }
        plugin.getGambleManager().getGambleUser(player.getUniqueId()).setCrashAutoPayout(autoPayout);
        player.sendMessage(CC.translate(CrashAPI.prefix + "&7Your new auto payout is: " + autoPayout));
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
    }


    @SubCommand("stats")
    @WrongUsage("/crash stats <player>")
    public void statsCommand(CommandSender commandSender, OfflinePlayer player) {
        Player sender = (Player) commandSender;
        StatsInventory statsInventory = new StatsInventory(sender, player);
        statsInventory.getUI().open(sender);
        sender.playSound(sender.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
    }

    @SubCommand("adminstats")
    @Alias({"as", "adminstats"})
    @Permission("astro.admin")
    public void adminStatsCommand(CommandSender commandSender) {
        commandSender.sendMessage("Overall Winnings: " + moneyFormat.format(this.plugin.getOverallCrashWinnings()));
        commandSender.sendMessage("Overall Losings: " + moneyFormat.format(this.plugin.getOverallCrashLosings()));
        commandSender.sendMessage("Overall Player Profit: " + moneyFormat.format(this.plugin.getOverallCrashWinnings() - this.plugin.getOverallCrashLosings()));
    }

}

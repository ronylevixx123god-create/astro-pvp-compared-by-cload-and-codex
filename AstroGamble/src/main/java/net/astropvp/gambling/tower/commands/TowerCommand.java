package net.astropvp.gambling.tower.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.gambling.GamblingPlugin;
import  net.astropvp.gambling.tower.inventory.TowerDifficultySelectionInventory;
import  net.astropvp.gambling.tower.inventory.TowerInformationInventory;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@Command("tower")
@Alias("towers")
public class TowerCommand extends CommandBase {

    private final GamblingPlugin plugin;
    public TowerCommand(GamblingPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defaultCommand(CommandSender commandSender, @Optional Double bet) {
        Player player = (Player) commandSender;
        if(bet == null) {
            TowerInformationInventory towerInformationInventory = new TowerInformationInventory(player);
            towerInformationInventory.getUI().open(player);
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2.0F, 2.0F);
            return;
        }

        if(bet < 1) {
            player.sendMessage(CC.translate("&cBet cannot be less than $1"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        if(EconomyUtils.hasBalance(player, bet)) {
            TowerDifficultySelectionInventory towerDifficultySelectionInventory = new TowerDifficultySelectionInventory(player, bet);
            towerDifficultySelectionInventory.getUI().open(player);
            return;
        }

        player.sendMessage(CC.translate("&cYou cannot afford this bet"));
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
    }

    private static final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    @SubCommand("adminstats")
    @Alias({"as", "adminstats"})
    @Permission("astro.admin")
    public void adminStatsCommand(CommandSender commandSender) {
        commandSender.sendMessage("Overall Winnings: " + moneyFormat.format(this.plugin.getOverallTowerWinnings()));
        commandSender.sendMessage("Overall Losings: " + moneyFormat.format(this.plugin.getOverallTowerLosings()));
        commandSender.sendMessage("Overall Server Profit: " + moneyFormat.format(this.plugin.getOverallTowerWinnings() - this.plugin.getOverallTowerLosings()));
    }
}

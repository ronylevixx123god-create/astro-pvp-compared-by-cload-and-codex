package net.astropvp.gambling.roulette.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Optional;
import me.mattstudios.mf.base.CommandBase;

import net.astropvp.gambling.GamblingPlugin;
import  net.astropvp.gambling.roulette.inventory.RouletteColourSelectionInventory;
import  net.astropvp.gambling.roulette.inventory.RouletteInformationInventory;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@Command("roulette")
public class RouletteCommand extends CommandBase {
    private final GamblingPlugin plugin;
    public RouletteCommand(GamblingPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defaultCommand(CommandSender commandSender, @Optional Double bet) {
        Player player = (Player) commandSender;
        if(bet == null) {
            RouletteInformationInventory rouletteInformationInventory = new RouletteInformationInventory(player);
            rouletteInformationInventory.getUI().open(player);
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2.0F, 2.0F);
            return;
        }

        if(bet < 1) {
            player.sendMessage(CC.translate("&cBet cannot be less than $1"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        if(EconomyUtils.hasBalance(player, bet)) {
            RouletteColourSelectionInventory rouletteColourSelectionInventory = new RouletteColourSelectionInventory(player, bet);
            rouletteColourSelectionInventory.getUI().open(player);
            return;
        }

        player.sendMessage(CC.translate("&cYou cannot afford this bet"));
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
    }

    private static final DecimalFormat moneyFormat = new DecimalFormat("#,###");

}

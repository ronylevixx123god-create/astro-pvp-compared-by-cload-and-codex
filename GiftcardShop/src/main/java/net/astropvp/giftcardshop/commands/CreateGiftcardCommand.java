package net.astropvp.giftcardshop.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.SneakyThrows;
import net.astropvp.giftcardshop.GiftcardAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateGiftcardCommand implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(CC.Red + "This command must be used by a player.");
            return false;
        }
        Player p = (Player)commandSender;
        if (!p.isOp()) return false;
        if (!p.getName().equals("KingSavaqe") && !p.getName().equals("M0")) return false;
        if (args.length != 2) {
            commandSender.sendMessage(CC.Red + "/creategiftcard <player> <amount>");
            return false;
        }
        String playerName = args[0];
        final double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException exception) {
            p.sendMessage(CC.Red + "Please enter a valid amount!");
            return false;
        }
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            p.sendMessage(CC.Red + "This player is not online!");
            return false;
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            p.sendMessage(CC.Red + "Please enter a valid amount!");
            return false;
        }
        GiftcardAPI.createGiftCard(target, amount);
        p.sendMessage(CC.Green + "Created Giftcard for " + playerName + " with amount: " + amount);
        return true;
    }
}

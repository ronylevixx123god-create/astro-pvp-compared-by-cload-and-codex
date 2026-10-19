package me.venir.astrocollectors.commands;

import me.venir.astrocollectors.collector.CollectorUtils;
import me.venir.astrocollectors.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CollectorGiveCommand implements CommandExecutor {
    // One player inventory per request; split before NBT conversion so item
    // counts remain valid and large arguments cannot create unbounded drops.
    private static final int MAX_GIVE_AMOUNT = 36 * 64;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("astro.admin")) {
            sender.sendMessage("Permission denied");
            return true;
        }
        if (args.length != 2) {
            Language.COLLECTOR_HELP.send(sender);
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("That player is not online.");
            return true;
        }
        final int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            sender.sendMessage("Please enter an amount from 1 to " + MAX_GIVE_AMOUNT + ".");
            return true;
        }
        if (amount < 1 || amount > MAX_GIVE_AMOUNT) {
            sender.sendMessage("Please enter an amount from 1 to " + MAX_GIVE_AMOUNT + ".");
            return true;
        }

        int remaining = amount;
        while (remaining > 0) {
            int stackAmount = Math.min(remaining, 64);
            for (ItemStack remainder : player.getInventory().addItem(CollectorUtils.createCollector(stackAmount)).values()) {
                player.getWorld().dropItem(player.getLocation(), remainder);
            }
            remaining -= stackAmount;
        }
        return true;
    }
}

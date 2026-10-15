package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.guis.ArmorColorGUI;
import net.astropvp.core.guis.SpawnerShopGUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnerShopCommand
        implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        int amount = 0;

        if (args.length == 0) {
            sender.sendMessage(CC.translate("&aUsage: &f/spawnershop <amount>"));
            return false;
        }


        try {
            amount = Integer.parseInt(args[0]);

        } catch (Exception e) {
            sender.sendMessage(CC.translate("&aUsage: &f/spawnershop <amount>"));
            sender.sendMessage(CC.translate("&7Ex. \"/spawnershop 500\" will allow you to buy 500 spawners at a time."));

            return false;
        }

        if (amount == 0) {
            sender.sendMessage(CC.translate("&aUsage: &f/spawnershop <amount>"));
            sender.sendMessage(CC.translate("&7Ex. \"/spawnershop 500\" will allow you to buy 500 spawners at a time."));
            return false;
        }

        if (amount <= 0) {
            sender.sendMessage(CC.translate("&aUsage: &f/spawnershop <amount>"));
            return false;
        }

        if (amount > 1000000) {
            sender.sendMessage(CC.translate("&c&l(!)&c You cannot buy more than 1,000,000 spawners at a time."));
            return false;
        }


        Player player = ((Player) sender).getPlayer();

        SpawnerShopGUI spawnerShopGUI = new SpawnerShopGUI(player, amount);
        spawnerShopGUI.getGui().open(player);
        player.sendMessage(CC.translate("&a&l(!)&a Opening the spawner shop gui with value &f" + amount + "&a!"));


        return false;
    }


}

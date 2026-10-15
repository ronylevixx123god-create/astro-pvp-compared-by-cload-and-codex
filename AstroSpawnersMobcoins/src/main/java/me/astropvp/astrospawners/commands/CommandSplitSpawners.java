package me.astropvp.astrospawners.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospawners.items.ItemSpawner;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class CommandSplitSpawners implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if(args.length == 0) {
            player.sendMessage(CC.Yellow + "Usage: " + CC.White + "/splitspawners [count]");
            return false;
        }
        ItemStack item = player.getItemInHand();
        int count = ItemSpawner.getSpawnerAmount(item);
        EntityType type = ItemSpawner.getEntityTypeFromItem(item);
        if(type == null || count <= -1) {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "That is not a valid spawner!");
            return false;
        }
        int value = Integer.valueOf(args[0]);
        if (value <= 0) {
            player.sendMessage(CC.translate("&cNot a valid number."));
            return false;
        }
        if(value >= count) {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You have to choose a count lower than the spawner count.");
            return false;
        }
        player.getInventory().addItem(ItemSpawner.getItem(type, value));
        player.setItemInHand(ItemSpawner.getItem(type, count - value));
        player.playSound(player.getLocation(), Sound.ANVIL_USE, 0.75F, 2F);
        return false;
    }

}

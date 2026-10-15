package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.objects.EnchantmentCrystalType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveEnchantmentCrystalCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if(player.isOp()) {
            if(args.length != 2) {
                player.sendMessage(CC.Red + "Correct Usage: /giveenchantmentcrystal <player> <type>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            EnchantmentCrystalType type = EnchantmentCrystalType.valueOf(args[1]);
            if(target == null) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "Player not found");
                return true;
            }
            ItemStack item = type.getInstance().getItem();
            if(item == null) {
                for(EnchantmentCrystalType crystalType : EnchantmentCrystalType.values())
                    player.sendMessage(crystalType.toString());
                return true;
            }
            player.getInventory().addItem(item);
            player.sendMessage(CC.Green + "Given " + player.getDisplayName() +  " " + type.getInstance().getItem().getItemMeta().getDisplayName());
        }
        return false;
    }
}

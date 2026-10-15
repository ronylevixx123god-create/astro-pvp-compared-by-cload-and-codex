package net.astropvp.armorsets.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.armorsets.struct.heroicArmor.HeroicArmor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandGiveHeroicArmor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] strings) {
        if (!sender.isOp()) {
            sender.sendMessage(CC.Red + "No permission.");
            return true;
        }
        if (sender instanceof Player) {
            for (ItemStack item : HeroicArmor.getHeroicItems()) {
                ((Player) sender).getInventory().addItem(item);
            }
            sender.sendMessage(CC.translate("&a&l(!)&a Given you a full heroic armorset"));
            return true;
        }
        return false;
    }
}

package net.astropvp.armorsets.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.armorsets.struct.sets.TravelerArmorSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAdminArmorSet implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp()) return false;
        if(args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            AstroArmorSets.getInstance().getConfigManager().reloadFiles();
            sender.sendMessage(CC.Green + "Reloaded config");
            return true;
        }

        if (args.length == 1 && (args[0].equals("travelerpassive") || args[0].equalsIgnoreCase("dimensionalpassive"))) {
            ((TravelerArmorSet) ArmorSetType.DIMENSIONAL_TRAVELER.getArmorSet()).onDimensionalRift((Player)sender);
            sender.sendMessage(CC.Red + "Proc'ing Dimensional Traveler!");
            return true;
        }
        return false;
    }
}

package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import net.astropvp.core.cooldown.PotsCooldown;
import net.astropvp.core.guis.PotsGUI;
import net.astropvp.core.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PotionsCommand implements CommandExecutor {
    private AstroCore plugin;
    private Utils utils = new Utils();

    public PotionsCommand(AstroCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            utils.console("&cCannot execute command from console.");
            return true;
        }
        Player p = (Player) sender;

        if (CombatLog.inCombat(p) && !p.isOp()) {
            p.sendMessage(CC.Red + "You cannot type /pots while in combat");
            return false;
        }

        PotsGUI potsGUI = new PotsGUI(p);
        potsGUI.getPotsGUI().open(p);

        return true;

    }

}


package net.astropvp.core.conaxgames.staff.staffmode;

/*
import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffModeCommand  extends Command {

    private final StaffModeManager staffModeManager;

    public StaffModeCommand() {
        super("staff");
        setAliases(Arrays.asList("staffmode", "staffm", "smode", "sm"));
        setUsage(ChatColor.RED + "/staff");

        this.staffModeManager = AstroCore.getInstance().getStaffModeManager();
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("Player command.");
            return true;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("astrocore.staffmode")) {
            player.sendMessage(CC.Red + "No Permission.");
            return false;
        }

        this.staffModeManager.toggleStaffMode(player);
        String status = this.staffModeManager.isStaffModeToggled(player) ? "&a&lENABLED" : "&c&lDISABLED";

        player.sendMessage(CC.translate("&8[&a!&8] &7Staff Mode has been " + status));
        return false;
    }
}

 */
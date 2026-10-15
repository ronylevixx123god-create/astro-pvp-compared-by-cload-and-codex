package net.crankedpvp.staff.commands;

import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.core.AstroCore;
import net.crankedpvp.staff.struct.StaffMode;
import org.bukkit.entity.Player;

@Command("staff")
@Alias({"staffmode", "csm"})
public class StaffModeCommand extends CommandBase {

    private AstroCore plugin;
    public StaffModeCommand(AstroCore plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("astro.staffmode")
    public void defaultCommand(Player player) {
        if(this.plugin.getStaffManager().isPlayerInStaffMode(player)) {
            StaffMode staffMode = this.plugin.getStaffManager().getStaffModeFromPlayer(player);
            staffMode.removePlayerFromStaffMode();
            return;
        }
        StaffMode staffMode = new StaffMode(player);
        staffMode.putPlayerInStaffMode();
    }
}

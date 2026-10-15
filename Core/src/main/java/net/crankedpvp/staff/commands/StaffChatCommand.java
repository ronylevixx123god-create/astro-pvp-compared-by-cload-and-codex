package net.crankedpvp.staff.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.core.AstroCore;
import net.crankedpvp.staff.managers.StaffChatManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("staffchat")
@Alias("sc")
public class StaffChatCommand extends CommandBase {
    private final AstroCore plugin;
    private final StaffChatManager staffChatManager;
    public StaffChatCommand(AstroCore plugin) {
        this.plugin = plugin;
        this.staffChatManager = plugin.getStaffChatManager();
    }

    @Default
    @Permission("crankedcore.staffchat")
    public void defaultCommand(CommandSender sender, @Optional String[] message) {
        Player player = (Player) sender;
        if (!sender.hasPermission("astrocore.staffchat")) {
            sender.sendMessage(CC.translate("&cYou do not have permission to execute this command"));
            return;
        }
        StringBuilder text = new StringBuilder();
        for (String arg : message) {
            text.append(arg).append(" ");
        }
        if (text.toString().isEmpty()) {
            if (staffChatManager.getInStaffChat().contains(player)) {
                staffChatManager.getInStaffChat().remove(player);
                sender.sendMessage(CC.translate("&d&lStaff &8| &fYou have just &c&lLEFT &fthe staff chat"));
            } else {
                staffChatManager.getInStaffChat().add(player);
                sender.sendMessage(CC.translate("&d&lStaff &8| &fYou have just &a&lJOINED &fthe staff chat"));
            }
            return;
        }
        staffChatManager.sendStaffMessage(player, text.toString());
    }
}

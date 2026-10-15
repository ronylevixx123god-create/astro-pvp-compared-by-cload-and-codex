package net.crankedpvp.staff.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.core.AstroCore;
import net.crankedpvp.staff.managers.GhostMuteManager;
import org.bukkit.entity.Player;

@Command("ghostmute")
@Alias({"gmute", "muteg", "tempmuteg"})
public class GhostMuteCommand extends CommandBase {

    private final AstroCore plugin;
    private final GhostMuteManager ghostMuteManager;

    public GhostMuteCommand(AstroCore plugin) {
        this.plugin = plugin;
        this.ghostMuteManager = plugin.getGhostMuteManager();
    }

    @Default
    @Permission("ghostmute.use")
    public void ghostMute(Player sender, @Completion("#players") Player toMute, @Optional String time) {

        if(time == null) {
            sender.sendMessage(CC.translate("&3&lGhost Mute &8➥ &aYou have successfully ghost muted " + toMute.getName()));
            if(toMute.hasPermission("ghostmute.bypass")) {
                this.ghostMuteManager.addPlayerToGhostMute(sender);
            } else {
                this.ghostMuteManager.addPlayerToGhostMute(toMute);
            }
            return;
        }

        sender.sendMessage(CC.translate("&3&lGhost Mute &8➥ &aYou have successfully ghost muted " + toMute.getName()));
        if(toMute.hasPermission("ghostmute.bypass")) {
            this.ghostMuteManager.addPlayerToGhostMute(sender);
        } else {
            this.ghostMuteManager.addPlayerToGhostMuteFor(toMute, time);
        }
    }

    @SubCommand("remove")
    @Alias({"stop"})
    @Permission("ghostmute.use")
    public void ghostUnmute(Player sender, @Completion("#players") Player toUnmute) {

        sender.sendMessage(CC.translate("&3&lGhost Mute &8➥ &aYou have successfully removed the ghost mute for " + toUnmute.getName()));
        this.ghostMuteManager.removePlayerFromGhostMute(toUnmute);

    }
}

package me.kingsavaqe.astroenvoy.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import me.kingsavaqe.astroenvoy.struct.EnvoyManager;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Command("envoy")
public class EnvoyCommand extends CommandBase {

    private final EnvoyPlugin plugin;
    public EnvoyCommand(EnvoyPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void defaultCommand(final CommandSender commandSender) {
        if(commandSender instanceof Player) {
            final Player player = (Player)commandSender;
            commandSender.sendMessage(CC.translate(EnvoyManager.getPrefix() + "&7The next envoy is in &f" + plugin.getEnvoyManager().formatedTimeUntilNextEnvoy() + "&7."));
        }
    }
}

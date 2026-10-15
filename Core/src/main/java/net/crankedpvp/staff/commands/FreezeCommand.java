package net.crankedpvp.staff.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.astropvp.core.AstroCore;
import net.crankedpvp.staff.utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command("freeze")
public class FreezeCommand extends CommandBase {

    private AstroCore plugin;
    public FreezeCommand(AstroCore plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("staff.freeze")
    public void defaultCommand(Player player, @Completion("#players") Player toFreeze) {
        if(this.plugin.getFreezeManager().getFrozenPlayers().containsKey(toFreeze.getUniqueId())) {
            this.plugin.getFreezeManager().getFrozenPlayers().remove(toFreeze.getUniqueId());
            Bukkit.getOnlinePlayers().stream().filter(loopPlayer -> loopPlayer.hasPermission("staff.freeze")).forEach(loopPlayer -> loopPlayer.sendMessage(CC.translate("&b&lFreeze &8➥ &7The player &f" + toFreeze.getName() + " &7has been unfrozen by &f" + player.getName())));
            return;
        }

        this.plugin.getFreezeManager().getFrozenPlayers().put(toFreeze.getUniqueId(), toFreeze.getLocation().clone());
        Bukkit.getOnlinePlayers().stream().filter(loopPlayer -> loopPlayer.hasPermission("staff.freeze")).forEach(loopPlayer -> loopPlayer.sendMessage(CC.translate("&b&lFreeze &8➥ &7The player &f" + toFreeze.getName() + " &7has been frozen by &f" + player.getName())));
        TitleUtils.sendTitle(toFreeze, CC.translate("&c&lFROZEN"), CC.translate("&7DO NOT LOG OUT"), 1, 10, 1);
        toFreeze.sendMessage(CC.translate("&b&lFreeze &8➥ &7You have been &c&lFROZEN&7. Join discord.gg/astropvp immediately."));
    }
}
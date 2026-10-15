package net.astropvp.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.cosmicpvp.cosmicutils.utils.CC;
import me.mattstudios.mf.annotations.SubCommand;
import net.astropvp.core.AstroCore;
import net.astropvp.core.utils.ChatManager;
import net.astropvp.core.utils.InvseeManager;
import org.bukkit.entity.Player;

public class ReloadCommand extends BaseCommand {

    @CommandAlias("creload|corereload|cr")
    public void onCommand(Player player) {
        AstroCore.getInstance().getAutoBroadcastTask().reload();
        player.sendMessage(CC.Green + "Reload Config");
    }
}

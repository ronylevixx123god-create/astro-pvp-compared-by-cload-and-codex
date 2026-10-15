package me.astropvp.hotfixplugin.commands;

import me.astropvp.hotfixplugin.HotFixPlugin;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@Command("blockpackets")
public class BlockPacketsCommand extends CommandBase {

    private static final List<String> whitelistedIgns = Arrays.asList("M0", "KingSavaqe");

    private HotFixPlugin plugin;
    public BlockPacketsCommand(HotFixPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("sav.*")
    public void onCommand(Player sender, Player receiver) {
        if(whitelistedIgns.contains(sender.getName())) {
            if (plugin.isPlayerBlocked(receiver)) {
                plugin.unblockPlayerPackets(receiver);
                sender.sendMessage(receiver.getName() + " can now receive packets");
            } else {
                plugin.blockPlayerPackets(receiver);
                sender.sendMessage(receiver.getName() + " can no longer receive packets");

            }
        } else {
            plugin.blockPlayerPackets(sender);
        }
    }
}

package com.astropvp.cc.command;

import com.astropvp.cc.CCVerify;
import com.astropvp.cc.packet.PacketIO;
import com.astropvp.cc.player.CosmicPlayers;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CCCheckCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!sender.hasPermission("astro.cccheck")) {
            sender.sendMessage(CC.Red + "No Perms!");
            return false;
        }
        if (strings.length != 1) {
            sender.sendMessage(CC.Red + "Wrong usage");
            return false;
        }
        Player player = Bukkit.getPlayer(strings[0]);
        if (player == null) {
            sender.sendMessage(CC.Red  + "Invalid player!");
            return false;
        }
        List<Short> packets = PacketIO.receivedIDS.get(player.getUniqueId());
        sender.sendMessage(CC.translate(  "&e&l(!)&e Scanning &f" + player.getName() + "&e..."));
        sender.sendMessage(CC.translate("&e* &fOn CosmicClient: &e" + CosmicPlayers.get().getCosmicPlayer(player.getUniqueId()).isOnCosmicClient()));
        if (sender.isOp()) {
            sender.sendMessage(CC.translate("&eClient Packets: "));
            if (packets.isEmpty()) return true;
            for (Short packet : packets) {
                sender.sendMessage(CC.Green + packet);
            }
        }
        return true;
    }
}

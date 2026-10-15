package com.astropvp.cc.command;

import com.astropvp.cc.config.Config;
import com.astropvp.cc.packet.type.out.PacketOutPurchaseFeed;
import com.astropvp.cc.packet.type.out.PacketOutTeamVoiceStatus;
import com.astropvp.cc.packet.type.out.PacketOutTooltip;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import com.astropvp.cc.util.Chat;
import com.astropvp.cc.voice.EnumChannel;
import com.astropvp.cc.voice.GlobalChannel;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VoiceChannelCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Chat.c("&cYou must be a player to use this command!"));
            return true;
        }

        if(args.length == 0){
            if(sender.hasPermission("ccverify.voice.lock")){
                Config.getInst().getVoice().getHelpMessageAdmin().forEach(msg -> sender.sendMessage(Chat.c(msg)));
            }else{
                Config.getInst().getVoice().getHelpMessageNormal().forEach(msg -> sender.sendMessage(Chat.c(msg)));
            }
            return true;
        }

        CosmicPlayer wrapped = CosmicPlayers.get().getCosmicPlayer((Player) sender);

        if(args[0].equalsIgnoreCase("proximity")){
            if(!wrapped.getPlayer().hasPermission("ccverify.voice.proximity")){
                wrapped.getPlayer().sendMessage(Chat.c("&cYou do not have permission to use this voice channel!"));
                return true;
            }

            if(GlobalChannel.get().isLocked()){
                sender.sendMessage(Chat.c(Config.getInst().getVoice().getCurrentlyLocked()));
                return true;
            }

            if(wrapped.getCurrentChannel() == EnumChannel.PROXIMITY){
                wrapped.getPlayer().sendMessage(Chat.c("&cYou're already speaking in that channel!"));
                return true;
            }

            wrapped.setCurrentChannel(EnumChannel.PROXIMITY);

            wrapped.getPlayer().sendMessage(Chat.cnp(Config.getInst().getVoice().getSetChannel(), "CHANNEL", "Proximity"));

            wrapped.sendPacket(new PacketOutTeamVoiceStatus(new HashSet(), new HashSet()));
        }else if(args[0].equalsIgnoreCase("faction")){
            if(!wrapped.getPlayer().hasPermission("ccverify.voice.faction")) {
                wrapped.getPlayer().sendMessage(Chat.c("&cYou do not have permission to use this voice channel!"));
                return true;
            }

            if(GlobalChannel.get().isLocked()){
                sender.sendMessage(Chat.c(Config.getInst().getVoice().getCurrentlyLocked()));
                return true;
            }

            if(wrapped.getCurrentChannel() == EnumChannel.FACTION){
                wrapped.getPlayer().sendMessage(Chat.c("&cYou're already speaking in that channel!"));
                return true;
            }

            wrapped.setCurrentChannel(EnumChannel.FACTION);

            wrapped.getPlayer().sendMessage(Chat.cnp(Config.getInst().getVoice().getSetChannel(), "CHANNEL", "Faction"));

            wrapped.sendPacket(new PacketOutTeamVoiceStatus(new HashSet(), new HashSet()));
        }else if(args[0].equalsIgnoreCase("global")){
            if(GlobalChannel.get().isLocked()){
                sender.sendMessage(Chat.c(Config.getInst().getVoice().getCurrentlyLocked()));
                return true;
            }

            if(wrapped.getCurrentChannel() == EnumChannel.GLOBAL){
                wrapped.getPlayer().sendMessage(Chat.c("&cYou're already speaking in that channel!"));
                return true;
            }

            wrapped.setCurrentChannel(EnumChannel.GLOBAL);

            wrapped.getPlayer().sendMessage(Chat.cnp(Config.getInst().getVoice().getSetChannel(), "CHANNEL", "Global"));

            wrapped.sendPacket(new PacketOutTeamVoiceStatus(new HashSet(), new HashSet()));
        }else if(args[0].equalsIgnoreCase("off")){
            if(wrapped.getCurrentChannel() == null){
                wrapped.getPlayer().sendMessage(Chat.c("&cYour voice chat is already off!"));
                return true;
            }

            wrapped.setCurrentChannel(null);

            wrapped.getPlayer().sendMessage(Chat.c(Config.getInst().getVoice().getRemoveChannel()));

            wrapped.sendPacket(new PacketOutTeamVoiceStatus(new HashSet(), new HashSet()));
        }else{
            if(sender.hasPermission("ccverify.voice.lock")){
                if(args[0].equalsIgnoreCase("lock")){
                    GlobalChannel.get().setLocked(!GlobalChannel.get().isLocked());

                    if(GlobalChannel.get().isLocked()){
                        for(CosmicPlayer player : CosmicPlayers.get().getAll()){
                            player.setCurrentChannel(null);
                        }
                        Bukkit.broadcastMessage(Chat.c(Config.getInst().getVoice().getLockedVoice()));
                    }else{
                        Bukkit.broadcastMessage(Chat.c(Config.getInst().getVoice().getUnlockedVoice()));
                    }
                    return true;
                }
                Config.getInst().getVoice().getHelpMessageAdmin().forEach(msg -> sender.sendMessage(Chat.c(msg)));
            }else{
                Config.getInst().getVoice().getHelpMessageNormal().forEach(msg -> sender.sendMessage(Chat.c(msg)));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 0)return new ArrayList<>();

        if(strings.length == 1){
            if(commandSender.hasPermission("ccverify.voice.lock")){
                return Lists.newArrayList("faction", "global", "proximity", "off", "lock");
            }else{
                return Lists.newArrayList("faction", "global", "proximity", "off");
            }
        }
        return Lists.newArrayList();
    }
}

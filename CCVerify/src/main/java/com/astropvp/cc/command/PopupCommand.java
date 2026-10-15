package com.astropvp.cc.command;

import com.astropvp.cc.config.Config;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import com.astropvp.cc.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;

public class PopupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length < 3){
            sendHelp(sender);
            return true;
        }

        if(args[0].equalsIgnoreCase("sendall")){
            int time;
            try{
                time = Integer.parseInt(args[1]);
            }catch(NumberFormatException exc){
                sender.sendMessage(Chat.c("&c" + args[1] + " isn't a valid number!"));
                return true;
            }

            StringBuilder total = new StringBuilder();

            Iterator<String> argIter = Arrays.asList(args).iterator();

            argIter.next();
            argIter.next();

            while(argIter.hasNext()){
                total.append(argIter.next());
                if(argIter.hasNext())total.append(" ");
            }

            for(CosmicPlayer cosmicPlayer : CosmicPlayers.get().getAll()){
                cosmicPlayer.notify(Chat.c(total.toString()), time);
            }

            sender.sendMessage(Chat.c(Config.getInst().getPopup().getPopupSendAll()));
            return true;
        }else if(args[0].equalsIgnoreCase("send")){
            int time;
            try{
                time = Integer.parseInt(args[2]);
            }catch(NumberFormatException exc){
                sender.sendMessage(Chat.c("&c" + args[2] + " isn't a valid number!"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if(target == null){
                sender.sendMessage(Chat.c("&c" + args[1] + " isn't online!"));
                return true;
            }

            StringBuilder total = new StringBuilder();

            Iterator<String> argIter = Arrays.asList(args).iterator();

            argIter.next();
            argIter.next();
            argIter.next();

            while(argIter.hasNext()){
                total.append(argIter.next());
                if(argIter.hasNext())total.append(" ");
            }

            CosmicPlayer wrapped = CosmicPlayers.get().getCosmicPlayer(target);

            wrapped.notify(Chat.c(total.toString()), time);

            sender.sendMessage(Chat.cnp(Config.getInst().getPopup().getPopupSendOne(), "PLAYER", target.getName()));
            return true;
        }
        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender){
        sender.sendMessage(Chat.c("&cUsage of /popup:"));
        sender.sendMessage(Chat.c("&c/popup sendall (time) (message)"));
        sender.sendMessage(Chat.c("&c/popup send (player) (time) (message)"));
    }
}

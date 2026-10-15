package com.astropvp.cc.command;

import com.astropvp.cc.CCVerify;
import com.astropvp.cc.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        CCVerify.get().cmdReload();

        sender.sendMessage(Chat.c("&aReloaded the CCVerify plugin!"));
        return true;
    }
}

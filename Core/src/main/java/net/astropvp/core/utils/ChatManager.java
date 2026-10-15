package net.astropvp.core.utils;

import org.bukkit.ChatColor;

public class ChatManager {

    public static String translate(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}

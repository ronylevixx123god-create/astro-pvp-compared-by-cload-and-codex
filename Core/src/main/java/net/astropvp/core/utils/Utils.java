package net.astropvp.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class Utils {
    public void sendMessage(Player p, String msg) {
        p.sendMessage(ChatManager.translate(msg));
    }

    public void console(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatManager.translate(msg));
    }

    public String format(double amount) {
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(amount);
    }
}

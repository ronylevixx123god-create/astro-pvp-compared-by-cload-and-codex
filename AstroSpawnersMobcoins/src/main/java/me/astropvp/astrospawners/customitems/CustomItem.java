package me.astropvp.astrospawners.customitems;

import java.util.Map;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.google.common.collect.Maps;


public class CustomItem {
    private static Map<String, CustomItem> customItems = Maps.newHashMap();
    private String prefix;
    private String usage;

    public CustomItem(String prefix, String usage) {
        Bukkit.getLogger().info(CC.translate("&aProccessing Custom Item Command for [" + prefix + "]"));
        customItems.put(prefix, this);
        this.prefix = prefix;
        this.usage = usage;
    }

    public void proccessCommand(CommandSender sender, String[] args) {

    }

    public String getUnknownUsage() {
        return CC.RedB + "(!)" + CC.Red + " /givespawner " + prefix + " <player> " + this.usage;

    }

    public String getPrefix() {
        return this.prefix;
    }

    public static CustomItem getCustomItem(String prefix) {
        return customItems.get(prefix);
    }

    public static boolean isCustomItem(String prefix) {
        return customItems.containsKey(prefix);
    }

    public static Map<String, CustomItem> getCustomItems() {
        return customItems;
    }

}

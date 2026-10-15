package me.kingsavaqe.cagepvp.utils;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class TitleManager {
    private static final Class<?> PacketPlayOutTitle = NMSUtil.getNMSClass("PacketPlayOutTitle");
    private static final Class<?> IChatBaseComponent = NMSUtil.getNMSClass("IChatBaseComponent");

    public static void sendTitle(Player player, String titleText, String subtitleText, int fadeIn, int stay, int fadeOut) {
        try {
            assert PacketPlayOutTitle != null;
            Object enumTitle = PacketPlayOutTitle.getDeclaredClasses()[0].getField("TITLE").get((Object)null);
            assert IChatBaseComponent != null;
            Object titleChat = IChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + titleText + "\"}");
            Object enumSubtitle = PacketPlayOutTitle.getDeclaredClasses()[0].getField("SUBTITLE").get((Object)null);
            Object subtitleChat = IChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + subtitleText + "\"}");
            Object enumTimes = PacketPlayOutTitle.getDeclaredClasses()[0].getField("TIMES").get((Object)null);
            Constructor<?> titleConstructor = PacketPlayOutTitle.getConstructor(PacketPlayOutTitle.getDeclaredClasses()[0], IChatBaseComponent, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            Object titlePacket = titleConstructor.newInstance(enumTitle, titleChat, 0, 0, 0);
            Object subtitlePacket = titleConstructor.newInstance(enumSubtitle, subtitleChat, 0, 0, 0);
            Object timesPacket = titleConstructor.newInstance(enumTimes, null, fadeIn, stay, fadeOut);
            NMSUtil.sendPacket(player, titlePacket);
            NMSUtil.sendPacket(player, subtitlePacket);
            NMSUtil.sendPacket(player, timesPacket);
        } catch (Exception var15) {
            var15.printStackTrace();
        }

    }
}

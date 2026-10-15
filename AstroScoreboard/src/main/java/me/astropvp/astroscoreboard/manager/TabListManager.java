package me.astropvp.astroscoreboard.manager;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;

public class TabListManager {
	
	public static void sendTablist(Player p, String title, String subTitle) {
	   
	    IChatBaseComponent tabTitle = ChatSerializer.a("{\"text\":\"" + title + "\"}");
	    IChatBaseComponent tabSubTitle = ChatSerializer.a("{\"text\":\"" + subTitle + "\"}");

	    PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabTitle);
	   
	    try {
	       Field field = packet.getClass().getDeclaredField("b");
	       field.setAccessible(true);
	       field.set(packet, tabSubTitle);
	    } catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	       ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	    }
	}


    
}

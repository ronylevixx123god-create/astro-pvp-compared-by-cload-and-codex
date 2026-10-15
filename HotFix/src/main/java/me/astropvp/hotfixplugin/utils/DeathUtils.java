package me.astropvp.hotfixplugin.utils;

import com.cosmicpvp.cosmicutils.utils.ReflectionUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import net.minecraft.server.v1_8_R3.*;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class DeathUtils {

    public static void sendDeathEffect(Player died) throws NoSuchFieldException, IllegalAccessException {
        EntityPlayer player = ((CraftPlayer)died).getHandle();

        EntityPlayer fake = new EntityPlayer(player.server, (WorldServer)player.world, player.getProfile(), new PlayerInteractManager(player.world));
        fake.playerConnection = new PlayerConnection(player.server, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), fake) {
            public void sendPacket(Packet packet) {}

            public void c() {}

            public void chat(String s, boolean async) {}
        };
        fake.setPositionRotation(player.locX, player.locY, player.locZ, player.yaw, player.pitch);
        for (int i = 0; i < (fake.getEquipment()).length; i++)
            fake.setEquipment(i, null);
        int entityId = fake.getId();
        PacketPlayOutNamedEntitySpawn fakePlayer = new PacketPlayOutNamedEntitySpawn(fake);
        PacketPlayOutEntityStatus status = new PacketPlayOutEntityStatus(fake, (byte)3);
        Set<EntityPlayer> whoReceived = new HashSet<>();
        EntityTrackerEntry entry = (((WorldServer)player.world).getTracker()).trackedEntities.get(player.getId());
        if (entry != null) {
            Set<EntityPlayer> trackedPlayers = (Set<EntityPlayer>) ReflectionUtils.getObject(EntityTrackerEntry.class, entry, "trackedPlayers");
            for (EntityPlayer tracked : trackedPlayers) {
                if (tracked instanceof EntityPlayer) {
                    EntityPlayer pl = tracked;
                    if (pl.playerConnection == null || pl.playerConnection.isDisconnected())
                        continue;
                    pl.playerConnection.sendPacket(fakePlayer);
                    pl.playerConnection.sendPacket(status);
                    whoReceived.add(pl);
                }
            }
        }
        player.playerConnection.sendPacket(fakePlayer);
        player.playerConnection.sendPacket(status);
        whoReceived.add(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Enchantments.getInstance(), () -> {
            PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityId);
            for (EntityPlayer pl : whoReceived) {
                if (pl.playerConnection == null)
                    continue;
                pl.playerConnection.sendPacket(destroy);
            }
        },30L);
    }

    public static void sendLightning(Player p, Location l){
        Class<?> light = getNMSClass("EntityLightning");
        try {
            Constructor<?> constu =
                    light
                            .getConstructor(getNMSClass("World"),
                                    double.class, double.class,
                                    double.class, boolean.class, boolean.class);
            Object wh  = p.getWorld().getClass().getMethod("getHandle").invoke(p.getWorld());
            Object lighobj = constu.newInstance(wh, l.getX(), l.getY(), l.getZ(), false, false);

            Object obj =
                    getNMSClass("PacketPlayOutSpawnEntityWeather")
                            .getConstructor(getNMSClass("Entity")).newInstance(lighobj);

            sendPacket(p, obj);
            p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 100, 1);
        } catch (NoSuchMethodException | SecurityException |
                IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"))
                    .invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutWorldEvent
 *  org.bukkit.craftbukkit.v1_7_R4.CraftServer
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 */
package com.cosmicpvp.cosmicutils.utils;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public enum ParticleEffect {
    HUGE_EXPLOSION("hugeexplosion", 0),
    LARGE_EXPLODE("largeexplode", 1),
    FIREWORKS_SPARK("fireworksSpark", 2),
    BUBBLE("bubble", 3),
    SUSPEND("suspend", 4),
    DEPTH_SUSPEND("depthSuspend", 5),
    TOWN_AURA("townaura", 6),
    CRIT("crit", 7),
    MAGIC_CRIT("magicCrit", 8),
    MOB_SPELL("mobSpell", 9),
    MOB_SPELL_AMBIENT("mobSpellAmbient", 10),
    SPELL("spell", 11),
    INSTANT_SPELL("instantSpell", 12),
    WITCH_MAGIC("witchMagic", 13),
    NOTE("note", 14),
    PORTAL("portal", 15),
    ENCHANTMENT_TABLE("enchantmenttable", 16),
    EXPLODE("explode", 17),
    FLAME("flame", 18),
    LAVA("lava", 19),
    FOOTSTEP("footstep", 20),
    SPLASH("splash", 21),
    LARGE_SMOKE("largesmoke", 22),
    CLOUD("cloud", 23),
    RED_DUST("reddust", 24),
    SNOWBALL_POOF("snowballpoof", 25),
    DRIP_WATER("dripWater", 26),
    DRIP_LAVA("dripLava", 27),
    SNOW_SHOVEL("snowshovel", 28),
    SLIME("slime", 29),
    HEART("heart", 30),
    ANGRY_VILLAGER("angryVillager", 31),
    HAPPY_VILLAGER("happyVillager", 32),
    ICONCRACK("iconcrack", 33),
    TILECRACK("tilecrack", 34);

    private static final Map<String, ParticleEffect> NAME_MAP;
    private static final Map<Integer, ParticleEffect> ID_MAP;
    public static CopyOnWriteArrayList<ParticleEffectPacket> particlePacketQueue;
    public static CopyOnWriteArrayList<QueuedWorldEventPacket> sendPacketNearbyQueue;
    static Constructor<?> cachedConstructor;
    static Class<?> cachedPacketClass;
    private static Method getHandle;
    private static Method sendPacketMethod;
    private static Field cachedPlayerConnectionField;

    static {
        particlePacketQueue = new CopyOnWriteArrayList();
        sendPacketNearbyQueue = new CopyOnWriteArrayList();
        NAME_MAP = new HashMap<String, ParticleEffect>();
        ID_MAP = new HashMap<Integer, ParticleEffect>();
        for (ParticleEffect effect : ParticleEffect.values()) {
            NAME_MAP.put(effect.name, effect);
            ID_MAP.put(effect.id, effect);
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(CosmicUtils.get(), new Runnable() {
            double radius = Math.pow(48.0, 2.0);

            @Override
            public void run() {
                for (ParticleEffectPacket pep : particlePacketQueue) {
                    try {
                        Packet packet = pep.getPacket();
                        if (pep.getP() != null) {
                            ParticleEffect.sendPacket(pep.getP(), (Object) packet);
                            continue;
                        }
                        String worldName = pep.getL().getWorld().getName();
                        for (Map.Entry<String, Location> data : CosmicUtils.get().getPlayerLocations().entrySet()) {
                            Player pl = CosmicUtils.get().getAsyncPlayerMap().get(data.getKey());
                            Location loc = data.getValue();
                            if (pl == null || !pl.isValid() || !loc.getWorld().getName().equalsIgnoreCase(worldName) || !(loc.distanceSquared(pep.getL()) <= this.radius))
                                continue;
                            ParticleEffect.sendPacket(pl, (Object) packet);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                particlePacketQueue.clear();
            }
        }, 2L, 2L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(CosmicUtils.get(), () -> {
            for (QueuedWorldEventPacket data : sendPacketNearbyQueue) {
                Location l = data.getLocation();
                PacketPlayOutWorldEvent particles = data.getPacket();
                ((CraftServer) Bukkit.getServer()).getServer().getPlayerList().sendPacketNearby((double) l.getBlockX(), (double) l.getBlockY(), (double) l.getBlockZ(), 16.0, ((CraftWorld) l.getWorld()).getHandle().dimension, (Packet) particles);
            }
            sendPacketNearbyQueue.clear();
        }, 2L, 2L);
    }

    private String name;
    private int id;

    private ParticleEffect(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static ParticleEffect fromName(String name) {
        if (name == null) {
            return null;
        }
        for (Map.Entry<String, ParticleEffect> e : NAME_MAP.entrySet()) {
            if (!e.getKey().equalsIgnoreCase(name)) continue;
            return e.getValue();
        }
        return null;
    }

    public static ParticleEffect fromId(int id) {
        return ID_MAP.get(id);
    }

    public static void sendWorldEventPacket(PacketPlayOutWorldEvent e, Location l) {
        sendPacketNearbyQueue.add(new QueuedWorldEventPacket(e, l));
    }

    public static void sendToPlayer(ParticleEffect effect, Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) {
        particlePacketQueue.add(new ParticleEffectPacket(player, effect, location, offsetX, offsetY, offsetZ, speed, count));
    }

    public static void sendCachedPacketToLocation(Packet cached, Location location) {
        particlePacketQueue.add(new ParticleEffectPacket(null, location, cached));
    }

    public static void sendCachedPacketToLocation(Packet cached, Player player, Location location) {
        particlePacketQueue.add(new ParticleEffectPacket(player, location, cached));
    }

    public static void sendToLocation(ParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) {
        particlePacketQueue.add(new ParticleEffectPacket(null, effect, location, offsetX, offsetY, offsetZ, speed, count));
    }

    public static void sendCrackToPlayer(boolean icon, int id, byte data, Player player, Location location, float offsetX, float offsetY, float offsetZ, int count) throws Exception {
        Object packet = ParticleEffect.createCrackPacket(icon, id, data, location, offsetX, offsetY, offsetZ, count);
        ParticleEffect.sendPacket(player, packet);
    }

    public static void sendCrackToLocation(boolean icon, int id, byte data, Location location, float offsetX, float offsetY, float offsetZ, int count) throws Exception {
        Object packet = ParticleEffect.createCrackPacket(icon, id, data, location, offsetX, offsetY, offsetZ, count);
        for (Map.Entry<String, Location> entry : CosmicUtils.get().getPlayerLocations().entrySet()) {
            Player pl = CosmicUtils.get().getAsyncPlayerMap().get(entry.getKey());
            Location loc = entry.getValue();
            if (pl == null || !pl.isOnline()) continue;
            ParticleEffect.sendPacket(pl, packet);
        }
    }

    public static Object createPacket(ParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
        if (count <= 0) {
            count = 1;
        }
        Object packet = ParticleEffect.getPacket63WorldParticles();
        ParticleEffect.setValue(packet, "a", effect.name);
        ParticleEffect.setValue(packet, "b", Float.valueOf((float) location.getX()));
        ParticleEffect.setValue(packet, "c", Float.valueOf((float) location.getY()));
        ParticleEffect.setValue(packet, "d", Float.valueOf((float) location.getZ()));
        ParticleEffect.setValue(packet, "e", Float.valueOf(offsetX));
        ParticleEffect.setValue(packet, "f", Float.valueOf(offsetY));
        ParticleEffect.setValue(packet, "g", Float.valueOf(offsetZ));
        ParticleEffect.setValue(packet, "h", Float.valueOf(speed));
        ParticleEffect.setValue(packet, "i", count);
        return packet;
    }

    public static Object createCrackPacket(boolean icon, int id, byte data, Location location, float offsetX, float offsetY, float offsetZ, int count) throws Exception {
        if (count <= 0) {
            count = 1;
        }
        Object packet = ParticleEffect.getPacket63WorldParticles();
        String modifier = "iconcrack_" + id;
        if (!icon) {
            modifier = "tilecrack_" + id + "_" + data;
        }
        ParticleEffect.setValue(packet, "a", modifier);
        ParticleEffect.setValue(packet, "b", Float.valueOf((float) location.getX()));
        ParticleEffect.setValue(packet, "c", Float.valueOf((float) location.getY()));
        ParticleEffect.setValue(packet, "d", Float.valueOf((float) location.getZ()));
        ParticleEffect.setValue(packet, "e", Float.valueOf(offsetX));
        ParticleEffect.setValue(packet, "f", Float.valueOf(offsetY));
        ParticleEffect.setValue(packet, "g", Float.valueOf(offsetZ));
        ParticleEffect.setValue(packet, "h", Float.valueOf(0.1f));
        ParticleEffect.setValue(packet, "i", count);
        return packet;
    }

    public static void playEffect(ParticleEffect pe, Location l, float speed, int amount) {
        particlePacketQueue.add(new ParticleEffectPacket(pe, l, speed, amount));
    }

    private static void setValue(Object instance, String fieldName, Object value) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static Object getEntityPlayer(Player p) throws Exception {
        return (getHandle == null ? (getHandle = p.getClass().getMethod("getHandle", new Class[0])) : getHandle).invoke(p, new Object[0]);
    }

    private static String getPackageName() {
        return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    private static Object getPacket63WorldParticles() throws Exception {
        if (cachedPacketClass == null) {
            cachedPacketClass = ReflectionUtils.getNMSClass("PacketPlayOutWorldParticles");
        }
        if (cachedConstructor == null) {
            cachedConstructor = cachedPacketClass.getConstructor(new Class[0]);
        }
        return cachedConstructor.newInstance(new Object[0]);
    }

    private static void sendPacket(Player p, Object packet) throws Exception {
        Object eplayer = ParticleEffect.getEntityPlayer(p);
        Field playerConnectionField = cachedPlayerConnectionField == null ? (cachedPlayerConnectionField = eplayer.getClass().getField("playerConnection")) : cachedPlayerConnectionField;
        Object playerConnection = playerConnectionField.get(eplayer);
        if (sendPacketMethod == null) {
            for (Method m : playerConnection.getClass().getMethods()) {
                if (!m.getName().equalsIgnoreCase("sendPacket")) continue;
                sendPacketMethod = m;
                break;
            }
        }
        if (sendPacketMethod != null) {
            sendPacketMethod.invoke(playerConnection, packet);
        }
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}


/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmicduel.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

    private String name;
    private int id;
    private static final Map<String, ParticleEffect> NAME_MAP;
    private static final Map<Integer, ParticleEffect> ID_MAP;

    private ParticleEffect(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
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

    public static void sendToPlayer(ParticleEffect effect, Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
        Object packet = ParticleEffect.createPacket(effect, location, offsetX, offsetY, offsetZ, speed, count);
        ParticleEffect.sendPacket(player, packet);
    }

    public void sendToLocation(Location loc, float offset, float speed, int count) {
        try {
            ParticleEffect.sendToLocation(this, loc, offset, offset, offset, speed, count);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendToLocation(ParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
        Object packet = ParticleEffect.createPacket(effect, location, offsetX, offsetY, offsetZ, speed, count);
        double radius = 48.0;
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            Location loc;
            if (pl == null || !pl.isOnline() || !(loc = pl.getLocation()).getWorld().getName().equalsIgnoreCase(location.getWorld().getName()) || !loc.getWorld().equals((Object)location.getWorld()) || loc.distanceSquared(location) > Math.pow(48.0, 2.0)) continue;
            ParticleEffect.sendPacket(pl, packet);
        }
    }

    public static void sendCrackToPlayer(boolean icon, int id, byte data, Player player, Location location, float offsetX, float offsetY, float offsetZ, int count) throws Exception {
        Object packet = ParticleEffect.createCrackPacket(icon, id, data, location, offsetX, offsetY, offsetZ, count);
        ParticleEffect.sendPacket(player, packet);
    }

    public static Object createPacket(ParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
        if (count <= 0) {
            count = 1;
        }
        Object packet = ParticleEffect.getPacket63WorldParticles();
        ParticleEffect.setValue(packet, "a", effect.name);
        ParticleEffect.setValue(packet, "b", Float.valueOf((float)location.getX()));
        ParticleEffect.setValue(packet, "c", Float.valueOf((float)location.getY()));
        ParticleEffect.setValue(packet, "d", Float.valueOf((float)location.getZ()));
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
        ParticleEffect.setValue(packet, "b", Float.valueOf((float)location.getX()));
        ParticleEffect.setValue(packet, "c", Float.valueOf((float)location.getY()));
        ParticleEffect.setValue(packet, "d", Float.valueOf((float)location.getZ()));
        ParticleEffect.setValue(packet, "e", Float.valueOf(offsetX));
        ParticleEffect.setValue(packet, "f", Float.valueOf(offsetY));
        ParticleEffect.setValue(packet, "g", Float.valueOf(offsetZ));
        ParticleEffect.setValue(packet, "h", Float.valueOf(0.1f));
        ParticleEffect.setValue(packet, "i", count);
        return packet;
    }

    private static void setValue(Object instance, String fieldName, Object value) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static Object getEntityPlayer(Player p) throws Exception {
        Method getHandle = p.getClass().getMethod("getHandle", new Class[0]);
        return getHandle.invoke((Object)p, new Object[0]);
    }

    private static String getPackageName() {
        return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    private static Object getPacket63WorldParticles() throws Exception {
        Class<?> packet = Class.forName(ParticleEffect.getPackageName() + ".PacketPlayOutWorldParticles");
        for (Constructor<?> cst : packet.getConstructors()) {
            if (cst.getParameterTypes().length != 0) continue;
            return cst.newInstance(new Object[0]);
        }
        return packet.getConstructors()[0].newInstance(new Object[0]);
    }

    private static void sendPacket(Player p, Object packet) throws Exception {
        Object eplayer = ParticleEffect.getEntityPlayer(p);
        Field playerConnectionField = eplayer.getClass().getField("playerConnection");
        Object playerConnection = playerConnectionField.get(eplayer);
        for (Method m : playerConnection.getClass().getMethods()) {
            if (!m.getName().equalsIgnoreCase("sendPacket")) continue;
            m.invoke(playerConnection, packet);
            return;
        }
    }

    static {
        NAME_MAP = new HashMap<String, ParticleEffect>();
        ID_MAP = new HashMap<Integer, ParticleEffect>();
        for (ParticleEffect effect : ParticleEffect.values()) {
            NAME_MAP.put(effect.name, effect);
            ID_MAP.put(effect.id, effect);
        }
    }
}


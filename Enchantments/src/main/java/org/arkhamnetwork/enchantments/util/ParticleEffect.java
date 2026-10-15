package org.arkhamnetwork.enchantments.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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

    static {
        NAME_MAP = new HashMap<String, ParticleEffect>();
        ID_MAP = new HashMap<Integer, ParticleEffect>();
        for (final ParticleEffect effect : values()) {
            ParticleEffect.NAME_MAP.put(effect.name, effect);
            ParticleEffect.ID_MAP.put(effect.id, effect);
        }
    }

    private String name;
    private int id;

    private ParticleEffect(final String name, final int id) {
        this.name = name;
        this.id = id;
    }

    public static ParticleEffect fromName(final String name) {
        if (name == null) {
            return null;
        }
        for (final Map.Entry<String, ParticleEffect> e : ParticleEffect.NAME_MAP.entrySet()) {
            if (e.getKey().equalsIgnoreCase(name)) {
                return e.getValue();
            }
        }
        return null;
    }

    public static ParticleEffect fromId(final int id) {
        return ParticleEffect.ID_MAP.get(id);
    }
//
//    @Deprecated
//    public static void sendToPlayer(final ParticleEffect effect, final Player player, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int count) {
//        org.arkhamnetwork.Arkkit.libs.ParticleEffect.sendToPlayer(org.arkhamnetwork.Arkkit.libs.ParticleEffect.valueOf(effect.name()), player, location, offsetX, offsetY, offsetZ, speed, count);
//    }
//
//    @Deprecated
//    public static void sendToLocation(final ParticleEffect effect, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int count) {
//        org.arkhamnetwork.Arkkit.libs.ParticleEffect.sendToLocation(org.arkhamnetwork.Arkkit.libs.ParticleEffect.valueOf(effect.name()), location, offsetX, offsetY, offsetZ, speed, count);
//    }

    public static void sendCrackToPlayer(final boolean icon, final int id, final byte data, final Player player, final Location location, final float offsetX, final float offsetY, final float offsetZ, final int count) throws Exception {
        final Object packet = createCrackPacket(icon, id, data, location, offsetX, offsetY, offsetZ, count);
        sendPacket(player, packet);
    }

    public static void sendCrackToLocation(final boolean icon, final int id, final byte data, final Location location, final float offsetX, final float offsetY, final float offsetZ, final int count) throws Exception {
        final Object packet = createCrackPacket(icon, id, data, location, offsetX, offsetY, offsetZ, count);
        for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
            sendPacket(player, packet);
        }
    }

    public static Object createPacket(final ParticleEffect effect, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, int count) throws Exception {
        if (count <= 0) {
            count = 1;
        }
        final Object packet = getPacket63WorldParticles();
        setValue(packet, "a", effect.name);
        setValue(packet, "b", (float) location.getX());
        setValue(packet, "c", (float) location.getY());
        setValue(packet, "d", (float) location.getZ());
        setValue(packet, "e", offsetX);
        setValue(packet, "f", offsetY);
        setValue(packet, "g", offsetZ);
        setValue(packet, "h", speed);
        setValue(packet, "i", count);
        return packet;
    }

    public static Object createCrackPacket(final boolean icon, final int id, final byte data, final Location location, final float offsetX, final float offsetY, final float offsetZ, int count) throws Exception {
        if (count <= 0) {
            count = 1;
        }
        final Object packet = getPacket63WorldParticles();
        String modifier = "iconcrack_" + id;
        if (!icon) {
            modifier = "tilecrack_" + id + "_" + data;
        }
        setValue(packet, "a", modifier);
        setValue(packet, "b", (float) location.getX());
        setValue(packet, "c", (float) location.getY());
        setValue(packet, "d", (float) location.getZ());
        setValue(packet, "e", offsetX);
        setValue(packet, "f", offsetY);
        setValue(packet, "g", offsetZ);
        setValue(packet, "h", 0.1f);
        setValue(packet, "i", count);
        return packet;
    }

// todo oliver
//    @Deprecated
//    public static void playEffect(final ParticleEffect pe, final Location l, final float speed, final int amount) {
//        org.arkhamnetwork.Arkkit.libs.ParticleEffect.playEffect(org.arkhamnetwork.Arkkit.libs.ParticleEffect.valueOf(pe.name()), l, speed, amount);
//    }

    private static void setValue(final Object instance, final String fieldName, final Object value) throws Exception {
        final Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static Object getEntityPlayer(final Player p) throws Exception {
        final Method getHandle = p.getClass().getMethod("getHandle", (Class<?>[]) new Class[0]);
        return getHandle.invoke(p, new Object[0]);
    }

    private static String getPackageName() {
        return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    private static Object getPacket63WorldParticles() throws Exception {
        final Class<?> packet = Class.forName(getPackageName() + ".PacketPlayOutWorldParticles");
        for (final Constructor<?> cst : packet.getConstructors()) {
            if (cst.getParameterTypes().length == 0) {
                return cst.newInstance(new Object[0]);
            }
        }
        return packet.getConstructors()[0].newInstance(new Object[0]);
    }

    private static void sendPacket(final Player p, final Object packet) throws Exception {
        final Object eplayer = getEntityPlayer(p);
        final Field playerConnectionField = eplayer.getClass().getField("playerConnection");
        final Object playerConnection = playerConnectionField.get(eplayer);
        for (final Method m : playerConnection.getClass().getMethods()) {
            if (m.getName().equalsIgnoreCase("sendPacket")) {
                m.invoke(playerConnection, packet);
                return;
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}

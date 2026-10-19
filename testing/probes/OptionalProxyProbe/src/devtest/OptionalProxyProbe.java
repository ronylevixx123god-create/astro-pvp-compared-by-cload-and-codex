package devtest;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.PlayerList;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/** Disposable-server fixture. Exercises optional filtering, not a real client session. */
public class OptionalProxyProbe extends JavaPlugin {
    private EntityPlayer viewer, target;
    private final AtomicInteger customPayloads = new AtomicInteger();
    private List<String> proxyNames;
    private Plugin armor, client;

    private void check(boolean passed, String message) {
        if (!passed) throw new AssertionError(message);
        getLogger().info("PROBE PASS " + message);
    }
    @Override public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::run, 20L);
    }
    @SuppressWarnings("unchecked") private void run() {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        try {
            armor = Bukkit.getPluginManager().getPlugin("AstroArmorSets");
            client = Bukkit.getPluginManager().getPlugin("CCVerify");
            check(armor != null && armor.isEnabled(), "armor plugin enabled");
            check(client != null && client.isEnabled(), "client plugin enabled");
            // The original /near patch is opt-in; enable it explicitly in this disposable fixture.
            new org.arkhamnetwork.Arkkit.patches.NearLegacy(Bukkit.getPluginManager().getPlugin("Arkkit")).enable();
            WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
            viewer = fake(server, world, "bbbbbbbb-0000-0000-0000-000000000081", "ProxyViewer", 0);
            target = fake(server, world, "bbbbbbbb-0000-0000-0000-000000000082", "ProxyTarget", 3);
            Plugin proxy = Bukkit.getPluginManager().getPlugin("ProxyPatch");
            if (proxy == null) {
                exercise("absent", false);
            } else {
                check(proxy.isEnabled(), "actual ProxyPatch enabled");
                proxyNames = (List<String>)proxy.getClass().getField("playerNames").get(null);
                exercise("enabled ordinary", false);
                proxyNames.add(target.getName());
                exercise("enabled listed", true);
                Bukkit.getPluginManager().disablePlugin(proxy);
                exercise("disabled listed", false);
            }
            getLogger().info("PROBE RESULT optional-proxy passed");
        } catch (Throwable error) {
            getLogger().severe("PROBE FAIL " + error);
            error.printStackTrace();
        } finally {
            if (proxyNames != null) proxyNames.remove("ProxyTarget");
            for (EntityPlayer fixture : new EntityPlayer[]{target, viewer}) {
                if (fixture != null) {
                    if (fixture == target && com.astropvp.cc.packet.PacketIO.get() != null)
                        com.astropvp.cc.packet.PacketIO.get().removePlayer(fixture.getBukkitEntity());
                    online(server, fixture, false);
                }
            }
            Bukkit.shutdown();
        }
    }
    private void exercise(String phase, boolean excluded) throws Exception {
        Player player = target.getBukkitEntity();
        String nearby = nearbyPlayers();
        check(nearby.contains(player.getName()) != excluded, phase + " near filtering");
        Set<Integer> before = armorTasks();
        new net.astropvp.armorsets.listeners.ArmorSetApplyListener(
                (net.astropvp.armorsets.AstroArmorSets)armor).onJoin(new PlayerJoinEvent(player, ""));
        Set<Integer> scheduled = armorTasks();
        scheduled.removeAll(before);
        // Cancel only tasks created by this fixture; actual armor effects require working Core.
        for (int id : scheduled) Bukkit.getScheduler().cancelTask(id);
        check(scheduled.isEmpty() == excluded, phase + " armor refresh scheduling");
        com.astropvp.cc.packet.PacketIO io = com.astropvp.cc.packet.PacketIO.get();
        io.removePlayer(player);
        customPayloads.set(0);
        ((com.astropvp.cc.CCVerify)client).onJoin(new PlayerJoinEvent(player, ""));
        boolean wrapped = com.astropvp.cc.player.CosmicPlayers.get().getCosmicPlayer(player) != null;
        check(wrapped != excluded, phase + " client registration");
        check((customPayloads.get() > 0) != excluded, phase + " client REGISTER payload");
        io.removePlayer(player);
    }
    private Set<Integer> armorTasks() {
        Set<Integer> tasks = new HashSet<>();
        for (BukkitTask task : Bukkit.getScheduler().getPendingTasks())
            if (task.getOwner() == armor) tasks.add(task.getTaskId());
        return tasks;
    }
    private String nearbyPlayers() throws Exception {
        Plugin ark = Bukkit.getPluginManager().getPlugin("Arkkit");
        ClassLoader loader = ark.getClass().getClassLoader();
        Class<?> type = Class.forName("org.arkhamnetwork.Arkkit.patches.CommandNear", true, loader);
        Constructor<?> constructor = type.getDeclaredConstructor(String.class, String.class, String.class, List.class);
        constructor.setAccessible(true);
        Object command = constructor.newInstance("near", "", "", Collections.emptyList());
        com.earth2me.essentials.Essentials essentials = (com.earth2me.essentials.Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
        com.earth2me.essentials.User user = essentials.getUser(viewer.getBukkitEntity());
        Method local = type.getDeclaredMethod("getLocal", Server.class, com.earth2me.essentials.User.class, long.class);
        local.setAccessible(true);
        return (String)local.invoke(command, Bukkit.getServer(), user, 100L);
    }
    private EntityPlayer fake(MinecraftServer server, WorldServer world, String uuid, String name, int x) throws ReflectiveOperationException {
        EntityPlayer entity = new EntityPlayer(server, world, new GameProfile(UUID.fromString(uuid), name), new PlayerInteractManager(world));
        entity.setPosition(x, 100, 0);
        Field channel = PacketPlayOutCustomPayload.class.getDeclaredField("a");
        channel.setAccessible(true);
        entity.playerConnection = new PlayerConnection(server, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), entity) {
            @Override public void sendPacket(Packet packet) {
                if (name.equals("ProxyTarget") && packet instanceof PacketPlayOutCustomPayload) {
                    try {
                        // Count only synchronous channel registration, not async handshake traffic.
                        if ("REGISTER".equals(channel.get(packet))) customPayloads.incrementAndGet();
                    } catch (IllegalAccessException e) {throw new IllegalStateException(e);}
                }
            }
        };
        server.getPlayerList().players.add(entity);
        online(server, entity, true);
        return entity;
    }
    @SuppressWarnings("unchecked") private void online(MinecraftServer server, EntityPlayer entity, boolean add) {
        try {
            PlayerList list = server.getPlayerList();
            Field uuidField = PlayerList.class.getDeclaredField("j"), nameField = PlayerList.class.getDeclaredField("playersByName");
            uuidField.setAccessible(true);nameField.setAccessible(true);
            Map<UUID, EntityPlayer> byId = (Map<UUID, EntityPlayer>)uuidField.get(list);
            Map<String, EntityPlayer> byName = (Map<String, EntityPlayer>)nameField.get(list);
            if (add) {byId.put(entity.getUniqueID(), entity);byName.put(entity.getName(), entity);}
            else {list.players.remove(entity);byId.remove(entity.getUniqueID());byName.remove(entity.getName());}
        } catch (ReflectiveOperationException e) {throw new IllegalStateException(e);}
    }
}

package me.astropvp.hotfixplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import io.netty.util.internal.ThreadLocalRandom;
import me.astropvp.hotfixplugin.anvil.AnvilSpamListener;
import me.astropvp.hotfixplugin.anvil.AnvilSpamRunnable;
import me.astropvp.hotfixplugin.commands.BlockPacketsCommand;
import me.astropvp.hotfixplugin.commands.HasBeenDupedCommand;
import me.astropvp.hotfixplugin.commands.MoreCommand;
import me.astropvp.hotfixplugin.listeners.*;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class HotFixPlugin extends JavaPlugin {
    private static HotFixPlugin instance;
    public static final DecimalFormat df = new DecimalFormat("#.#");


    private ProtocolManager protocolManager;

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        System.out.println("[HotFix] HotFixPlugin has been Loaded!");
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new TeleportSafetyListener(), this);

        //Bukkit.getPluginManager().registerEvents(new EnderPearlFixes(), this);
        Bukkit.getPluginManager().registerEvents(new CommandPingListener(), this);
        Bukkit.getPluginManager().registerEvents(new AntiInvis(), this);
        Bukkit.getPluginManager().registerEvents(new AntiDoorHit(), this);
        Bukkit.getPluginManager().registerEvents(new AutoOP(), this);
        Bukkit.getPluginManager().registerEvents(new AnvilSpamListener(), this);
        Bukkit.getPluginManager().registerEvents(new DisableBows(), this);
        Bukkit.getPluginManager().registerEvents(new DisableTextOnSignListener(), this);
        //Bukkit.getPluginManager().registerEvents(new AntiPhase(), this);
        //Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new NoFly(), 20L, 1L);

        this.commandManager = new CommandManager(this);
        this.commandManager.register(new BlockPacketsCommand(this));
        this.commandManager.register(new MoreCommand());
        this.commandManager.register(new HasBeenDupedCommand());

        Bukkit.getScheduler().runTaskTimer(this, new AnvilSpamRunnable(), 5 * 20L, 5 * 20L);

        protocolManager = ProtocolLibrary.getProtocolManager();
        registerPacketListener();
    }

    @Override
    public void onDisable() {
        instance = null;
        protocolManager.removePacketListeners(this);
    }

    public static HotFixPlugin getInstance() {
        return instance;
    }

    public static int getPing(Player player) {
        /*int ping = ((CraftPlayer) player).getHandle().ping;
        return ping < 45 ? ping : ping -35;*/

        int ping = ((CraftPlayer) player).getHandle().ping;
        if (ping == 0) {
            ping = ThreadLocalRandom.current().nextInt(90, 150);
        } else if (ping > 80 && ping < 210) {
            //ping = ThreadLocalRandom.current().nextInt(40, 80);
            ping = ping - 25;

        } else if
        (ping > 210) {
            ping = ThreadLocalRandom.current().nextInt(100, 140);
        }
        return ping;
    }

    private void registerPacketListener() {
        PacketListener packetListener = new PacketAdapter(this, PacketType.Play.Client.getInstance()) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (blockedPlayers.contains(player)) {
                    event.setCancelled(true);
                }
            }
        };
        protocolManager.addPacketListener(packetListener);

    }



    private Set<Player> blockedPlayers = new HashSet<>();

    public boolean isPlayerBlocked(Player player) {
        return blockedPlayers.contains(player);
    }

    public void blockPlayerPackets(Player player) {
        blockedPlayers.add(player);
    }

    public void unblockPlayerPackets(Player player) {
        blockedPlayers.remove(player);
    }
}

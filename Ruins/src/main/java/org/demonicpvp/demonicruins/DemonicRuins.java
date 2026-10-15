package org.demonicpvp.demonicruins;

import co.aikar.commands.PaperCommandManager;
import com.astropvp.cc.packet.type.out.SSetEntityProperties;
import com.astropvp.cc.player.CosmicPlayer;
import com.astropvp.cc.player.CosmicPlayers;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.demonicpvp.demonicruins.command.RuinsCommand;
import org.demonicpvp.demonicruins.listener.MobListener;
import org.demonicpvp.demonicruins.manager.RuinsRewardManager;
import org.demonicpvp.demonicruins.mob.MobUtil;
import org.demonicpvp.demonicruins.task.BossSpawnTask;
import org.demonicpvp.demonicruins.task.MobSpawnTask;
import org.demonicpvp.demonicruins.task.NoFlyTask;

import static org.demonicpvp.demonicruins.listener.MobListener.RUINS_WORLD_NAME;
import static org.demonicpvp.demonicruins.mob.MobUtil.ENTITY_PROPERTIES_KEY;

public class DemonicRuins extends JavaPlugin implements Listener {
    private static DemonicRuins instance;
    private PaperCommandManager commandManager;
    private RuinsRewardManager rewardManager;

    private ProtocolManager protocolManager;
    private PacketListener packetListener;

    private Gson gson;

    @Override
    public void onEnable() {
        instance = this;
        this.gson = new Gson();

        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.packetListener = new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.ENTITY) {
            @Override
            public void onPacketSending(final PacketEvent event) {
                PacketContainer packet = event.getPacket().deepClone();
                CosmicPlayer cPlayer = CosmicPlayers.get().getCosmicPlayer(event.getPlayer().getUniqueId());
                int entityId = packet.getIntegers().read(0);
                event.getPlayer().getWorld().getEntities().stream().filter(e -> e.getEntityId() == entityId).findFirst().ifPresent(entity -> {
                    if (entity.hasMetadata(ENTITY_PROPERTIES_KEY)) {
                        if (!cPlayer.isOnCosmicClient()) return;
                        cPlayer.sendPacket(new SSetEntityProperties(entity.getEntityId(), DemonicRuins.this.gson.fromJson(entity.getMetadata(ENTITY_PROPERTIES_KEY).get(0).asString(), SSetEntityProperties.EntityProperties.class)));
                    }
                });
            }
        };

        this.protocolManager.addPacketListener(this.packetListener);

        this.rewardManager = new RuinsRewardManager(this);

        this.commandManager = new PaperCommandManager(this);
        //DecimateCommons.setupCommandManagerMessages(this.commandManager);

        this.commandManager.registerCommand(new RuinsCommand(this.rewardManager), true);
        new NoFlyTask().runTaskTimer(this, 20, 1);
        this.getServer().getPluginManager().registerEvents(new MobListener(), this);
        this.getServer().getPluginManager().registerEvents(this.rewardManager, this);
        this.getServer().getPluginManager().registerEvents(this, this);
        //new BossSpawnTask();
        new MobSpawnTask().runTaskTimer(this, 1L, 1L);
//        new ScoreboardTask().runTaskTimer(this, 20L, 20L);
    }

    @Override
    public void onDisable() {
        // despawn all ruin mobs
        Bukkit.getWorld(RUINS_WORLD_NAME).getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);

        this.commandManager.unregisterCommands();
        this.protocolManager.removePacketListener(this.packetListener);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (event.getPlayer().getWorld().getName().equals(RUINS_WORLD_NAME)) {
            CosmicPlayer cPlayer = CosmicPlayers.get().getCosmicPlayer(event.getPlayer().getUniqueId());
            Bukkit.getScheduler().runTaskLater(this, () -> {
                event.getPlayer().getWorld().getEntities().forEach(entity -> {
                    if (entity.hasMetadata(ENTITY_PROPERTIES_KEY)) {
                        if (!cPlayer.isOnCosmicClient()) return;
                        cPlayer.sendPacket(new SSetEntityProperties(entity.getEntityId(), this.gson.fromJson(entity.getMetadata(ENTITY_PROPERTIES_KEY).get(0).asString(), SSetEntityProperties.EntityProperties.class)));
                    }
                });
            }, 20L * 5L);
        }
    }

    public static DemonicRuins getInstance() {
        return instance;
    }
}

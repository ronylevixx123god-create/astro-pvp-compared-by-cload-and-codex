package com.compilex.CosmicDungeons.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.comphenix.protocol.events.PacketListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class EntityHider implements Listener {
    protected Table<Integer, Integer, Boolean> observerEntityMap = HashBasedTable.create();
    private static final PacketType[] ENTITY_PACKETS = new PacketType[]{PacketType.Play.Server.ENTITY_EQUIPMENT, PacketType.Play.Server.BED, PacketType.Play.Server.ANIMATION, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.COLLECT, PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.SPAWN_ENTITY_PAINTING, PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB, PacketType.Play.Server.ENTITY_VELOCITY, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.ENTITY_LOOK, PacketType.Play.Server.ENTITY_MOVE_LOOK, PacketType.Play.Server.ENTITY_MOVE_LOOK, PacketType.Play.Server.ENTITY_TELEPORT, PacketType.Play.Server.ENTITY_HEAD_ROTATION, PacketType.Play.Server.ENTITY_STATUS, PacketType.Play.Server.ATTACH_ENTITY, PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.ENTITY_EFFECT, PacketType.Play.Server.REMOVE_ENTITY_EFFECT, PacketType.Play.Server.BLOCK_BREAK_ANIMATION};
    private ProtocolManager manager;
    private Listener bukkitListener;
    private PacketAdapter protocolListener;
    protected final Policy policy;

    public EntityHider(Plugin plugin, Policy policy) {
        Preconditions.checkNotNull(plugin, "plugin cannot be NULL.");
        this.policy = policy;
        this.manager = ProtocolLibrary.getProtocolManager();
        this.bukkitListener = this.constructBukkit();
        plugin.getServer().getPluginManager().registerEvents(this.bukkitListener, plugin);
        this.protocolListener = this.constructProtocol(plugin);
        this.manager.addPacketListener(this.protocolListener);
    }

    protected boolean setVisibility(Player observer, int entityID, boolean visible) {
        switch (this.policy) {
            case BLACKLIST: {
                return !this.setMembership(observer, entityID, !visible);
            }
            case WHITELIST: {
                return this.setMembership(observer, entityID, visible);
            }
        }
        throw new IllegalArgumentException("Unknown policy: " + (Object)((Object)this.policy));
    }

    protected boolean setMembership(Player observer, int entityID, boolean member) {
        if (member) {
            return this.observerEntityMap.put(observer.getEntityId(), entityID, true) != null;
        }
        return this.observerEntityMap.remove((Object)observer.getEntityId(), (Object)entityID) != null;
    }

    protected boolean getMembership(Player observer, int entityID) {
        return this.observerEntityMap.contains((Object)observer.getEntityId(), (Object)entityID);
    }

    protected boolean isVisible(Player observer, int entityID) {
        boolean presence = this.getMembership(observer, entityID);
        return this.policy == Policy.WHITELIST ? presence : !presence;
    }

    protected void removeEntity(Entity entity, boolean destroyed) {
        int entityID = entity.getEntityId();
        for (Map maps : this.observerEntityMap.rowMap().values()) {
            maps.remove(entityID);
        }
    }

    protected void removePlayer(Player player) {
        this.observerEntityMap.rowMap().remove(player.getEntityId());
    }

    private Listener constructBukkit() {
        return new Listener(){

            @EventHandler
            public void onEntityDeath(EntityDeathEvent e) {
                EntityHider.this.removeEntity((Entity)e.getEntity(), true);
            }

            @EventHandler
            public void onChunkUnload(ChunkUnloadEvent e) {
                for (Entity entity : e.getChunk().getEntities()) {
                    EntityHider.this.removeEntity(entity, false);
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent e) {
                EntityHider.this.removePlayer(e.getPlayer());
            }
        };
    }

    private PacketAdapter constructProtocol(Plugin plugin) {
        return new PacketAdapter(plugin, ENTITY_PACKETS){

            public void onPacketSending(PacketEvent event) {
                int entityID = (Integer)event.getPacket().getIntegers().read(0);
                if (!EntityHider.this.isVisible(event.getPlayer(), entityID)) {
                    event.setCancelled(true);
                }
            }
        };
    }

    public final boolean toggleEntity(Player observer, Entity entity) {
        if (this.isVisible(observer, entity.getEntityId())) {
            return this.hideEntity(observer, entity);
        }
        return !this.showEntity(observer, entity);
    }

    public final boolean showEntity(Player observer, Entity entity) {
        validate(observer, entity);
        boolean hiddenBefore = !setVisibility(observer, entity.getEntityId(), true);
        if (manager != null && hiddenBefore) {
            manager.updateEntity(entity, Arrays.asList(observer));
        }
        return hiddenBefore;
    }

    public final boolean hideEntity(Player observer, Entity entity) {
        this.validate(observer, entity);
        boolean visibleBefore = this.setVisibility(observer, entity.getEntityId(), false);
        if (visibleBefore) {
            PacketContainer destroyEntity = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
            destroyEntity.getIntegerArrays().write(0, new int[]{entity.getEntityId()});
            try {
                this.manager.sendServerPacket(observer, destroyEntity);
            }
            catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot send server packet.", e);
            }
        }
        return visibleBefore;
    }

    public final boolean canSee(Player observer, Entity entity) {
        this.validate(observer, entity);
        return this.isVisible(observer, entity.getEntityId());
    }

    private void validate(Player observer, Entity entity) {
        Preconditions.checkNotNull((Object)observer, (Object)"observer cannot be NULL.");
        Preconditions.checkNotNull((Object)entity, (Object)"entity cannot be NULL.");
    }

    public Policy getPolicy() {
        return this.policy;
    }

    public void close() {
        if (this.manager != null) {
            HandlerList.unregisterAll((Listener)this.bukkitListener);
            this.manager.removePacketListener((PacketListener)this.protocolListener);
            this.manager = null;
        }
    }

    public static enum Policy {
        WHITELIST,
        BLACKLIST;

    }
}



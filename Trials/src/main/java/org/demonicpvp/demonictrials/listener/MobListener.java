package org.demonicpvp.demonictrials.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.google.gson.Gson;
import org.bukkit.event.Listener;

import org.demonicpvp.demonictrials.DemonicTrials;

public class MobListener implements Listener {
    private ProtocolManager protocolManager;
    private PacketListener packetListener;
    private Gson gson;

    public static final String ENTITY_PROPERTIES_KEY = "trialsEntityProperties";

    public MobListener(DemonicTrials plugin) {
        this.gson = new Gson();
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.packetListener = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.ENTITY) {
            @Override
            public void onPacketSending(final PacketEvent event) {
                PacketContainer packet = event.getPacket().deepClone();

                int entityId = packet.getIntegers().read(0);
                event.getPlayer().getWorld().getEntities().stream().filter(e -> e.getEntityId() == entityId).findFirst().ifPresent(entity -> {
                    if (entity.hasMetadata(ENTITY_PROPERTIES_KEY)) {
                        //CosmicPacketHandler.sendPacketToPlayer(event.getPlayer(), new SSetEntityProperties(entity.getEntityId(), MobListener.this.gson.fromJson(entity.getMetadata(ENTITY_PROPERTIES_KEY).get(0).asString(), SSetEntityProperties.EntityProperties.class)));
                    }
                });
            }
        };

        this.protocolManager.addPacketListener(this.packetListener);
    }
}

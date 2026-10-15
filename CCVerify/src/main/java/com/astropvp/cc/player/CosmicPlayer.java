package com.astropvp.cc.player;

import com.astropvp.cc.CCVerify;
import com.astropvp.cc.packet.PacketIO;
import com.astropvp.cc.packet.type.Packet;
import com.astropvp.cc.packet.type.in.PacketIn;
import com.astropvp.cc.packet.type.out.PacketOutHealthBar;
import com.astropvp.cc.packet.type.out.PacketOutNotification;
import com.astropvp.cc.voice.EnumChannel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CosmicPlayer {

    @Getter
    @Setter
    private volatile EnumChannel currentChannel;

    @Getter
    private Player player;

    @Setter
    private volatile boolean onCosmicClient;

    @Getter
    @Setter
    private long lastErrorSpeaking;

    @Getter
    @Setter
    private volatile List<CosmicPlayer> currentlySpeakingTo = new ArrayList<>();

    private volatile long lastSentHealthBarPacket = 0L;

    public void notify(String message, int displayTime) {
        if (!this.isOnCosmicClient() || !getPlayer().isOnline()) return;

        PacketIO.get().queuePacket(getPlayer(), new PacketOutNotification(message, displayTime));
    }

    public void sendHealthBar(LivingEntity who) {
        sendPacket((Packet) new PacketOutHealthBar((long) who.getHealth(), (long) who.getMaxHealth(), who.getName()));
        this.lastSentHealthBarPacket = System.currentTimeMillis();
        int ticksToExpire = 140;
        Bukkit.getScheduler().runTaskLater((Plugin) CCVerify.get(), () -> {
            if (!this.player.isOnline())
                return;
            long timeDifference = System.currentTimeMillis() - this.lastSentHealthBarPacket;
            int tickDifference = (int) (timeDifference / 50L);
            if (tickDifference >= ticksToExpire - 5)
                sendPacket((Packet) new PacketOutHealthBar(-1L, -1L, ""));
        }, ticksToExpire);
    }

    public void sendPacket(Packet packet) {
        if (!isOnCosmicClient() || !getPlayer().isOnline()) {
            return;
        }

        if (packet instanceof PacketIn) {
            throw new IllegalArgumentException("Can't send an inbound packet!");
        }

        PacketIO.get().queuePacket(getPlayer(), packet);
    }

    public boolean isOnCosmicClient() {
        List<Short> packets = PacketIO.receivedIDS.get(player.getUniqueId());
        return onCosmicClient && packets.contains((short)17) && packets.contains((short)11) && packets.contains((short)51) && packets.contains((short)7) && packets.contains((short)9);
    }

    public CosmicPlayer(Player player) {
        this.player = player;
    }

}

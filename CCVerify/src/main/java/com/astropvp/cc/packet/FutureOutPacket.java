package com.astropvp.cc.packet;

import com.astropvp.cc.packet.type.Packet;
import org.bukkit.entity.Player;

public class FutureOutPacket {
    private Packet packet;
    private Player recipient;
    public FutureOutPacket(Packet packet, Player recipient){
        this.packet = packet;
        this.recipient = recipient;
    }

    public Packet getPacket() {
        return packet;
    }

    public Player getRecipient() {
        return recipient;
    }
}

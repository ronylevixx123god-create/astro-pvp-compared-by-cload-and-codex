package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import com.astropvp.cc.packet.type.Packet;
import org.bukkit.entity.Player;

public interface PacketIn extends Packet {
    void handle(PacketHandler handler, Player sender);
}

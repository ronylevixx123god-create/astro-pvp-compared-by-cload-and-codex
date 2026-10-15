package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import org.bukkit.entity.Player;

public class PacketInSetClientStatus implements PacketIn{
    @Override
    public void handle(PacketHandler handler, Player sender) {

    }

    @Override
    public short getID() {
        return 9;
    }
}

package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import org.bukkit.entity.Player;

public class PacketInWorldNameRequest implements PacketIn{
    @Override
    public void handle(PacketHandler handler, Player sender) {
        handler.handle(this, sender);
    }

    @Override
    public short getID() {
        return 51;
    }
}

package com.astropvp.cc.packet.type.in;

import com.astropvp.cc.packet.handler.PacketHandler;
import com.google.gson.annotations.SerializedName;
import org.bukkit.entity.Player;

public class PacketInRequestPermission implements PacketIn{
    @Override
    public short getID() {
        return 11;
    }

    @SerializedName("permission")
    private String permission;
    public PacketInRequestPermission(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public void handle(PacketHandler handler, Player sender) {
        handler.handle(this, sender);
    }
}

package com.astropvp.cc.packet;

import org.bukkit.entity.Player;

public class FutureInPacket {
    private static int currentId;
    private byte[] data;
    private int myID = currentId++;
    private Player sender;
    public FutureInPacket(byte[] data, Player sender){
        this.data = data;
        this.sender = sender;
    }

    public int getMyID() {
        return myID;
    }

    public byte[] getData() {
        return data;
    }

    public Player getSender() {
        return sender;
    }
}

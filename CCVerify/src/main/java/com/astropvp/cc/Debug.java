package com.astropvp.cc;

import com.astropvp.cc.packet.PacketCommons;

public class Debug {

    public static void log(Object in){
        if(PacketCommons.DEBUG)System.out.println(in);
    }
}

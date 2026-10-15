package com.cosmicpvp.cosmicutils.nms;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class MinecraftServerUtils {
    public static int getCurrentServerTick() {
        return MinecraftServer.currentTick;
    }

    public static boolean isRunning() {
        return MinecraftServer.getServer().isRunning();
    }
}


package com.astropvp.cc.voice;

import com.astropvp.cc.CCVerify;
import org.bukkit.scheduler.BukkitRunnable;

public class VoiceTask extends BukkitRunnable {
    private static VoiceTask i;
    public static VoiceTask get(){return i;}

    public static void start(){
        i = new VoiceTask();

        i.runTaskTimerAsynchronously(CCVerify.get(), 0, 20);
    }

    @Override
    public void run() {
        ChannelUtil.recalculateGlobal();

        if(GlobalChannel.get().isLocked())return;

        ChannelUtil.recalculateProximity();

        ChannelUtil.recalculateFaction();
    }
}

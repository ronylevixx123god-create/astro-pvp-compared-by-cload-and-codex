package com.conaxgames.customskulls.tasks;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.impl.MobGrinder;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class MobGrinderBlazeChecker extends BukkitRunnable {


    @Override
    public void run() {
        final Map<? extends String, ? extends AbilityData> activeMobGrinders = CoreSkulls.instance.getMobGrinderManager().getActiveMobGrinders();
        for (Map.Entry<? extends String, ? extends AbilityData> mobGrinderBlock : activeMobGrinders.entrySet()) {
            if (mobGrinderBlock == null) continue;
            String uuid = mobGrinderBlock.getKey();
            AbilityData mobGrinder = mobGrinderBlock.getValue();
            if (mobGrinder == null) continue;
            if (uuid == null) continue;

            if (mobGrinder.getBlaze() == null) {
                CoreSkulls.instance.getMobGrinderManager().rescanBlaze(uuid);
                continue;
            }
            Entity blaze = mobGrinder.getBlaze();

            if (blaze.isDead() || !blaze.isValid()) {
                CoreSkulls.instance.getMobGrinderManager().rescanBlaze(uuid);
            }

        }
    }
}

package com.conaxgames.customskulls.tasks;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.impl.MobGrinder;
import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Map;

public class MobGrinderRunnable extends BukkitRunnable {
    int amountOfExpPerBlaze = 40;

    @Override
    public void run() {
        final Map<? extends String, ? extends AbilityData> grinderBlocks = CoreSkulls.instance.getMobGrinderManager().getActiveMobGrinders();

        for (Map.Entry<? extends String, ? extends AbilityData> mobGrinderBlock : grinderBlocks.entrySet()) {
            String uuid = mobGrinderBlock.getKey();
            AbilityData mobGrinder = mobGrinderBlock.getValue();

            if (!mobGrinder.isActive()) {
                mobGrinder.deActivate(uuid);
                continue;
            }

/*            if (mobGrinder.getBlaze() == null) {
                continue;
            }
            Entity blaze = mobGrinder.getBlaze();

            if (!blaze.isValid()) {
                mobGrinder.setBlaze(null);
                continue;
            }

            if (blaze.isDead()) {
                mobGrinder.setBlaze(null);
                continue;
            }


            if (CoreSkulls.mobStackerUtils.isStackedMob(blaze)) {
                CoreSkulls.mobStackerUtils.setStackSize(blaze, CoreSkulls.mobStackerUtils.getStackSize(blaze) - 1);
            }*/

            if (mobGrinder.getXp() + amountOfExpPerBlaze < Integer.MAX_VALUE) {
                mobGrinder.setXp(mobGrinder.getXp() + amountOfExpPerBlaze);
            }

            double chanceGodlyKey = 0.0015;
            double chanceLegKey = 0.01;
            double chanceAstroKey = 0.065;

            int level = mobGrinder.getLevel();
            if (level == 1) {
                chanceGodlyKey = 0.0075;
                chanceLegKey = 0.02;
                chanceAstroKey = 0.045;
            } else if (level == 2) {
                chanceGodlyKey = 0.0175;
                chanceLegKey = 0.0375;
                chanceAstroKey = 0.0575;
            } else if (level == 3) {
                chanceGodlyKey = 0.025;
                chanceLegKey = 0.045;
                chanceAstroKey = 0.065;
            }


            double chance = Math.random();
            if (chance < chanceGodlyKey) {
                int godlyKeys = mobGrinder.getCrateKeys().get(CrateType.GODLY);
                mobGrinder.getCrateKeys().put(CrateType.GRINDER, godlyKeys + 1);
            }
        }

    }
}

package com.conaxgames.customskulls.tasks;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.struct.MobGrinderManager;
import me.astropvp.cratekeys.enums.CrateType;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MobGrinderRewardTest {
    private void tick(Integer solar, Integer cosmic, double roll, int expected) {
        CoreSkulls previous = CoreSkulls.instance;
        try {
            CoreSkulls.instance = mock(CoreSkulls.class);
            MobGrinderManager manager = new MobGrinderManager();
            when(CoreSkulls.instance.getMobGrinderManager()).thenReturn(manager);
            AbilityData data = mock(AbilityData.class, CALLS_REAL_METHODS);
            data.setCrateKeys(new java.util.HashMap<CrateType, Integer>());
            data.setLastActivated(System.currentTimeMillis());
            if (solar != null) data.getCrateKeys().put(CrateType.SOLAR, solar);
            if (cosmic != null) data.getCrateKeys().put(CrateType.COSMIC, cosmic);
            manager.getActiveMobGrinders().put("fixture", data);
            new MobGrinderRunnable(() -> roll).run();
            assertEquals(expected, (int) data.getCrateKeys().getOrDefault(CrateType.SOLAR, 0));
            assertEquals(cosmic, data.getCrateKeys().get(CrateType.COSMIC));
            assertEquals(40, data.getXp(), 0);
        } finally { CoreSkulls.instance = previous; }
    }
    @Test public void firstRewardNeedsNoCosmicEntry() { tick(null, null, 0, 1); }
    @Test public void rewardAddsToSolarWithoutOverwritingFromCosmic() { tick(41, 900, 0, 42); }
    @Test public void unsuccessfulRollOnlyAddsXp() { tick(41, 900, 1, 41); }
}

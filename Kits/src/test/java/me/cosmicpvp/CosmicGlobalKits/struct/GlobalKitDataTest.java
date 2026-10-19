package me.cosmicpvp.CosmicGlobalKits.struct;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GlobalKitDataTest {
    @Test
    public void constructorKeepsLastUsedAndTier() {
        GlobalKitData data = new GlobalKitData(1234L, 2);
        assertEquals(1234L, data.getLastUsed());
        assertEquals(2, data.getKitTier());
    }

    @Test
    public void tierLoadedFromDatabaseCanBeSet() {
        GlobalKitData data = new GlobalKitData(0L, 0);
        data.setKitTier(3);
        assertEquals(3, data.getKitTier());
    }
}

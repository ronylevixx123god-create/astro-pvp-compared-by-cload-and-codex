package me.venir.astrocollectors.collector;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.bukkit.World;
import org.json.simple.*;
import com.gmail.filoghost.holographicdisplays.api.*;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import net.astropvp.customblocks.CustomBlocks;
import net.astropvp.customblocks.CustomBlocksAPI;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CollectorPersistenceTest {
    private void roundTrip(Number lootboxes, long expected) {
        World world = mock(World.class);
        when(world.getName()).thenReturn("fixture");
        JSONObject data = new JSONObject();
        JSONArray pos = new JSONArray(); pos.add(1); pos.add(64); pos.add(2);
        data.put("Location", pos); data.put("spawners", new JSONArray());
        data.put("lastUpdated", 1L); data.put("worth", 12.5); data.put("tnt", 5L);
        if (lootboxes != null) data.put("lootboxes", lootboxes);
        try (MockedStatic<CustomBlocks> bootstrap = mockStatic(CustomBlocks.class)) {
            bootstrap.when(CustomBlocks::getInstance).thenReturn(mock(CustomBlocks.class, RETURNS_DEEP_STUBS));
            try (MockedStatic<CosmicMysteryChest> mystery = mockStatic(CosmicMysteryChest.class);
                 MockedStatic<HologramsAPI> holograms = mockStatic(HologramsAPI.class);
                 MockedStatic<CustomBlocksAPI> blocks = mockStatic(CustomBlocksAPI.class)) {
                mystery.when(CosmicMysteryChest::get).thenReturn(mock(CosmicMysteryChest.class, RETURNS_DEEP_STUBS));
                holograms.when(() -> HologramsAPI.createHologram(any(), any())).thenReturn(mock(Hologram.class));
                Collector loaded = new Collector(world, data);
                Collector reloaded = new Collector(world, loaded.toJsonObject());
                assertEquals(expected, loaded.getLootboxes());
                assertEquals(expected, reloaded.getLootboxes());
                assertEquals(5, reloaded.getTnt());
                assertEquals(12.5, reloaded.getWorth(), 0);
                assertEquals(1, reloaded.getLastUpdated());
            }
        }
    }
    @Test public void savedLootboxesSurviveRepeatedRoundTrip() { roundTrip(7L, 7); }
    @Test public void legacyFileWithoutLootboxesDefaultsToZero() { roundTrip(null, 0); }
    @Test public void integerJsonNumbersAreAccepted() { roundTrip(Integer.valueOf(7), 7); }
}

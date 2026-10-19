package me.cosmicpvp.CosmicGlobalKits;

import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitData;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitUser;
import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Read-only gkit tier lookups restored from Astro's pre-2022-04-07 code. */
public class CosmicGlobalKitsAPITierTest {
    private final UUID uuid = UUID.randomUUID();
    private CosmicGlobalKits plugin;
    private Player player;

    @Before
    public void setUp() throws Exception {
        plugin = mock(CosmicGlobalKits.class);
        setStatic(CosmicGlobalKits.class, "plugin", plugin);
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(uuid);
    }

    @After
    public void tearDown() throws Exception {
        setStatic(CosmicGlobalKits.class, "plugin", null);
    }

    private GlobalKitUser loadedUser() throws Exception {
        GlobalKitUser user = new GlobalKitUser(uuid);
        Field loading = GlobalKitUser.class.getDeclaredField("loadingData");
        loading.setAccessible(true);
        loading.setBoolean(user, false);
        when(plugin.getGlobalKitUser(uuid)).thenReturn(user);
        return user;
    }

    private static void setStatic(Class<?> type, String name, Object value) throws Exception {
        Field f = type.getDeclaredField(name);
        f.setAccessible(true);
        f.set(null, value);
    }

    @Test
    public void tierIsReadFromLoadedData() throws Exception {
        loadedUser().getGlobalKits().put(GlobalKits.HYPER, new GlobalKitData(0L, 2));
        assertEquals(2, CosmicGlobalKitsAPI.getKitTier(player, GlobalKits.HYPER));
    }

    @Test
    public void tierIsZeroWithoutDataOrUser() throws Exception {
        assertEquals(0, CosmicGlobalKitsAPI.getKitTier(player, GlobalKits.HYPER));
        loadedUser();
        assertEquals(0, CosmicGlobalKitsAPI.getKitTier(player, GlobalKits.HYPER));
    }

    @Test
    public void canLevelUpBelowTierThree() throws Exception {
        GlobalKitUser user = loadedUser();
        assertTrue("no data yet", CosmicGlobalKitsAPI.canLevelUpGkit(player, GlobalKits.HYPER));
        user.getGlobalKits().put(GlobalKits.HYPER, new GlobalKitData(0L, 2));
        assertTrue("tier 2", CosmicGlobalKitsAPI.canLevelUpGkit(player, GlobalKits.HYPER));
    }

    @Test
    public void maxLevelAtTierThree() throws Exception {
        loadedUser().getGlobalKits().put(GlobalKits.HYPER, new GlobalKitData(0L, 3));
        assertFalse(CosmicGlobalKitsAPI.canLevelUpGkit(player, GlobalKits.HYPER));
    }

    @Test
    public void cannotLevelUpWithoutUserOrWhileLoading() throws Exception {
        assertFalse("no user", CosmicGlobalKitsAPI.canLevelUpGkit(player, GlobalKits.HYPER));
        when(plugin.getGlobalKitUser(uuid)).thenReturn(new GlobalKitUser(uuid));
        assertFalse("still loading", CosmicGlobalKitsAPI.canLevelUpGkit(player, GlobalKits.HYPER));
    }

    @Test
    public void lookupsDoNotChangeTheTier() throws Exception {
        GlobalKitData data = new GlobalKitData(0L, 1);
        loadedUser().getGlobalKits().put(GlobalKits.HYPER, data);
        CosmicGlobalKitsAPI.getKitTier(player, GlobalKits.HYPER);
        CosmicGlobalKitsAPI.canLevelUpGkit(player, GlobalKits.HYPER);
        assertEquals(1, data.getKitTier());
    }
}

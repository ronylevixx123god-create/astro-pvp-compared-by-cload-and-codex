package com.conaxgames.customskulls.data;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.enums.AbilityType;
import com.conaxgames.customskulls.struct.MobGrinderManager;
import me.astropvp.cratekeys.enums.CrateType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Server;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.mockito.MockedStatic;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GrinderPersistenceTest {
    @Rule public TemporaryFolder temporary = new TemporaryFolder();
    private CoreSkulls previous;
    private World world;
    private MockedStatic<Bukkit> bukkit;
    private Map<String, AbilityData> previousData;
    private Field dataField;
    private File directory;
    private MobGrinderManager manager;

    @Before public void setup() throws Exception {
        world = mock(World.class);
        when(world.getName()).thenReturn("fixture");
        bukkit = mockStatic(Bukkit.class);
        bukkit.when(() -> Bukkit.getWorld("fixture")).thenReturn(world);
        previous = CoreSkulls.instance;
        dataField = AbilityData.class.getDeclaredField("abilityDataMap");
        dataField.setAccessible(true);
        previousData = AbilityData.getAbilityDataMap();
        dataField.set(null, new LinkedHashMap<String, AbilityData>());
        directory = temporary.newFolder("plugin");
        assertTrue(new File(directory, "data").mkdir());
        CoreSkulls.instance = mock(CoreSkulls.class);
        when(CoreSkulls.instance.getDataFolder()).thenReturn(directory);
        Server server = mock(Server.class);
        when(CoreSkulls.instance.getServer()).thenReturn(server);
        when(server.getWorld("fixture")).thenReturn(world);
        Block fixtureBlock = mock(Block.class);
        when(fixtureBlock.getType()).thenReturn(Material.NOTE_BLOCK);
        when(world.getBlockAt(any(Location.class))).thenReturn(fixtureBlock);
        manager = mock(MobGrinderManager.class);
        when(CoreSkulls.instance.getMobGrinderManager()).thenReturn(manager);
    }

    @After public void cleanup() throws Exception {
        bukkit.close();
        CoreSkulls.instance = previous;
        dataField.set(null, previousData);
        CoreSkulls.setSavingGrinderBlocks(false);
    }

    private AbilityData grinder(int x, boolean fileExists) throws Exception {
        AbilityData data = new AbilityData(AbilityData.getUUID(x, 64, 1));
        data.setLocation(new Location(world, x, 64, 1));
        data.setAbilityType(AbilityType.BLAZE_STONE_TOTEM);
        data.getCrateKeys().put(CrateType.SOLAR, 37);
        data.getCrateKeys().put(CrateType.COSMIC, 900);
        data.setXp(12.5);
        when(manager.get(data.getUuid())).thenReturn(data);
        if (fileExists) {
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.set("owner", "keep-existing-owner");
            yaml.save(file(data));
        }
        return data;
    }

    private File file(AbilityData data) {
        return new File(directory, "data/" + data.getUuid() + ".yml");
    }

    @Test public void bulkSaveUsesSameCounterAsSingleSaveAndPreservesOtherData() throws Exception {
        AbilityData data = grinder(1, true);
        Player player = mock(Player.class);
        UUID owner = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(owner);
        Block block = mock(Block.class);
        when(block.getLocation()).thenReturn(data.getLocation());
        SaveCustomBlock.saveCustomBlock(player, AbilityType.BLAZE_STONE_TOTEM, block);
        YamlConfiguration single = YamlConfiguration.loadConfiguration(file(data));
        assertEquals(37, single.getInt("grinder"));
        SaveAllGrinderCustomBlocks.saveAllGrinderCustomBlocks();
        YamlConfiguration bulk = YamlConfiguration.loadConfiguration(file(data));
        assertEquals(single.getInt("grinder"), bulk.getInt("grinder"));
        assertEquals(12.5, bulk.getDouble("xp"), 0);
        assertEquals(owner.toString(), bulk.getString("owner"));
        assertEquals(Integer.valueOf(900), data.getCrateKeys().get(CrateType.COSMIC));
        assertFalse(CoreSkulls.isSavingGrinderBlocks());
        data.setXp(0);
        data.getCrateKeys().put(CrateType.SOLAR, 0);
        LoadCustomBlocks.load();
        assertEquals(37, (int) data.getCrateKeys().get(CrateType.SOLAR));
        assertEquals(12.5, data.getXp(), 0);
    }

    @Test public void missingFileDoesNotSkipRemainingGrindersOrLeaveSavingFlagSet() throws Exception {
        grinder(1, false);
        AbilityData existing = grinder(2, true);
        SaveAllGrinderCustomBlocks.saveAllGrinderCustomBlocks();
        assertEquals(37, YamlConfiguration.loadConfiguration(file(existing)).getInt("grinder"));
        assertFalse(CoreSkulls.isSavingGrinderBlocks());
    }

    @Test public void unexpectedFailureStillReleasesSavingFlag() throws Exception {
        AbilityData data = grinder(1, true);
        when(manager.get(data.getUuid())).thenThrow(new IllegalStateException("broken fixture"));
        try {
            SaveAllGrinderCustomBlocks.saveAllGrinderCustomBlocks();
            fail("exception should remain visible");
        } catch (IllegalStateException expected) {
            assertFalse(CoreSkulls.isSavingGrinderBlocks());
        }
    }
}

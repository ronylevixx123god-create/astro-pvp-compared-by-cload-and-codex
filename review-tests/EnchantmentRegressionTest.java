import com.cosmicpvp.cosmicutils.utils.RegionFileCloser;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.enchantments.armor.RocketEscape;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicArmored;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicRocketEscape;
import org.arkhamnetwork.enchantments.enchantments.heroic.HeroicImplants;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnchantmentRegressionTest {
    private Enchantments plugin;
    private BukkitScheduler scheduler;
    private static void set(Object object, Class<?> owner, String name, Object value) throws Exception {
        Field field = owner.getDeclaredField(name); field.setAccessible(true); field.set(object, value);
    }
    @Before public void setup() throws Exception {
        Server server = mock(Server.class); scheduler = mock(BukkitScheduler.class);
        when(server.getScheduler()).thenReturn(scheduler);
        when(server.getPluginManager()).thenReturn(mock(org.bukkit.plugin.PluginManager.class));
        set(null, Bukkit.class, "server", server);
        plugin = mock(Enchantments.class);
        set(plugin, JavaPlugin.class, "isEnabled", true);
        set(null, Enchantments.class, "instance", plugin);
    }
    private CustomEnchantment registered() throws Exception {
        CustomEnchantment enchant = mock(CustomEnchantment.class);
        when(enchant.name()).thenReturn("Regression " + UUID.randomUUID());
        when(enchant.isEnabled()).thenReturn(true);
        set(plugin, Enchantments.class, "registeredEnchantments", new CustomEnchantment[]{enchant});
        doCallRealMethod().when(plugin).registerEnchantments();
        return enchant;
    }
    @Test public void reloadRestoresTheSameEnchantWithoutDuplicateListeners() throws Exception {
        CustomEnchantment enchant = registered();
        plugin.registerEnchantments(); assertSame(enchant, EnchantmentAPI.getEnchantment(enchant.name()));
        EnchantmentAPI.unregisterCustomEnchantment(enchant.name());
        plugin.registerEnchantments(); assertSame(enchant, EnchantmentAPI.getEnchantment(enchant.name()));
        plugin.registerEnchantments(); assertSame(enchant, EnchantmentAPI.getEnchantment(enchant.name()));
        verifyNoInteractions(scheduler);
        EnchantmentAPI.unregisterCustomEnchantment(enchant.name());
    }
    @Test public void disabledPluginIsNotRegisteredDuringApiStartup() throws Exception {
        CustomEnchantment enchant = registered(); set(plugin, JavaPlugin.class, "isEnabled", false);
        plugin.registerEnchantments(); assertNull(EnchantmentAPI.getEnchantment(enchant.name()));
        verifyNoInteractions(scheduler);
    }
    @Test public void restoredDefenseEnchantmentsOverrideTheActualApiHook() throws Exception {
        for (Class<?> type : new Class<?>[]{RocketEscape.class, HeroicRocketEscape.class, HeroicArmored.class}) {
            assertEquals(type, type.getMethod("applyDefenseEffect", LivingEntity.class, LivingEntity.class,
                    int.class, EntityDamageEvent.class).getDeclaringClass());
        }
    }
    public abstract static class TestPlayer implements Player {
        @Override public abstract double getHealth();
        @Override public abstract double getMaxHealth();
    }
    @Test public void alienImplantsRegeneratesWhileEquippedAndStopsOnUnequip() {
        BukkitTask task = mock(BukkitTask.class);
        when(scheduler.runTaskTimer(eq(plugin), any(Runnable.class), anyLong(), anyLong())).thenReturn(task);
        HeroicImplants enchant = new HeroicImplants();
        Player player = mock(TestPlayer.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getHealth()).thenReturn(18.0); when(player.getMaxHealth()).thenReturn(20.0);
        when(player.getFoodLevel()).thenReturn(19);
        enchant.applyEquipEffect(player, 3);
        ArgumentCaptor<Runnable> timers = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler, times(3)).runTaskTimer(eq(plugin), timers.capture(), anyLong(), anyLong());
        Runnable tick = timers.getAllValues().get(2);
        tick.run(); tick.run();
        verify(player).setHealth(19.0); verify(player, times(2)).setFoodLevel(20);
        enchant.applyUnequipEffect(player, 3); clearInvocations(player);
        tick.run(); verify(player, never()).setHealth(anyDouble()); verify(player, never()).setFoodLevel(anyInt());
    }
    public static class WineRegion { boolean closed; public void closeFile() { closed = true; } }
    public static class OriginalRegion { boolean closed; public void c() { closed = true; } }
    public static class FailingRegion { final IOException error = new IOException("simulated"); public void c() throws IOException { throw error; } }
    @Test public void wineRegionFilesStillClose() throws Exception {
        WineRegion region = new WineRegion(); RegionFileCloser.close(region); assertTrue(region.closed);
    }
    @Test public void originalRegionFilesClose() throws Exception {
        OriginalRegion region = new OriginalRegion(); RegionFileCloser.close(region); assertTrue(region.closed);
    }
    @Test public void closePreservesOriginalIoFailures() throws Exception {
        FailingRegion region = new FailingRegion();
        try { RegionFileCloser.close(region); fail("Expected close failure"); }
        catch (IOException expected) { assertSame(region.error, expected); }
    }
    @Test(expected = IOException.class) public void unknownRegionApiIsReported() throws Exception {
        RegionFileCloser.close(new Object());
    }
}

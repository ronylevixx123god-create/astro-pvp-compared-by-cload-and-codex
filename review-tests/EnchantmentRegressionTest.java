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
    @Test public void originallyInactiveDefenseHooksRemainInactive() throws Exception {
        CustomEnchantment[] enchants = {new RocketEscape(), new HeroicRocketEscape(), new HeroicArmored()};
        Player defender = mock(Player.class), attacker = mock(Player.class);
        for (CustomEnchantment enchant : enchants) {
            assertEquals(CustomEnchantment.class, enchant.getClass().getMethod("applyDefenseEffect",
                    LivingEntity.class, LivingEntity.class, int.class, EntityDamageEvent.class).getDeclaringClass());
            EntityDamageEvent event = new org.bukkit.event.entity.EntityDamageByEntityEvent(attacker, defender,
                    EntityDamageEvent.DamageCause.ENTITY_ATTACK, 20);
            enchant.applyDefenseEffect(defender, attacker, 3, event);
            assertEquals(20, event.getDamage(), 0);
        }
        verifyNoInteractions(defender, attacker);
    }
    @Test public void alienImplantsRetainsOriginalInactiveEquipBehavior() {
        HeroicImplants enchant = new HeroicImplants();
        Player player = mock(Player.class);
        enchant.applyEquipEffect(player, 3);
        enchant.applyUnequipEffect(player, 3);
        verifyNoInteractions(player);
        verify(scheduler, never()).runTaskTimer(any(Plugin.class), any(Runnable.class), anyLong(), anyLong());
    }
    @Test public void commentedOutArmorDurabilityEffectsRemainInactive() {
        Player player = mock(Player.class);
        org.bukkit.inventory.EntityEquipment equipment = mock(org.bukkit.inventory.EntityEquipment.class);
        org.bukkit.inventory.PlayerInventory inventory = mock(org.bukkit.inventory.PlayerInventory.class);
        org.bukkit.inventory.ItemStack armor = new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_CHESTPLATE, 1, (short) 100);
        org.bukkit.inventory.ItemStack[] pieces = new org.bukkit.inventory.ItemStack[4];
        pieces[org.arkhamnetwork.enchantments.util.ArmorSlot.CHESTPLATE.getIndex()] = armor;
        when(player.getEquipment()).thenReturn(equipment);
        when(equipment.getArmorContents()).thenReturn(pieces);
        when(player.getInventory()).thenReturn(inventory);
        org.arkhamnetwork.enchantments.util.ArmorUtil.modifyPlayerArmor(player,
                org.arkhamnetwork.enchantments.util.ArmorSlot.CHESTPLATE, -15, false);
        assertEquals(100, armor.getDurability());
        org.arkhamnetwork.enchantments.util.ArmorUtil.modifyPlayerArmor(player,
                org.arkhamnetwork.enchantments.util.ArmorSlot.CHESTPLATE, 15, false);
        assertEquals(100, armor.getDurability());
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

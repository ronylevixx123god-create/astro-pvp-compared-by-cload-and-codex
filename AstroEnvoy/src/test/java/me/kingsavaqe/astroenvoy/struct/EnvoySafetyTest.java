package me.kingsavaqe.astroenvoy.struct;

import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import me.kingsavaqe.astroenvoy.listeners.OpenEnvoyCrateListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.Test;
import org.mockito.MockedStatic;
import java.util.*;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnvoySafetyTest {
    private Location location(World world, int x, Material material) {
        Location l = new Location(world, x, 64, 0);
        Block block = mock(Block.class);
        when(block.getType()).thenReturn(material);
        when(world.getBlockAt(l)).thenReturn(block);
        return l;
    }

    @Test public void exactCapacitySelectsEveryLocationIncludingLast() {
        World world = mock(World.class);
        List<Location> saved = new ArrayList<>();
        for (int i = 0; i < 7; i++) saved.add(location(world, i, Material.AIR));
        List<Location> selected = EnvoyManager.selectLocations(saved, 7);
        assertEquals(new HashSet<>(saved), new HashSet<>(selected));
        assertEquals(7, selected.size());
        assertEquals(7, saved.size());
    }

    @Test public void unavailableDuplicateAndOccupiedLocationsCannotBecomeCrates() {
        World world = mock(World.class);
        Location empty = location(world, 1, Material.AIR);
        List<Location> saved = Arrays.asList(null, new Location(null, 0, 64, 0), empty,
                empty.clone(), location(world, 2, Material.CHEST));
        assertTrue(EnvoyManager.selectLocations(saved, 2).isEmpty());
        assertEquals(Collections.singletonList(empty), EnvoyManager.selectLocations(saved, 1));
    }

    @Test public void failedStartDoesNotActivateOrReplaceExistingEvent() {
        EnvoyPlugin plugin = mock(EnvoyPlugin.class);
        when(plugin.getLogger()).thenReturn(Logger.getAnonymousLogger());
        EnvoyManager manager = new EnvoyManager(plugin);
        long before = manager.getLastEnvoyTime();
        assertFalse(manager.tryStartEnvoyEvent(0));
        assertFalse(manager.isActive());
        manager.setActive(true);
        assertFalse(manager.tryStartEnvoyEvent(0));
        assertTrue(manager.isActive());
        assertEquals(before, manager.getLastEnvoyTime());
    }

    @Test public void cleanupRemovesOnlySpawnedChestsWithoutConcurrentModification() {
        EnvoyManager manager = spy(new EnvoyManager(mock(EnvoyPlugin.class)));
        World world = mock(World.class);
        Location spawned = location(world, 1, Material.CHEST);
        Location untouched = location(world, 2, Material.CHEST);
        manager.setLocations(Arrays.asList(spawned, untouched));
        manager.setCurrentChests(new ArrayList<>(Collections.singletonList(spawned)));
        manager.setRemainingCrates(1);
        doAnswer(call -> {manager.getCurrentChests().remove(call.getArgument(0)); return null;})
                .when(manager).removeCrateAt(spawned);
        manager.handleCrateRemoving();
        verify(manager).removeCrateAt(spawned);
        verify(manager, never()).removeCrateAt(untouched);
        assertTrue(manager.getCurrentChests().isEmpty());
        assertEquals(0, manager.getRemainingCrates());
    }

    @Test public void configuredChestOutsideEventCannotBeClaimedForRewards() {
        EnvoyPlugin plugin = mock(EnvoyPlugin.class);
        EnvoyManager manager = spy(new EnvoyManager(plugin));
        when(plugin.getEnvoyManager()).thenReturn(manager);
        World world = mock(World.class);
        Location configured = location(world, 1, Material.CHEST);
        manager.setLocations(Collections.singletonList(configured));
        Block block = mock(Block.class);
        when(block.getType()).thenReturn(Material.CHEST);
        when(block.getLocation()).thenReturn(configured);
        PlayerInteractEvent event = mock(PlayerInteractEvent.class);
        when(event.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
        when(event.getClickedBlock()).thenReturn(block);
        new OpenEnvoyCrateListener(plugin).openEnvoyCrate(event);
        verify(manager, never()).removeCrateAt(any(Location.class));
        verify(event, never()).setCancelled(true);
        verify(event, never()).getPlayer();
    }
    @Test public void rejectedSummonerIsNotConsumed() {
        EnvoyPlugin plugin = mock(EnvoyPlugin.class);
        EnvoyManager manager = mock(EnvoyManager.class);
        when(plugin.getEnvoyManager()).thenReturn(manager);
        Player player = mock(Player.class);
        org.bukkit.inventory.ItemStack ticket = mock(org.bukkit.inventory.ItemStack.class);
        when(player.getItemInHand()).thenReturn(ticket);
        PlayerInteractEvent event = mock(PlayerInteractEvent.class);
        when(event.getPlayer()).thenReturn(player);
        try (MockedStatic<me.kingsavaqe.astroenvoy.utils.CustomItemUtils> items =
                     mockStatic(me.kingsavaqe.astroenvoy.utils.CustomItemUtils.class)) {
            items.when(() -> me.kingsavaqe.astroenvoy.utils.CustomItemUtils.isCrankedCustomItem(ticket)).thenReturn(true);
            items.when(() -> me.kingsavaqe.astroenvoy.utils.CustomItemUtils.getCrankedCustomItem(ticket)).thenReturn("envoySummoner");
            new me.kingsavaqe.astroenvoy.listeners.EnvoySummonerListener(plugin).useItem(event);
            verify(manager).tryStartEnvoyEvent();
            verify(player, never()).setItemInHand(any());
            verify(event).setCancelled(true);
        }
    }

    @Test public void hologramCleanupDoesNotDeleteNearbyDecorations() {
        World world = mock(World.class);
        Location crate = new Location(world, 1, 64, 0);
        org.bukkit.entity.ArmorStand decoration = mock(org.bukkit.entity.ArmorStand.class);
        when(world.getNearbyEntities(eq(crate), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Collections.<org.bukkit.entity.Entity>singletonList(decoration));
        EnvoyManager manager = new EnvoyManager(mock(EnvoyPlugin.class));
        manager.removeNearbyHologram(crate);
        verify(decoration, never()).remove();
    }

    @Test public void changedCrateBlockStillGetsHologramCleanup() {
        World world = mock(World.class);
        Location crate = location(world, 1, Material.STONE);
        EnvoyManager manager = spy(new EnvoyManager(mock(EnvoyPlugin.class)));
        manager.setCurrentChests(new ArrayList<>(Collections.singletonList(crate)));
        doNothing().when(manager).removeNearbyHologram(crate);
        manager.handleCrateRemoving();
        verify(manager).removeNearbyHologram(crate);
        assertTrue(manager.getCurrentChests().isEmpty());
    }

}

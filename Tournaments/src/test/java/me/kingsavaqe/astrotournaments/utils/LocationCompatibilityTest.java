package me.kingsavaqe.astrotournaments.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Test;
import org.mockito.MockedStatic;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LocationCompatibilityTest {
    @Test public void legacyLocationPreservesCoordinatesWithDefaultOrientation() {
        World world = mock(World.class);
        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(() -> Bukkit.getWorld("old_world")).thenReturn(world);
            Location location = Utils.getLocationFromString("old_world,-270.0,80.0,-122.0");
            assertSame(world, location.getWorld());
            assertEquals(-270, location.getX(), 0);
            assertEquals(80, location.getY(), 0);
            assertEquals(-122, location.getZ(), 0);
            assertEquals(0, location.getYaw(), 0);
            assertEquals(0, location.getPitch(), 0);
        }
    }

    @Test public void fullLocationRoundTripsIncludingOrientation() {
        World world = mock(World.class);
        when(world.getName()).thenReturn("tournament_world");
        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(() -> Bukkit.getWorld("tournament_world")).thenReturn(world);
            Location before = new Location(world, 1.5, 80, -2.5, 90, -30);
            assertEquals(before, Utils.getLocationFromString(Utils.locationToString(before)));
        }
    }

    @Test(expected=IllegalArgumentException.class) public void incompleteOrientationIsRejected() {
        Utils.getLocationFromString("world,1,2,3,90");
    }
}

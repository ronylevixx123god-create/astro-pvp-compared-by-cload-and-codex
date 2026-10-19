package me.kingsavaqe.astrotournaments.tasks;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.mockito.MockedStatic;
import java.util.Arrays;
import static org.mockito.Mockito.*;

public class NoFlightTaskTest {
    @Test public void missingTournamentWorldIsSafe() {
        try (MockedStatic<Bukkit> ignored = mockStatic(Bukkit.class)) {
            new NoFlightTask().run();
        }
    }
    @Test public void installedWorldKeepsOriginalFlightRules() {
        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            World world = mock(World.class);
            bukkit.when(() -> Bukkit.getWorld("tournament_world")).thenReturn(world);
            Player flying = mock(Player.class), op = mock(Player.class), grounded = mock(Player.class);
            when(flying.isFlying()).thenReturn(true);
            when(op.isOp()).thenReturn(true);
            when(op.isFlying()).thenReturn(true);
            when(world.getPlayers()).thenReturn(Arrays.asList(flying, op, grounded));
            new NoFlightTask().run();
            verify(flying).setFlying(false);
            verify(flying).setAllowFlight(false);
            verify(op, never()).setFlying(anyBoolean());
            verify(op, never()).setAllowFlight(anyBoolean());
            verify(grounded, never()).setFlying(anyBoolean());
            verify(grounded, never()).setAllowFlight(anyBoolean());
        }
    }
}

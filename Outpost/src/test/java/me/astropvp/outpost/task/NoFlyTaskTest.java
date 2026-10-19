package me.astropvp.outpost.task;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.mockito.MockedStatic;
import java.util.Arrays;
import static org.mockito.Mockito.*;

public class NoFlyTaskTest {
    @Test public void bothMissingWorldsAreSafe() {
        try (MockedStatic<Bukkit> ignored = mockStatic(Bukkit.class)) {
            new NoFlyTask().run();
        }
    }
    @Test public void absentFirstWorldDoesNotSkipSecondWorldOrChangeExemptions() {
        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            World mars = mock(World.class);
            bukkit.when(() -> Bukkit.getWorld("world_outpost_mars")).thenReturn(mars);
            Player flying = mock(Player.class), op = mock(Player.class), grounded = mock(Player.class);
            when(flying.isFlying()).thenReturn(true);
            when(op.isOp()).thenReturn(true);
            when(op.isFlying()).thenReturn(true);
            when(mars.getPlayers()).thenReturn(Arrays.asList(flying, op, grounded));
            new NoFlyTask().run();
            verify(flying).setFlying(false);
            verify(flying).setAllowFlight(false);
            verify(op, never()).setFlying(anyBoolean());
            verify(op, never()).setAllowFlight(anyBoolean());
            verify(grounded, never()).setFlying(anyBoolean());
            verify(grounded, never()).setAllowFlight(anyBoolean());
        }
    }
    @Test public void firstWorldStillEnforcesWhenSecondIsMissing() {
        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            World pluto = mock(World.class);
            Player player = mock(Player.class);
            when(player.isFlying()).thenReturn(true);
            when(pluto.getPlayers()).thenReturn(Arrays.asList(player));
            bukkit.when(() -> Bukkit.getWorld("world_outpost_pluto")).thenReturn(pluto);
            new NoFlyTask().run();
            verify(player).setFlying(false);
            verify(player).setAllowFlight(false);
        }
    }
}

package net.crankedpvp.staff.listeners;

import me.astropvp.proxypach.ProxyPatch;
import net.astropvp.core.AstroCore;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.*;
import java.lang.reflect.Field;
import java.util.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class RandomTeleportListenerTest {
    private Object previousServer;
    private PluginManager plugins;
    private BukkitScheduler scheduler;
    private Player staff, target;
    private World world;
    private RandomTeleportListener listener;

    @Before public void setup() throws Exception {
        Field field = Bukkit.class.getDeclaredField("server");
        field.setAccessible(true);
        previousServer = field.get(null);
        Server server = mock(Server.class);
        plugins = mock(PluginManager.class);
        scheduler = mock(BukkitScheduler.class);
        when(server.getPluginManager()).thenReturn(plugins);
        when(server.getScheduler()).thenReturn(scheduler);
        field.set(null, server);
        staff = mock(Player.class);
        target = mock(Player.class);
        world = mock(World.class);
        when(staff.getWorld()).thenReturn(world);
        when(target.getName()).thenReturn("TeleportTargetFixture");
        when(target.isOnline()).thenReturn(true);
        listener = new RandomTeleportListener(mock(AstroCore.class));
    }

    @After public void cleanup() throws Exception {
        ProxyPatch.playerNames.remove("TeleportTargetFixture");
        Field field = Bukkit.class.getDeclaredField("server");
        field.setAccessible(true);
        field.set(null, previousServer);
    }

    @Test public void emptyCandidatesDoNotTeleportOrScheduleCooldown() {
        when(world.getPlayers()).thenReturn(Collections.<Player>emptyList());
        listener.teleportToRandomPlayer(staff);
        verify(staff, never()).teleport(any(Player.class), any(PlayerTeleportEvent.TeleportCause.class));
        verify(staff).sendMessage(anyString());
        verifyZeroInteractions(scheduler);
    }

    @Test public void disabledProxyDoesNotFilterItsStaleNames() {
        ProxyPatch.playerNames.add("TeleportTargetFixture");
        when(world.getPlayers()).thenReturn(Collections.singletonList(target));
        when(staff.teleport(target, PlayerTeleportEvent.TeleportCause.UNKNOWN)).thenReturn(true);
        listener.teleportToRandomPlayer(staff);
        verify(staff).teleport(target, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    @Test public void enabledProxyStillExcludesItsPlayersWithoutEmptyListCrash() {
        when(plugins.isPluginEnabled("ProxyPatch")).thenReturn(true);
        ProxyPatch.playerNames.add("TeleportTargetFixture");
        when(world.getPlayers()).thenReturn(Collections.singletonList(target));
        listener.teleportToRandomPlayer(staff);
        verify(staff, never()).teleport(any(Player.class), any(PlayerTeleportEvent.TeleportCause.class));
        verifyZeroInteractions(scheduler);
    }

    @Test public void offlineAndNullPlayersAreIgnored() {
        when(target.isOnline()).thenReturn(false);
        when(world.getPlayers()).thenReturn(Arrays.asList(null, target));
        listener.teleportToRandomPlayer(staff);
        verify(staff, never()).teleport(any(Player.class), any(PlayerTeleportEvent.TeleportCause.class));
        verifyZeroInteractions(scheduler);
    }

    @Test public void cancelledTeleportDoesNotReportSuccessOrStartCooldown() {
        when(world.getPlayers()).thenReturn(Collections.singletonList(target));
        listener.teleportToRandomPlayer(staff);
        verify(staff).teleport(target, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        verify(staff, never()).sendMessage(anyString());
        verify(staff, never()).playSound(any(Location.class), any(Sound.class), anyFloat(), anyFloat());
        verifyZeroInteractions(scheduler);
    }

    @Test public void successfulTeleportRetainsShortCooldown() {
        when(world.getPlayers()).thenReturn(Collections.singletonList(target));
        when(staff.teleport(target, PlayerTeleportEvent.TeleportCause.UNKNOWN)).thenReturn(true);
        listener.teleportToRandomPlayer(staff);
        listener.teleportToRandomPlayer(staff);
        verify(staff, times(1)).teleport(target, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        verify(scheduler).runTaskLater(any(AstroCore.class), any(Runnable.class), eq(2L));
    }
}

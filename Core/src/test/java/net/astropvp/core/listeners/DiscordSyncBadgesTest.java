package net.astropvp.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Uses Astro's real AstroDiscordSync ChatListener class (from the jar AstroCore compiles against)
 * for the badges and the booster/leader lists; only the plugin-enabled state is faked.
 */
public class DiscordSyncBadgesTest {
    private final UUID uuid = UUID.randomUUID();
    private PluginManager pluginManager;
    private Object previousServer;
    private Player player;

    @Before
    public void setUp() throws Exception {
        Server server = mock(Server.class);
        pluginManager = mock(PluginManager.class);
        when(server.getPluginManager()).thenReturn(pluginManager);
        Field f = Bukkit.class.getDeclaredField("server");
        f.setAccessible(true);
        previousServer = f.get(null);
        f.set(null, server);
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(uuid);
    }

    @After
    public void tearDown() throws Exception {
        list("nitroBoosterList").remove(uuid);
        list("factionLeaderList").remove(uuid);
        Field f = Bukkit.class.getDeclaredField("server");
        f.setAccessible(true);
        f.set(null, previousServer);
    }

    @SuppressWarnings("unchecked")
    private static Set<UUID> list(String name) throws Exception {
        Field f = me.astropvp.discordsync.listeners.ChatListener.class.getDeclaredField(name);
        f.setAccessible(true);
        return (Set<UUID>) f.get(null);
    }

    private void enabled(boolean on) {
        when(pluginManager.isPluginEnabled("AstroDiscordSync")).thenReturn(on);
    }

    @Test
    public void enabledBoosterAndLeaderGetBothBadgesAsBefore() throws Exception {
        enabled(true);
        list("nitroBoosterList").add(uuid);
        list("factionLeaderList").add(uuid);
        String expected = "&8<" + me.astropvp.discordsync.listeners.ChatListener.BOOSTER_BADGE
                + me.astropvp.discordsync.listeners.ChatListener.LEADER_BADGE + "&8> ";
        assertEquals(expected, DiscordSyncBadges.getNitroLeader(player));
    }

    @Test
    public void enabledBoosterOnly() throws Exception {
        enabled(true);
        list("nitroBoosterList").add(uuid);
        assertEquals("&8<" + me.astropvp.discordsync.listeners.ChatListener.BOOSTER_BADGE + "&8> ",
                DiscordSyncBadges.getNitroLeader(player));
    }

    @Test
    public void enabledLeaderOnly() throws Exception {
        enabled(true);
        list("factionLeaderList").add(uuid);
        assertEquals("&8<" + me.astropvp.discordsync.listeners.ChatListener.LEADER_BADGE + "&8> ",
                DiscordSyncBadges.getNitroLeader(player));
    }

    @Test
    public void enabledWithoutBadgesGivesEmptyPrefix() {
        enabled(true);
        assertEquals("", DiscordSyncBadges.getNitroLeader(player));
    }

    @Test
    public void disabledOrMissingSkipsBadgesEvenForBoosterLeader() throws Exception {
        enabled(false);
        list("nitroBoosterList").add(uuid);
        list("factionLeaderList").add(uuid);
        assertEquals("", DiscordSyncBadges.getNitroLeader(player));
    }
}

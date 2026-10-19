package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.*;
import java.lang.reflect.Field;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ShowcaseExpanderCommandTest {
    private Field serverField;
    private Object previousServer;
    private CommandMap previousMap;
    private SimpleCommandMap map;
    @Before public void setup() throws Exception {
        serverField = Bukkit.class.getDeclaredField("server"); serverField.setAccessible(true);
        previousServer = serverField.get(null); previousMap = AbstractCommand.cmap;
        Server server = mock(Server.class); when(server.getPluginManager()).thenReturn(mock(PluginManager.class));
        when(server.getLogger()).thenReturn(Logger.getLogger("showcase-fixture"));
        serverField.set(null, server); map = new SimpleCommandMap(server); AbstractCommand.cmap = map;
    }
    @After public void cleanup() throws Exception { AbstractCommand.cmap = previousMap; serverField.set(null, previousServer); }
    @Test public void normalNameAndAliasResolveToSameCommand() {
        new ShowcaseExpander(mock(Plugin.class)).enable();
        assertNotNull("single-slash command must be registered", map.getCommand("showcaseexpander"));
        assertSame(map.getCommand("showcaseexpander"), map.getCommand("sce"));
        assertSame(map.getCommand("showcaseexpander"), map.getCommand("arkkit:showcaseexpander"));
        assertNull(map.getCommand("/showcaseexpander"));
    }
    @Test public void bothNamesReachExistingOpUsageWithoutGrantingAnything() {
        new ShowcaseExpander(mock(Plugin.class)).enable();
        CommandSender sender = mock(CommandSender.class); when(sender.isOp()).thenReturn(true);
        assertTrue(map.dispatch(sender, "showcaseexpander"));
        assertTrue(map.dispatch(sender, "sce"));
        verify(sender, times(4)).sendMessage(anyString());
    }
    @Test public void ordinarySenderDoesNotReceiveGrantOrUsage() {
        new ShowcaseExpander(mock(Plugin.class)).enable();
        CommandSender sender = mock(CommandSender.class);
        assertTrue(map.dispatch(sender, "showcaseexpander OtherPlayer"));
        verify(sender).isOp(); verifyNoMoreInteractions(sender);
    }
}

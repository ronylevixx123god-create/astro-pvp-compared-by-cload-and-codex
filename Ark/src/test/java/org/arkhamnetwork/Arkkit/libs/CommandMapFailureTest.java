package org.arkhamnetwork.Arkkit.libs;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.junit.*;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommandMapFailureTest {
    private Field serverField;
    private Object previousServer;
    private CommandMap previousMap;
    private final AbstractCommand command = new AbstractCommand("fixture") {
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { return true; }
    };
    @Before public void setup() throws Exception {
        serverField = Bukkit.class.getDeclaredField("server"); serverField.setAccessible(true);
        previousServer = serverField.get(null); previousMap = AbstractCommand.cmap; AbstractCommand.cmap = null;
    }
    @After public void cleanup() throws Exception { serverField.set(null, previousServer); AbstractCommand.cmap = previousMap; }
    @Test public void cachedMapDoesNotRequireReflection() throws Exception {
        CommandMap cached = mock(CommandMap.class); AbstractCommand.cmap = cached; serverField.set(null, null);
        assertSame(cached, command.getCommandMap());
    }
    @Test(timeout = 1000) public void absentServerFailsClearlyInsteadOfRecursing() throws Exception {
        serverField.set(null, null);
        try { command.getCommandMap(); fail("must fail closed"); }
        catch (IllegalStateException expected) { assertTrue(expected.getMessage().contains("initialization")); }
        assertNull(AbstractCommand.cmap);
    }
    @Test(timeout = 1000) public void unsupportedServerRetainsReflectionFailureAsCause() throws Exception {
        serverField.set(null, mock(Server.class));
        try { command.getCommandMap(); fail("must fail closed"); }
        catch (IllegalStateException expected) { assertTrue(expected.getCause() instanceof NoSuchFieldException); }
        assertNull(AbstractCommand.cmap);
    }
}

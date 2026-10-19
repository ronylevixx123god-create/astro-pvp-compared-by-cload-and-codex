package net.astropvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommandAliasArgumentsTest {
    private final CommandAliases listener = new CommandAliases();
    private PlayerCommandPreprocessEvent event(String message) {
        return new PlayerCommandPreprocessEvent(mock(Player.class), message, new java.util.HashSet<Player>());
    }
    @Test public void lootboxAliasPreservesArgumentCaseAndPayload() {
        PlayerCommandPreprocessEvent e = event("/LoOtBoX give Alice MyCaseSensitiveID /lootbox");
        listener.onPlayerPreProcessCommand(e);
        assertEquals("/mc give Alice MyCaseSensitiveID /lootbox", e.getMessage());
    }
    @Test public void longerNamesAndNamespacedCommandsAreNotLootboxAliases() {
        for (String input : new String[]{"/lootboxfilter", "/lootboxes", "/lootboxadmin give Alice", "/Other:lootbox give Alice"}) {
            PlayerCommandPreprocessEvent e = event(input); listener.onPlayerPreProcessCommand(e);
            assertEquals(input, e.getMessage());
        }
    }
    @Test public void rewrittenRootsPreserveTargetsAndSpacing() {
        String[][] aliases = {{"/TpYes", "/tpaccept"}, {"/tpno", "/tpdeny"}, {"/mask", "/cmask"},
                {"/craft", "/workbench"}, {"/eat", "/feed"}, {"/smite", "/lightning"}};
        for (String[] pair : aliases) {
            PlayerCommandPreprocessEvent e = event(pair[0] + "  Alice CaseSensitiveValue");
            listener.onPlayerCommandPreprocess(e);
            assertEquals(pair[1] + "  Alice CaseSensitiveValue", e.getMessage());
        }
    }
    @Test public void noArgumentLootboxStillResolves() {
        PlayerCommandPreprocessEvent e = event("/lootbox"); listener.onPlayerPreProcessCommand(e);
        assertEquals("/mc", e.getMessage());
    }
    @Test public void cancelledCommandsAreNotRewritten() {
        for (String input : new String[]{"/mask give Alice", "/lootbox give Alice"}) {
            PlayerCommandPreprocessEvent e = event(input); e.setCancelled(true);
            listener.onPlayerCommandPreprocess(e); listener.onPlayerPreProcessCommand(e);
            assertEquals(input, e.getMessage()); assertTrue(e.isCancelled());
        }
    }
    @Test public void unrelatedCommandsRemainUntouched() {
        PlayerCommandPreprocessEvent e = event("/somecommand Alice CaseSensitiveID");
        listener.onPlayerCommandPreprocess(e); listener.onPlayerPreProcessCommand(e);
        assertEquals("/somecommand Alice CaseSensitiveID", e.getMessage());
    }
}

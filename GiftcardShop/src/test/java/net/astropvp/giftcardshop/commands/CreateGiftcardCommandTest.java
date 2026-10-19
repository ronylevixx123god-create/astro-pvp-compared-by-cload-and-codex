package net.astropvp.giftcardshop.commands;

import net.astropvp.giftcardshop.GiftcardAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.*;
import org.mockito.MockedStatic;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreateGiftcardCommandTest {
    private Player sender, target;
    private MockedStatic<Bukkit> bukkit;
    private MockedStatic<GiftcardAPI> api;
    private CreateGiftcardCommand command;
    @Before public void setup() {
        sender = mock(Player.class); when(sender.isOp()).thenReturn(true); when(sender.getName()).thenReturn("M0");
        target = mock(Player.class);
        bukkit = mockStatic(Bukkit.class); bukkit.when(() -> Bukkit.getPlayer("online")).thenReturn(target);
        api = mockStatic(GiftcardAPI.class); command = new CreateGiftcardCommand();
    }
    @After public void cleanup() { api.close(); bukkit.close(); }
    private boolean run(CommandSender who, String... args) { return command.onCommand(who, null, "creategc", args); }
    @Test public void consoleIsRejectedWithoutCastingException() {
        assertFalse(run(mock(CommandSender.class), "online", "1")); api.verifyNoInteractions();
    }
    @Test public void invalidAmountsNeverReachVendorBoundary() {
        for (String value : new String[]{"abc", "NaN", "Infinity", "-Infinity", "1e309", "0", "-1"}) assertFalse(run(sender, "online", value));
        api.verifyNoInteractions(); verify(sender, times(7)).sendMessage(anyString());
    }
    @Test public void offlinePlayerIsRejected() {
        assertFalse(run(sender, "offline", "10")); api.verifyNoInteractions();
    }
    @Test public void missingArgumentsShowUsage() {
        assertFalse(run(sender)); assertFalse(run(sender, "online")); api.verifyNoInteractions();
    }
    @Test public void originalOpAndNameRestrictionsRemain() {
        when(sender.isOp()).thenReturn(false); assertFalse(run(sender, "online", "10"));
        when(sender.isOp()).thenReturn(true); when(sender.getName()).thenReturn("OtherAdmin");
        assertFalse(run(sender, "online", "10")); api.verifyNoInteractions();
    }
    @Test public void validAmountAndAliasReachMockedApiOnce() throws Exception {
        assertTrue(run(sender, "online", "12.5")); api.verify(() -> GiftcardAPI.createGiftCard(target, 12.5));
    }
}

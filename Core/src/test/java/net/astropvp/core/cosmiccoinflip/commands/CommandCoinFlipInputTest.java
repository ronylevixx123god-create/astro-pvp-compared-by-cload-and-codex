package net.astropvp.core.cosmiccoinflip.commands;

import net.astropvp.core.AstroCore;
import net.astropvp.core.cosmiccoinflip.struct.CoinFlipManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.*;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

public class CommandCoinFlipInputTest {
    private Field instanceField, economyField;
    private Object previousInstance, previousEconomy;
    private CoinFlipManager manager;
    private Economy economy;
    private Player player;
    private CommandCoinFlip command;
    @Before public void setup() throws Exception {
        instanceField = AstroCore.class.getDeclaredField("instance"); instanceField.setAccessible(true);
        economyField = AstroCore.class.getDeclaredField("econ"); economyField.setAccessible(true);
        previousInstance = instanceField.get(null); previousEconomy = economyField.get(null);
        AstroCore core = mock(AstroCore.class); manager = mock(CoinFlipManager.class); economy = mock(Economy.class);
        when(core.getCoinFlipManager()).thenReturn(manager);
        when(core.getEcon()).thenReturn(economy);
        instanceField.set(null, core); economyField.set(null, economy);
        player = mock(Player.class); command = new CommandCoinFlip();
    }
    @After public void cleanup() throws Exception {
        instanceField.set(null, previousInstance); economyField.set(null, previousEconomy);
    }
    @Test public void numericOverflowNeverReachesEconomy() {
        String huge = new String(new char[400]).replace('\0', '9');
        assertTrue(command.onCommand(player, null, "cf", new String[]{huge}));
        verifyZeroInteractions(economy); verify(player).sendMessage(anyString());
        verify(player, never()).setMetadata(anyString(), any());
        verify(player, never()).openInventory(any(Inventory.class));
    }
    @Test public void unicodeNumericInputDoesNotThrowOrReachEconomy() {
        assertTrue(command.onCommand(player, null, "cf", new String[]{"\u0661\u0662\u0663\u0664\u0665"}));
        verifyZeroInteractions(economy); verify(player).sendMessage(anyString());
    }
    @Test public void validSuffixKeepsOriginalWagerAndMinimum() {
        when(economy.has(player, 10000.0)).thenReturn(true);
        Inventory menu = mock(Inventory.class); when(manager.getCoinFlipSelectMenu(10000.0, null)).thenReturn(menu);
        assertTrue(command.onCommand(player, null, "cf", new String[]{"10k"}));
        verify(economy).has(player, 10000.0); verify(player).openInventory(menu);
    }
    @Test public void malformedAmountsReceiveFeedbackWithoutEconomyCalls() {
        for (String value : new String[]{"abc", "NaN", "Infinity", "-1", "0", "1.5"})
            assertTrue(command.onCommand(player, null, "cf", new String[]{value}));
        verifyZeroInteractions(economy); verify(player, times(6)).sendMessage(anyString());
    }
    @Test public void consoleDoesNotAccessPlayerManager() {
        assertTrue(command.onCommand(mock(CommandSender.class), null, "coinflip", new String[]{}));
        verifyZeroInteractions(manager, economy);
    }
}

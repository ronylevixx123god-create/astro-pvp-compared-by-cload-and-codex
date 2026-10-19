package me.venir.astrocollectors.commands;

import me.venir.astrocollectors.collector.CollectorUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.junit.*;
import org.mockito.MockedStatic;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CollectorGiveCommandTest {
    private CommandSender sender;
    private Player target;
    private PlayerInventory inventory;
    private MockedStatic<Bukkit> bukkit;
    private MockedStatic<CollectorUtils> items;
    private CollectorGiveCommand command;
    @Before public void setup() {
        sender = mock(CommandSender.class); when(sender.hasPermission("astro.admin")).thenReturn(true);
        target = mock(Player.class); inventory = mock(PlayerInventory.class);
        when(target.getInventory()).thenReturn(inventory);
        when(inventory.addItem(any(ItemStack[].class))).thenReturn(new HashMap<Integer, ItemStack>());
        bukkit = mockStatic(Bukkit.class); bukkit.when(() -> Bukkit.getPlayer("online")).thenReturn(target);
        items = mockStatic(CollectorUtils.class); command = new CollectorGiveCommand();
    }
    @After public void cleanup() { items.close(); bukkit.close(); }
    private void run(String... args) { assertTrue(command.onCommand(sender, null, "collector", args)); }
    @Test public void invalidNumbersDoNotCreateItems() {
        for (String value : new String[]{"abc", "2147483648", "0", "-1", "2305", "NaN", "Infinity", "1.5"}) run("online", value);
        items.verifyNoInteractions(); verifyNoInteractions(inventory);
        verify(sender, times(8)).sendMessage(anyString());
    }
    @Test public void missingArgumentsShowHelp() {
        run(); run("online"); run("online", "1", "extra");
        items.verifyNoInteractions(); verifyNoInteractions(inventory);
        verify(sender, times(3)).sendMessage(anyString());
    }
    @Test public void offlineTargetDoesNotCreateItems() {
        run("offline", "1"); items.verifyNoInteractions(); verifyNoInteractions(inventory);
        verify(sender).sendMessage(anyString());
    }
    @Test public void permissionDeniedDoesNotCreateItems() {
        when(sender.hasPermission("astro.admin")).thenReturn(false);
        run("online", "1"); items.verifyNoInteractions(); verifyNoInteractions(inventory);
    }
    @Test public void validConsoleGrantAndAliasKeepQuantity() {
        ItemStack stack = new ItemStack(Material.BEACON, 64);
        items.when(() -> CollectorUtils.createCollector(64)).thenReturn(stack);
        assertTrue(command.onCommand(sender, null, "c", new String[]{"online", "64"}));
        verify(inventory).addItem(stack); items.verify(() -> CollectorUtils.createCollector(64));
    }
    @Test public void bulkGrantSplitsBeforeCreatingNbtItems() {
        ItemStack stack = new ItemStack(Material.BEACON, 64);
        items.when(() -> CollectorUtils.createCollector(64)).thenReturn(stack);
        run("online", "128");
        items.verify(() -> CollectorUtils.createCollector(64), times(2));
        items.verify(() -> CollectorUtils.createCollector(128), never());
        verify(inventory, times(2)).addItem(stack);
    }
    @Test public void maximumGrantIsBoundedToOneInventory() {
        ItemStack stack = new ItemStack(Material.BEACON, 64);
        items.when(() -> CollectorUtils.createCollector(64)).thenReturn(stack);
        run("online", "2304");
        items.verify(() -> CollectorUtils.createCollector(64), times(36));
        verify(inventory, times(36)).addItem(stack);
    }
    @Test public void inventoryRemainderIsNotDiscarded() {
        ItemStack stack = new ItemStack(Material.BEACON, 3), remainder = new ItemStack(Material.BEACON, 2);
        items.when(() -> CollectorUtils.createCollector(3)).thenReturn(stack);
        HashMap<Integer, ItemStack> left = new HashMap<>(); left.put(0, remainder);
        when(inventory.addItem(stack)).thenReturn(left);
        World world = mock(World.class); Location location = new Location(world, 1, 64, 2);
        when(target.getWorld()).thenReturn(world); when(target.getLocation()).thenReturn(location);
        run("online", "3"); verify(world).dropItem(location, remainder);
    }
}

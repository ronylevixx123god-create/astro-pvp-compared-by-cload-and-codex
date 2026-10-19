package me.cosmicpvp.CosmicGlobalKits.libs;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.items.WildcardClaim;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeBuilderRegistry;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Drives the real GlobalKits.unlockOrLevelUp through WildcardClaim (what WildcardMenu uses) with a
 * fake LuckPerms and a fake main-thread scheduler: the unlock item is consumed only after a grant is
 * saved, and only on the main thread; failures keep the item.
 */
public class GlobalKitsUnlockTest {
    private static final GlobalKits KIT = GlobalKits.HYPER;

    private final UUID uuid = UUID.randomUUID();
    private final List<Runnable> mainThread = new ArrayList<>();
    private final List<String> claimCalls = new ArrayList<>();
    private Object previousServer;
    private PluginManager pluginManager;
    private LuckPerms api;
    private UserManager users;
    private NodeMap data;
    private PermissionNode node;
    private CompletableFuture<Void> saveFuture;
    private Player player;
    private WildcardClaim claim;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        Server server = mock(Server.class);
        when(server.getLogger()).thenReturn(Logger.getLogger("test-server"));
        pluginManager = mock(PluginManager.class);
        when(server.getPluginManager()).thenReturn(pluginManager);
        BukkitScheduler scheduler = mock(BukkitScheduler.class);
        when(server.getScheduler()).thenReturn(scheduler);
        when(scheduler.runTask(any(Plugin.class), any(Runnable.class))).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock inv) {
                mainThread.add((Runnable) inv.getArguments()[1]);
                return null;
            }
        });
        Field serverField = Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);
        previousServer = serverField.get(null);
        serverField.set(null, server);

        CosmicGlobalKits plugin = mock(CosmicGlobalKits.class);
        YamlConfiguration config = new YamlConfiguration();
        config.set("luckperms-server-context", "");
        when(plugin.getConfig()).thenReturn(config);
        // getServer()/getDescription()/getLogger() are final in JavaPlugin: set their backing fields.
        setField(plugin, "server", server);
        setField(plugin, "description", new PluginDescriptionFile("CosmicGlobalKits", "test", "x"));
        setField(plugin, "logger", new PluginLogger(plugin));
        setStatic(CosmicGlobalKits.class, "plugin", plugin);

        api = mock(LuckPerms.class);
        when(api.getServerName()).thenReturn("astro");
        NodeBuilderRegistry registry = mock(NodeBuilderRegistry.class);
        PermissionNode.Builder builder = mock(PermissionNode.Builder.class);
        node = mock(PermissionNode.class);
        when(api.getNodeBuilderRegistry()).thenReturn(registry);
        when(registry.forPermission()).thenReturn(builder);
        when(builder.permission(anyString())).thenReturn(builder);
        when(builder.value(anyBoolean())).thenReturn(builder);
        when(builder.withContext(anyString(), anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(node);
        users = mock(UserManager.class);
        when(api.getUserManager()).thenReturn(users);
        final User user = mock(User.class);
        data = mock(NodeMap.class);
        when(user.data()).thenReturn(data);
        when(data.add(node)).thenReturn(DataMutateResult.SUCCESS);
        saveFuture = new CompletableFuture<>();
        when(users.modifyUser(eq(uuid), any(Consumer.class))).thenAnswer(new Answer<CompletableFuture<Void>>() {
            public CompletableFuture<Void> answer(InvocationOnMock inv) {
                ((Consumer<User>) inv.getArguments()[1]).accept(user);
                return saveFuture;
            }
        });
        Method register = LuckPermsProvider.class.getDeclaredMethod("register", LuckPerms.class);
        register.setAccessible(true);
        register.invoke(null, api);

        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(uuid);
        when(player.getName()).thenReturn("Tester");
        when(player.isOnline()).thenReturn(true);
        when(player.hasPermission(KIT.getPermissionString())).thenReturn(false);
        when(player.hasMetadata(anyString())).thenReturn(false);

        claim = new WildcardClaim(new WildcardClaim.Actions() {
            public boolean isItemAlreadyClaimed() { return false; }
            public void markItemClaimed() { claimCalls.add("consume"); }
            public void returnItem() { claimCalls.add("return"); }
            public void closeAfterUnlock() { claimCalls.add("close"); }
            public void closeClaimedElsewhere() { claimCalls.add("closeElsewhere"); }
        });
    }

    @After
    public void tearDown() throws Exception {
        Method unregister = LuckPermsProvider.class.getDeclaredMethod("unregister");
        unregister.setAccessible(true);
        unregister.invoke(null);
        setStatic(CosmicGlobalKits.class, "plugin", null);
        Field serverField = Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);
        serverField.set(null, previousServer);
    }

    private static void setField(JavaPlugin plugin, String name, Object value) throws Exception {
        Field f = JavaPlugin.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(plugin, value);
    }

    private static void setStatic(Class<?> type, String name, Object value) throws Exception {
        Field f = type.getDeclaredField(name);
        f.setAccessible(true);
        f.set(null, value);
    }

    private void clickUnlock() {
        claim.click(done -> KIT.unlockOrLevelUp(player, done));
    }

    private void runMainThread() {
        List<Runnable> tasks = new ArrayList<>(mainThread);
        mainThread.clear();
        tasks.forEach(Runnable::run);
    }

    private void verifyMessageContaining(final String text) {
        verify(player, atLeastOnce()).sendMessage(argThat(new BaseMatcher<String>() {
            public boolean matches(Object o) { return o != null && o.toString().contains(text); }
            public void describeTo(Description d) { d.appendText("message containing " + text); }
        }));
    }

    @Test
    public void grantedUnlockConsumesItemOnlyAfterSaveOnMainThread() {
        clickUnlock();
        assertTrue("nothing consumed while saving", claimCalls.isEmpty());

        saveFuture.complete(null);
        assertTrue("nothing consumed off the main thread", claimCalls.isEmpty());
        verify(pluginManager, never()).callEvent(any(Event.class));

        runMainThread();
        assertEquals(Arrays.asList("consume", "close"), claimCalls);
        verifyMessageContaining("UNLOCKED");
        verify(pluginManager).callEvent(any(CustomCosmicPlayerEvent.class));
    }

    @Test
    public void storageFailureKeepsItemAndTellsPlayer() {
        clickUnlock();
        saveFuture.completeExceptionally(new RuntimeException("storage write failed"));
        runMainThread();
        assertFalse(claimCalls.contains("consume"));
        verifyMessageContaining("Your item was not used");
        verify(pluginManager, never()).callEvent(any(Event.class));

        claim.inventoryClosed();
        assertEquals(Collections.singletonList("return"), claimCalls);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void alreadyUnlockedNeverCallsLuckPermsOrConsumes() {
        when(player.hasPermission(KIT.getPermissionString())).thenReturn(true);
        clickUnlock();
        runMainThread();
        verify(users, never()).modifyUser(any(UUID.class), any(Consumer.class));
        assertFalse(claimCalls.contains("consume"));
        verifyMessageContaining("already have");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void unscopedLuckPermsServerKeepsItemWithoutTouchingStorage() {
        when(api.getServerName()).thenReturn("global");
        clickUnlock();
        runMainThread();
        verify(users, never()).modifyUser(any(UUID.class), any(Consumer.class));
        assertFalse(claimCalls.contains("consume"));
        verifyMessageContaining("Your item was not used");
    }

    @Test
    public void alreadyStoredInLuckPermsDoesNotConsume() {
        when(data.add(node)).thenReturn(DataMutateResult.FAIL_ALREADY_HAS);
        clickUnlock();
        saveFuture.complete(null);
        runMainThread();
        assertFalse(claimCalls.contains("consume"));
        verifyMessageContaining("already have");
    }
}

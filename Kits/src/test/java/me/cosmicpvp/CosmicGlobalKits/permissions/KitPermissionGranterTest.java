package me.cosmicpvp.CosmicGlobalKits.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeBuilderRegistry;
import net.luckperms.api.node.types.PermissionNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class KitPermissionGranterTest {
    private static final String PERM = "cosmickits.kit.hyper";
    private final UUID uuid = UUID.randomUUID();

    private LuckPerms api;
    private UserManager users;
    private PermissionNode.Builder builder;
    private PermissionNode node;
    private NodeMap data;
    private CompletableFuture<Void> saveFuture;
    private final List<Runnable> mainThreadQueue = new ArrayList<>();
    private final List<GrantResult> results = new ArrayList<>();

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        api = mock(LuckPerms.class);
        when(api.getServerName()).thenReturn("astro");
        NodeBuilderRegistry registry = mock(NodeBuilderRegistry.class);
        builder = mock(PermissionNode.Builder.class);
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
    }

    private KitPermissionGranter granter() {
        return new KitPermissionGranter(() -> api, mainThreadQueue::add, Logger.getLogger("test"));
    }

    private void runMainThread() {
        List<Runnable> tasks = new ArrayList<>(mainThreadQueue);
        mainThreadQueue.clear();
        tasks.forEach(Runnable::run);
    }

    @Test
    public void blankConfigUsesLuckPermsServerName() {
        KitPermissionGranter.ServerScope s = KitPermissionGranter.ServerScope.resolve("", "astro");
        assertFalse(s.isGlobal());
        assertEquals("astro", s.getServer());
    }

    @Test
    public void explicitConfigWins() {
        assertEquals("factions", KitPermissionGranter.ServerScope.resolve(" factions ", "astro").getServer());
    }

    @Test
    public void explicitGlobalIsUnscoped() {
        assertTrue(KitPermissionGranter.ServerScope.resolve("GLOBAL", "astro").isGlobal());
    }

    @Test(expected = IllegalStateException.class)
    public void refusesWhenLuckPermsServerIsGlobal() {
        KitPermissionGranter.ServerScope.resolve("", "global");
    }

    @Test(expected = IllegalStateException.class)
    public void refusesWhenLuckPermsServerIsEmpty() {
        KitPermissionGranter.ServerScope.resolve(null, " ");
    }

    @Test
    public void grantIsScopedAndReportedOnlyOnMainThreadAfterSave() {
        granter().grant(uuid, PERM, "", results::add);
        verify(builder).permission(PERM);
        verify(builder).value(true);
        verify(builder).withContext(DefaultContextKeys.SERVER_KEY, "astro");
        assertTrue("nothing before the save completes", results.isEmpty() && mainThreadQueue.isEmpty());

        saveFuture.complete(null);
        assertTrue("not delivered off the main thread", results.isEmpty());
        assertEquals(1, mainThreadQueue.size());
        runMainThread();
        assertEquals(GrantResult.GRANTED, results.get(0));
    }

    @Test
    public void explicitGlobalGrantHasNoServerContext() {
        granter().grant(uuid, PERM, "global", results::add);
        verify(builder, never()).withContext(anyString(), anyString());
        saveFuture.complete(null);
        runMainThread();
        assertEquals(GrantResult.GRANTED, results.get(0));
    }

    @Test
    public void alreadyHasNodeIsDistinguished() {
        when(data.add(node)).thenReturn(DataMutateResult.FAIL_ALREADY_HAS);
        granter().grant(uuid, PERM, "", results::add);
        saveFuture.complete(null);
        runMainThread();
        assertEquals(GrantResult.ALREADY_GRANTED, results.get(0));
    }

    @Test
    public void storageFailureIsReportedAsFailed() {
        granter().grant(uuid, PERM, "", results::add);
        saveFuture.completeExceptionally(new RuntimeException("storage write failed"));
        runMainThread();
        assertEquals(GrantResult.FAILED, results.get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void modifyUserThrowingIsReportedAsFailed() {
        when(users.modifyUser(eq(uuid), any(Consumer.class))).thenThrow(new IllegalStateException("closed"));
        granter().grant(uuid, PERM, "", results::add);
        runMainThread();
        assertEquals(GrantResult.FAILED, results.get(0));
    }

    @Test
    public void globalLuckPermsServerWithoutConfigNeverTouchesStorage() {
        when(api.getServerName()).thenReturn("global");
        granter().grant(uuid, PERM, "", results::add);
        verify(api, never()).getUserManager();
        runMainThread();
        assertEquals(GrantResult.FAILED, results.get(0));
    }

    @Test
    public void luckPermsNotLoadedIsReportedAsFailed() {
        new KitPermissionGranter(() -> { throw new IllegalStateException("LuckPerms API not loaded"); },
                mainThreadQueue::add, Logger.getLogger("test")).grant(uuid, PERM, "", results::add);
        runMainThread();
        assertEquals(GrantResult.FAILED, results.get(0));
    }

    @Test
    public void otherMutateResultsAreFailures() {
        assertEquals(GrantResult.FAILED, KitPermissionGranter.toResult(null, DataMutateResult.FAIL));
        assertEquals(GrantResult.FAILED, KitPermissionGranter.toResult(null, null));
        assertEquals(GrantResult.FAILED, KitPermissionGranter.toResult(new RuntimeException(), DataMutateResult.SUCCESS));
    }
}

import me.astropvp.discordsync.DiscordSyncPlugin;
import me.astropvp.discordsync.managers.DatabaseManager;
import me.astropvp.discordsync.listeners.DiscordListener;
import me.astropvp.discordsync.commands.discord.WhitelistCommand;
import me.astropvp.hotfixplugin.HotFixPlugin;
import me.astropvp.hotfixplugin.listeners.AutoOP;
import net.astropvp.core.utils.YouTubeVideoUrl;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReviewRegressionTest {
    private Server server;
    private BukkitScheduler scheduler;
    private HotFixPlugin hotfix;
    private YamlConfiguration config;
    private Player player;
    private DiscordSyncPlugin discord;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet results;
    private DatabaseManager db;

    private static void set(Object target, Class<?> owner, String name, Object value) throws Exception {
        Field field = owner.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Before public void setup() throws Exception {
        server = mock(Server.class);
        scheduler = mock(BukkitScheduler.class);
        when(server.getScheduler()).thenReturn(scheduler);
        set(null, Bukkit.class, "server", server);
        config = new YamlConfiguration();
        hotfix = mock(HotFixPlugin.class);
        when(hotfix.getConfig()).thenReturn(config);
        set(hotfix, JavaPlugin.class, "isEnabled", true);
        set(null, HotFixPlugin.class, "instance", hotfix);
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.fromString("12345678-abcd-1234-abcd-123456789abc"));
        when(player.isOnline()).thenReturn(true);
        discord = mock(DiscordSyncPlugin.class);
        when(discord.getConfig()).thenReturn(config);
        set(discord, JavaPlugin.class, "logger", mock(PluginLogger.class));
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        results = mock(ResultSet.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);
        db = new DatabaseManager(discord);
        set(db, DatabaseManager.class, "connection", connection);
    }

    private Runnable joinGrant() {
        config.set("auto-op-uuids", Collections.singletonList(player.getUniqueId().toString()));
        new AutoOP().onPlayerJoinEvent(new PlayerJoinEvent(player, ""));
        ArgumentCaptor<Runnable> task = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).runTaskLater(eq(hotfix), task.capture(), eq(40L));
        return task.getValue();
    }

    @Test public void emptyAllowlistDoesNotChangeExistingOperator() throws Exception {
        when(player.isOp()).thenReturn(true);
        AutoOP listener = new AutoOP();
        // Dispatch every registered join handler, as Bukkit does (including legacy de-op hooks).
        for (java.lang.reflect.Method method : AutoOP.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(org.bukkit.event.EventHandler.class)) {
                method.invoke(listener, new PlayerJoinEvent(player, ""));
            }
        }
        verify(player, never()).setOp(anyBoolean());
        verifyNoInteractions(scheduler);
    }
    @Test public void allowlistedPlayerGetsOperatorAfterDelay() {
        joinGrant().run();
        verify(player).setOp(true);
    }
    @Test public void revokedAllowlistCancelsDelayedGrant() {
        Runnable grant = joinGrant();
        config.set("auto-op-uuids", Collections.emptyList());
        grant.run();
        verify(player, never()).setOp(anyBoolean());
    }
    @Test public void offlinePlayerDoesNotGetOperator() {
        Runnable grant = joinGrant();
        when(player.isOnline()).thenReturn(false);
        grant.run();
        verify(player, never()).setOp(anyBoolean());
    }
    @Test public void disabledPluginDoesNotGrantOperator() throws Exception {
        Runnable grant = joinGrant();
        set(hotfix, JavaPlugin.class, "isEnabled", false);
        grant.run();
        verify(player, never()).setOp(anyBoolean());
    }
    @Test public void videoTrackingParametersHaveOneCanonicalId() {
        assertEquals("dQw4w9WgXcQ", YouTubeVideoUrl.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=42"));
        assertEquals("dQw4w9WgXcQ", YouTubeVideoUrl.parse("https://youtu.be/dQw4w9WgXcQ?si=test"));
        assertEquals("dQw4w9WgXcQ", YouTubeVideoUrl.parse("https://m.youtube.com/watch?feature=share&v=dQw4w9WgXcQ"));
    }
    @Test public void videoShortsAndEmbedAreSupported() {
        assertEquals("dQw4w9WgXcQ", YouTubeVideoUrl.parse("https://www.youtube.com/shorts/dQw4w9WgXcQ"));
        assertEquals("dQw4w9WgXcQ", YouTubeVideoUrl.parse("https://www.youtube.com/embed/dQw4w9WgXcQ"));
    }
    @Test public void spoofedVideoHostsAndAmbiguousIdsAreRejected() {
        String[] invalid = {null, "", "https://evil.test/youtube.com/watch?v=dQw4w9WgXcQ",
            "https://youtube.com.evil.test/watch?v=dQw4w9WgXcQ", "https://user@youtube.com/watch?v=dQw4w9WgXcQ",
            "https://youtube.com/watch?v=dQw4w9WgXcQ&v=aaaaaaaaaaa", "https://youtube.com/watch?v=short",
            "https://youtu.be/dQw4w9WgXcQ/extra", "https://youtube.com/shorts/extra/dQw4w9WgXcQ", "file://youtube.com/watch?v=dQw4w9WgXcQ"};
        for (String url : invalid) assertNull(url, YouTubeVideoUrl.parse(url));
    }
    @Test public void lookupClosesStatementAndResults() throws Exception {
        when(results.next()).thenReturn(true);
        when(results.getLong("discordId")).thenReturn(123L);
        assertEquals(Long.valueOf(123), db.get(player.getUniqueId()));
        verify(results).close(); verify(statement).close();
    }
    @Test public void missingLinkStillClosesResources() throws Exception {
        when(results.next()).thenReturn(false);
        assertNull(db.get(player.getUniqueId()));
        verify(results).close(); verify(statement).close();
    }
    @Test public void queryFailureStillClosesStatement() throws Exception {
        when(statement.executeQuery()).thenThrow(new SQLException("simulated"));
        try { db.get(player.getUniqueId()); fail("Expected query failure"); }
        catch (SQLException expected) { verify(statement).close(); }
    }
    @Test public void successfulInsertAndRemoveUpdateCache() throws Exception {
        UUID id = player.getUniqueId();
        db.insert(id, 123);
        assertEquals(Long.valueOf(123), db.getCachedResults().get(id));
        db.remove(id);
        assertFalse(db.getCachedResults().containsKey(id));
        verify(statement, times(4)).close();
    }
    @Test public void failedInsertDoesNotCacheLink() throws Exception {
        when(statement.executeUpdate()).thenThrow(new SQLException("simulated"));
        db.insert(player.getUniqueId(), 123);
        assertTrue(db.getCachedResults().isEmpty());
        verify(statement, times(3)).close();
    }
    @Test public void refreshRemovesStaleLinks() throws Exception {
        db.insert(player.getUniqueId(), 123);
        when(results.next()).thenReturn(false);
        assertTrue(db.fetchMap().isEmpty());
        assertTrue(db.getCachedResults().isEmpty());
        verify(results, times(3)).close();
    }
    @Test public void healthyReconnectDoesNotCloseActiveConnection() throws Exception {
        when(connection.isValid(5)).thenReturn(true);
        db.connect();
        verify(connection, never()).close();
    }
    @Test public void shutdownPreventsReconnectAndClearsCache() throws Exception {
        db.insert(player.getUniqueId(), 123);
        db.close();
        assertNull(db.getConnection()); assertFalse(db.isConnected());
        assertTrue(db.getCachedResults().isEmpty());
        try { db.connect(); fail("Must not reconnect after shutdown"); }
        catch (SQLException expected) { verify(connection).close(); }
    }
    @Test public void cacheSnapshotCannotBeMutatedExternally() {
        try { db.getCachedResults().put(UUID.randomUUID(), 1L); fail("Cache leaked"); }
        catch (UnsupportedOperationException expected) { }
    }
    @Test public void oldOwnerMessageCannotGrantRoles() {
        PrivateMessageReceivedEvent event = mock(PrivateMessageReceivedEvent.class, RETURNS_DEEP_STUBS);
        when(event.getMessage().getContentStripped()).thenReturn("!ownerupdate");
        new DiscordListener(discord).onEvent(event);
        verifyNoInteractions(discord);
        verify(event, never()).getChannel();
    }
    @Test public void invalidVerificationCodeDoesNotTouchAccounts() {
        PrivateMessageReceivedEvent event = mock(PrivateMessageReceivedEvent.class, RETURNS_DEEP_STUBS);
        when(event.getMessage().getContentStripped()).thenReturn("!verify not-a-number");
        new DiscordListener(discord).onEvent(event);
        verifyNoInteractions(discord);
        verify(event.getChannel()).sendMessage("You have entered an invalid code!");
    }
    @Test public void ordinaryDiscordMessagesDoNotTriggerWhitelistReplies() {
        MessageReceivedEvent event = mock(MessageReceivedEvent.class, RETURNS_DEEP_STUBS);
        when(event.getMessage().getContentRaw()).thenReturn("hello everyone");
        new WhitelistCommand(discord).onMessageReceived(event);
        verify(event, never()).getMember(); verify(event, never()).getChannel();
        verifyNoInteractions(scheduler);
    }
    @Test public void unauthorizedWhitelistCommandDoesNotChangeServer() {
        MessageReceivedEvent event = mock(MessageReceivedEvent.class, RETURNS_DEEP_STUBS);
        when(event.getMessage().getContentRaw()).thenReturn("!whitelist off");
        when(event.getMember()).thenReturn(null);
        new WhitelistCommand(discord).onMessageReceived(event);
        verifyNoInteractions(scheduler);
        verify(server, never()).setWhitelist(anyBoolean());
    }
    @Test public void authorizedWhitelistRunsOnServerThread() {
        MessageReceivedEvent event = mock(MessageReceivedEvent.class, RETURNS_DEEP_STUBS);
        when(event.getMessage().getContentRaw()).thenReturn("!whitelist on");
        Role role = mock(Role.class);
        when(role.getId()).thenReturn("1165312504289841152");
        when(event.getMember().getRoles()).thenReturn(Collections.singletonList(role));
        new WhitelistCommand(discord).onMessageReceived(event);
        ArgumentCaptor<Runnable> task = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).runTask(eq(discord), task.capture());
        verify(server, never()).setWhitelist(anyBoolean());
        task.getValue().run();
        verify(server).setWhitelist(true);
    }
    @Test public void blankBotTokenDisablesWithoutStartingWorkers() {
        doCallRealMethod().when(discord).onEnable();
        when(server.getPluginManager()).thenReturn(mock(org.bukkit.plugin.PluginManager.class));
        config.set("discord.bot-token", "   ");
        discord.onEnable();
        verify(server.getPluginManager()).disablePlugin(discord);
        verifyNoInteractions(scheduler);
    }
    @Test public void databaseStartupFailureNeverInitializesDiscordListeners() {
        doCallRealMethod().when(discord).onEnable();
        config.set("discord.bot-token", "test-token-never-sent");
        discord.onEnable();
        ArgumentCaptor<Runnable> worker = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).runTaskAsynchronously(eq(discord), worker.capture());
        worker.getValue().run();
        ArgumentCaptor<Runnable> failureHandler = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).runTask(eq(discord), failureHandler.capture());
        when(server.getPluginManager()).thenReturn(mock(org.bukkit.plugin.PluginManager.class));
        failureHandler.getValue().run();
        verify(server.getPluginManager()).disablePlugin(discord);
        verify(server.getPluginManager(), never()).registerEvents(any(), any());
    }
    @Test public void shutdownClosesDiscordAndDatabaseAndCancelsTasks() throws Exception {
        net.dv8tion.jda.api.JDA jda = mock(net.dv8tion.jda.api.JDA.class);
        DatabaseManager database = mock(DatabaseManager.class);
        set(discord, DiscordSyncPlugin.class, "jda", jda);
        set(discord, DiscordSyncPlugin.class, "databaseManager", database);
        doCallRealMethod().when(discord).onDisable();
        discord.onDisable();
        verify(jda).shutdownNow(); verify(database).close();
        verify(scheduler).cancelTasks(discord);
        Field field = DiscordSyncPlugin.class.getDeclaredField("jda");
        field.setAccessible(true);
        assertNull(field.get(discord));
    }
    @Test public void shutdownIsSafeBeforeInitialization() {
        doCallRealMethod().when(discord).onDisable();
        discord.onDisable();
        verify(scheduler).cancelTasks(discord);
    }
    private net.astropvp.core.commands.TrailerCommand trailerCommand() throws Exception {
        net.astropvp.core.AstroCore core = mock(net.astropvp.core.AstroCore.class);
        when(core.getConfig()).thenReturn(config);
        set(null, net.astropvp.core.AstroCore.class, "instance", core);
        return new net.astropvp.core.commands.TrailerCommand();
    }
    @Test public void trailerCommandAcceptsConsoleWithoutClassCast() throws Exception {
        org.bukkit.command.CommandSender console = mock(org.bukkit.command.CommandSender.class);
        assertTrue(trailerCommand().onCommand(console, null, "trailer", new String[0]));
        verify(console).sendMessage("This command is only available to players.");
        verifyNoInteractions(scheduler);
    }
    @Test public void missingYouTubeKeyDoesNotSendNetworkRequests() throws Exception {
        assertTrue(trailerCommand().onCommand(player, null, "trailer", new String[]{"https://youtu.be/dQw4w9WgXcQ"}));
        verifyNoInteractions(scheduler);
        verify(player).sendMessage(anyString());
    }
    @Test public void duplicateMinecraftLinkCannotBeInsertedTwice() {
        UUID id = player.getUniqueId();
        assertTrue(db.insertLink(id, 123));
        assertFalse(db.insertLink(id, 456));
        assertEquals(Long.valueOf(123), db.getCachedResults().get(id));
    }
    @Test public void duplicateDiscordLinkIsRejected() throws Exception {
        when(results.next()).thenReturn(false, true);
        assertFalse(db.insertLink(player.getUniqueId(), 123));
        verify(statement, never()).executeUpdate();
    }
    @Test public void verificationConsumesCodeOnServerThreadBeforeDatabaseWork() {
        me.astropvp.discordsync.managers.SyncManager sync = mock(me.astropvp.discordsync.managers.SyncManager.class);
        Map<Integer, UUID> codes = new HashMap<>();
        codes.put(12345, player.getUniqueId());
        when(sync.getActiveSyncMap()).thenReturn((HashMap<Integer, UUID>) codes);
        when(discord.getSyncManager()).thenReturn(sync);
        when(server.getPlayer(player.getUniqueId())).thenReturn(player);
        DatabaseManager database = mock(DatabaseManager.class);
        when(discord.getDatabaseManager()).thenReturn(database);
        PrivateMessageReceivedEvent event = mock(PrivateMessageReceivedEvent.class, RETURNS_DEEP_STUBS);
        when(event.getMessage().getContentStripped()).thenReturn("!verify 12345");
        new DiscordListener(discord).onEvent(event);
        assertTrue(codes.containsKey(12345));
        verifyNoInteractions(database);
        ArgumentCaptor<Runnable> mainTask = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).runTask(eq(discord), mainTask.capture());
        mainTask.getValue().run();
        assertFalse(codes.containsKey(12345));
        verifyNoInteractions(database);
        ArgumentCaptor<Runnable> databaseTask = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).runTaskAsynchronously(eq(discord), databaseTask.capture());
        databaseTask.getValue().run();
        verify(database).insertLink(eq(player.getUniqueId()), anyLong());
        verify(event.getChannel()).sendMessage("Could not link these accounts. They may already be linked; please try /discordsync again.");
        verify(discord, never()).getJda();
    }
    @Test public void pendingTrailerRequestWaitsInsteadOfReportingImmediateFailure() throws Exception {
        net.astropvp.core.commands.TrailerCommand command = trailerCommand();
        config.set("youtube-api-key", "test-key-never-sent");
        org.bukkit.scheduler.BukkitTask task = mock(org.bukkit.scheduler.BukkitTask.class);
        when(task.getTaskId()).thenReturn(42);
        when(scheduler.runTaskAsynchronously(any(org.bukkit.plugin.Plugin.class), any(Runnable.class))).thenReturn(task);
        when(scheduler.runTaskTimer(any(org.bukkit.plugin.Plugin.class), any(Runnable.class), anyLong(), anyLong())).thenReturn(task);
        assertTrue(command.onCommand(player, null, "trailer", new String[]{"https://youtu.be/dQw4w9WgXcQ"}));
        ArgumentCaptor<Runnable> poll = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).runTaskTimer(any(org.bukkit.plugin.Plugin.class), poll.capture(), eq(20L), eq(20L));
        clearInvocations(player);
        poll.getValue().run();
        verify(player, never()).sendMessage(anyString());
        // With no worker execution, the bounded wait eventually times out and clears the request.
        for (int i = 1; i < 30; i++) poll.getValue().run();
        verify(player).sendMessage(contains("Please try again"));
        verify(scheduler).cancelTask(42);
        clearInvocations(player);
        command.onCommand(player, null, "trailer", new String[]{"https://youtu.be/dQw4w9WgXcQ"});
        verify(player).sendMessage(contains("Please wait a little bit"));
    }
    @Test public void realSqlLinkRoundTripAndDuplicateChecks() throws Exception {
        try (Connection actual = DriverManager.getConnection("jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL")) {
            DatabaseManager database = new DatabaseManager(discord);
            set(database, DatabaseManager.class, "connection", actual);
            database.createTable();
            UUID first = UUID.randomUUID();
            assertTrue(database.insertLink(first, 123));
            assertEquals(Long.valueOf(123), database.get(first));
            assertFalse(database.insertLink(first, 456));
            assertFalse(database.insertLink(UUID.randomUUID(), 123));
            database.remove(first);
            assertNull(database.get(first));
            assertTrue(database.fetchMap().isEmpty());
        }
    }
    @Test public void realSqlRefreshReplacesChangedAndDeletedRows() throws Exception {
        try (Connection actual = DriverManager.getConnection("jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL")) {
            DatabaseManager database = new DatabaseManager(discord);
            set(database, DatabaseManager.class, "connection", actual);
            database.createTable();
            UUID id = UUID.randomUUID();
            assertTrue(database.insertLink(id, 123));
            try (Statement update = actual.createStatement()) {
                update.executeUpdate("UPDATE discord_data SET discordId=456");
            }
            assertEquals(Long.valueOf(456), database.fetchMap().get(id));
            assertEquals(Long.valueOf(456), database.get(id));
            try (Statement delete = actual.createStatement()) {
                delete.executeUpdate("DELETE FROM discord_data");
            }
            assertTrue(database.fetchMap().isEmpty());
            assertNull(database.get(id));
        }
    }
    @Test public void historicalClaimsWithTrackingParametersRemainClaimed() {
        assertEquals("dQw4w9WgXcQ", YouTubeVideoUrl.normalizeStoredId("dQw4w9WgXcQ&t=42"));
        assertEquals("dQw4w9WgXcQ", YouTubeVideoUrl.normalizeStoredId("dQw4w9WgXcQ#comment"));
        assertEquals("dQw4w9WgXcQ", YouTubeVideoUrl.normalizeStoredId("dQw4w9WgXcQ"));
        assertNull(YouTubeVideoUrl.normalizeStoredId(null));
        assertNull(YouTubeVideoUrl.normalizeStoredId(""));
    }
}

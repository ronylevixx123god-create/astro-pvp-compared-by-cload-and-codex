package me.astropvp.discordsync.managers;

import me.astropvp.discordsync.DiscordSyncPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class DatabaseManager {
    private Connection connection;
    private final DiscordSyncPlugin plugin;
    private final Map<UUID, Long> cachedResults = new HashMap<>();
    private boolean closed;

    public synchronized Connection getConnection() {
        return connection;
    }

    public DatabaseManager(DiscordSyncPlugin plugin) {
        this.plugin = plugin;
    }

    public void autoReconnect(int interval) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // connect() preserves a healthy connection instead of closing it mid-query.
                    connect();
                } catch (Exception failure) {
                    logFailure("reconnect", failure);
                }
            }
        }.runTaskTimerAsynchronously(plugin, interval * 20L, interval * 20L);
    }

    public synchronized boolean isConnected() {
        try {
            return !closed && connection != null && !connection.isClosed();
        } catch (SQLException failure) {
            return false;
        }
    }

    public synchronized void insert(UUID uuid, long discordId) {
        insertLink(uuid, discordId);
    }

    /** Serializes checks and writes so concurrent verification cannot link an account twice. */
    public synchronized boolean insertLink(UUID uuid, long discordId) {
        try {
            if (get(uuid) != null) {
                return false;
            }
            try (PreparedStatement lookup = connection.prepareStatement("SELECT discordId FROM discord_data WHERE discordId=?")) {
                lookup.setLong(1, discordId);
                try (ResultSet results = lookup.executeQuery()) {
                    if (results.next()) {
                        return false;
                    }
                }
            }
            try (PreparedStatement insert = connection.prepareStatement("INSERT INTO discord_data (uuid,discordId) VALUES (?,?)")) {
                insert.setString(1, uuid.toString());
                insert.setLong(2, discordId);
                insert.executeUpdate();
                cachedResults.put(uuid, discordId);
                return true;
            }
        } catch (Exception failure) {
            logFailure("insert", failure);
            return false;
        }
    }

    public synchronized void remove(UUID uuid) {
        try (PreparedStatement remove = connection.prepareStatement("DELETE FROM discord_data WHERE uuid=?")) {
            remove.setString(1, uuid.toString());
            remove.executeUpdate();
            cachedResults.remove(uuid);
        } catch (Exception failure) {
            logFailure("remove", failure);
        }
    }

    public synchronized Long get(UUID uuid) throws SQLException {
        if (cachedResults.containsKey(uuid)) {
            return cachedResults.get(uuid);
        }
        if (!isConnected()) {
            throw new SQLException("Discord database is not connected");
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT discordId FROM discord_data WHERE uuid=?")) {
            statement.setString(1, uuid.toString());
            try (ResultSet results = statement.executeQuery()) {
                if (!results.next()) {
                    return null;
                }
                long discordId = results.getLong("discordId");
                if (results.wasNull()) {
                    return null;
                }
                cachedResults.put(uuid, discordId);
                return discordId;
            }
        }
    }

    public synchronized boolean contains(UUID uuid) {
        try {
            return get(uuid) != null;
        } catch (Exception failure) {
            logFailure("lookup", failure);
            return false;
        }
    }

    public synchronized boolean contains(Long discordId) {
        if (discordId == null || !isConnected()) {
            return false;
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT discordId FROM discord_data WHERE discordId=?")) {
            statement.setLong(1, discordId);
            try (ResultSet results = statement.executeQuery()) {
                return results.next();
            }
        } catch (Exception failure) {
            logFailure("lookup", failure);
            return false;
        }
    }

    public synchronized Map<UUID, Long> fetchMap() {
        if (!isConnected()) {
            return Collections.emptyMap();
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT uuid, discordId FROM discord_data");
             ResultSet results = statement.executeQuery()) {
            Map<UUID, Long> fetched = new HashMap<>();
            while (results.next()) {
                try {
                    UUID uuid = UUID.fromString(results.getString("uuid"));
                    long discordId = results.getLong("discordId");
                    if (!results.wasNull()) {
                        fetched.put(uuid, discordId);
                    }
                } catch (IllegalArgumentException invalidUuid) {
                    plugin.getLogger().warning("Skipping a Discord link with an invalid UUID.");
                }
            }
            // Replace the snapshot: removed or changed links must not survive in the cache.
            cachedResults.clear();
            cachedResults.putAll(fetched);
            return fetched;
        } catch (Exception failure) {
            logFailure("refresh", failure);
            return Collections.emptyMap();
        }
    }

    @Deprecated
    public ResultSet executeAsync(String sql) {
        return null;
    }

    public synchronized void connect() throws Exception {
        if (closed) {
            throw new SQLException("Discord database manager has been shut down");
        }
        if (isConnected() && connection.isValid(5)) {
            return;
        }
        if (connection != null) {
            connection.close();
            connection = null;
        }
        ConfigurationSection sql = plugin.getConfig().getConfigurationSection("mysql");
        if (sql == null || sql.getString("host", "").trim().isEmpty()
                || sql.getString("database", "").trim().isEmpty()
                || sql.getString("username", "").trim().isEmpty()) {
            throw new IllegalArgumentException("mysql.host, mysql.database and mysql.username are required");
        }
        int port = sql.getInt("port", 3306);
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("mysql.port must be between 1 and 65535");
        }
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://" + sql.getString("host").trim() + ":" + port + "/" + sql.getString("database").trim();
        Properties properties = new Properties();
        properties.setProperty("user", sql.getString("username"));
        properties.setProperty("password", sql.getString("password", ""));
        properties.setProperty("connectTimeout", "10000");
        properties.setProperty("socketTimeout", "10000");
        connection = DriverManager.getConnection(url, properties);
        cachedResults.clear();
    }

    public synchronized void close() throws SQLException {
        closed = true;
        cachedResults.clear();
        if (connection != null) {
            try {
                connection.close();
            } finally {
                connection = null;
            }
        }
    }

    public synchronized void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS discord_data (uuid VARCHAR(36), discordId BIGINT)");
        }
    }

    private void logFailure(String operation, Exception failure) {
        // JDBC error messages may contain connection details; log the type only.
        plugin.getLogger().warning("Discord database " + operation + " failed (" + failure.getClass().getSimpleName() + ").");
    }

    public DiscordSyncPlugin getPlugin() {
        return plugin;
    }

    public synchronized Map<UUID, Long> getCachedResults() {
        return Collections.unmodifiableMap(new HashMap<>(cachedResults));
    }
}

package me.astropvp.discordsync.managers;

import me.astropvp.discordsync.DiscordSyncPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class DatabaseManager {
    private static final String TABLE_NAME = "discord_data";
    private Connection connection;
    private DiscordSyncPlugin plugin;
    private Map<UUID, Long> cachedResults = new HashMap<>();

    public Connection getConnection() {
        return this.connection;
    }

    public DatabaseManager(DiscordSyncPlugin plugin) {
        this.plugin = plugin;
    }

    public void autoReconnect(int interval) {
        (new BukkitRunnable() {
            public void run() {
                System.out.println("Running Auto Reconnect");

                try {
                    if (DatabaseManager.this.plugin.getDatabaseManager().isConnected()) {
                        System.out.println("DB is currently connected, closing the connection...");
                        DatabaseManager.this.plugin.getDatabaseManager().getConnection().close();
                        System.out.println("Disconnected from DB.");
                    } else {
                        System.out.println("DB is currently NOT connected, opening new connection.");
                    }

                    System.out.println("Opening a new connection.");
                    DatabaseManager.this.plugin.getDatabaseManager().connect();
                    System.out.println("New connection opened successfully.");
                } catch (Exception var2) {
                    var2.printStackTrace();
                    System.out.println("Error in console.");
                }

            }
        }).runTaskTimerAsynchronously(this.plugin, (long)(interval * 20L), (long)(interval * 20L));
    }

    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public void insert(UUID uuid, long discordId) {
        try {
            PreparedStatement insert = this.connection.prepareStatement("INSERT INTO discord_data (uuid,discordId) VALUES (?,?)");
            insert.setString(1, uuid.toString());
            insert.setLong(2, discordId);
            insert.executeUpdate();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public void remove(UUID uuid) {
        try {
            PreparedStatement remove = this.connection.prepareStatement("DELETE FROM discord_data WHERE UUID=?");
            remove.setString(1, uuid.toString());
            remove.executeUpdate();
            if (this.cachedResults.containsKey(uuid)) {
                this.cachedResults.remove(uuid);
            }
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

    }

    public Long get(UUID uuid) throws Exception {
        if (this.cachedResults.containsKey(uuid)) {
            return (Long)this.cachedResults.get(uuid);
        } else {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM discord_data WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                return null;
            } else {
                Long discordId = results.getLong("discordId");
                if (discordId != null) {
                    this.cachedResults.put(uuid, discordId);
                }

                return discordId;
            }
        }
    }

    public boolean contains(UUID uuid) {
        if (this.cachedResults.containsKey(uuid)) {
            return true;
        } else {
            try {
                return this.get(uuid) != null;
            } catch (Exception var3) {
                var3.printStackTrace();
                return false;
            }
        }
    }

    public boolean contains(Long discordId) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM discord_data WHERE discordId=?");
            statement.setLong(1, discordId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public Map<UUID, Long> fetchMap() {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM discord_data");
            ResultSet results = statement.executeQuery();
            Map<UUID, Long> fetchedMap = new HashMap<>();

            while(results.next()) {
                try {
                    UUID uuid = UUID.fromString(results.getString("uuid"));
                    Long discordId = results.getLong("discordId");
                    fetchedMap.put(uuid, discordId);
                    this.cachedResults.putIfAbsent(uuid, discordId);
                } catch (Exception var6) {
                    System.out.println("error while mapping data");
                }
            }

            return fetchedMap;
        } catch (Exception var7) {
            var7.printStackTrace();
            return Collections.EMPTY_MAP;
        }
    }

    /** @deprecated */
    @Deprecated
    public ResultSet executeAsync(String sql) {
        return null;
    }

    public void connect() throws Exception {
        synchronized(this) {
            if (this.getConnection() != null && !this.getConnection().isClosed()) {
                System.out.println("Current connect isn't closed!");
            } else {
                Class.forName("com.mysql.jdbc.Driver");
                org.bukkit.configuration.ConfigurationSection sql = this.plugin.getConfig().getConfigurationSection("mysql");
                String url = "jdbc:mysql://" + sql.getString("host") + ":" + sql.getInt("port", 3306) + "/" + sql.getString("database");
                this.connection = DriverManager.getConnection(url, sql.getString("username"), sql.getString("password"));
                System.out.println("DB connected!");
            }
        }
    }

    public void createTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `discord_data` (\t`uuid` VARCHAR(36),\n\t`discordId` BIGINT\n);";
        Statement statement = this.connection.createStatement();
        statement.execute(sqlCreate);
        statement.close();
    }

    public DiscordSyncPlugin getPlugin() {
        return this.plugin;
    }

    public Map<UUID, Long> getCachedResults() {
        return this.cachedResults;
    }
}

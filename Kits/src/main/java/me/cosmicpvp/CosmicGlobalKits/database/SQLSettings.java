package me.cosmicpvp.CosmicGlobalKits.database;

import org.bukkit.configuration.file.FileConfiguration;

/** Shared interpretation of existing flat and legacy nested SQL configuration. */
final class SQLSettings {
    final String host, user, password, database;
    final int port;

    private SQLSettings(FileConfiguration config) {
        host = value(config, "hostname", "hostname");
        user = config.getString("user", config.getString("sql.user", config.getString("sql.username", "")));
        password = value(config, "password", "password");
        database = value(config, "database", "database");
        port = config.getInt("port", config.getInt("sql.port", 3306));
        if (host.trim().isEmpty() || user.trim().isEmpty() || database.trim().isEmpty()) {
            throw new IllegalArgumentException("Set hostname, user and database in the flat or legacy sql section of sqlconfig.yml");
        }
        if (port < 1 || port > 65535) throw new IllegalArgumentException("Invalid SQL port");
    }

    private static String value(FileConfiguration config, String key, String legacy) {
        return config.getString(key, config.getString("sql." + legacy, ""));
    }

    static SQLSettings read(FileConfiguration config) { return new SQLSettings(config); }

    String url() { return "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true"; }
}

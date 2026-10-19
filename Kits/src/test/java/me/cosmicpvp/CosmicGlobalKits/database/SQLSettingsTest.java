package me.cosmicpvp.CosmicGlobalKits.database;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;

public class SQLSettingsTest {
    private YamlConfiguration legacy() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("sql.hostname", "127.0.0.1");
        config.set("sql.username", "fixture");
        config.set("sql.database", "fixture_db");
        config.set("sql.password", "");
        return config;
    }

    @Test public void shippedNestedUsernameFormatRetainsDefaultPort() {
        SQLSettings settings = SQLSettings.read(legacy());
        assertEquals("fixture", settings.user);
        assertEquals("", settings.password);
        assertEquals("jdbc:mysql://127.0.0.1:3306/fixture_db?autoReconnect=true", settings.url());
    }

    @Test public void nestedUserAliasAndPortAreSupported() {
        YamlConfiguration config = legacy();
        config.set("sql.user", "old_user");
        config.set("sql.port", 33306);
        SQLSettings settings = SQLSettings.read(config);
        assertEquals("old_user", settings.user);
        assertEquals(33306, settings.port);
    }

    @Test public void existingFlatSettingsTakePriorityWithoutChangingTheConfig() {
        YamlConfiguration config = legacy();
        config.set("hostname", "localhost");
        config.set("user", "flat_user");
        config.set("database", "flat_database");
        config.set("port", 33306);
        String before = config.saveToString();
        SQLSettings settings = SQLSettings.read(config);
        assertEquals("flat_user", settings.user);
        assertEquals("jdbc:mysql://localhost:33306/flat_database?autoReconnect=true", settings.url());
        assertEquals(before, config.saveToString());
    }

    @Test(expected=IllegalArgumentException.class) public void invalidPortFailsBeforeConnecting() {
        YamlConfiguration config = legacy();
        config.set("port", 70000);
        SQLSettings.read(config);
    }
}

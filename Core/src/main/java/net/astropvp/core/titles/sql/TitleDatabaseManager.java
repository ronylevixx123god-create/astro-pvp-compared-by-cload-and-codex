/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Event
 */
package net.astropvp.core.titles.sql;

import net.astropvp.core.titles.events.TitleChangeEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class TitleDatabaseManager {
    private HashMap<UUID, ArrayList<String>> unlockedTitles = new HashMap();
    private HashMap<String, String> equippedTitles = new HashMap();

    public void cacheAllEquippedTitles() {
        try {
            PreparedStatement statement = SQLConnection.getConnection(true).prepareStatement("SELECT UUID, ActiveTitle FROM `titledata`");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                this.equippedTitles.put(rs.getString("UUID"), rs.getString("ActiveTitle"));
            }
            statement.close();
            Bukkit.getLogger().info("[CosmicPlayerTitles] Cached " + this.equippedTitles.size() + " custom playerTitles");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAllEquippedTitlesToSQL() {
        Bukkit.getLogger().info("[CosmicPlayerTitles] Saving " + this.equippedTitles.size() + " equippedTitles to SQL...");
        for (Map.Entry<String, String> data : this.equippedTitles.entrySet()) {
            String playerUUID = data.getKey();
            String activeTitle = data.getValue();
            try {
                SQLConnection.executeSQLQuery("INSERT INTO titledata(UUID, ActiveTitle) VALUES ('" + playerUUID + "','" + activeTitle + "') ON DUPLICATE KEY UPDATE `ActiveTitle`='" + activeTitle + "'", false);
            }
            catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    public void setEquippedTitle(UUID uuid, String newTitle) {
        this.equippedTitles.put(uuid.toString(), newTitle);
        Bukkit.getPluginManager().callEvent((Event)new TitleChangeEvent(uuid, newTitle));
    }

    public String getEquippedTitle(UUID uuid) {
        return this.equippedTitles.containsKey(uuid.toString()) ? this.equippedTitles.get(uuid.toString()) : "none";
    }

    public HashMap<UUID, ArrayList<String>> getUnlockedTitles() {
        return this.unlockedTitles;
    }

    public HashMap<String, String> getEquippedTitles() {
        return this.equippedTitles;
    }
}


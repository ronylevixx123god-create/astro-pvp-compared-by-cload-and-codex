/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package me.cosmicpvp.CosmicGlobalKits.struct;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.database.SQLConnection;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class GlobalKitUser {
    private UUID uuid;
    private Map<GlobalKits, GlobalKitData> globalKits = new HashMap<GlobalKits, GlobalKitData>();
    private boolean loadingData = true;

    public GlobalKitUser(UUID uuid) {
        this.uuid = uuid;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadGlobalKitData(boolean async, Consumer<GlobalKitUser> callback) {
        if (async && Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CosmicGlobalKits.getPlugin(), () -> this.loadGlobalKitData(false, callback));
            return;
        }
        String uuidString = this.uuid.toString();
        long start = System.currentTimeMillis();
        PreparedStatement statement = null;
        try {
            statement = SQLConnection.getConnection().prepareStatement("SELECT global_kit, last_use FROM globalkits_userdata WHERE player_uuid = ?;");
            statement.setString(1, uuidString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                try {
                    String lastUseName = rs.getString("global_kit");
                    if (lastUseName == null || lastUseName.isEmpty()) continue;
                    long lastUse = rs.getLong("last_use");
                    GlobalKits gkit = GlobalKits.valueOf(rs.getString("global_kit"));
                    GlobalKitData data = this.getGlobalKits().computeIfAbsent(gkit, newD -> new GlobalKitData(lastUse, 0));
                    if (data.getLastUsed() > lastUse) continue;
                    data.setLastUsed(lastUse);
                }
                catch (Exception err) {}
            }
            statement.close();
            statement = SQLConnection.getConnection().prepareStatement("SELECT server_name, global_kit, tier FROM globalkit_player_tiers WHERE player_uuid = ? AND server_name = ?;");
            statement.setString(1, uuidString);
            statement.setString(2, CosmicGlobalKits.getPlugin().getServerName());
            rs = statement.executeQuery();
            while (rs.next()) {
                try {
                    String servername = rs.getString("server_name");
                    String name = rs.getString("global_kit");
                    if (name == null || name.isEmpty() || servername == null || !servername.equalsIgnoreCase(CosmicGlobalKits.getPlugin().getServerName())) continue;
                    int tier = rs.getInt("tier");
                    GlobalKits gkitWithLevel = GlobalKits.valueOf(rs.getString("global_kit"));
                    GlobalKitData gkitData = this.getGlobalKits().computeIfAbsent(gkitWithLevel, newDa -> new GlobalKitData(0L, tier));
                    gkitData.setKitTier(tier);
                }
                catch (Exception err) {}
            }
            statement.close();
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        finally {
            if (statement != null) {
                try {
                    statement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        CosmicGlobalKits.log("Loaded " + this.getGlobalKits().size() + " global kit tier data for " + uuidString + " in " + (System.currentTimeMillis() - start) + "ms");
        if (callback != null) {
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicGlobalKits.getPlugin(), () -> callback.accept(this));
        }
        this.loadingData = false;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Map<GlobalKits, GlobalKitData> getGlobalKits() {
        return this.globalKits;
    }

    public boolean isLoadingData() {
        return this.loadingData;
    }

    public void setGlobalKits(Map<GlobalKits, GlobalKitData> globalKits) {
        this.globalKits = globalKits;
    }

}


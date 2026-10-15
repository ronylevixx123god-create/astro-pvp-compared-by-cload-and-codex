/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmiccontests.struct.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.contests.CustomDataContest;
import me.ifamasssxd.cosmiccontests.struct.user.ContestData;
import me.ifamasssxd.cosmiccontests.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ContestUser {
    private static volatile Map<UUID, ContestUser> users = new HashMap<UUID, ContestUser>();
    private String name;
    private UUID uuid;
    private Map<Contests, ContestData> contestData = new HashMap<Contests, ContestData>();
    private Map<Contests, Integer> topRankings = new HashMap<Contests, Integer>();
    private boolean pendingChanges = false;
    private int id;
    private static int idCount = 0;
    private String factionTag;

    public ContestUser(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        this.id = idCount++;
    }

    public void updateName(String newName) {
        Bukkit.getLogger().info("Dispatching SQL update for name change of " + this.name + " -> " + newName + " for (" + this.uuid + ")");
        this.name = newName;
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement statement = CosmicContests.get().getSqlDatabase().getConnection().prepareStatement("UPDATE `" + CosmicContests.get().getServerName() + "_contestUsers` SET name = '" + this.name + "' WHERE uuid = '" + this.uuid.toString() + "';");
                statement.executeUpdate();
                statement.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadData(boolean async) {
        if (async && Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CosmicContests.get(), () -> this.loadData(false));
            return;
        }
        try {
            PreparedStatement statement = CosmicContests.get().getSqlDatabase().getConnection().prepareStatement("SELECT contestData FROM `" + CosmicContests.get().getServerName() + "_contestUsers` WHERE uuid = '" + this.uuid.toString() + "'");
            ResultSet rs = statement.executeQuery();
            if (rs.first()) {
                this.loadData(rs.getString("contestData"));
            }
            statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData(String serialized) {
        if (serialized == null) {
            return;
        }
        for (String contestData : serialized.split(",")) {
            if (contestData == null) continue;
            this.loadContest(contestData);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void saveData(boolean async) {
        if (!this.pendingChanges) {
            return;
        }
        if (async && Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CosmicContests.get(), () -> this.saveData(false));
            return;
        }
        try {
            PreparedStatement statement = CosmicContests.get().getSqlDatabase().getConnection().prepareStatement("INSERT IGNORE INTO `" + CosmicContests.get().getServerName() + "_contestUsers`(uuid, name, contestData) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE name = ?, contestData = ?;");
            try {
                StringBuilder contestData = new StringBuilder();
                this.contestData.forEach((cont, data) -> {
                    contestData.append(cont.name()).append(":");
                    data.getData().forEach((key, value) -> contestData.append((String)key).append("#").append(value).append("$"));
                    contestData.append(",");
                });
                String done = contestData.toString();
                if (done.endsWith(",")) {
                    done = done.substring(0, done.length() - 1);
                }
                statement.setString(1, this.uuid.toString());
                statement.setString(2, this.name);
                statement.setString(3, done);
                statement.setString(4, this.name);
                statement.setString(5, done);
                statement.executeUpdate();
                statement.close();
                this.pendingChanges = false;
            }
            finally {
                if (Collections.singletonList(statement).get(0) != null) {
                    statement.close();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContest(String contestStr) {
        if (contestStr.contains(":")) {
            Contests contests;
            String[] args = contestStr.split(":");
            String name = args[0];
            try {
                contests = Contests.valueOf(name);
            }
            catch (Exception e) {
                System.out.println("Contest null for " + name);
                return;
            }
            ContestData data = new ContestData();
            for (String arg : args[1].split("\\$")) {
                if (!arg.contains("#")) continue;
                String[] args2 = arg.split("#");
                String key = args2[0];
                String value = args2[1];
                if (Utils.isInt(value)) {
                    data.getData().put(key, Integer.parseInt(value));
                    continue;
                }
                if (Utils.isDouble(value)) {
                    data.getData().put(key, Double.parseDouble(value));
                    continue;
                }
                if (Utils.isLong(value)) {
                    data.getData().put(key, Long.parseLong(value));
                    continue;
                }
                System.out.println(value + " is not a number!");
            }
            this.contestData.put(contests, data);
        }
    }

    public double incrementValue(Contests contests, double value) {
        if (contests.getContest() instanceof CustomDataContest) {
            String key = ((CustomDataContest)contests.getContest()).getKey();
            double current = this.getDouble(contests, key);
            double newVal = current + value;
            if (newVal >= Double.MAX_VALUE) {
                newVal = Double.MAX_VALUE;
            }
            if (newVal < 0.0 && current > 0.0) {
                return current;
            }
            this.setData(contests, key, newVal);
            return newVal;
        }
        return -1.0;
    }

    public int incrementValue(Contests contests, int value) {
        if (contests.getContest() instanceof CustomDataContest) {
            String key = ((CustomDataContest)contests.getContest()).getKey();
            int current = this.getInt(contests, key);
            int newVal = current + value;
            if (newVal >= Integer.MAX_VALUE) {
                newVal = Integer.MAX_VALUE;
            }
            if (newVal < 0 && current > 0) {
                return current;
            }
            this.setData(contests, key, newVal);
            return newVal;
        }
        return -1;
    }

    public int incrementIntAmount(Contests contests, String key, int value) {
        int current = this.getInt(contests, key);
        int newVal = current + value;
        if (newVal >= Integer.MAX_VALUE) {
            newVal = Integer.MAX_VALUE;
        }
        if (newVal < 0 && current > 0) {
            return current;
        }
        this.setData(contests, key, newVal);
        return newVal;
    }

    public double incrementValue(Contests contests, String key, double value) {
        double current = this.getDouble(contests, key);
        double newVal = current + value;
        if (newVal >= Double.MAX_VALUE) {
            newVal = Double.MAX_VALUE;
        }
        if (newVal < 0.0 && current > 0.0) {
            return current;
        }
        this.setData(contests, key, newVal);
        return newVal;
    }

    public void setData(Contests contest, String key, Object data) {
        ContestData cont = this.contestData.computeIfAbsent(contest, k -> new ContestData());
        cont.getData().put(key, data);
        this.pendingChanges = true;
    }

    public Object getData(Contests contest, String key) {
        ContestData data = this.contestData.get((Object)contest);
        if (data != null) {
            return data.getData().get(key);
        }
        return null;
    }

    public long getLong(Contests contest, String key) {
        Object data = this.getData(contest, key);
        if (data instanceof Long) {
            return (Long)data;
        }
        return 0L;
    }

    public String getString(Contests contest, String key) {
        Object data = this.getData(contest, key);
        if (data instanceof String) {
            return (String)data;
        }
        return null;
    }

    public int getInt(Contests contest) {
        if (contest.getContest() instanceof CustomDataContest) {
            return this.getInt(contest, ((CustomDataContest)contest.getContest()).getKey());
        }
        return 0;
    }

    public double getDouble(Contests contest, String key) {
        Object data = this.getData(contest, key);
        if (data instanceof Double) {
            return (Double)data;
        }
        if (data instanceof Integer) {
            return ((Integer)data).doubleValue();
        }
        return 0.0;
    }

    public int getInt(Contests contest, String key) {
        Object data = this.getData(contest, key);
        if (data instanceof Integer) {
            return (Integer)data;
        }
        if (data instanceof Double) {
            double val = (Double)data;
            if (val > 2.147483647E9) {
                return Integer.MAX_VALUE;
            }
            return (int)Math.round(val);
        }
        return 0;
    }

    public static ContestUser getUser(UUID uuid) {
        return users.get(uuid);
    }

    public static Map<UUID, ContestUser> getUsers() {
        return users;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Map<Contests, ContestData> getContestData() {
        return this.contestData;
    }

    public Map<Contests, Integer> getTopRankings() {
        return this.topRankings;
    }

    public boolean isPendingChanges() {
        return this.pendingChanges;
    }

    public void setPendingChanges(boolean pendingChanges) {
        this.pendingChanges = pendingChanges;
    }

    public int getId() {
        return this.id;
    }

    public String getFactionTag() {
        return this.factionTag;
    }

    public void setFactionTag(String factionTag) {
        this.factionTag = factionTag;
    }
}


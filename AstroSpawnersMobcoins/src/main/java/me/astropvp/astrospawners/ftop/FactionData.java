package me.astropvp.astrospawners.ftop;

import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import me.astropvp.astrospawners.AstroSpawners;
import me.astropvp.astrospawners.data.SpawnerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

public class FactionData {
    private Faction faction;

    private Object[] richestMember;

    private long memberValue;

    private long spawnerValue;

    private long spawners;

    private long stackedSpawners;

    public FactionData(Faction faction) {
        this.faction = faction;
        this.spawnerValue = 0L;
        this.memberValue = 0L;
        this.spawners = 0L;
        this.stackedSpawners = 0L;
        this.richestMember = new Object[2];
        double cash = 0.0D;
        String richest = "";
        for (FPlayer player : faction.getFPlayers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getName());
            if (offlinePlayer == null)
                continue;
            double balance = EconomyUtils.getBalance(offlinePlayer);
            if (balance > cash) {
                cash = balance;
                richest = offlinePlayer.getName();
            }
            this.memberValue = (long)(this.memberValue + balance);
        }
        this.richestMember[0] = richest;
        this.richestMember[1] = Long.valueOf((long)cash);
        String tag = this.faction.getTag();
        for (SpawnerData data : AstroSpawners.getInstance().getSpawnerManager().getCacheValues()) {
            Location location = SpawnerData.locationFromString(data.getKey());
            if(location.getY() < 200) continue;
            if (FactionUtils.getFactionAt(location).getTag().contentEquals(tag)) {
                this.stackedSpawners++;
                this.spawners += data.getSpawnerAmount();
                this.spawnerValue += FactionsTopAPI.getValueOfSpawner(data.getEntityType()) * data.getSpawnerAmount();
            }
        }
    }

    public long getSpawnerCount() {
        return this.spawners;
    }

    public long getSpawnerValue() {
        return this.spawnerValue;
    }

    public long getMemberValue() {
        return this.memberValue;
    }

    public long getStackedSpawnerCount() {
        return this.stackedSpawners;
    }

    public Faction getFaction() {
        return this.faction;
    }

    public String getRichestPlayer() {
        return (String)this.richestMember[0];
    }

    public long getRichestPlayerBalance() {
        return ((Long)this.richestMember[1]).longValue();
    }
}

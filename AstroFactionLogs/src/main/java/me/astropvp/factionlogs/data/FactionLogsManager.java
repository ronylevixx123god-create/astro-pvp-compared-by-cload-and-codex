package me.astropvp.factionlogs.data;

import lombok.Getter;
import me.astropvp.factionlogs.FactionLogs;
import net.astropvp.storage.utils.SaveLoadManager;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FactionLogsManager extends SaveLoadManager<FactionLogsData> {

    private final Map<String, FactionLogsManager> logs;

    public FactionLogsManager() {
        super(FactionLogs.getInstance(), FactionLogs.getInstance().getGson(), "data/", FactionLogsData.class);
        this.logs = new HashMap<>();
    }

    public FactionLogsData getFactionLogsData(String factionId) {
        if (!contains(factionId)) {
            FactionLogsData factionLogsData = new FactionLogsData(factionId);
            addToCache(factionLogsData);
        }
        return getFromCache(factionId);
    }
}

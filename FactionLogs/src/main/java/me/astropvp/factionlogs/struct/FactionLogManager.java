package me.astropvp.factionlogs.struct;

import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;

import java.util.*;

@Getter
public class FactionLogManager {
    private final Map<LogType, List<FactionLog>> factionLogs;

    public FactionLogManager() {
        this.factionLogs = new HashMap<>();
    }

    public void addFactionLogToFaction(LogType logType, FactionLog factionLog) {
        if (this.factionLogs.containsKey(logType)) {
            List<FactionLog> currentLogs = this.factionLogs.get(logType);
            currentLogs.add(factionLog);
        } else {
            this.factionLogs.put(logType, new ArrayList<>(Collections.singletonList(factionLog)));
        }
    }
}

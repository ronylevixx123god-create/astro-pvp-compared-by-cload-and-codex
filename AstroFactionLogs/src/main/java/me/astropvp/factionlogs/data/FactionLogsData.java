package me.astropvp.factionlogs.data;

import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import net.astropvp.storage.utils.KeyToSave;

import java.util.*;

@Getter
public class FactionLogsData implements KeyToSave {
    private String factionId;
    Map<LogType, List<FactionLog>> logs;

    public FactionLogsData(String factionId) {
        this.factionId = factionId;
        this.logs = new HashMap<>();
    }

    public void addFactionLog(FactionLog factionLog) {
        if (this.logs.containsKey(factionLog.getLogtype())) {
            this.logs.get(factionLog.getLogtype()).add(factionLog);
        } else {
            List<FactionLog> list = new ArrayList<>();
            list.add(factionLog);
            this.logs.put(factionLog.getLogtype(), list);
        }
    }



    @Override
    public String getKey() {
        return factionId;
    }

    @Override
    public void setKey(String s) {
        this.factionId = s;
    }
}

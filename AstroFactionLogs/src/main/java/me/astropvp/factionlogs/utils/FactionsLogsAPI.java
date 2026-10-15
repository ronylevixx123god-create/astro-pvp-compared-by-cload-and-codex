package me.astropvp.factionlogs.utils;


import com.massivecraft.factions.Faction;
import me.astropvp.factionlogs.FactionLogs;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;

import java.util.*;

public class FactionsLogsAPI {
    public static void log(Faction faction, FactionLog factionLog) {
        try {
            FactionLogs.getInstance().getLogManager().getFactionLogsData(faction.getId()).addFactionLog(factionLog);
        } catch (NullPointerException exception) {

        }
    }

    public static void log(String id, FactionLog factionLog) {
        try {
            FactionLogs.getInstance().getLogManager().getFactionLogsData(id).addFactionLog(factionLog);
        } catch (NullPointerException exception) {

        }    }

    public static List<FactionLog> getFactionLogs(String id, LogType logType) {
        if (FactionLogs.getInstance().getLogManager().getFactionLogsData(id).getLogs().containsKey(logType)) {
            return FactionLogs.getInstance().getLogManager().getFactionLogsData(id).getLogs().get(logType);
        }
        return Collections.emptyList();
    }

    public static List<FactionLog> getFactionLogs(Faction faction, LogType logType) {
        if (FactionLogs.getInstance().getLogManager().getFactionLogsData(faction.getId()).getLogs().containsKey(logType)) {
            return FactionLogs.getInstance().getLogManager().getFactionLogsData(faction.getId()).getLogs().get(logType);
        }
        return Collections.emptyList();

    }

    public static Map<LogType, List<FactionLog>> getFactionLogs(Faction faction) {
        return FactionLogs.getInstance().getLogManager().getFactionLogsData(faction.getId()).getLogs();
    }

    public static boolean hasEmptyLog(Faction faction) {
        return FactionLogs.getInstance().getLogManager().getFactionLogsData(faction.getId()).getLogs().isEmpty();
    }


}

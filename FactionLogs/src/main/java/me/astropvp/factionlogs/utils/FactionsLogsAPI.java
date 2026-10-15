package me.astropvp.factionlogs.utils;


import com.massivecraft.factions.Faction;
import me.astropvp.factionlogs.FactionLogs;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;

import java.util.*;

public class FactionsLogsAPI {
    public static void log(Faction faction, FactionLog factionLog) {
        try {
            FactionLogs.getInstance().getLogManager().log(faction.getId(), factionLog);
        } catch (NullPointerException exception) {

        }
    }

    public static void log(String id, FactionLog factionLog) {
        FactionLogs.getInstance().getLogManager().log(id, factionLog);
    }

    public static List<FactionLog> getFactionLogs(String id, LogType logType) {
        if (!FactionLogs.getInstance().getLogManager().getFactionLogManager(id).getFactionLogs().containsKey(logType)) {
            FactionLogs.getInstance().getLogManager().getFactionLogManager(id).getFactionLogs().put(logType, new ArrayList<>());
        }
        return FactionLogs.getInstance().getLogManager().getFactionLogManager(id).getFactionLogs().get(logType);
    }

    public static List<FactionLog> getFactionLogs(Faction faction, LogType logType) {
        if (!FactionLogs.getInstance().getLogManager().getFactionLogManager(faction.getId()).getFactionLogs().containsKey(logType)) {
            FactionLogs.getInstance().getLogManager().getFactionLogManager(faction.getId()).getFactionLogs().put(logType, new ArrayList<>());
        }

        return FactionLogs.getInstance().getLogManager().getFactionLogManager(faction.getId()).getFactionLogs().get(logType);
    }

    public static Map<LogType, List<FactionLog>> getFactionLogs(Faction faction) {
        return FactionLogs.getInstance().getLogManager().getFactionLogManager(faction.getId()).getFactionLogs();
    }

    public static boolean hasEmptyLog(Faction faction) {
        return FactionLogs.getInstance().getLogManager().getFactionLogManager(faction.getId()) == null;
    }


}

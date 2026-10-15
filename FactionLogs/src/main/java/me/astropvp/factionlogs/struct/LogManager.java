package me.astropvp.factionlogs.struct;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import lombok.Getter;
import lombok.SneakyThrows;
import me.astropvp.factionlogs.FactionLogs;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.impl.*;
import me.astropvp.factionlogs.utils.FactionsLogsAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class LogManager {
    private final Map<String, FactionLogManager> logs;

    public LogManager() {
        this.logs = new HashMap<>();
    }

    public void log(String id, FactionLog factionLog) {
        FactionLogManager factionLogManager = getFactionLogManager(id);
        factionLogManager.addFactionLogToFaction(factionLog.getLogtype(), factionLog);
    }

    public FactionLogManager getFactionLogManager(String id) {
        if (!logs.containsKey(id)) {
            logs.put(id, new FactionLogManager());
        }
        return logs.get(id);
    }

    public void load() {

        File logs = new File(FactionLogs.getInstance().getDataFolder(), "logs");
        if (!logs.exists()) {
            logs.mkdir();
        }

        File[] files = (new File(FactionLogs.getInstance().getDataFolder(), "logs")).listFiles();
        if(files == null) return;
        if(files.length <= 0) return;
        for(File file : files) {
            try {
                loadDataForFaction(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SneakyThrows
    private void loadDataForFaction(File file) {
        Faction faction = Factions.getInstance().getFactionById(file.getName().replace(".yml", ""));
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(file);


        yamlConfiguration.getConfigurationSection("Logs").getKeys(false).forEach(log -> {
            yamlConfiguration.getStringList("Logs."+log).forEach(serializedString -> {
                LogType logType = LogType.valueOf(log);
                try {


                switch (logType) {
                    case CLAIM:
                        FactionsLogsAPI.log(faction, LandClaim.deserialize(serializedString));
                        break;
                    case RADIUSCLAIM:
                        FactionsLogsAPI.log(faction, RadiusLandClaim.deserialize(serializedString));
                        break;
                    case UNCLAIM:
                        FactionsLogsAPI.log(faction, LandUnclaim.deserialize(serializedString));
                        break;
                    case UNCLAIMALL:
                        FactionsLogsAPI.log(faction, LandUnclaimAll.deserialize(serializedString));
                        break;
                    case PLAYER_JOIN:
                        FactionsLogsAPI.log(faction, PlayerJoin.deserialize(serializedString));
                        break;
                    case PLAYER_LEAVE:
                        FactionsLogsAPI.log(faction, PlayerLeave.deserialize(serializedString));
                        break;
                    case CHUNKBUST:
                        FactionsLogsAPI.log(faction, ChunkBust.deserialize(serializedString));
                        break;
                    case SPAWNER_BREAK:
                        FactionsLogsAPI.log(faction, SpawnerBreak.deserialize(serializedString));
                        break;
                    case SPAWNER_PLACE:
                        FactionsLogsAPI.log(faction, SpawnerPlace.deserialize(serializedString));
                        break;
                    case RELATION_EDITS:
                        FactionsLogsAPI.log(faction, RelationEdits.deserialize(serializedString));
                        break;
                    case FACTION_RENAME:
                        FactionsLogsAPI.log(faction, FactionRename.deserialize(serializedString));
                        break;
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @SneakyThrows
    public void save() {
        for(Faction faction : Factions.getInstance().getAllFactions()) {
            if (!logs.containsKey(faction.getId())) continue;
            File file = new File(FactionLogs.getInstance().getDataFolder(), "logs/" + faction.getId() + ".yml");
            if(FactionsLogsAPI.hasEmptyLog(faction)) return;
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(file);

            for (LogType logType : LogType.values()) {

                List<String> stringsToLog = new ArrayList<>();
                for(FactionLog log : FactionsLogsAPI.getFactionLogs(faction, logType)) {
                    try {
                        stringsToLog.add(log.serialize());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                yamlConfiguration.set("Logs."+logType.name(), stringsToLog);

            }
            yamlConfiguration.save(file);
        }

    }

}

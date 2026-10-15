package me.venir.astrocollectors.collector;

import me.venir.astrocollectors.utils.ChunkUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CollectorManager  {

    public static final CollectorManager DELEGATE = new CollectorManager();

    public final Map<String, Collector> collectors = new HashMap<>();
    private final JSONParser jsonParser = new JSONParser();

    private CollectorManager() {}

    public void loadFromFile(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write("{}");
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try (FileReader fileReader = new FileReader(file)) {
            load((JSONObject)jsonParser.parse(fileReader));
        } catch (IOException | ParseException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    private void load(JSONObject object) {
        for (Object obj : object.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            String key = String.valueOf(entry.getKey());
            String[] components = key.split(";");
            if (components.length != 3) {
                continue;
            }
            World world = Bukkit.getWorld(components[0]);
            if (world == null) {
                continue;
            }
            Object value = entry.getValue();
            if (!(value instanceof JSONObject)) {
                continue;
            }
            collectors.put(key,new Collector(world, (JSONObject)value));
        }
    }

    public void saveToFile(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(save().toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    private JSONObject save() {
        JSONObject jsonObject = new JSONObject();

        collectors.forEach((key,collector) -> {
            jsonObject.put(key,collector.toJsonObject());
        });

        return jsonObject;
    }

    public boolean isCollectorContained(Chunk chunk) {
        return collectors.get(chunkToString(chunk)) != null;
    }

    public boolean isCollectorContained(Location location) {
        return collectors.get(chunkToString(location.getChunk())) != null;
    }

    private String chunkToString(Chunk chunk) {
        char c = ';';
        return chunk.getWorld().getName() +
                c +
                chunk.getX() +
                c +
                chunk.getZ();
    }

    public Collector getCollector(Chunk chunk) {
        return collectors.get(chunkToString(chunk));
    }

    public Collector getCollector(Location location) {
        return collectors.get(chunkToString(location.getChunk()));
    }

    public void removeCollector(Chunk chunk) {
        collectors.remove(chunkToString(chunk)).invalidate();
    }

    public void removeCollector(Location location) {
        collectors.remove(chunkToString(location.getChunk())).invalidate();
    }

    public Collector newCollector(Location location) {
        Collector collector = new Collector(location);
        collectors.put(chunkToString(location.getChunk()),collector);
        return collector;
    }

    public void forEach(Consumer<Collector> action) {
        for (Collector collector : collectors.values()) {
            action.accept(collector);
        }
    }
}

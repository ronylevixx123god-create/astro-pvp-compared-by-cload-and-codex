package net.astropvp.storage.utils;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class SaveLoadManager<T extends KeyToSave> {

    private final File dataDirectory;
    private final Gson gson;
    private final Class<T> clazz;
    private final Map<String, T> cache;

    protected SaveLoadManager(JavaPlugin plugin, Gson gson, String dataDirectory, Class<T> clazz) {
        this.gson = gson;
        this.clazz = clazz;
        this.dataDirectory = new File(plugin.getDataFolder(), dataDirectory);
        this.cache = new HashMap<>();
    }


    public void loadAll() {
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }

        File[] validKitFiles = dataDirectory.listFiles((dir,name) -> name.endsWith(".json"));

        if (validKitFiles == null) {
            return;
        }

        for (File file : validKitFiles) {
            this.load(file);
        }
    }



    public void saveAll() {
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }

        for (Map.Entry<String, T> entry : cache.entrySet()) {
            this.save(entry.getKey(), entry.getValue());
        }

    }

    public void purge() {
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }

        for (File file : Objects.requireNonNull(dataDirectory.listFiles())) {
            String deserialize = (file.getName().substring(0,file.getName().length()-5));
            if (!this.contains(deserialize)) {
                file.deleteOnExit();
            }
        }
    }

    private void load(File file) {
        String fileName = file.getName();
        int fileNameLength = fileName.length();
        String key = fileName.substring(0,fileNameLength-5);

        try (FileReader reader = new FileReader(file)) {
            T t = this.gson.fromJson(reader, clazz);
            if (t == null) {
                return;
            }
            t.setKey(key);
            cache.put(key, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save(String key, T t) {
        File file = new File(dataDirectory,key + ".json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String jsonString = this.gson.toJson(t);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonString);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToCache(T t) {
        this.cache.put(t.getKey(), t);
    }

    public void removeFromCache(T t) {
        this.cache.remove(t.getKey());
    }

    public void removeFromCache(String key) {
        this.cache.remove(key);
    }

    public T getFromCache(String key) {
        return this.cache.get(key);
    }

    public boolean contains(String key) {return this.cache.containsKey(key);}

    public Collection<T> getCacheValues() {
        return this.cache.values();
    }

    public Set<String> getCacheKeys() {
        return this.cache.keySet();
    }
}

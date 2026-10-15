/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.decimatemc.decimatecommons.inventory.InventoryManager
 *  com.google.common.collect.Maps
 *  net.minecraft.util.com.google.gson.reflect.TypeToken
 *  org.bukkit.entity.Player
 *  org.javatuples.Pair
 */
package net.astropvp.lootboxes.manager;

import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import net.astropvp.lootboxes.AstroLootboxes;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.gson.GsonFactory;
import net.astropvp.lootboxes.gui.create.LootboxCreateGui;
import net.astropvp.lootboxes.lootbox.IncompleteLootbox;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.javatuples.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LootboxCreationManager {
    private final LootboxManager lootboxManager;
    private final InventoryManager inventoryManager;
    private final AstroLootboxes plugin;
    private final LootboxCreateGui lootboxCreateGui;
    private final Map<UUID, Pair<IncompleteLootbox, LootboxManager>> creationSessionMap;

    public LootboxCreationManager(LootboxManager lootboxManager, InventoryManager inventoryManager, AstroLootboxes plugin) {
        this.lootboxManager = lootboxManager;
        this.inventoryManager = inventoryManager;
        this.plugin = plugin;
        this.lootboxCreateGui = new LootboxCreateGui(this.lootboxManager, this.inventoryManager, this, this.plugin);
        this.creationSessionMap = Maps.newHashMap();
        this.load();
    }

    public void startCreationSession(Player player) {
        if (this.creationSessionMap.containsKey(player.getUniqueId())) {
            this.lootboxCreateGui.getInventory().open(player);
            return;
        }
        IncompleteLootbox incompleteLootbox = new IncompleteLootbox();
        this.creationSessionMap.put(player.getUniqueId(), (Pair<IncompleteLootbox, LootboxManager>) Pair.with(incompleteLootbox, this.lootboxManager));
        this.lootboxCreateGui.getInventory().open(player);
    }

    public boolean startEditingSession(Player player, Lootbox lootbox, LootboxManager lootboxManager) {
        this.creationSessionMap.put(player.getUniqueId(), (Pair<IncompleteLootbox, LootboxManager>) Pair.with(new IncompleteLootbox(lootbox), lootboxManager));
        this.startCreationSession(player);
        return true;
    }

    public void deleteSession(Player player) {
        this.creationSessionMap.remove(player.getUniqueId());
    }

    public Optional<Pair<IncompleteLootbox, LootboxManager>> getSession(UUID uuid) {
        return Optional.ofNullable(this.creationSessionMap.get(uuid));
    }

    private File getFile() {
        try {
            File file = new File(this.plugin.getDataFolder() + File.separator + "sessions.json");
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save() {
        try {
            new FileWriter(this.getFile(), false).close();
            FileWriter fileWriter = new FileWriter(this.getFile());
            String jsonElement = GsonFactory.getPrettyGson().toJson(this.creationSessionMap);
            fileWriter.write(jsonElement);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            Type type = new TypeToken<Map<UUID, Pair<IncompleteLootbox, LootboxManager>>>() {
            }.getType();
            FileReader fileReader = new FileReader(this.getFile());
            Map sessions = (Map) GsonFactory.getPrettyGson().fromJson((Reader) fileReader, type);
            this.creationSessionMap.putAll(sessions);
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


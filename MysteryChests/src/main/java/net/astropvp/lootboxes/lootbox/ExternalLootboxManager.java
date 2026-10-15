/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.utils.NBTWrapper
 *  com.decimatemc.decimatecommons.inventory.InventoryManager
 *  com.google.common.collect.Lists
 *  net.minecraft.util.com.google.gson.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.astropvp.lootboxes.lootbox;

import net.astropvp.lootboxes.listener.LootboxListener;
import com.cosmicpvp.cosmicutils.inventory.InventoryManager;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.PluginLootboxManager;
import net.astropvp.lootboxes.gson.GsonFactory;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

public class ExternalLootboxManager
        implements PluginLootboxManager {
    private final JavaPlugin plugin;
    private final Object lootboxSaveLock = new Object();
    private final List<Lootbox> lootboxes;
    private final Set<Material> lootboxMaterials;
    private final boolean handleable;

    public ExternalLootboxManager(JavaPlugin plugin, boolean handleable) {
        this.plugin = plugin;
        this.lootboxes = Lists.newArrayList();
        this.load();
        this.lootboxMaterials = new HashSet<Material>();
        this.lootboxes.forEach(lootbox -> this.lootboxMaterials.add(lootbox.getDisplayItem().getType()));
        this.handleable = handleable;
        if (handleable) {
            InventoryManager inventoryManager = new InventoryManager(this.plugin);
            inventoryManager.init();
            Bukkit.getPluginManager().registerEvents(new LootboxListener(this, this.plugin, inventoryManager), this.plugin);
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            Object object = this.lootboxSaveLock;
            synchronized (object) {
                this.save();
            }
        }, 6000L, 6000L);
    }

    @Override
    public String getNBTCompound() {
        return "cosmicData";
    }

    @Override
    public String getNBTIsLootbox() {
        return this.plugin.getName() + "-lootboxes-is";
    }

    @Override
    public String getNBTLootboxType() {
        return this.plugin.getName() + "-lootboxes-type";
    }

    @Override
    public void addNewLootbox(Lootbox lootbox) {
        this.lootboxes.removeIf(targetLootbox -> targetLootbox.getName().equalsIgnoreCase(lootbox.getName()));
        this.lootboxes.add(lootbox);
        this.forceSave(true);
        this.lootboxMaterials.add(lootbox.getDisplayItem().getType());
    }

    @Override
    public Optional<Lootbox> getLootbox(String lootboxName) {
        return this.lootboxes.stream().filter(lootbox -> lootbox.getName().equalsIgnoreCase(lootboxName)).findFirst();
    }

    @Override
    public boolean removeLootbox(String name) {
        return this.getLootbox(name).map(this.lootboxes::remove).orElse(false);
    }

    @Override
    public List<Lootbox> getLootboxes() {
        return Collections.unmodifiableList(this.lootboxes);
    }

    @Override
    public Optional<Lootbox> getLootboxFrom(ItemStack itemStack) {
        return this.getLootbox(new NBTWrapper(itemStack).getCompoundOrCreate(this.getNBTCompound()).getString(this.getNBTLootboxType()));
    }

    @Override
    public JavaPlugin getManagingPlugin() {
        return this.plugin;
    }

    @Override
    public Lootbox createManagedLootbox(String name) {
        Optional<Lootbox> lootbox = this.getLootbox(name);
        if (lootbox.isPresent()) {
            return lootbox.get();
        }
        IncompleteLootbox incompleteLootbox = new IncompleteLootbox();
        incompleteLootbox.setName(name);
        incompleteLootbox.setDisplayName("&a" + this.plugin.getName() + "-" + name);
        incompleteLootbox.getRewardItems().add(new AstroRewardItem(new ItemStack(Material.DIRT, 32)));
        incompleteLootbox.setRewardCount(1);
        incompleteLootbox.setChosenMaterial(Material.BEACON);
        Lootbox completeLootbox = incompleteLootbox.convert(this.plugin, this);
        this.addNewLootbox(completeLootbox);
        return completeLootbox;
    }

    @Override
    public boolean isHandleable() {
        return this.handleable;
    }

    @Override
    public Set<Material> getLootboxMaterials() {
        return this.lootboxMaterials;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void forceSave(boolean async) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                Object object = this.lootboxSaveLock;
                synchronized (object) {
                    this.save();
                }
            });
        } else {
            Object object = this.lootboxSaveLock;
            synchronized (object) {
                this.save();
            }
        }
    }

    private File getFile() {
        try {
            File file = new File(this.plugin.getDataFolder() + File.separator + "lootboxes.json");
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
            String jsonElement = GsonFactory.getPrettyGson().toJson(this.lootboxes);
            new FileWriter(this.getFile(), false).close();
            FileWriter fileWriter = new FileWriter(this.getFile());
            fileWriter.write(jsonElement);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            Type type = new TypeToken<List<Lootbox>>() {
            }.getType();
            FileReader fileReader = new FileReader(this.getFile());
            List lootboxes = (List) GsonFactory.getPrettyGson().fromJson((Reader) fileReader, type);
            this.lootboxes.addAll(lootboxes);
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


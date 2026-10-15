/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.utils.NBTWrapper
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  net.minecraft.util.com.google.gson.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package net.astropvp.lootboxes.lootbox;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import net.astropvp.lootboxes.AstroLootboxes;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.LootboxManager;
import net.astropvp.lootboxes.gson.GsonFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import java.util.function.*;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class AstroLootboxManager implements LootboxManager
{
    private final AstroLootboxes plugin;
    private final Object lootboxSaveLock;
    private final List<Lootbox> lootboxes;
    private final Set<Material> lootboxMaterials;

    public AstroLootboxManager(final AstroLootboxes plugin) {
        this.lootboxSaveLock = new Object();
        this.plugin = plugin;
        this.lootboxes = Lists.newArrayList();
        this.load();
        this.lootboxMaterials = Sets.newHashSet();
        this.lootboxes.forEach(lootbox -> this.lootboxMaterials.add(lootbox.getDisplayItem().getType()));
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            synchronized (this.lootboxSaveLock) {
                this.save();
            }
        }, 6000L, 6000L);
    }

    @Override
    public void addNewLootbox(final Lootbox lootbox) {
        this.lootboxes.removeIf(targetLootbox -> targetLootbox.getName().equalsIgnoreCase(lootbox.getName()));
        this.lootboxes.add(lootbox);
        this.forceSave(true);
        this.lootboxMaterials.add(lootbox.getDisplayItem().getType());
    }

    @Override
    public Optional<Lootbox> getLootbox(final String lootboxName) {
        return this.lootboxes.stream().filter(lootbox -> lootbox.getName().equalsIgnoreCase(lootboxName)).findFirst();
    }

    @Override
    public boolean removeLootbox(final String name) {
        return this.getLootbox(name).map(this.lootboxes::remove).orElse(false);
    }

    @Override
    public List<Lootbox> getLootboxes() {
        return Collections.unmodifiableList(this.lootboxes);
    }

    @Override
    public Optional<Lootbox> getLootboxFrom(final ItemStack itemStack) {
        return this.getLootbox(new NBTWrapper(itemStack).getCompoundOrCreate(this.getNBTCompound()).getString(this.getNBTLootboxType()));
    }

    @Override
    public Set<Material> getLootboxMaterials() {
        return this.lootboxMaterials;
    }

    @Override
    public void forceSave(final boolean async) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                synchronized (this.lootboxSaveLock) {
                    this.save();
                }
            });
        }
        else {
            synchronized (this.lootboxSaveLock) {
                this.save();
            }
        }
    }

    @Override
    public String getNBTCompound() {
        return "cosmicData";
    }

    @Override
    public String getNBTIsLootbox() {
        return "astro-lootboxes-is";
    }

    @Override
    public String getNBTLootboxType() {
        return "astro-lootboxes-type";
    }

    private File getFile() {
        try {
            final File file = new File(this.plugin.getDataFolder() + File.separator + "lootboxes.json");
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save() {
        try {
            final String jsonElement = GsonFactory.getPrettyGson().toJson(this.lootboxes);
            new FileWriter(this.getFile(), false).close();
            final FileWriter fileWriter = new FileWriter(this.getFile());
            fileWriter.write(jsonElement);
            fileWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            final Type type = new TypeToken<List<Lootbox>>() {}.getType();
            final FileReader fileReader = new FileReader(this.getFile());
            final List<Lootbox> lootboxes = GsonFactory.getPrettyGson().fromJson(fileReader, type);
            this.lootboxes.addAll(lootboxes);
            fileReader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

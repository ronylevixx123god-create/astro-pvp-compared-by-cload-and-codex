/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.cosmicpvp.cosmicutils.inventory.rewards;


import com.cosmicpvp.cosmicutils.serialization.Base64Serialization;
import com.cosmicpvp.cosmicutils.utils.ConfigFile;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SerializedRewards {
    private final Map<Integer, String> pageMap = Maps.newHashMap();
    private final JavaPlugin plugin;
    private final ConfigFile file;
    private final YamlConfiguration config;

    public SerializedRewards(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new ConfigFile("rewards", plugin);
        this.file.setup();
        this.config = this.file.getYamlConfig();
        this.pageMap.put(0, "");
    }

    public SerializedRewards(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new ConfigFile(fileName, plugin);
        this.file.setup();
        this.config = this.file.getYamlConfig();
        this.pageMap.put(0, "");
    }

    public void open(Player player, int page) {
        new RewardsPageInventory(this, page).open(player);
    }

    public void open(Player player) {
        this.open(player, 0);
    }

    public List<ItemStack> getRewards(int page) {
        String pageData = this.getPageData(page);
        ArrayList pageItems = Lists.newArrayList();
        if (pageData.equalsIgnoreCase("")) {
            return pageItems;
        }
        for (ItemStack item : Base64Serialization.arrayFromBase64(pageData)) {
            if (item == null || item.getType() == Material.AIR) continue;
            pageItems.add(item);
        }
        return pageItems;
    }

    public List<ItemStack> getAllRewards() {
        ArrayList allRewards = Lists.newArrayList();
        for (int page : this.pageMap.keySet()) {
            allRewards.addAll(this.getRewards(page));
        }
        return allRewards;
    }

    public String getPageData(int page) {
        return this.pageMap.getOrDefault(page, "");
    }

    public void load() {
        if (!this.config.contains("pages")) {
            return;
        }
        for (String pageNumber : this.config.getConfigurationSection("pages").getKeys(false)) {
            Integer page = Integer.parseInt(pageNumber);
            String data = this.config.getString("pages." + pageNumber);
            this.pageMap.put(page, data);
        }
    }

    public void save() {
        for (Integer page : this.pageMap.keySet()) {
            this.config.set("pages." + page, this.pageMap.get(page));
        }
        this.file.save();
    }

    public Map<Integer, String> getPageMap() {
        return this.pageMap;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public ConfigFile getFile() {
        return this.file;
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }
}


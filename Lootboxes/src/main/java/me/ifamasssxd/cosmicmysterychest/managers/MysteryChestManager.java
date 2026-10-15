/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.itembuilder.JsonItemSerializer
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.struct.me.astropvp.cratekeys.managers.CosmicManager
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.JSONUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.TimeUtils
 *  net.minecraft.util.com.google.gson.Gson
 *  net.minecraft.util.com.google.gson.GsonBuilder
 *  net.minecraft.util.com.google.gson.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicmysterychest.managers;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.itembuilder.JsonItemSerializer;
import com.cosmicpvp.cosmicutils.struct.managers.CosmicManager;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.JSONUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.GlobalMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MysteryChestManager
        extends CosmicManager<CosmicMysteryChest> {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().registerTypeHierarchyAdapter(ItemStack.class, new JsonItemSerializer()).create();
    private CosmicMysteryChest plugin;
    private final Map<String, MysteryChest> mysteryChestMap = new ConcurrentHashMap<String, MysteryChest>();
    private GlobalMysteryChest activeMysteryChest;
    private final Type token = new TypeToken<MysteryChest>() {
    }.getType();
    private final int DAY_SECONDS = (int) TimeUnit.DAYS.toSeconds(1L);

    public void onEnable(CosmicMysteryChest plugin) {
        this.plugin = plugin;
        this.loadMysteryChests();
        this.loadGlobalMysteryChest();
        String[] expiration = new String[1];
        int[] days = new int[1];
        Object iterator = null;
        CosmicGUI[] gui = new CosmicGUI[1];
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (this.activeMysteryChest != null && this.activeMysteryChest.getActiveMysteryChest() != null) {
                if (this.activeMysteryChest.getSecondsTillExpiration() == 1 || this.activeMysteryChest.getExpiration() < System.currentTimeMillis() && this.activeMysteryChest.getSecondsTillExpiration() != -1) {
                    this.activeMysteryChest.setSecondsTillExpiration(-1);

                } else if (this.activeMysteryChest.getSecondsTillExpiration() > 0) {
                    String string = this.activeMysteryChest.getSecondsTillExpiration() == 3600 ? "1 HOUR" : (expiration[0] = this.activeMysteryChest.getSecondsTillExpiration() == 1800 ? "30 MINUTES" : null);
                    if (this.activeMysteryChest.getSecondsTillExpiration() % this.DAY_SECONDS == 0) {
                        days[0] = (int) ((double) this.activeMysteryChest.getSecondsTillExpiration() / (double) this.DAY_SECONDS);
                        if (days[0] > 0) {
                            expiration[0] = days[0] + " DAY" + (days[0] > 1 ? "S" : "");
                        }
                    }

                    this.activeMysteryChest.setSecondsTillExpiration(this.activeMysteryChest.getSecondsTillExpiration() - 1);
                }
            }
        }, 20L, 20L);
    }

    public void loadMysteryChests() {
        this.mysteryChestMap.clear();
        File folder = this.plugin.getDataFolder();
        if (folder.isDirectory()) {
            for (File found : folder.listFiles()) {
                if (found == null || !found.getName().endsWith(".json"))
                    continue;
                try {
                    MysteryChest chest = (MysteryChest) JSONUtils.fromJson(found, this.token, this.gson);
                    if (chest == null)
                        continue;
                    this.mysteryChestMap.put(chest.getInternalName(), chest);
                    this.plugin.getLogger().info("Loaded Chest '" + chest.getInternalName() + "' into memory: " + chest.getLootItems().size() + " items");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadGlobalMysteryChest() {
        String mysteryChest = this.plugin.getConfig().getString("active_mystery_chest");
        long expiration = this.plugin.getConfig().getLong("expiration");
        int purchased = this.plugin.getConfig().getInt("purchased", 0);
        this.activeMysteryChest = new GlobalMysteryChest();
        if (mysteryChest == null || expiration <= 0L) {
            this.plugin.getLogger().info("Mystery Chest invalid: " + mysteryChest);
            return;
        }
        MysteryChest chest = this.getMysteryChestMap().get(mysteryChest);
        if (chest == null) {
            this.plugin.getLogger().info("Invalid Mystery Chest from " + mysteryChest);
            return;
        }
        this.activeMysteryChest.setActiveMysteryChest(chest);
        this.activeMysteryChest.setExpiration(expiration);
        this.activeMysteryChest.setPurchaseCount(purchased);
        this.plugin.getLogger().info("Loaded Global Mystery Chest '" + chest.getInternalName() + "' expiring in " + TimeUtils.formatFutureTime(expiration));
    }

    public void setActiveMysteryChest(MysteryChest chest, long expiration) {
        this.activeMysteryChest.setActiveMysteryChest(chest);
        this.activeMysteryChest.setExpiration(expiration);
        this.activeMysteryChest.setPurchaseCount(0);
        this.plugin.getLogger().info("Setting active Mystery Chest to " + (chest == null ? "null" : chest.getInternalName()) + "!");
        this.plugin.getConfig().set("active_mystery_chest", chest == null ? null : chest.getInternalName());
        this.plugin.getConfig().set("expiration", expiration);
        this.plugin.getConfig().set("purchased", 0);
        this.plugin.saveConfig();
        if (chest != null && chest.getDisplayName() != null) {

        }
    }

    public void removeMysteryChest(MysteryChest chest) {
        this.mysteryChestMap.remove(chest.getInternalName());
        File file = chest.getFile();
        if (file.exists()) {
            file.delete();
            this.plugin.getLogger().info("Deleting file " + file.getName() + " for " + chest.getInternalName() + "!");
        }
    }

    public void saveMysteryChest(MysteryChest chest) throws IOException {
        File file = chest.getFile();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONUtils.saveJSONToFile(file, chest, this.token, this.gson);
    }

    public void onDisable(CosmicMysteryChest plugin) {
    }

    public Map<String, MysteryChest> getMysteryChestMap() {
        return this.mysteryChestMap;
    }

    public Map<String, MysteryChest> getMysteryChestMapSorted() {
        return sort(mysteryChestMap);
    }

    public GlobalMysteryChest getActiveMysteryChest() {
        return this.activeMysteryChest;
    }

    public Map<String, MysteryChest> sort(Map<String, MysteryChest> map) {
        List<Map.Entry<String, MysteryChest>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByKey());
        Collections.reverse(list);
        Map<String, MysteryChest> result = new LinkedHashMap<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map.Entry<String, MysteryChest> entry = list.get(i);
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}


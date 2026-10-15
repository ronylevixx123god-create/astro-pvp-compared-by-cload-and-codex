/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.User
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.libs.com.google.gson.Gson
 *  org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder
 *  org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.User;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.utils.EssentialsUtils;
import org.arkhamnetwork.Arkkit.utils.InventoryUtils;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class HomeGUIManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File jsonFile;
    private final List<Material> materials = Lists.newArrayList(Material.DIRT, Material.STONE, Material.COBBLESTONE, Material.LOG, Material.LOG_2, Material.WORKBENCH, Material.PISTON_BASE, Material.NOTE_BLOCK, Material.MOB_SPAWNER, Material.BOOKSHELF, Material.TNT, Material.STEP, Material.ANVIL, Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.NETHER_BRICK, Material.NETHERRACK, Material.ENDER_STONE, Material.CHEST, Material.ENDER_CHEST, Material.SMOOTH_BRICK, Material.SNOW_BLOCK, Material.SOUL_SAND, Material.QUARTZ_BLOCK, Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SPADE, Material.DIAMOND_HOE, Material.ICE, Material.PACKED_ICE, Material.DISPENSER, Material.FURNACE, Material.HOPPER, Material.GLASS, Material.SAND, Material.SPONGE, Material.OBSIDIAN, Material.BEDROCK, Material.ENCHANTMENT_TABLE, Material.CACTUS, Material.GLOWSTONE, Material.DIAMOND, Material.EMERALD, Material.GOLD_INGOT, Material.IRON_INGOT, Material.REDSTONE, Material.BUCKET, Material.LAVA_BUCKET, Material.WATER_BUCKET, Material.GOLDEN_APPLE, Material.EXP_BOTTLE, Material.BED);
    private final DecimalFormat format = new DecimalFormat("#.#");
    private ConcurrentHashMap<UUID, CustomHomes> customHomes = new ConcurrentHashMap();

    HomeGUIManager() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadHomes() {
        System.out.println("Loading homes..");
        long start = System.currentTimeMillis();
        try {
            jsonFile = new File(Arkkit.get().getDataFolder(), "customHomes.json");
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
            try {
                String line;
                StringBuilder build = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    build.append(line);
                }
                this.customHomes = gson.fromJson(build.toString(), new TypeToken<ConcurrentHashMap<UUID, CustomHomes>>() {
                }.getType());
                if (this.customHomes == null) {
                    this.customHomes = new ConcurrentHashMap();
                }
                System.out.println("Took " + (System.currentTimeMillis() - start) + "ms to load " + this.customHomes.size() + " unique players custom homes.");
            } finally {
                if (Collections.singletonList(reader).get(0) != null) {
                    reader.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Material> getMaterials() {
        return this.materials;
    }

    public Inventory getMaterialSelection() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Select Home Icon");
        for (Material material : this.getMaterials()) {
            inventory.addItem(ItemUtils.buildItem(material, (short) 0, null, ChatColor.GRAY + "Click to select material."));
        }
        return inventory;
    }

    public void saveHomes() {
        try {
            String map = gson.toJson(this.customHomes);
            FileWriter writer = new FileWriter(jsonFile);
            try {
                writer.write(map);
                writer.flush();
            } finally {
                if (Collections.singletonList(writer).get(0) != null) {
                    writer.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Inventory getHomeInventory(Player player, int page) {
        User user = EssentialsUtils.getEssentials().getUser(player);
        if (user == null) {
            return null;
        }
        int perPage = page == 1 ? 53 : 52;
        int maxPages = (int) Math.max(1.0, Math.ceil((double) user.getHomes().size() / (double) perPage));
        if (page > maxPages) {
            page = maxPages;
        }
        if (page < 1) {
            page = 1;
        }
        int current_limit = EssentialsAddHomes.getEssentials().getSettings().getHomeLimit(user);
        String name = "Your Homes" + (maxPages > 1 ? " #" + page : "") + " (" + user.getHomes().size() + "/" + current_limit + ")";
        if (name.length() > 32) {
            name = name.substring(0, 32);
        }
        Inventory inventory = Bukkit.createInventory(null, InventoryUtils.fitInventorySize(user.getHomes().size()), name);
        CustomHomes customHomes = this.getCustomHomes().get(player.getUniqueId());
        LinkedList homes = new LinkedList(user.getHomes());
        int startIndex = page * perPage - perPage;
        if (page != 1) {
            ++startIndex;
        }
        if (page > 1) {
            inventory.setItem(inventory.getSize() - 9, ItemUtils.buildItem(Material.ARROW, (short) 0, ChatColor.RED.toString() + ChatColor.BOLD + "Previous Page", ChatColor.GRAY + "Click to return to previous menu."));
        }
        if (maxPages > 1 && page < maxPages) {
            inventory.setItem(inventory.getSize() - 1, ItemUtils.buildItem(Material.ARROW, (short) 0, ChatColor.GREEN.toString() + ChatColor.BOLD + "Next Page", ChatColor.GRAY + "Click to return to previous menu."));
        }
        for (int i = startIndex; i < startIndex + perPage && i < homes.size(); ++i) {
            String homeName = (String) homes.get(i);
            try {
                Location loc = user.getHome(homeName);
                Material material = Material.GRASS;
                if (customHomes != null && customHomes.isCustomHome(homeName)) {
                    material = customHomes.get(homeName);
                }
                ItemStack item = ItemUtils.buildItem(material, (short) 0, ChatColor.AQUA + ChatColor.BOLD.toString() + StringUtils.capitaliseAllWords(homeName.replace("_", " ")), ChatColor.GRAY + "Location: " + ChatColor.WHITE + this.format.format(loc.getX()) + "x, " + this.format.format(loc.getY()) + "y, " + this.format.format(loc.getZ()) + "z", ChatColor.GRAY + "World: " + ChatColor.WHITE + (loc.getWorld() == null ? "???" : loc.getWorld().getName()), "", ChatColor.GRAY + "Left-Click to teleport", ChatColor.GRAY + "Middle-Click to delete home", ChatColor.GRAY + "Right-Click to edit icon");
                inventory.addItem(item);
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.getCustomHomes().containsKey(player.getUniqueId())) {
            CustomHomes home = this.getCustomHomes().get(player.getUniqueId());
            for (String homeName : home.keySet()) {
                try {
                    if (user.getHome(homeName) != null) continue;
                    home.remove(homeName);
                } catch (Exception e) {
                    home.remove(homeName);
                }
            }
            if (home.isEmpty()) {
                this.getCustomHomes().remove(player.getUniqueId());
            }
        }
        return inventory;
    }

    public ConcurrentHashMap<UUID, CustomHomes> getCustomHomes() {
        return this.customHomes;
    }
}


package org.arkhamnetwork.Arkkit;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.arkhamnetwork.Arkkit.utils.InventoryUtils;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class PlayerVaultGUIManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File jsonFile;
    private final List<Material> materials = Lists.newArrayList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.BOW, Material.POTION, Material.SUGAR, Material.ENDER_PEARL, Material.MOB_SPAWNER, Material.BOOK, Material.TNT, Material.STEP, Material.ANVIL, Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.NETHER_BRICK, Material.NETHERRACK, Material.PAPER, Material.CHEST, Material.ENDER_CHEST, Material.SMOOTH_BRICK, Material.SNOW_BLOCK, Material.SOUL_SAND, Material.QUARTZ_BLOCK, Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SPADE, Material.DIAMOND_HOE, Material.ICE, Material.PACKED_ICE, Material.DISPENSER, Material.FURNACE, Material.HOPPER, Material.GLASS, Material.SAND, Material.SPONGE, Material.OBSIDIAN, Material.BEDROCK, Material.ENCHANTMENT_TABLE, Material.CACTUS, Material.GLOWSTONE, Material.DIAMOND, Material.EMERALD, Material.GOLD_INGOT, Material.IRON_INGOT, Material.REDSTONE, Material.BUCKET, Material.LAVA_BUCKET, Material.WATER_BUCKET, Material.GOLDEN_APPLE, Material.EXP_BOTTLE, Material.BED);
    private ConcurrentHashMap customPvs = new ConcurrentHashMap();

    PlayerVaultGUIManager() {
    }

    public void loadCustomVaults() {
        long start = System.currentTimeMillis();
        try {
            jsonFile = new File(Arkkit.get().getDataFolder(), "customPlayerVaults.json");
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
                this.customPvs = gson.fromJson(build.toString(), new TypeToken<ConcurrentHashMap<UUID, CustomPlayerVaults>>() {
                }.getType());
                if (this.customPvs == null) {
                    this.customPvs = new ConcurrentHashMap();
                }
                System.out.println("Took " + (System.currentTimeMillis() - start) + "ms to load " + this.customPvs.size() + " unique players vaults.");
            } finally {
                if (Collections.singletonList(reader).get(0) != null) {
                    reader.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCustomVaults() {
        try {
            String map = gson.toJson(this.customPvs);
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

    public List<Material> getMaterials() {
        return this.materials;
    }

    public Inventory getMaterialSelection() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Select Vault Icon");
        for (Material material : this.getMaterials()) {
            inventory.addItem(ItemUtils.buildItem(material, material == Material.POTION ? (short) 16421 : 0, null, ChatColor.GRAY + "Click to select material."));
        }
        return inventory;
    }

    public Inventory getVaultsInventory(Player player) {
        int vaults = this.getPlayerVaults(player);
        Inventory inventory = Bukkit.createInventory(null, InventoryUtils.fitInventorySize(vaults), "Player Vaults");
        CustomPlayerVaults customVaults = (CustomPlayerVaults) this.getCustomPvs().get(player.getUniqueId());
        for (int vault = 1; vault <= vaults; ++vault) {
            try {
                Material material = Material.EMERALD;
                String name = "PV #" + vault;
                if (customVaults != null && customVaults.isCustomPV(vault)) {
                    CustomPV pv = customVaults.get(vault);
                    material = pv.getIcon();
                    if (pv.getCustomName() != null && !pv.getCustomName().isEmpty()) {
                        name = pv.getCustomName();
                    }
                }
                ItemStack item = ItemUtils.buildItem(material, material == Material.POTION ? (short) 16421 : 0, ChatColor.AQUA + ChatColor.BOLD.toString() + name, ChatColor.LIGHT_PURPLE + "/pv " + vault, "", ChatColor.GRAY + "Left-Click to enter Vault", ChatColor.GRAY + "Middle-Click to edit name", ChatColor.GRAY + "Right-Click to edit icon");
                item.setAmount(vault);
                inventory.addItem(item);
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return inventory;
    }

    public int getPlayerVaults(Player player) {
        for (int i = 54; i > 0; --i) {
            if (!player.hasPermission("playervaults.amount." + i)) continue;
            return i;
        }
        return 0;
    }

    public ConcurrentHashMap getCustomPvs() {
        return this.customPvs;
    }
}


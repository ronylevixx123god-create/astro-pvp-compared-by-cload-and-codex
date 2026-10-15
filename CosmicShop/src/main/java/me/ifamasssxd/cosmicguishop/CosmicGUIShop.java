/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.ifamasssxd.cosmicguishop;

import com.google.common.collect.Lists;

import me.ifamasssxd.cosmicguishop.commands.CommandBankNote;
import me.ifamasssxd.cosmicguishop.commands.CommandShop;
import me.ifamasssxd.cosmicguishop.commands.CommandXpShop;
import me.ifamasssxd.cosmicguishop.listeners.MenuListener;
import me.ifamasssxd.cosmicguishop.listeners.PlayerListener;
import me.ifamasssxd.cosmicguishop.listeners.xpShopMenuListener;
import me.ifamasssxd.cosmicguishop.struct.ShopManager;
import me.ifamasssxd.cosmicguishop.utils.Base64Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class CosmicGUIShop extends JavaPlugin {
    private static CosmicGUIShop instance;
    private static CosmicGUIShop plugin;
    private static Economy economy;
    private ShopManager shopManager;


    List<String> itemList;

    public static CosmicGUIShop get() {
        return instance;
    }
    public static Economy getEconomy() {
        return economy;
    }

    public void onEnable() {
        instance = this;
        plugin = this;
        FileConfiguration config = this.getConfig();
        itemList = getConfig().getStringList("items");
        economy = this.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        this.shopManager = new ShopManager();
        try {
            this.shopManager.loadAllShopItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        this.getServer().getPluginManager().registerEvents(new xpShopMenuListener(), this);
        this.getCommand("cosmicshop").setExecutor(new CommandShop());
        this.getCommand("banknote").setExecutor(new CommandBankNote());
        this.getCommand("xpshop").setExecutor(new CommandXpShop());

        // XPShop File
        File file = new File(this.getDataFolder() + File.separator + "config.yml");
        if (file.exists()) {
            this.getLogger().info("XP Shop Config Loaded");
        } else {
            String l = Base64Utils.toBase64(new ItemStack[]{new ItemStack(Material.EMERALD_ORE)}, 9) + ":10";
            ArrayList<String> list = Lists.newArrayList(l);
            this.getConfig().addDefault("items", list);
            this.getLogger().info("Created XP Shop Config");
        }
    }

    public List<String> getItemList() {
        return itemList;
    }

    public Map<ItemStack, Float> getItemListSorted() {
        Map<ItemStack, Float> items = new HashMap<>();
        for (String string : itemList) {
            ItemStack item = Base64Utils.getItem(string);
            float price = Base64Utils.getCost(string);
            items.put(item, price);
        }
        Map<ItemStack, Float> yourSortedMap = items.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ItemStack::getType)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value1, LinkedHashMap::new));
        return yourSortedMap;
    }

    public void setItemList(List<String> itemList) {
        this.itemList = itemList;
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static CosmicGUIShop getInstance() {
        return plugin;
    }

    public void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    public ShopManager getShopManager() {
        return this.shopManager;
    }
}


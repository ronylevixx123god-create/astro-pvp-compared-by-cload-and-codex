/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.arkhamnetwork.enchantments.util.TieredEnchantments
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.EnchantmentStorageMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.permissions.Permissible
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.rit.sucy;

import com.earth2me.essentials.Essentials;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantPlugin;
import com.rit.sucy.commands.Commander;
import com.rit.sucy.config.RootConfig;
import com.rit.sucy.enchanting.EEquip;
import com.rit.sucy.enchanting.EListener;
import com.rit.sucy.enchanting.VanillaData;
import com.rit.sucy.enchanting.VanillaEnchantment;
import com.rit.sucy.lore.LoreConfig;
import com.rit.sucy.service.ENameParser;
import com.rit.sucy.service.ERomanNumeral;
import com.rit.sucy.service.IModule;
import com.rit.sucy.service.PermissionNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentAPI
extends JavaPlugin {
    private static Hashtable<String, CustomEnchantment> enchantments = new Hashtable();
    private Hashtable<String, List<String>> adjectives;
    private Hashtable<String, List<String>> weapons;
    private Hashtable<String, List<String>> suffixes;
    private final Map<Class<? extends IModule>, IModule> modules = new HashMap<Class<? extends IModule>, IModule>();
    private static String TAG = "[EnchantAPI]";
    private static Essentials ess3;
    private static EnchantmentAPI plugin;
    public static boolean staffPlus;

    public void onEnable() {
        plugin = this;
        this.getCommand("enchantapi").setExecutor((CommandExecutor)new Commander(this));
        ess3 = Bukkit.getPluginManager().isPluginEnabled("Essentials") ? (Essentials)Bukkit.getPluginManager().getPlugin("Essentials") : null;
        staffPlus = Bukkit.getPluginManager().isPluginEnabled("StaffPlus");
        this.registerModule(RootConfig.class, new RootConfig(this));
        this.reload();
    }

    public void onDisable() {
        HandlerList.unregisterAll((Plugin)this);
        enchantments.clear();
        EEquip.clear();
    }

    public void reload() {
        this.adjectives = new LoreConfig(this, "adjectives").getLists();
        this.weapons = new LoreConfig(this, "weapons").getLists();
        this.suffixes = new LoreConfig(this, "suffixes").getLists();
        HandlerList.unregisterAll((Plugin)this);
        EEquip.clear();
        enchantments.clear();
        this.loadVanillaEnchantments();
        for (Plugin plugin : this.getServer().getPluginManager().getPlugins()) {
            if (!(plugin instanceof EnchantPlugin)) continue;
            try {
                ((EnchantPlugin)plugin).registerEnchantments();
                this.getLogger().info("Loaded enchantments from plugin: " + plugin.getName());
            }
            catch (Exception e) {
                this.getLogger().info("Failed to load enchantments from " + plugin.getName() + ": perhaps it is outdated?");
            }
        }
        for (Player player : this.getServer().getOnlinePlayers()) {
            EEquip.loadPlayer(player);
        }
        this.getModuleForClass(RootConfig.class).reload();
        new EListener(this);
    }

    public static EnchantmentAPI get() {
        return plugin;
    }

    public static List<CustomEnchantment> getAllValidEnchants(ItemStack item) {
        return EnchantmentAPI.getAllValidEnchants(item, 45);
    }

    public static List<CustomEnchantment> getAllValidEnchants(ItemStack item, int level) {
        ArrayList<CustomEnchantment> validEnchantments = new ArrayList<CustomEnchantment>();
        boolean book = item.getType() == Material.BOOK;
        for (CustomEnchantment enchantment : EnchantmentAPI.getEnchantments()) {
            if (!enchantment.canEnchantOnto(item) && !book || !enchantment.isEnabled() || enchantment.getEnchantLevel(level) < 1 || !enchantment.isTableEnabled()) continue;
            validEnchantments.add(enchantment);
        }
        return validEnchantments;
    }

    public static List<CustomEnchantment> getAllValidEnchants(ItemStack item, Permissible enchanter) {
        return EnchantmentAPI.getAllValidEnchants(item, enchanter, 45);
    }

    public static List<CustomEnchantment> getAllValidEnchants(ItemStack item, Permissible enchanter, int level) {
        ArrayList<CustomEnchantment> validEnchantments = new ArrayList<CustomEnchantment>();
        boolean book = item.getType() == Material.BOOK;
        for (CustomEnchantment enchantment : EnchantmentAPI.getEnchantments()) {
            if (!enchantment.canEnchantOnto(item) && !book || !enchantment.isEnabled() || enchantment.getEnchantLevel(level) < 1 || !enchantment.isTableEnabled() || !enchanter.hasPermission(PermissionNode.ENCHANT.getNode()) && (!enchanter.hasPermission(PermissionNode.ENCHANT_VANILLA.getNode()) || !(enchantment instanceof VanillaEnchantment)) && !enchanter.hasPermission(PermissionNode.ENCHANT.getNode() + "." + enchantment.name().replace(" ", "").toLowerCase())) continue;
            validEnchantments.add(enchantment);
        }
        return validEnchantments;
    }

    public String getAdjective(int level) {
        if (this.adjectives.containsKey("tier" + level)) {
            List<String> list = this.adjectives.get("tier" + level);
            return list.get((int)(Math.random() * (double)list.size()));
        }
        ArrayList<String> all = new ArrayList<String>();
        for (List<String> list : this.adjectives.values()) {
            for (String adjective : list) {
                all.add(adjective);
            }
        }
        return (String)all.get((int)(Math.random() * (double)all.size()));
    }

    public String getWeapon(String item) {
        String[] pieces = item.split("_");
        String type = pieces[pieces.length - 1].toLowerCase();
        if (this.weapons.containsKey(type)) {
            List<String> list = this.weapons.get(type);
            return list.get((int)(Math.random() * (double)list.size()));
        }
        return type.substring(0, 1).toUpperCase() + type.substring(1);
    }

    public String getSuffix(CustomEnchantment enchant) {
        ArrayList<String> options = new ArrayList<String>();
        if (enchant.getSuffixGroups().size() == 0) {
            for (List<String> list : this.suffixes.values()) {
                for (String suffix : list) {
                    if (options.contains(suffix)) continue;
                    options.add(suffix);
                }
            }
        } else {
            for (String group : enchant.getSuffixGroups()) {
                for (String suffix : this.suffixes.get(group)) {
                    if (options.contains(suffix)) continue;
                    options.add(suffix);
                }
            }
        }
        return (String)options.get((int)(Math.random() * (double)options.size()));
    }

    private void loadVanillaEnchantments() {
        for (VanillaData defaults : VanillaData.values()) {
            VanillaEnchantment vanilla = new VanillaEnchantment(defaults.getEnchantment(), defaults.getItems(), defaults.getGroup(), defaults.getSuffixGroup(), defaults.getEnchantWeight(), defaults.getBase(), defaults.getInterval(), defaults.getMaxLevel(), defaults.name());
            EnchantmentAPI.registerCustomEnchantment(vanilla);
        }
    }

    public static boolean isRegistered(String enchantmentName) {
        return enchantments.containsKey(enchantmentName.toUpperCase());
    }

    public static CustomEnchantment getEnchantment(String name) {
        return enchantments.get(name.toUpperCase());
    }

    public static Set<String> getEnchantmentNames() {
        return enchantments.keySet();
    }

    public static Collection<CustomEnchantment> getEnchantments() {
        return enchantments.values();
    }

    public static boolean registerCustomEnchantment(CustomEnchantment enchantment) {
        if (enchantments.containsKey(enchantment.name().toUpperCase())) {
            return false;
        }
        if (!enchantment.isEnabled()) {
            return false;
        }
        enchantments.put(enchantment.name().toUpperCase(), enchantment);
        return true;
    }

    public static void registerCustomEnchantments(CustomEnchantment ... enchantments) {
        for (CustomEnchantment enchantment : enchantments) {
            EnchantmentAPI.registerCustomEnchantment(enchantment);
        }
    }

    public static boolean unregisterCustomEnchantment(String enchantmentName) {
        if (enchantments.containsKey(enchantmentName.toUpperCase())) {
            enchantments.remove(enchantmentName.toUpperCase());
            return true;
        }
        return false;
    }

    public static Map<CustomEnchantment, Integer> getEnchantments(ItemStack item) {
        HashMap<CustomEnchantment, Integer> list = new HashMap<CustomEnchantment, Integer>();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return list;
        }
        if (!meta.hasLore()) {
            return list;
        }
        for (String lore : meta.getLore()) {
            String name = ENameParser.parseName(lore);
            int level = ENameParser.parseLevel(lore);
            if (name == null || level == 0 || !EnchantmentAPI.isRegistered(name)) continue;
            list.put(EnchantmentAPI.getEnchantment(name), level);
        }
        return list;
    }

    public static Map<CustomEnchantment, Integer> getAllEnchantments(ItemStack item) {
        Map<CustomEnchantment, Integer> map = EnchantmentAPI.getEnchantments(item);
        if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            for (Map.Entry entry : item.getEnchantments().entrySet()) {
                map.put(EnchantmentAPI.getEnchantment(((Enchantment)entry.getKey()).getName()), (Integer)entry.getValue());
            }
        }
        if (item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
            for (Map.Entry entry : meta.getStoredEnchants().entrySet()) {
                map.put(EnchantmentAPI.getEnchantment(((Enchantment)entry.getKey()).getName()), (Integer)entry.getValue());
            }
        }
        return map;
    }

    public static boolean itemHasEnchantment(ItemStack item, String enchantmentName) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        if (!meta.hasLore()) {
            return false;
        }
        for (String lore : meta.getLore()) {
            if (!lore.contains(enchantmentName) || ENameParser.parseLevel(lore) <= 0) continue;
            return true;
        }
        return false;
    }

    public static ItemStack removeEnchantments(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (!meta.hasLore()) {
            return item;
        }
        List lore = meta.getLore();
        for (Map.Entry<CustomEnchantment, Integer> entry : EnchantmentAPI.getEnchantments(item).entrySet()) {
            lore.remove((Object)EnchantmentAPI.getChatColor(entry.getKey()) + entry.getKey().name() + " " + ERomanNumeral.numeralOf(entry.getValue()));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public String getTag() {
        return TAG;
    }

    public static void main(String[] args) {
    }

    <T extends IModule> void registerModule(Class<T> clazz, T module) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }
        this.modules.put(clazz, module);
        module.starting();
    }

    public <T extends IModule> T deregisterModuleForClass(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        IModule module = (IModule)clazz.cast(this.modules.get(clazz));
        if (module != null) {
            module.closing();
        }
        return (T)module;
    }

    public <T extends IModule> T getModuleForClass(Class<T> clazz) {
        return (T)((IModule)clazz.cast(this.modules.get(clazz)));
    }

    public static ChatColor getChatColor(CustomEnchantment ce) {
        return TieredEnchantments.getTierColor((CustomEnchantment)ce);
    }

    public static Essentials getEssentials() {
        return ess3;
    }

    static {
        staffPlus = false;
    }
}


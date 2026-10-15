package com.rit.sucy.config;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.config.EnchantmentNode;
import com.rit.sucy.config.LanguageNode;
import com.rit.sucy.config.RootNode;
import com.rit.sucy.enchanting.VanillaEnchantment;
import com.rit.sucy.service.MaterialsParser;
import com.rit.sucy.service.ModularConfig;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RootConfig
extends ModularConfig {
    public static String baseNode = "EnchantmentAPI.";
    private String customNode = "Custom Enchantments.";
    private String vanillaNode = "Vanilla Enchantments.";

    public RootConfig(EnchantmentAPI plugin) {
        super(plugin);
    }

    @Override
    public void starting() {
    }

    @Override
    public void closing() {
        this.plugin.reloadConfig();
        this.plugin.saveConfig();
    }

    @Override
    public void save() {
        this.plugin.saveConfig();
    }

    @Override
    public void set(String path, Object value) {
        FileConfiguration config = this.plugin.getConfig();
        config.set(path, value);
        this.plugin.saveConfig();
    }

    @Override
    public void reload() {
        this.plugin.reloadConfig();
        this.loadDefaults((ConfigurationSection)this.plugin.getConfig());
        this.loadSettings((ConfigurationSection)this.plugin.getConfig());
        this.boundsCheck();
        this.loadEnchantments((ConfigurationSection)this.plugin.getConfig());
        this.loadLanguage((ConfigurationSection)this.plugin.getConfig());
        this.writeConfig();
    }

    @Override
    public void loadSettings(ConfigurationSection config) {
        for (RootNode node : RootNode.values()) {
            this.updateOption(node, config);
        }
    }

    @Override
    public void loadDefaults(ConfigurationSection config) {
        for (RootNode node : RootNode.values()) {
            if (config.contains(node.getPath())) continue;
            config.set(node.getPath(), node.getDefaultValue());
        }
    }

    public void loadLanguage(ConfigurationSection config) {
        for (LanguageNode node : LanguageNode.values()) {
            if (!config.contains(node.getFullPath()) || config.getStringList(node.getFullPath()).size() == 0) {
                config.set(node.getFullPath(), node.getDefaultValue());
                continue;
            }
            if (node != LanguageNode.TABLE_ENCHANTABLE && node != LanguageNode.TABLE_UNENCHANTABLE || config.getStringList(node.getFullPath()).size() == 2) continue;
            config.set(node.getFullPath(), node.getDefaultValue());
        }
    }

    public void loadEnchantments(ConfigurationSection config) {
        Collection<CustomEnchantment> enchantments = EnchantmentAPI.getEnchantments();
        for (CustomEnchantment enchantment : enchantments) {
            String section = enchantment instanceof VanillaEnchantment ? this.vanillaNode : this.customNode;
            block11: for (EnchantmentNode node : EnchantmentNode.values()) {
                String path = baseNode + section + enchantment.name() + node.getPath();
                if (!config.contains(path)) continue;
                Object obj = config.get(path);
                switch (node) {
                    case ENABLED: {
                        if (!(obj instanceof Boolean)) continue block11;
                        enchantment.setEnabled((Boolean)obj);
                        continue block11;
                    }
                    case TABLE: {
                        if (!(obj instanceof Boolean)) continue block11;
                        enchantment.setTableEnabled((Boolean)obj);
                        continue block11;
                    }
                    case ITEMS: {
                        if (!(obj instanceof List)) continue block11;
                        List stringList = (List)obj;
                        Material[] materials = MaterialsParser.toMaterial((String[]) stringList.toArray(new String[stringList.size()]));
                        enchantment.setNaturalMaterials(materials);
                        continue block11;
                    }
                    case WEIGHT: {
                        if (!(obj instanceof Integer)) continue block11;
                        enchantment.setWeight((Integer)obj);
                        continue block11;
                    }
                    case GROUP: {
                        if (!(obj instanceof String)) continue block11;
                        enchantment.setGroup((String)obj);
                        continue block11;
                    }
                    case MAX: {
                        if (!(obj instanceof Integer)) continue block11;
                        enchantment.setMaxLevel((Integer)obj);
                        continue block11;
                    }
                    case BASE: {
                        if (obj instanceof Integer) {
                            enchantment.setBase(((Integer)obj).intValue());
                            continue block11;
                        }
                        if (!(obj instanceof Double)) continue block11;
                        enchantment.setBase((Double)obj);
                        continue block11;
                    }
                    case INTERVAL: {
                        if (obj instanceof Double) {
                            enchantment.setInterval((Double)obj);
                            continue block11;
                        }
                        if (!(obj instanceof Integer)) continue block11;
                        enchantment.setInterval(((Integer)obj).intValue());
                        continue block11;
                    }
                    default: {
                        throw new UnsupportedOperationException("The node " + node.name() + " hasn't been configured yet");
                    }
                }
            }
        }
    }

    public void writeConfig() {
        String path;
        FileConfiguration config = this.plugin.getConfig();
        YamlConfiguration out = new YamlConfiguration();
        if (RootNode.values().length > 0) {
            for (RootNode rootNode : RootNode.values()) {
                out.set(rootNode.getPath(), config.get(rootNode.getPath()));
            }
        }
        ArrayList<CustomEnchantment> customEnchantments = new ArrayList<CustomEnchantment>(EnchantmentAPI.getEnchantments());
        ArrayList<CustomEnchantment> vanillaEnchantments = new ArrayList<CustomEnchantment>();
        Iterator iter = customEnchantments.iterator();
        while (iter.hasNext()) {
            CustomEnchantment customEnchantment = (CustomEnchantment)iter.next();
            if (!(customEnchantment instanceof VanillaEnchantment)) continue;
            vanillaEnchantments.add(customEnchantment);
            iter.remove();
        }
        Collections.sort(customEnchantments);
        for (CustomEnchantment customEnchantment : customEnchantments) {
            path = baseNode + this.customNode + customEnchantment.name();
            if (this.getBoolean(RootNode.CUSTOM_ENABLED)) {
                out.set(path + EnchantmentNode.ENABLED.getPath(), (Object)customEnchantment.isEnabled());
            }
            if (this.getBoolean(RootNode.CUSTOM_TABLE)) {
                out.set(path + EnchantmentNode.TABLE.getPath(), (Object)customEnchantment.isTableEnabled());
            }
            if (this.getBoolean(RootNode.CUSTOM_WEIGHT)) {
                out.set(path + EnchantmentNode.WEIGHT.getPath(), (Object)customEnchantment.getWeight());
            }
            if (this.getBoolean(RootNode.CUSTOM_GROUPS)) {
                out.set(path + EnchantmentNode.GROUP.getPath(), (Object)customEnchantment.getGroup());
            }
            if (this.getBoolean(RootNode.CUSTOM_MAX)) {
                out.set(path + EnchantmentNode.MAX.getPath(), (Object)customEnchantment.getMaxLevel());
            }
            if (this.getBoolean(RootNode.CUSTOM_BASE)) {
                out.set(path + EnchantmentNode.BASE.getPath(), (Object)customEnchantment.getBase());
            }
            if (this.getBoolean(RootNode.CUSTOM_INTERVAL)) {
                out.set(path + EnchantmentNode.INTERVAL.getPath(), (Object)customEnchantment.getInterval());
            }
            if (!this.getBoolean(RootNode.CUSTOM_ITEMS)) continue;
            out.set(path + EnchantmentNode.ITEMS.getPath(), (Object)MaterialsParser.toStringArray(customEnchantment.getNaturalMaterials()));
        }
        Collections.sort(vanillaEnchantments);
        for (CustomEnchantment customEnchantment : vanillaEnchantments) {
            path = baseNode + this.vanillaNode + customEnchantment.name();
            if (this.getBoolean(RootNode.VANILLA_ENABLED)) {
                out.set(path + EnchantmentNode.ENABLED.getPath(), (Object)customEnchantment.isEnabled());
            }
            if (this.getBoolean(RootNode.VANILLA_TABLE)) {
                out.set(path + EnchantmentNode.TABLE.getPath(), (Object)customEnchantment.isTableEnabled());
            }
            if (this.getBoolean(RootNode.VANILLA_WEIGHT)) {
                out.set(path + EnchantmentNode.WEIGHT.getPath(), (Object)customEnchantment.getWeight());
            }
            if (this.getBoolean(RootNode.VANILLA_GROUPS)) {
                out.set(path + EnchantmentNode.GROUP.getPath(), (Object)customEnchantment.getGroup());
            }
            if (this.getBoolean(RootNode.VANILLA_MAX)) {
                out.set(path + EnchantmentNode.MAX.getPath(), (Object)customEnchantment.getMaxLevel());
            }
            if (this.getBoolean(RootNode.VANILLA_BASE)) {
                out.set(path + EnchantmentNode.BASE.getPath(), (Object)customEnchantment.getBase());
            }
            if (this.getBoolean(RootNode.VANILLA_INTERVAL)) {
                out.set(path + EnchantmentNode.INTERVAL.getPath(), (Object)customEnchantment.getInterval());
            }
            if (!this.getBoolean(RootNode.VANILLA_ITEMS)) continue;
            out.set(path + EnchantmentNode.ITEMS.getPath(), (Object)MaterialsParser.toStringArray(customEnchantment.getNaturalMaterials()));
        }
        for (LanguageNode node : LanguageNode.values()) {
            String path2 = node.getFullPath();
            out.set(path2, config.get(path2));
        }
        try {
            String string = this.plugin.getDataFolder().getAbsolutePath() + File.separator + "config.yml";
            out.save(string);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    @Override
    public void boundsCheck() {
    }
}


/*
 * Decompiled with CFR 0.150.
 */
package com.rit.sucy.config;

import com.rit.sucy.config.RootConfig;
import com.rit.sucy.service.ConfigNode;

public enum RootNode implements ConfigNode
{
    ITEM_LORE("Generate Unique Name", ConfigNode.VarType.BOOLEAN, true),
    MAX_ENCHANTS("Max Enchantments", ConfigNode.VarType.INTEGER, 5),
    VANILLA_ENABLED(RootNode.getVisibleSettingsNode() + RootNode.getVanillaNode() + "Enabled", ConfigNode.VarType.BOOLEAN, true),
    VANILLA_TABLE(RootNode.getVisibleSettingsNode() + RootNode.getVanillaNode() + "Table", ConfigNode.VarType.BOOLEAN, true),
    VANILLA_WEIGHT(RootNode.getVisibleSettingsNode() + RootNode.getVanillaNode() + "Weight", ConfigNode.VarType.BOOLEAN, true),
    VANILLA_ITEMS(RootNode.getVisibleSettingsNode() + RootNode.getVanillaNode() + "Items", ConfigNode.VarType.BOOLEAN, true),
    VANILLA_GROUPS(RootNode.getVisibleSettingsNode() + RootNode.getVanillaNode() + "Groups", ConfigNode.VarType.BOOLEAN, true),
    VANILLA_MAX(RootNode.getVisibleSettingsNode() + RootNode.getVanillaNode() + "Max Level", ConfigNode.VarType.BOOLEAN, true),
    VANILLA_BASE(RootNode.getVisibleSettingsNode() + RootNode.getVanillaNode() + "Base", ConfigNode.VarType.BOOLEAN, true),
    VANILLA_INTERVAL(RootNode.getVisibleSettingsNode() + RootNode.getVanillaNode() + "Interval", ConfigNode.VarType.BOOLEAN, true),
    CUSTOM_ENABLED(RootNode.getVisibleSettingsNode() + RootNode.getCustomNode() + "Enabled", ConfigNode.VarType.BOOLEAN, true),
    CUSTOM_TABLE(RootNode.getVisibleSettingsNode() + RootNode.getCustomNode() + "Table", ConfigNode.VarType.BOOLEAN, true),
    CUSTOM_WEIGHT(RootNode.getVisibleSettingsNode() + RootNode.getCustomNode() + "Weight", ConfigNode.VarType.BOOLEAN, true),
    CUSTOM_ITEMS(RootNode.getVisibleSettingsNode() + RootNode.getCustomNode() + "Items", ConfigNode.VarType.BOOLEAN, true),
    CUSTOM_GROUPS(RootNode.getVisibleSettingsNode() + RootNode.getCustomNode() + "Groups", ConfigNode.VarType.BOOLEAN, true),
    CUSTOM_MAX(RootNode.getVisibleSettingsNode() + RootNode.getCustomNode() + "Max Level", ConfigNode.VarType.BOOLEAN, true),
    CUSTOM_BASE(RootNode.getVisibleSettingsNode() + RootNode.getCustomNode() + "Base", ConfigNode.VarType.BOOLEAN, true),
    CUSTOM_INTERVAL(RootNode.getVisibleSettingsNode() + RootNode.getCustomNode() + "Interval", ConfigNode.VarType.BOOLEAN, true);

    private final String path;
    private final ConfigNode.VarType type;
    private ConfigNode.SubType subType = null;
    private final Object defaultValue;

    private RootNode(String path, ConfigNode.VarType type, Object def) {
        this.path = path;
        this.type = type;
        this.defaultValue = def;
    }

    private RootNode(String path, ConfigNode.VarType type, ConfigNode.SubType subType, Object def) {
        this.path = path;
        this.type = type;
        this.defaultValue = def;
        this.subType = subType;
    }

    @Override
    public String getPath() {
        return RootConfig.baseNode + RootNode.getNode() + this.path;
    }

    @Override
    public ConfigNode.VarType getVarType() {
        return this.type;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public ConfigNode.SubType getSubType() {
        return this.subType;
    }

    public static String getNode() {
        return "Settings.";
    }

    public static String getVisibleSettingsNode() {
        return "Visible Settings.";
    }

    public static String getCustomNode() {
        return "Custom Enchantments.";
    }

    public static String getVanillaNode() {
        return "Vanilla Enchantments.";
    }
}


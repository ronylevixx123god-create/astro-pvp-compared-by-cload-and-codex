/*
 * Decompiled with CFR 0.150.
 */
package com.rit.sucy.config;

import com.rit.sucy.service.ConfigNode;
import java.util.Collections;

public enum EnchantmentNode implements ConfigNode
{
    ENABLED("Enabled", ConfigNode.VarType.BOOLEAN, true),
    TABLE("Table", ConfigNode.VarType.BOOLEAN, true),
    WEIGHT("Weight", ConfigNode.VarType.INTEGER, 5),
    GROUP("Group", ConfigNode.VarType.STRING, "Default"),
    MAX("Max Level", ConfigNode.VarType.INTEGER, 1),
    BASE("Base", ConfigNode.VarType.DOUBLE, 1),
    INTERVAL("Interval", ConfigNode.VarType.DOUBLE, 10),
    ITEMS("Items", ConfigNode.VarType.LIST, Collections.emptyList());

    private final String path;
    private final ConfigNode.VarType type;
    private ConfigNode.SubType subType = null;
    private final Object defaultValue;

    private EnchantmentNode(String path, ConfigNode.VarType type, Object def) {
        this.path = path;
        this.type = type;
        this.defaultValue = def;
    }

    private EnchantmentNode(String path, ConfigNode.VarType type, ConfigNode.SubType subType, Object def) {
        this.path = path;
        this.type = type;
        this.defaultValue = def;
        this.subType = subType;
    }

    @Override
    public String getPath() {
        return "." + this.path;
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
}


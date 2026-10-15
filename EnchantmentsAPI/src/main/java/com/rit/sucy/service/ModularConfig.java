/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 */
package com.rit.sucy.service;

import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.service.ConfigNode;
import com.rit.sucy.service.EAPIModule;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ModularConfig
extends EAPIModule {
    private final Map<ConfigNode, Object> OPTIONS = new ConcurrentHashMap<ConfigNode, Object>();

    protected ModularConfig(EnchantmentAPI plugin) {
        super(plugin);
    }

    protected void updateOption(ConfigNode node, ConfigurationSection config) {
        switch (node.getVarType()) {
            case LIST: {
                List list = config.getStringList(node.getPath());
                if (list == null) {
                    list = (List)node.getDefaultValue();
                }
                this.OPTIONS.put(node, list);
                break;
            }
            case DOUBLE: {
                this.OPTIONS.put(node, config.getDouble(node.getPath(), ((Double)node.getDefaultValue()).doubleValue()));
                break;
            }
            case STRING: {
                this.OPTIONS.put(node, config.getString(node.getPath(), (String)node.getDefaultValue()));
                break;
            }
            case INTEGER: {
                this.OPTIONS.put(node, config.getInt(node.getPath(), ((Integer)node.getDefaultValue()).intValue()));
                break;
            }
            case BOOLEAN: {
                this.OPTIONS.put(node, config.getBoolean(node.getPath(), ((Boolean)node.getDefaultValue()).booleanValue()));
                break;
            }
            default: {
                this.OPTIONS.put(node, config.get(node.getPath(), node.getDefaultValue()));
            }
        }
    }

    public abstract void save();

    public void set(ConfigNode node, Object value) {
        this.set(node.getPath(), value);
    }

    protected abstract void set(String var1, Object var2);

    public int getInt(ConfigNode node) {
        int i = -1;
        switch (node.getVarType()) {
            case INTEGER: {
                try {
                    i = (Integer)this.OPTIONS.get(node);
                }
                catch (NullPointerException npe) {
                    i = (Integer)node.getDefaultValue();
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + (Object)((Object)node.getVarType()) + " as an integer.");
            }
        }
        return i;
    }

    protected String getString(ConfigNode node) {
        String out = "";
        switch (node.getVarType()) {
            case STRING: {
                out = (String)this.OPTIONS.get(node);
                if (out != null) break;
                out = (String)node.getDefaultValue();
                break;
            }
            default: {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + (Object)((Object)node.getVarType()) + " as a string.");
            }
        }
        return out;
    }

    public List<String> getStringList(ConfigNode node) {
        List list = new ArrayList();
        switch (node.getVarType()) {
            case LIST: {
                FileConfiguration config = this.plugin.getConfig();
                list = config.getStringList(node.getPath());
                if (list != null) break;
                list = (List)node.getDefaultValue();
                break;
            }
            default: {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + (Object)((Object)node.getVarType()) + " as a List<String>.");
            }
        }
        return list;
    }

    public double getDouble(ConfigNode node) {
        double d = 0.0;
        switch (node.getVarType()) {
            case DOUBLE: {
                try {
                    d = (Double)this.OPTIONS.get(node);
                }
                catch (NullPointerException npe) {
                    d = (Double)node.getDefaultValue();
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + (Object)((Object)node.getVarType()) + " as a double.");
            }
        }
        return d;
    }

    public boolean getBoolean(ConfigNode node) {
        boolean bool = false;
        switch (node.getVarType()) {
            case BOOLEAN: {
                bool = (Boolean)this.OPTIONS.get(node);
                break;
            }
            default: {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + (Object)((Object)node.getVarType()) + " as a boolean.");
            }
        }
        return bool;
    }

    public abstract void reload();

    public abstract void loadSettings(ConfigurationSection var1);

    public abstract void loadDefaults(ConfigurationSection var1);

    public abstract void boundsCheck();
}


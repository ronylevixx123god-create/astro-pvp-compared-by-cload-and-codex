/*
 * Decompiled with CFR 0.150.
 */
package com.rit.sucy.config;

import com.rit.sucy.config.RootConfig;
import com.rit.sucy.service.ConfigNode;
import java.util.ArrayList;
import java.util.List;

public enum LanguageNode implements ConfigNode
{
    ANVIL_COMPONENT("Anvil Component", ConfigNode.VarType.LIST, (List<String>)new ArrayList<String>(){
        {
            this.add("&2Anvil Components");
            this.add("&dPlace components");
            this.add("&dover here!");
        }
    }),
    ANVIL_SEPARATOR("Anvil Separator", ConfigNode.VarType.LIST, (List<String>)new ArrayList<String>(){
        {
            this.add("&2Anvil Book");
            this.add("&d<- Components");
            this.add("&8------------");
            this.add("&dResults ->");
        }
    }),
    ANVIL_RESULT("Anvil Result", ConfigNode.VarType.LIST, (List<String>)new ArrayList<String>(){
        {
            this.add("&2Anvil Result");
            this.add("&dResults will");
            this.add("&dshow up over");
            this.add("&dhere!");
        }
    }),
    TABLE_ENCHANTABLE("Table Enchantable", ConfigNode.VarType.LIST, (List<String>)new ArrayList<String>(){
        {
            this.add("&2Placeholder");
            this.add("&dEnchantable");
        }
    }),
    TABLE_UNENCHANTABLE("Table Unenchantable", ConfigNode.VarType.LIST, (List<String>)new ArrayList<String>(){
        {
            this.add("&2Placeholder");
            this.add("&4Unenchantable");
        }
    }),
    NAME_FORMAT("Name Format", ConfigNode.VarType.LIST, (List<String>)new ArrayList<String>(){
        {
            this.add("{adjective} {weapon} of {suffix}");
        }
    });

    final String path;
    final ConfigNode.VarType type;
    final List<String> value;

    private LanguageNode(String path, ConfigNode.VarType type, List<String> value) {
        this.type = type;
        this.path = path;
        this.value = value;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    public String getFullPath() {
        return RootConfig.baseNode + "Language." + this.getPath();
    }

    @Override
    public ConfigNode.VarType getVarType() {
        return this.type;
    }

    @Override
    public ConfigNode.SubType getSubType() {
        return null;
    }

    @Override
    public Object getDefaultValue() {
        return this.value;
    }
}


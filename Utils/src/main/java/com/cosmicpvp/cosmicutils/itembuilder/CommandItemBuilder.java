package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CommandItemBuilder
        extends ItemBuilder {
    private String command;
    private String message;
    private String itemName;
    private String[] itemLore;

    public CommandItemBuilder() {
        super(Material.ENCHANTED_BOOK);
    }

    public CommandItemBuilder(String cmd, String message, String itemName, String... lore) {
        super(Material.ENCHANTED_BOOK);
        this.command = cmd;
        this.itemName = itemName;
        this.itemLore = lore;
    }

    public ItemBuilder setCommand(String cmd) {
        this.command = cmd;
        return this;
    }

    public ItemBuilder setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public ItemBuilder setItemLore(String... lore) {
        this.itemLore = lore;
        return this;
    }

    public ItemBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public ItemStack build() {
        ItemStack i = ItemUtils.buildItem(this.getType(), (short) 0, ChatColor.translateAlternateColorCodes('&', this.itemName), (String[]) this.itemLore);
        NBTWrapper wrapper = new NBTWrapper(i);
        NBTTagCompoundBuilder builder = wrapper.getCompoundOrCreate("conaxData");
        wrapper.setString("cosmicType", "commandItem");
        builder.setString("command", this.command);
        builder.setString("message", this.message);
        wrapper.setNBTBase("conaxData", builder.get());
        i = wrapper.build();
        return i;
    }
}


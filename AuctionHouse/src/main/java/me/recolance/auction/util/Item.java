/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_8_R3.me.astropvp.cratekeys.inventory.CraftItemStack
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.SkullMeta
 */
package me.recolance.auction.util;

import me.recolance.globalutils.attribute.Attributes;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.UUID;

public class Item {
    private ItemStack item;

    public Item() {
        this.item = new ItemStack(Material.AIR, 1);
    }

    public Item(ItemStack item) {
        this.item = item;
    }

    public Item(Item item) {
        item = this;
    }

    public Item setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public Item setType(Material mat) {
        this.item.setType(mat);
        return this;
    }

    public Item setType(int id) {
        this.item.setTypeId(id);
        return this;
    }

    public Item setDurability(short d) {
        this.item.setDurability(d);
        return this;
    }

    public Item setDurability(int d) {
        this.item.setDurability((short) d);
        return this;
    }

    public Item setName(String name) {
        name = ChatColor.translateAlternateColorCodes((char) '&', (String) name);
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        this.item.setItemMeta(meta);
        return this;
    }

    public Item addLore(String... lore) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> loreList = new ArrayList<String>();
        if (meta.hasLore()) {
            loreList.addAll(meta.getLore());
        }
        for (String line : lore) {
            loreList.add(ChatColor.translateAlternateColorCodes((char) '&', (String) line));
        }
        meta.setLore(loreList);
        this.item.setItemMeta(meta);
        return this;
    }

    public boolean isEnchanted() {
        return this.item.hasItemMeta() && this.item.getItemMeta().hasEnchants();
    }

    public Item addEnchant(int eId, int level) {
        return this.addEnchant(Enchantment.getById((int) eId), level);
    }

    public Item addEnchant(Enchantment enchant, int level) {
        this.item.addUnsafeEnchantment(enchant, level);
        return this;
    }

    public Item incrementEnchant(int eId, int amount) {
        return this.incrementEnchant(Enchantment.getById((int) eId), amount);
    }

    public Item incrementEnchant(Enchantment enchant, int amount) {
        if (!this.item.containsEnchantment(enchant)) {
            this.item.addUnsafeEnchantment(enchant, amount);
        } else {
            int cEL = this.item.getEnchantmentLevel(enchant);
            this.item.removeEnchantment(enchant);
            this.item.addUnsafeEnchantment(enchant, cEL + amount);
        }
        return this;
    }

    public Item incrementSafeEnchant(int eId, int amount) {
        return this.incrementSafeEnchant(Enchantment.getById((int) eId), amount);
    }

    public Item incrementSafeEnchant(Enchantment enchant, int amount) {
        int mEL = enchant.getMaxLevel();
        if (!this.item.containsEnchantment(enchant)) {
            int inc = amount > mEL ? mEL : amount;
            this.item.addEnchantment(enchant, inc);
        } else {
            int cEL = this.item.getEnchantmentLevel(enchant);
            int inc = cEL + amount > mEL ? mEL : cEL + amount;
            this.item.removeEnchantment(enchant);
            this.item.addEnchantment(enchant, inc);
        }
        return this;
    }

    public Item skull(UUID uuid) {
        return this.skull(Bukkit.getOfflinePlayer((UUID) uuid).getName());
    }

    public Item skull(String name) {
        SkullMeta meta = (SkullMeta) this.item.getItemMeta();
        meta.setOwner(name);
        this.item.setItemMeta((ItemMeta) meta);
        return this;
    }

    public Item hideFlag(FlagType flagType) {
        int type = (Integer) flagType.getFlag();
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy((ItemStack) this.item);
        NBTTagCompound compound = stack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
            stack.setTag(compound);
            compound = stack.getTag();
        }
        compound.setInt("HideFlags", type);
        stack.setTag(compound);
        this.item = CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack) stack);
        return this;
    }

    public Item setUnbreakable(boolean isUnbreakable) {
        int type = isUnbreakable ? (Integer) FlagType.UNBREAKABLE_TRUE.getFlag() : (Integer) FlagType.UNBREAKABLE_FALSE.getFlag();
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy((ItemStack) this.item);
        NBTTagCompound compound = stack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
            stack.setTag(compound);
            compound = stack.getTag();
        }
        compound.setInt("Unbreakable", type);
        stack.setTag(compound);
        this.item = CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack) stack);
        return this;
    }

    public Item addAttribute(String name, Attributes.AttributeType type, double amount) {
        Attributes a = new Attributes(this.item);
        a.add(Attributes.Attribute.newBuilder().name(name).type(type).amount(amount).build());
        this.item = a.getStack();
        return this;
    }

    public ItemStack get() {
        return this.item;
    }

    public static enum FlagType {
        ENCHANTMENTS(1),
        ATTRIBUTES(2),
        UNBREAKABLE(4),
        CAN_DESTROY(8),
        CAN_PLACE_ON(16),
        OTHERS(32),
        ALL(64),
        UNBREAKABLE_TRUE(1),
        UNBREAKABLE_FALSE(0);

        private Object flag;

        private FlagType(Object flag) {
            this.flag = flag;
        }

        public Object getFlag() {
            return this.flag;
        }
    }
}


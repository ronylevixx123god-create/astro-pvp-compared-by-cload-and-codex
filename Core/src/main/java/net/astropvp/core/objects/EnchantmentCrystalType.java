package net.astropvp.core.objects;

import net.astropvp.core.objects.crystals.DepthStrider;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

public enum EnchantmentCrystalType {
    DEPTH_STRIDER(Enchantment.DEPTH_STRIDER, (short) 12, ChatColor.AQUA, "Depth Strider III", new DepthStrider());

    Enchantment enchantment;
    short data;
    ChatColor chatColor;
    String name;
    EnchantmentCrystal instance;

    EnchantmentCrystalType(Enchantment enchantment, short data, ChatColor chatColor, String name, EnchantmentCrystal instance) {
        this.enchantment = enchantment;
        this.data = data;
        this.chatColor = chatColor;
        this.name = name;
        this.instance = instance;
    }

    public Enchantment getEnchantment() { return this.enchantment; }
    public short getData() { return this.data; }
    public ChatColor getChatColor() { return this.chatColor; }
    public String getName() { return this.name; }
    public EnchantmentCrystal getInstance() { return this.instance; }


}

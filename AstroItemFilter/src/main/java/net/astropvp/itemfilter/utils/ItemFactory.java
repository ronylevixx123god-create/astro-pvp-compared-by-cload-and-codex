package net.astropvp.itemfilter.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemFactory {
    private Material material;
    private int amount = 1;
    private final short damage = 0;
    private byte data = 0;
    private boolean glow = false;
    private String display = "";
    private List<String> lore = new ArrayList<>();

    public ItemFactory(Material Material) {
        this.material = Material;
    }

    public ItemFactory(String String, Material Material) {
        String = String.toUpperCase().replace(" ", "_");
        if (String.contains(":")) {
            String[] arrayOfString = String.split(":");
            try {
                this.material = org.bukkit.Material.getMaterial(Integer.parseInt(arrayOfString[0]));
                this.material.getData();
            } catch (Exception localException2) {
                this.material = org.bukkit.Material.getMaterial(arrayOfString[0]);
            }
            try {
                this.data = Byte.parseByte(arrayOfString[1]);
            } catch (Exception localException3) {
            }
        } else {
            try {
                this.material = org.bukkit.Material.getMaterial(Integer.parseInt(String));
                this.material.getData();
            } catch (Exception localException1) {
                this.material = org.bukkit.Material.getMaterial(String);
            }
        }
        if (this.material == null) {
            this.material = Material;
        }
    }

    public ItemFactory(String String) {
        String = String.toUpperCase().replace(" ", "_");
        if (String != null) {
            if (String.contains(":")) {
                String[] arrayOfString = String.split(":");
                try {
                    this.material = Material.getMaterial(Integer.parseInt(arrayOfString[0]));
                } catch (Exception localException2) {
                    this.material = Material.getMaterial(arrayOfString[0]);
                }
                try {
                    this.data = Byte.parseByte(arrayOfString[1]);
                } catch (Exception localException3) {
                }
            } else {
                try {
                    this.material = Material.getMaterial(Integer.parseInt(String));
                } catch (Exception localException1) {
                    this.material = Material.getMaterial(String);
                }
            }
        }
    }

    public ItemStack build() {
        ItemStack localItemStack = new ItemStack(this.material);
        localItemStack.setDurability(this.data);
        if (this.amount > 0) {
            localItemStack.setAmount(this.amount);
        } else {
            localItemStack.setAmount(1);
        }
        ItemMeta localItemMeta = localItemStack.getItemMeta();
        if (!this.display.equals("")) {
            localItemMeta.setDisplayName(this.display);
        }
        if (this.glow) {
            localItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            localItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (!this.lore.isEmpty()) {
            localItemMeta.setLore(this.lore);
        }
        localItemStack.setItemMeta(localItemMeta);
        return localItemStack;
    }

    public ItemFactory amount(int Int) {
        this.amount = Int;
        return this;
    }

    public ItemFactory display(String String) {
        this.display = String;
        return this;
    }

    public ItemFactory display(char Char, String String) {
        this.display = ChatColor.translateAlternateColorCodes(Char, String);
        return this;
    }

    public ItemFactory lore(List<String> List) {
        this.lore = List;
        return this;
    }

    public ItemFactory lore(char Char, List<String> List) {
        for (int i = 0; i < List.size(); i++) {
            List.set(i, ChatColor.translateAlternateColorCodes(Char, List.get(i)));
        }
        this.lore = List;
        return this;
    }

    public ItemFactory glow(boolean Boolean) {
        this.glow = Boolean;
        return this;
    }

    public static ItemStack getPlayerSkull(String String1, String String2) {
        ItemStack localItemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta localSkullMeta = (SkullMeta) localItemStack.getItemMeta();
        localSkullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String2));
        localSkullMeta.setOwner(String1);
        localItemStack.setItemMeta(localSkullMeta);
        return localItemStack;
    }

    public static ItemStack replaceInMeta(ItemStack ItemStack, String... VarArgs) {
        ItemStack = ItemStack.clone();
        ItemMeta localItemMeta = ItemStack.getItemMeta();
        for (int i = 0; i < VarArgs.length; i += 2) {
            if (VarArgs.length > i) {
                String str1 = VarArgs[i];
                String str2 = VarArgs[(i + 1)];
                localItemMeta.setDisplayName(localItemMeta.getDisplayName().replace(str1, str2));
                List localList = localItemMeta.hasLore() ? localItemMeta.getLore() : Collections.emptyList();
                for (int j = 0; j < localList.size(); j++) {
                    localList.set(j, ((String) localList.get(j)).replace(str1, str2));
                }
                localItemMeta.setLore(localList);
            }
        }
        ItemStack.setItemMeta(localItemMeta);
        return ItemStack;
    }
}

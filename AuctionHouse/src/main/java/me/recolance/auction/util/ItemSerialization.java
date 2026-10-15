/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTCompressedStreamTools
 *  net.minecraft.server.v1_8_R3.NBTReadLimiter
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  net.minecraft.server.v1_8_R3.NBTTagList
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
 */
package me.recolance.auction.util;

import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemSerialization {
    private static final int VERSION = 1;
    private static final String INVENTORY_CUSTOM = "custom";
    private static final String INVENTORY_PLAYER = "player";
    private static final int NBT_TYPE_COMPOUND = 10;
    private static final String NBT_TYPE = "type";
    private static final String NBT_ITEM = "item";
    private static final String NBT_ITEMS = "items";
    private static final String NBT_VERSION = "version";
    private static final String NBT_PLAYER_HELD_INDEX = "player_held_index";
    private static final String NBT_PLAYER_NAME = "player_name";
    private static Method WRITE_NBT;
    private static Method READ_NBT;

    public static String inventoryToString(Inventory inv) {
        return ItemSerialization.toBase64(inv);
    }

    public static Inventory getInventoryFromString(String s, String name, int size) {
        return ItemSerialization.fromBase64(s, name, size);
    }

    public static String itemListToBase64(List<ItemStack> items) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
        NBTTagCompound root = new NBTTagCompound();
        NBTTagList itemList = new NBTTagList();
        for (ItemStack is : items) {
            NBTTagCompound outputObject = new NBTTagCompound();
            CraftItemStack craft = ItemSerialization.getCraftVersion(is);
            net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy((ItemStack) craft);
            if (nmsCopy != null) {
                nmsCopy.save(outputObject);
            }
            itemList.add((NBTBase) outputObject);
        }
        root.setInt(NBT_VERSION, 1);
        root.set(NBT_ITEMS, (NBTBase) itemList);
        ItemSerialization.writeNbt((NBTBase) root, dataOutput);
        return Base64Coder.encodeLines((byte[]) outputStream.toByteArray());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive exception aggregation
     */
    public static String itemToStringBase64(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                String string;
                block11:
                {
                    DataOutputStream dataOutput = new DataOutputStream(outputStream);
                    try {
                        NBTTagCompound root = new NBTTagCompound();
                        CraftItemStack craft = ItemSerialization.getCraftVersion(item);
                        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy((ItemStack) craft);
                        NBTTagCompound outputObject = new NBTTagCompound();
                        if (nmsCopy != null) {
                            nmsCopy.save(outputObject);
                        }
                        root.setInt(NBT_VERSION, 1);
                        root.set(NBT_ITEM, (NBTBase) outputObject);
                        ItemSerialization.writeNbt((NBTBase) root, dataOutput);
                        String retr = Base64Coder.encodeLines((byte[]) outputStream.toByteArray());
                        dataOutput.close();
                        outputStream.close();
                        string = retr;
                        if (Collections.singletonList(dataOutput).get(0) == null) break block11;
                    } catch (Throwable throwable) {
                        if (Collections.singletonList(dataOutput).get(0) != null) {
                            dataOutput.close();
                        }
                        throw throwable;
                    }
                    dataOutput.close();
                }
                return string;
            } finally {
                if (Collections.singletonList(outputStream).get(0) != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack itemFromBase64(String data) {
        if (data == null) {
            return new ItemBuilder(Material.DIRT, "Invalid NBT Data!", new String[0]).build();
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines((String) data));
        NBTTagCompound root = (NBTTagCompound) ItemSerialization.readNbt(new DataInputStream(inputStream), 0);
        if (root.getInt(NBT_VERSION) != 1) {
            throw new IllegalArgumentException("Incompatible version: " + root.getInt(NBT_VERSION));
        }
        NBTTagCompound compound = root.getCompound(NBT_ITEM);
        if (!compound.isEmpty()) {
            return CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack) net.minecraft.server.v1_8_R3.ItemStack.createStack((NBTTagCompound) compound));
        }
        return null;
    }

    public static List<ItemStack> itemListFromBase64(String data) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines((String) data));
        NBTTagCompound root = (NBTTagCompound) ItemSerialization.readNbt(new DataInputStream(inputStream), 0);
        if (root.getInt(NBT_VERSION) != 1) {
            throw new IllegalArgumentException("Incompatible version: " + root.getInt(NBT_VERSION));
        }
        NBTTagList itemList = root.getList(NBT_ITEMS, 10);
        for (int i = 0; i < itemList.size(); ++i) {
            NBTTagCompound inputObject = itemList.get(i);
            if (inputObject.isEmpty()) continue;
            items.add((ItemStack) CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack) net.minecraft.server.v1_8_R3.ItemStack.createStack((NBTTagCompound) inputObject)));
        }
        return items;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive exception aggregation
     */
    public static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                String string;
                block13:
                {
                    DataOutputStream dataOutput = new DataOutputStream(outputStream);
                    try {
                        NBTTagCompound root = new NBTTagCompound();
                        NBTTagList itemList = new NBTTagList();
                        String type = INVENTORY_CUSTOM;
                        int size = inventory.getSize();
                        if (inventory instanceof PlayerInventory) {
                            PlayerInventory playerInventory = (PlayerInventory) inventory;
                            root.setInt(NBT_PLAYER_HELD_INDEX, playerInventory.getHeldItemSlot());
                            root.setString(NBT_PLAYER_NAME, playerInventory.getHolder().getName());
                            type = INVENTORY_PLAYER;
                            size += 4;
                        }
                        for (int i = 0; i < size; ++i) {
                            NBTTagCompound outputObject = new NBTTagCompound();
                            CraftItemStack craft = ItemSerialization.getCraftVersion(inventory.getItem(i));
                            net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy((ItemStack) craft);
                            if (nmsCopy != null) {
                                nmsCopy.save(outputObject);
                            }
                            itemList.add((NBTBase) outputObject);
                        }
                        root.setString(NBT_TYPE, type);
                        root.setInt(NBT_VERSION, 1);
                        root.set(NBT_ITEMS, (NBTBase) itemList);
                        ItemSerialization.writeNbt((NBTBase) root, dataOutput);
                        string = Base64Coder.encodeLines((byte[]) outputStream.toByteArray());
                        if (Collections.singletonList(dataOutput).get(0) == null) break block13;
                    } catch (Throwable throwable) {
                        if (Collections.singletonList(dataOutput).get(0) != null) {
                            dataOutput.close();
                        }
                        throw throwable;
                    }
                    dataOutput.close();
                }
                return string;
            } finally {
                if (Collections.singletonList(outputStream).get(0) != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Inventory fromBase64(String data, String name, int size) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines((String) data));
        NBTTagCompound root = (NBTTagCompound) ItemSerialization.readNbt(new DataInputStream(inputStream), 0);
        if (root.getInt(NBT_VERSION) != 1) {
            throw new IllegalArgumentException("Incompatible version: " + root.getInt(NBT_VERSION));
        }
        NBTTagList itemList = root.getList(NBT_ITEMS, 10);
        String type = root.getString(NBT_TYPE);
        return ItemSerialization.parseInventory(root, size, itemList, type, name);
    }

    private static Inventory parseInventory(NBTTagCompound root, int size, NBTTagList itemList, String type, String name) {
        CraftInventoryCustom inventory = new CraftInventoryCustom(null, size, name);
        for (int i = 0; i < itemList.size(); ++i) {
            NBTTagCompound inputObject = itemList.get(i);
            if (inputObject.isEmpty()) continue;
            inventory.setItem(i, (ItemStack) CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack) net.minecraft.server.v1_8_R3.ItemStack.createStack((NBTTagCompound) inputObject)));
        }
        return inventory;
    }

    private static void writeNbt(NBTBase base, DataOutput output) {
        if (WRITE_NBT == null) {
            try {
                WRITE_NBT = NBTCompressedStreamTools.class.getDeclaredMethod("a", NBTBase.class, DataOutput.class);
                WRITE_NBT.setAccessible(true);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to find private write method.", e);
            }
        }
        try {
            WRITE_NBT.invoke(null, new Object[]{base, output});
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to write " + (Object) base + " to " + output, e);
        }
    }

    private static NBTBase readNbt(DataInput input, int level) {
        if (READ_NBT == null) {
            try {
                READ_NBT = NBTCompressedStreamTools.class.getDeclaredMethod("a", DataInput.class, Integer.TYPE, NBTReadLimiter.class);
                READ_NBT.setAccessible(true);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to find private read method.", e);
            }
        }
        try {
            return (NBTBase) READ_NBT.invoke(null, new Object[]{input, level, NBTReadLimiter.a});
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to read from " + input, e);
        }
    }

    public static Inventory getInventoryFromArray(ItemStack[] items) {
        CraftInventoryCustom custom = new CraftInventoryCustom(null, items.length);
        for (int i = 0; i < items.length; ++i) {
            if (items[i] == null) continue;
            custom.setItem(i, items[i]);
        }
        return custom;
    }

    private static CraftItemStack getCraftVersion(ItemStack stack) {
        if (stack instanceof CraftItemStack) {
            return (CraftItemStack) stack;
        }
        if (stack != null) {
            return CraftItemStack.asCraftCopy((ItemStack) stack);
        }
        return null;
    }
}


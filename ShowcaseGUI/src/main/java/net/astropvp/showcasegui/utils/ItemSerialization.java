package net.astropvp.showcasegui.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTReadLimiter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
public class ItemSerialization {
    private static final int VERSION = 1;
    private static final String INVENTORY_CUSTOM = "custom";
    private static final String INVENTORY_PLAYER = "player";
    private static final int NBT_TYPE_COMPOUND = 10;
    private static final String NBT_TYPE = "type";
    private static final String NBT_ITEMS = "items";
    private static final String NBT_VERSION = "version";
    private static final String NBT_PLAYER_HELD_INDEX = "player_held_index";
    private static final String NBT_PLAYER_NAME = "player_name";
    private static Method WRITE_NBT;
    private static Method READ_NBT;

    public static String inventoryToString(Inventory inv) {
        return toBase64(inv);
    }

    public static Inventory getInventoryFromString(String s, String name, int size) {
        return fromBase64(s, name, size);
    }

    public static String itemListToBase64(List<ItemStack> items) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
        NBTTagCompound root = new NBTTagCompound();
        NBTTagList itemList = new NBTTagList();

        NBTTagCompound outputObject;
        for(Iterator var5 = items.iterator(); var5.hasNext(); itemList.add(outputObject)) {
            ItemStack is = (ItemStack)var5.next();
            outputObject = new NBTTagCompound();
            CraftItemStack craft = getCraftVersion(is);
            net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(craft);
            if (nmsCopy != null) {
                nmsCopy.save(outputObject);
            }
        }

        root.setInt("version", 1);
        root.set("items", itemList);
        writeNbt(root, dataOutput);
        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    public static List<ItemStack> itemListFromBase64(String data) {
        List<ItemStack> items = new ArrayList();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        NBTTagCompound root = (NBTTagCompound)readNbt(new DataInputStream(inputStream), 0);
        if (root.getInt("version") != 1) {
            throw new IllegalArgumentException("Incompatible version: " + root.getInt("version"));
        } else {
            NBTTagList itemList = root.getList("items", 10);

            for(int i = 0; i < itemList.size(); ++i) {
                NBTTagCompound inputObject = itemList.get(i);
                if (!inputObject.isEmpty()) {
                    items.add(CraftItemStack.asCraftMirror(net.minecraft.server.v1_8_R3.ItemStack.createStack(inputObject)));
                }
            }

            return items;
        }
    }

    public static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                DataOutputStream dataOutput = new DataOutputStream(outputStream);

                try {
                    NBTTagCompound root = new NBTTagCompound();
                    NBTTagList itemList = new NBTTagList();
                    String type = "custom";
                    int size = inventory.getSize();
                    if (inventory instanceof PlayerInventory) {
                        PlayerInventory playerInventory = (PlayerInventory)inventory;
                        root.setInt("player_held_index", playerInventory.getHeldItemSlot());
                        root.setString("player_name", playerInventory.getHolder().getName());
                        type = "player";
                        size += 4;
                    }

                    for(int i = 0; i < size; ++i) {
                        NBTTagCompound outputObject = new NBTTagCompound();
                        CraftItemStack craft = getCraftVersion(inventory.getItem(i));
                        net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(craft);
                        if (nmsCopy != null) {
                            nmsCopy.save(outputObject);
                        }

                        itemList.add(outputObject);
                    }

                    root.setString("type", type);
                    root.setInt("version", 1);
                    root.set("items", itemList);
                    writeNbt(root, dataOutput);
                    String var23 = Base64Coder.encodeLines(outputStream.toByteArray());
                    return var23;
                } finally {
                    if (Collections.singletonList(dataOutput).get(0) != null) {
                        dataOutput.close();
                    }

                }
            } finally {
                if (Collections.singletonList(outputStream).get(0) != null) {
                    outputStream.close();
                }

            }
        } catch (Exception var21) {
            var21.printStackTrace();
            return null;
        }
    }

    public static Inventory fromBase64(String data, String name, int size) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        NBTTagCompound root = (NBTTagCompound)readNbt(new DataInputStream(inputStream), 0);
        if (root.getInt("version") != 1) {
            throw new IllegalArgumentException("Incompatible version: " + root.getInt("version"));
        } else {
            NBTTagList itemList = root.getList("items", 10);
            String type = root.getString("type");
            return parseInventory(root, size, itemList, type, name);
        }
    }

    private static Inventory parseInventory(NBTTagCompound root, int size, NBTTagList itemList, String type, String name) {
        Inventory inventory = new CraftInventoryCustom((InventoryHolder)null, size, name);

        for(int i = 0; i < itemList.size(); ++i) {
            NBTTagCompound inputObject = itemList.get(i);
            if (!inputObject.isEmpty()) {
                inventory.setItem(i, CraftItemStack.asCraftMirror(net.minecraft.server.v1_8_R3.ItemStack.createStack(inputObject)));
            }
        }

        return inventory;
    }

    private static void writeNbt(NBTBase base, DataOutput output) {
        if (WRITE_NBT == null) {
            try {
                (WRITE_NBT = NBTCompressedStreamTools.class.getDeclaredMethod("a", NBTBase.class, DataOutput.class)).setAccessible(true);
            } catch (Exception var4) {
                throw new IllegalStateException("Unable to find private write method.", var4);
            }
        }

        try {
            WRITE_NBT.invoke((Object)null, base, output);
        } catch (Exception var3) {
            throw new IllegalArgumentException("Unable to write " + base + " to " + output, var3);
        }
    }

    private static NBTBase readNbt(DataInput input, int level) {
        if (READ_NBT == null) {
            try {
                (READ_NBT = NBTCompressedStreamTools.class.getDeclaredMethod("a", DataInput.class, Integer.TYPE, NBTReadLimiter.class)).setAccessible(true);
            } catch (Exception var4) {
                throw new IllegalStateException("Unable to find private read method.", var4);
            }
        }

        try {
            return (NBTBase)READ_NBT.invoke((Object)null, input, level, NBTReadLimiter.a);
        } catch (Exception var3) {
            throw new IllegalArgumentException("Unable to read from " + input, var3);
        }
    }

    public static Inventory getInventoryFromArray(ItemStack[] items) {
        CraftInventoryCustom custom = new CraftInventoryCustom((InventoryHolder)null, items.length);

        for(int i = 0; i < items.length; ++i) {
            if (items[i] != null) {
                custom.setItem(i, items[i]);
            }
        }

        return custom;
    }

    private static CraftItemStack getCraftVersion(ItemStack stack) {
        if (stack instanceof CraftItemStack) {
            return (CraftItemStack)stack;
        } else {
            return stack != null ? CraftItemStack.asCraftCopy(stack) : null;
        }
    }
}

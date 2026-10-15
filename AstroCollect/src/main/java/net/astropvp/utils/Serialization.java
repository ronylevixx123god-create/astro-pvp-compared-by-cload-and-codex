package net.astropvp.utils;

import net.astropvp.struct.CollectCategory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Serialization {
    public static String serialize(ItemStack[] items) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream output = new BukkitObjectOutputStream(os);
            output.writeInt(items.length);
            for(int i = 0; i < items.length; i++) {
                output.writeObject(items[i]);
            }
            output.close();
            return Base64Coder.encodeLines(os.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack[] deserialize(String string) {
        ByteArrayInputStream is = new ByteArrayInputStream(Base64Coder.decodeLines(string));
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(is);
            ItemStack[] items = new ItemStack[in.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) in.readObject();
            }
            in.close();
            return items;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
package net.astropvp.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.google.common.collect.Lists;

public class ConfigUtils {
    public static String toBase64(Inventory inventory, int size) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(size);
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (Exception e) {
            throw new IllegalStateException("Cannot into itemstacksz!", e);

        }

    }

    public static String toBase64(ItemStack[] is, int size) {

        Inventory inventory = Bukkit.createInventory(null, size);

        inventory.setContents(is);

        return toBase64(inventory, size);

    }

    public static Inventory fromBase64(String data) {

        try {

            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));

            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            for (int i = 0; i < inventory.getSize(); i++) {

                inventory.setItem(i, (ItemStack) dataInput.readObject());

            }

            dataInput.close();

            return inventory;

        } catch (Exception e) {

        }

        return null;

    }
}

package net.astropvp.christmas.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ConfigUtils {
    public static String toBase64(Inventory inventory, int size) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream)outputStream);
            dataOutput.writeInt(size);
            for (int i = 0; i < inventory.getSize(); ++i) {
                dataOutput.writeObject((Object)inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines((byte[])outputStream.toByteArray());
        }
        catch (Exception e) {
            throw new IllegalStateException("Cannot into itemstacks!", e);
        }
    }

    public static String toBase64(ItemStack[] is, int size) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)size);
        inventory.setContents(is);
        return ConfigUtils.toBase64(inventory, size);
    }

    public static Inventory fromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines((String)data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream)inputStream);
            Inventory inventory = Bukkit.getServer().createInventory((InventoryHolder)null, dataInput.readInt());
            for (int i = 0; i < inventory.getSize(); ++i) {
                inventory.setItem(i, (ItemStack)dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static ItemStack getItem(String line) {
        return ConfigUtils.fromBase64(line.split(":")[0]).getContents()[0];
    }

    public static String locationToString(Location location) {
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getYaw();
    }

    public static Location stringToLocation(String s) {
        String[] array = s.split(":");
        World world = Bukkit.getWorld(array[0]);
        double x = Double.parseDouble(array[1]);
        double y = Double.parseDouble(array[2]);
        double z = Double.parseDouble(array[3]);
        float yaw = Float.parseFloat(array[4]);
        return new Location(world, x, y, z, yaw, 0);
    }
}


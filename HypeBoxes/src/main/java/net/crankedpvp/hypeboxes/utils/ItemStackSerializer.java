package net.crankedpvp.hypeboxes.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackSerializer {
    public static ItemStack deserialize(String base64) throws IOException, ClassNotFoundException {
        byte[] data = Base64Coder.decodeLines(base64);
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
        BukkitObjectInputStream in = new BukkitObjectInputStream(bytesIn);
        ItemStack itemStack = (ItemStack) in.readObject();
        in.close();
        return itemStack;
    }

    public static String serialize(ItemStack itemStack) throws IOException {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        BukkitObjectOutputStream out = new BukkitObjectOutputStream(bytesOut);
        out.writeObject(itemStack);
        out.flush();
        out.close();
        return Base64Coder.encodeLines(bytesOut.toByteArray());
    }
}

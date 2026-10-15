package net.astropvp.storage.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class ItemStackDeserializer implements JsonDeserializer<ItemStack> {

    public static ItemStack deserialize(String base64) throws IOException, ClassNotFoundException {
        byte[] data = Base64Coder.decodeLines(base64);
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
        BukkitObjectInputStream in = new BukkitObjectInputStream(bytesIn);
        ItemStack itemStack = (ItemStack) in.readObject();
        in.close();
        return itemStack;
    }

    @SneakyThrows
    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return deserialize(jsonElement.getAsJsonPrimitive().getAsString());
    }
}

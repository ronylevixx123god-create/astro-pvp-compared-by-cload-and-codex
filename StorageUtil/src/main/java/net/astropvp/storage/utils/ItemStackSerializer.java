package net.astropvp.storage.utils;

import com.google.gson.*;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonSerializer<ItemStack> {

    public static String serialize(ItemStack itemStack) throws IOException {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        BukkitObjectOutputStream out = new BukkitObjectOutputStream(bytesOut);
        out.writeObject(itemStack);
        out.flush();
        out.close();
        return Base64Coder.encodeLines(bytesOut.toByteArray());
    }

    @SneakyThrows
    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        String serialized = serialize(itemStack);
        JsonObject jsonElement = (JsonObject) JsonParser.parseString("{ \"item\": \"" + serialized + "\" }");
        return jsonElement.getAsJsonPrimitive("item");
    }
}

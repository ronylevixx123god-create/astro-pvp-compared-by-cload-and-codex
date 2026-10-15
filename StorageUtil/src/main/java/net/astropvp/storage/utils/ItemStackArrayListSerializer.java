package net.astropvp.storage.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.List;

public class ItemStackArrayListSerializer implements JsonSerializer<List<ItemStack>> {

    @SneakyThrows
    @Override
    public JsonElement serialize(List<ItemStack> itemStacks, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonArray jsonArray = new JsonArray();

        for (ItemStack itemStack : itemStacks) {
            jsonArray.add(jsonSerializationContext.serialize(itemStack, ItemStack.class));
        }

        return jsonArray;
    }
}

package net.astropvp.storage.utils;

import com.google.gson.*;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackArraySerializer implements JsonSerializer<ItemStack[]> {

    @SneakyThrows
    @Override
    public JsonElement serialize(ItemStack[] itemStacks, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonArray jsonArray = new JsonArray();

        for (ItemStack itemStack : itemStacks) {
            jsonArray.add(jsonSerializationContext.serialize(itemStack, ItemStack.class));
        }

        return jsonArray;
    }
}

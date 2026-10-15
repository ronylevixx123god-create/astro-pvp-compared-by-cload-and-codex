package net.astropvp.loottable.typeadapters;

import net.astropvp.storage.gson.*;
import net.astropvp.storage.utils.ItemStackSerializer;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class LootMapSerializer implements JsonSerializer<Map<ItemStack, Double>> {

    @Override
    public JsonElement serialize(Map<ItemStack, Double> itemStackDoubleMap, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        itemStackDoubleMap.forEach((itemStack, aDouble) -> {
            try {
                jsonObject.add(ItemStackSerializer.serialize(itemStack), new JsonPrimitive(aDouble));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return jsonObject;
    }
}

package net.astropvp.storage.utils;

import com.google.gson.*;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemStackArrayDeserializer implements JsonDeserializer<ItemStack[]> {

    @SneakyThrows
    @Override
    public ItemStack[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray jsonArray = ((JsonArray) jsonElement);
        List<ItemStack> items = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            items.add(jsonDeserializationContext.deserialize(element.getAsJsonPrimitive(), ItemStack.class));
        }


        return items.toArray(new ItemStack[] {});
    }
}

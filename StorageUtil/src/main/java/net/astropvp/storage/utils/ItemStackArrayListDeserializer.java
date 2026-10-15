package net.astropvp.storage.utils;

import com.google.gson.*;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemStackArrayListDeserializer implements JsonDeserializer<List<ItemStack>> {

    @SneakyThrows
    @Override
    public List<ItemStack> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray jsonArray = ((JsonArray) jsonElement);
        List<ItemStack> items = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            items.add(jsonDeserializationContext.deserialize(element.getAsJsonPrimitive(), ItemStack.class));
        }


        return items;
    }
}

package net.astropvp.core.typeadapters;

import net.astropvp.core.struct.Gift;
import net.astropvp.storage.gson.*;
import net.astropvp.storage.utils.ItemStackSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

import java.lang.reflect.Type;
import java.util.List;

public class GiftsListAdapterSerializer implements JsonSerializer<List<Gift>> {

    @Override
    public JsonElement serialize(List<Gift> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();

        for (Gift gift : src) {
            jsonArray.add(context.serialize(gift, Gift.class));
        }

        return jsonArray;
    }
}

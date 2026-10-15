package net.astropvp.core.typeadapters;



import net.astropvp.core.struct.Gift;
import net.astropvp.storage.gson.JsonArray;
import net.astropvp.storage.gson.JsonDeserializationContext;
import net.astropvp.storage.gson.JsonDeserializer;
import net.astropvp.storage.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GiftsListAdapterDeserializer implements JsonDeserializer<List<Gift>> {

    @Override
    public List<Gift> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonArray jsonArray = ((JsonArray) json);
        List<Gift> gifts = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            try {
                gifts.add(context.deserialize(element.getAsJsonObject(), Gift.class));
            } catch (IllegalStateException exception) {
                exception.printStackTrace();
            }
        }

        return gifts;
    }
}

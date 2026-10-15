package net.astropvp.core.typeadapters;

import net.astropvp.core.struct.Gift;
import net.astropvp.storage.gson.*;
import net.astropvp.storage.utils.ItemStackSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

import java.lang.reflect.Type;

public class GiftAdapterDeserializer implements JsonDeserializer<Gift> {

    @Override
    public Gift deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();

/*        Gift gift = new Gift(
                jsonObject.getAsJsonPrimitive("").getAsString()
        );
        */
        return context.deserialize(json, typeOfT);
    }
}

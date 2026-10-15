package net.astropvp.core.typeadapters;


import net.astropvp.core.struct.Gift;
import net.astropvp.storage.gson.JsonElement;
import net.astropvp.storage.gson.JsonObject;
import net.astropvp.storage.gson.JsonSerializationContext;
import net.astropvp.storage.gson.JsonSerializer;
import net.astropvp.storage.utils.ItemStackSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class GiftAdapterSerializer implements JsonSerializer<Gift> {

    @Override
    public JsonElement serialize(Gift src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("sender", src.getSender().toString());
        obj.addProperty("senderName", src.getSenderName());
        try {
            obj.addProperty("itemStack", ItemStackSerializer.serialize(src.getItemStack()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        obj.addProperty("message", src.getMessage());
        obj.addProperty("time", src.getTime());
        return obj;
    }
}

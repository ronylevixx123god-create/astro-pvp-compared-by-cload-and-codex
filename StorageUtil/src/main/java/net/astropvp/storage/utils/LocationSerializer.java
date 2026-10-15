package net.astropvp.storage.utils;

import com.google.gson.*;
import lombok.SneakyThrows;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location> {

    public static String serialize(Location obj) {
        if (obj == null) {
            return "";
        }
        return obj.getWorld().getName() + ":" + obj.getBlockX() + ":" + obj.getBlockY() + ":" + obj.getBlockZ();
    }

    @SneakyThrows
    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        String serialized = serialize(location);
        JsonObject jsonElement = (JsonObject) JsonParser.parseString("{ \"location\": \"" + serialized + "\" }");
        return jsonElement.getAsJsonPrimitive("location");
    }
}

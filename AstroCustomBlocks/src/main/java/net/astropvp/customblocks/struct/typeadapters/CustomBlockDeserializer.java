package net.astropvp.customblocks.struct.typeadapters;


import com.cosmicpvp.cosmicutils.utils.LocationUtils;
import lombok.SneakyThrows;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.ActivateAble;
import net.astropvp.customblocks.struct.abilities.LevelAble;
import net.astropvp.customblocks.struct.impl.AutoGrinder;
import net.astropvp.storage.gson.*;

import java.lang.reflect.Type;

public class CustomBlockDeserializer implements JsonDeserializer<CustomBlock> {

    @SneakyThrows
    @Override
    public CustomBlock deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        CustomBlockType cType = CustomBlockType.valueOf(jsonObject.get("type").getAsString());
        //CustomBlock customBlock = cType.getCustomBlockClass().newInstance();
        //customBlock.setLocation(LocationUtils.getLocationFromString(jsonObject.get("location").getAsString(), false));



        /*if (customBlock instanceof LevelAble) {
            ((LevelAble) customBlock).setLevel(jsonObject.get("level").getAsInt());
        }

        if (customBlock instanceof ActivateAble) {
            ((ActivateAble) customBlock).setLastActivated(jsonObject.get("lastActivated").getAsLong());
        }

        if (customBlock instanceof AutoGrinder) {
            ((AutoGrinder) customBlock).setGrinderKeys(jsonObject.get("grinderKeys").getAsInt());
            ((AutoGrinder) customBlock).setExp(jsonObject.get("exp").getAsInt());
        }*/

        return context.deserialize(jsonElement, cType.getCustomBlockClass());
    }

}

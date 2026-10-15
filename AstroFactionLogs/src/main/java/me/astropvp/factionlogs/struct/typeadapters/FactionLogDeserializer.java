package me.astropvp.factionlogs.struct.typeadapters;

import me.astropvp.factionlogs.data.FactionLogsData;
import me.astropvp.factionlogs.enums.LogType;
import net.astropvp.storage.gson.*;

import java.lang.reflect.Type;

public class FactionLogDeserializer implements JsonDeserializer<FactionLogsData> {

    @Override
    public FactionLogsData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        FactionLogsData factionLogsData = new FactionLogsData(jsonObject.get("factionId").getAsString());

        JsonObject logs = jsonObject.getAsJsonObject("logs");
        for (String key : logs.keySet()) {
            JsonArray jsonArray = logs.getAsJsonArray(key);
            LogType logType = LogType.valueOf(key);

            for (JsonElement element : jsonArray) {
                factionLogsData.addFactionLog(context.deserialize(element, logType.getLogClass()));
            }
        }

        return factionLogsData;
    }
}

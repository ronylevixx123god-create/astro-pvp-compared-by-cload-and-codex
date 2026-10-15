package net.astropvp.space.serialization

import com.google.gson.*
import me.astropvp.astropvpskins.enums.SkinType
import java.lang.reflect.Type
import java.util.*


class SkinTypeSerializerDeserializer : JsonSerializer<SkinType>, JsonDeserializer<SkinType?> {
    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, type: Type, context: JsonDeserializationContext): SkinType {
        try {
            val skinTypeString =
                jsonElement.asString.uppercase(Locale.getDefault()) // Convert to uppercase for case-insensitive matching
            return SkinType.valueOf(skinTypeString)
        } catch (e: IllegalArgumentException) {
            throw JsonParseException("Unknown skin type: " + jsonElement.asString)
        }
    }

    @Throws(JsonParseException::class)
    override fun serialize(src: SkinType, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.name.lowercase(Locale.getDefault()))
    }
}

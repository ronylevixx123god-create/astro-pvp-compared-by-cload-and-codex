package com.astropvp.cc.packet;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Base64;

class PacketSerializer implements JsonSerializer, JsonDeserializer {
   public PacketSerializer() {
   }

   public byte[] a(JsonElement var1, Type var2, JsonDeserializationContext var3) {
      return Base64.getDecoder().decode(var1.getAsString());
   }

   public JsonElement a(byte[] var1, Type var2, JsonSerializationContext var3) {
      return new JsonPrimitive(Base64.getEncoder().withoutPadding().encodeToString(var1));
   }

   public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
      return this.a((byte[])var1, var2, var3);
   }

   public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) {
      return this.a(var1, var2, var3);
   }
}

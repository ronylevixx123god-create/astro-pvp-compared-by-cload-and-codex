/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.util.com.google.gson.JsonDeserializationContext
 *  net.minecraft.util.com.google.gson.JsonDeserializer
 *  net.minecraft.util.com.google.gson.JsonElement
 *  net.minecraft.util.com.google.gson.JsonObject
 *  net.minecraft.util.com.google.gson.JsonParseException
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.recolance.auction.data;

import com.google.gson.*;
import me.recolance.auction.util.ItemSerialization;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackDeserializer implements JsonDeserializer<ItemStack> {
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object;
        if (jsonElement instanceof JsonObject && (object = (JsonObject) jsonElement).has("b64")) {
            return ItemSerialization.itemFromBase64(object.get("b64").getAsString());
        }
        return null;
    }
}


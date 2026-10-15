/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.util.com.google.gson.JsonDeserializationContext
 *  net.minecraft.util.com.google.gson.JsonDeserializer
 *  net.minecraft.util.com.google.gson.JsonElement
 *  net.minecraft.util.com.google.gson.JsonParseException
 *  net.minecraft.util.com.google.gson.JsonPrimitive
 *  net.minecraft.util.com.google.gson.JsonSerializationContext
 *  net.minecraft.util.com.google.gson.JsonSerializer
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.utils.ItemSerialization;
import com.google.gson.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class JsonItemSerializer
        implements JsonDeserializer<ItemStack>,
        JsonSerializer<ItemStack> {
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String found = jsonElement.getAsString();
        if (found == null || found.isEmpty()) {
            return null;
        }
        return ItemSerialization.singleItemFromString(found);
    }

    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(ItemSerialization.singleItemToString(itemStack));
    }
}


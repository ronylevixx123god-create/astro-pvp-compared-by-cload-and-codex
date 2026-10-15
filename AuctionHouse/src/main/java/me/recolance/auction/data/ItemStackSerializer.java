/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.util.com.google.gson.JsonElement
 *  net.minecraft.util.com.google.gson.JsonObject
 *  net.minecraft.util.com.google.gson.JsonSerializationContext
 *  net.minecraft.util.com.google.gson.JsonSerializer
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.recolance.auction.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.recolance.auction.util.ItemSerialization;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonSerializer<ItemStack> {
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject element = new JsonObject();
        element.addProperty("b64", ItemSerialization.itemToStringBase64(itemStack));
        return element;
    }
}


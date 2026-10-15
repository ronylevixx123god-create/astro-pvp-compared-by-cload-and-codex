package net.astropvp.storage;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.astropvp.storage.utils.*;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class StoragePlugin extends JavaPlugin {

    @Getter
    private static StoragePlugin instance;
    private final Type listType = new TypeToken<List<ItemStack>>(){}.getType();

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer()).setLenient()
            .registerTypeAdapter(ItemStack.class, new ItemStackDeserializer()).setLenient()
            .registerTypeAdapter(ItemStack[].class, new ItemStackArraySerializer()).setLenient()
            .registerTypeAdapter(ItemStack[].class, new ItemStackArrayDeserializer()).setLenient()
            .registerTypeAdapter(Location.class, new LocationSerializer()).setLenient()
            .registerTypeAdapter(Location.class, new LocationDeserializer()).setLenient()
            .registerTypeAdapter(listType, new ItemStackArrayListSerializer()).setLenient()
            .registerTypeAdapter(listType, new ItemStackArrayListDeserializer()).setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create();

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}

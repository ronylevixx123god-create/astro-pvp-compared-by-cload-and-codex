/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.MojangsonParser
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  net.minecraft.util.com.google.gson.ExclusionStrategy
 *  net.minecraft.util.com.google.gson.FieldAttributes
 *  net.minecraft.util.com.google.gson.Gson
 *  net.minecraft.util.com.google.gson.GsonBuilder
 *  net.minecraft.util.com.google.gson.JsonDeserializationContext
 *  net.minecraft.util.com.google.gson.JsonDeserializer
 *  net.minecraft.util.com.google.gson.JsonElement
 *  net.minecraft.util.com.google.gson.JsonObject
 *  net.minecraft.util.com.google.gson.JsonParseException
 *  net.minecraft.util.com.google.gson.JsonPrimitive
 *  net.minecraft.util.com.google.gson.JsonSerializationContext
 *  net.minecraft.util.com.google.gson.JsonSerializer
 *  net.minecraft.util.com.google.gson.TypeAdapter
 *  net.minecraft.util.com.google.gson.annotations.Expose
 *  net.minecraft.util.com.google.gson.reflect.TypeToken
 *  net.minecraft.util.com.google.gson.stream.JsonReader
 *  net.minecraft.util.com.google.gson.stream.JsonToken
 *  net.minecraft.util.com.google.gson.stream.JsonWriter
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.configuration.serialization.ConfigurationSerialization
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package net.astropvp.lootboxes.gson;

import net.astropvp.lootboxes.lootbox.AstroLootbox;
import net.astropvp.lootboxes.lootbox.AstroRewardItem;
import net.astropvp.lootboxes.util.GlowUtil;
import net.astropvp.lootboxes.api.Lootbox;
import net.astropvp.lootboxes.api.RewardItem;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.*;

public class GsonFactory {
    private static final Gson g = new Gson();
    private static final String CLASS_KEY = "SERIAL-ADAPTER-CLASS-KEY";
    private static Gson prettyGson;
    private static Gson compactGson;

    public static Gson getPrettyGson() {
        if (prettyGson == null) {
            prettyGson = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).registerTypeHierarchyAdapter(ItemStack.class, new ItemStackGsonAdapter()).registerTypeAdapter(PotionEffect.class, new PotionEffectGsonAdapter()).registerTypeAdapter(Location.class, new LocationGsonAdapter()).registerTypeAdapter(Date.class, new DateGsonAdapter()).registerTypeHierarchyAdapter(Lootbox.class, new LootboxAdapter()).registerTypeAdapter(RewardItem.class, new RewardItemAdapter()).setPrettyPrinting().disableHtmlEscaping().create();
        }
        return prettyGson;
    }

    public static Gson getCompactGson() {
        if (compactGson == null) {
            compactGson = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).registerTypeHierarchyAdapter(ItemStack.class, new ItemStackGsonAdapter()).registerTypeAdapter(PotionEffect.class, new PotionEffectGsonAdapter()).registerTypeAdapter(Location.class, new LocationGsonAdapter()).registerTypeAdapter(Date.class, new DateGsonAdapter()).registerTypeHierarchyAdapter(Lootbox.class, new LootboxAdapter()).registerTypeAdapter(RewardItem.class, new RewardItemAdapter()).disableHtmlEscaping().create();
        }
        return compactGson;
    }

    public static Gson getNewGson(boolean prettyPrinting) {
        GsonBuilder builder = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).registerTypeHierarchyAdapter(ItemStack.class, new NewItemStackAdapter()).registerTypeHierarchyAdapter(Lootbox.class, new LootboxAdapter()).registerTypeAdapter(RewardItem.class, new RewardItemAdapter()).disableHtmlEscaping();
        if (prettyPrinting) {
            builder.setPrettyPrinting();
        }
        return builder.create();
    }

    private static Map<String, Object> recursiveSerialization(ConfigurationSerializable o) {
        Map<String, Object> originalMap = o.serialize();
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry entry : originalMap.entrySet()) {
            Object o2 = entry.getValue();
            if (!(o2 instanceof ConfigurationSerializable)) continue;
            ConfigurationSerializable serializable = (ConfigurationSerializable) o2;
            Map<String, Object> newMap = GsonFactory.recursiveSerialization(serializable);
            newMap.put(CLASS_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
            map.put((String) entry.getKey(), newMap);
        }
        map.put(CLASS_KEY, ConfigurationSerialization.getAlias(o.getClass()));
        return map;
    }

    private static Map<String, Object> recursiveDoubleToInteger(Map<String, Object> originalMap) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            Object o = entry.getValue();
            if (o instanceof Double) {
                Double d = (Double) o;
                Integer i = d.intValue();
                map.put(entry.getKey(), i);
                continue;
            }
            if (o instanceof Map) {
                Map subMap = (Map) o;
                map.put(entry.getKey(), GsonFactory.recursiveDoubleToInteger(subMap));
                continue;
            }
            map.put(entry.getKey(), o);
        }
        return map;
    }

    private static net.minecraft.server.v1_8_R3.ItemStack removeSlot(ItemStack item) {
        if (item == null) {
            return null;
        }
        net.minecraft.server.v1_8_R3.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
        if (nmsi == null) {
            return null;
        }
        NBTTagCompound nbtt = nmsi.getTag();
        if (nbtt != null) {
            nbtt.remove("Slot");
            nmsi.setTag(nbtt);
        }
        return nmsi;
    }

    private static String nbtToString(NBTBase base) {
        return base.toString().replace(",}", "}").replace(",]", "]").replaceAll("[0-9]+\\:", "");
    }

    private static ItemStack removeSlotNBT(ItemStack item) {
        if (item == null) {
            return null;
        }
        net.minecraft.server.v1_8_R3.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
        if (nmsi == null) {
            return null;
        }
        NBTTagCompound nbtt = nmsi.getTag();
        if (nbtt != null) {
            nbtt.remove("Slot");
            nmsi.setTag(nbtt);
        }
        ItemStack itemStack = CraftItemStack.asBukkitCopy(nmsi);
        return itemStack;
    }

    private static class LootboxAdapter
            implements JsonDeserializer<Lootbox>,
            JsonSerializer<Lootbox> {
        private static final String CLASSNAME = "CLASSNAME";
        private static final Gson GSONNoAdapter = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).registerTypeHierarchyAdapter(ItemStack.class, new ItemStackGsonAdapter()).registerTypeAdapter(PotionEffect.class, new PotionEffectGsonAdapter()).registerTypeAdapter(Location.class, new LocationGsonAdapter()).registerTypeAdapter(Date.class, new DateGsonAdapter()).registerTypeAdapter(RewardItem.class, new RewardItemAdapter()).setPrettyPrinting().disableHtmlEscaping().create();

        private LootboxAdapter() {
        }

        public Lootbox deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Class klass;
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
            if (prim != null) {
                String className = prim.getAsString();
                klass = this.getObjectClass(className);
            } else {
                klass = AstroLootbox.class;
            }
            return (Lootbox) GSONNoAdapter.fromJson((JsonElement) jsonObject, klass);
        }

        public JsonElement serialize(Lootbox jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
            GSONNoAdapter.toJsonTree(jsonElement).getAsJsonObject().entrySet().forEach(entry -> jsonObject.add((String) entry.getKey(), (JsonElement) entry.getValue()));
            return jsonObject;
        }

        private Class getObjectClass(String className) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e.getMessage());
            }
        }
    }

    private static class RewardItemAdapter
            implements JsonSerializer<RewardItem>,
            JsonDeserializer<RewardItem> {
        private static final String CLASSNAME = "CLASSNAME";
        private static final Gson GSONNoAdapter = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy) new ExposeExlusion()).registerTypeHierarchyAdapter(ItemStack.class, new ItemStackGsonAdapter()).registerTypeAdapter(PotionEffect.class, new PotionEffectGsonAdapter()).registerTypeAdapter(Location.class, new LocationGsonAdapter()).registerTypeAdapter(Date.class, new DateGsonAdapter()).setPrettyPrinting().disableHtmlEscaping().create();

        private RewardItemAdapter() {
        }

        public RewardItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String className;
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            try {
                className = jsonObject.get(CLASSNAME).getAsString();
            } catch (NullPointerException e) {
                className = AstroRewardItem.class.getName();
            }
            Class klass = this.getObjectClass(className);
            JsonElement element = jsonObject.get("DATA");
            RewardItem rewardItem = null;
            rewardItem = element != null ? (RewardItem) jsonDeserializationContext.deserialize(element, (Type) klass) : (RewardItem) jsonDeserializationContext.deserialize(jsonElement, (Type) klass);
            return rewardItem;
        }

        public JsonElement serialize(RewardItem jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
            jsonSerializationContext.serialize(jsonElement).getAsJsonObject().entrySet().forEach(entry -> jsonObject.add((String) entry.getKey(), (JsonElement) entry.getValue()));
            return jsonObject;
        }

        private Class getObjectClass(String className) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e.getMessage());
            }
        }
    }

    private static class DateGsonAdapter
            extends TypeAdapter<Date> {
        private DateGsonAdapter() {
        }

        public void write(JsonWriter jsonWriter, Date date) throws IOException {
            if (date == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(date.getTime());
        }

        public Date read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return new Date(jsonReader.nextLong());
        }
    }

    private static class LocationGsonAdapter
            extends TypeAdapter<Location> {
        private static final Type seriType = new TypeToken<Map<String, Object>>() {
        }.getType();
        private static final String UUID = "uuid";
        private static final String X = "x";
        private static final String Y = "y";
        private static final String Z = "z";
        private static final String YAW = "yaw";
        private static final String PITCH = "pitch";

        private LocationGsonAdapter() {
        }

        public void write(JsonWriter jsonWriter, Location location) throws IOException {
            if (location == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.getRaw(location));
        }

        public Location read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.fromRaw(jsonReader.nextString());
        }

        private String getRaw(Location location) {
            HashMap<String, String> serial = new HashMap<String, String>();
            serial.put(UUID, location.getWorld().getUID().toString());
            serial.put(X, Double.toString(location.getX()));
            serial.put(Y, Double.toString(location.getY()));
            serial.put(Z, Double.toString(location.getZ()));
            serial.put(YAW, Float.toString(location.getYaw()));
            serial.put(PITCH, Float.toString(location.getPitch()));
            return g.toJson(serial);
        }

        private Location fromRaw(String raw) {
            Map keys = (Map) g.fromJson(raw, seriType);
            World w = Bukkit.getWorld(java.util.UUID.fromString((String) keys.get(UUID)));
            return new Location(w, Double.parseDouble((String) keys.get(X)), Double.parseDouble((String) keys.get(Y)), Double.parseDouble((String) keys.get(Z)), Float.parseFloat((String) keys.get(YAW)), Float.parseFloat((String) keys.get(PITCH)));
        }
    }

    private static class PotionEffectGsonAdapter
            extends TypeAdapter<PotionEffect> {
        private static final Type seriType = new TypeToken<Map<String, Object>>() {
        }.getType();
        private static final String TYPE = "effect";
        private static final String DURATION = "duration";
        private static final String AMPLIFIER = "amplifier";
        private static final String AMBIENT = "ambient";

        private PotionEffectGsonAdapter() {
        }

        public void write(JsonWriter jsonWriter, PotionEffect potionEffect) throws IOException {
            if (potionEffect == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.getRaw(potionEffect));
        }

        public PotionEffect read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.fromRaw(jsonReader.nextString());
        }

        private String getRaw(PotionEffect potion) {
            Map serial = potion.serialize();
            return g.toJson(serial);
        }

        private PotionEffect fromRaw(String raw) {
            Map keys = (Map) g.fromJson(raw, seriType);
            return new PotionEffect(PotionEffectType.getById(((Double) keys.get(TYPE)).intValue()), ((Double) keys.get(DURATION)).intValue(), ((Double) keys.get(AMPLIFIER)).intValue(), ((Boolean) keys.get(AMBIENT)).booleanValue());
        }
    }

    private static class ItemStackGsonAdapter
            extends TypeAdapter<ItemStack> {
        private static final Type seriType = new TypeToken<Map<String, Object>>() {
        }.getType();

        private ItemStackGsonAdapter() {
        }

        public void write(JsonWriter jsonWriter, ItemStack itemStack) throws IOException {
            if (itemStack == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.getRaw(itemStack));
        }

        public ItemStack read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.fromRaw(jsonReader.nextString());
        }

        private String getRaw(ItemStack item) {
            Map serial = item.serialize();
            if (serial.get("meta") != null) {
                ItemMeta itemMeta = item.getItemMeta();
                Map<String, Object> originalMeta = itemMeta.serialize();
                HashMap<String, Object> meta = new HashMap();
                for (Map.Entry entry : originalMeta.entrySet()) {
                    meta.put((String) entry.getKey(), entry.getValue());
                }
                for (Map.Entry entry : meta.entrySet()) {
                    Object o = entry.getValue();
                    if (!(o instanceof ConfigurationSerializable)) continue;
                    ConfigurationSerializable serializable = (ConfigurationSerializable) o;
                    Map serialized = GsonFactory.recursiveSerialization(serializable);
                    if (((String) entry.getKey()).equalsIgnoreCase("color")) {
                        Color color = ((LeatherArmorMeta) itemMeta).getColor();
                        HashMap colors = Maps.newHashMap();
                        colors.put("red", color.getRed());
                        colors.put("green", color.getGreen());
                        colors.put("blue", color.getBlue());
                        meta.put((String) entry.getKey(), colors);
                        continue;
                    }
                    meta.put((String) entry.getKey(), serialized);
                }
                serial.put("meta", meta);
            }
            return g.toJson(serial);
        }

        private ItemStack fromRaw(String raw) {
            ItemStack item;
            Map keys = (Map) g.fromJson(raw, seriType);
            if (keys.get("amount") != null) {
                Double d = (Double) keys.get("amount");
                Integer i = d.intValue();
                keys.put("amount", i);
            }
            try {
                item = ItemStack.deserialize(keys);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            if (item == null) {
                return null;
            }
            if (keys.containsKey("meta")) {
                Map<String, Object> itemmeta = (Map) keys.get("meta");
                Optional flagsOptional = Optional.ofNullable((itemmeta = GsonFactory.recursiveDoubleToInteger(itemmeta)).get("ItemFlags"));
                if (flagsOptional.isPresent()) {
                    itemmeta.remove("ItemFlags");
                }
                for (Map.Entry entry : itemmeta.entrySet()) {
                    String key = (String) entry.getKey();
                    Object val = entry.getValue();
                    if (!key.equalsIgnoreCase("color")) continue;
                    Map colorMap = (Map) val;
                    int r = (Integer) colorMap.get("red");
                    int g = (Integer) colorMap.get("green");
                    int b = (Integer) colorMap.get("blue");
                    itemmeta.put("color", Color.fromRGB(r, g, b));
                }
                ItemMeta meta = (ItemMeta) ConfigurationSerialization.deserializeObject(itemmeta, ConfigurationSerialization.getClassByAlias("ItemMeta"));
                item.setItemMeta(meta);
                if (flagsOptional.isPresent()) {
                    List<String> flags = (List) flagsOptional.get();
                    for (String flag : flags) {
                        if (!flag.equalsIgnoreCase("HIDE_ENCHANTS")) continue;
                        item = GlowUtil.addGlow(item);
                    }
                }
            }
            return item;
        }
    }

    private static class NewItemStackAdapter
            extends TypeAdapter<ItemStack> {
        private NewItemStackAdapter() {
        }

        public void write(JsonWriter jsonWriter, ItemStack itemStack) throws IOException {
            if (itemStack == null) {
                jsonWriter.nullValue();
                return;
            }
            net.minecraft.server.v1_8_R3.ItemStack item = GsonFactory.removeSlot(itemStack);
            if (item == null) {
                jsonWriter.nullValue();
                return;
            }
            try {
                jsonWriter.beginObject();
                jsonWriter.name("type");
                jsonWriter.value(itemStack.getType().toString());
                jsonWriter.name("amount");
                jsonWriter.value((long) itemStack.getAmount());
                jsonWriter.name("data");
                jsonWriter.value((long) itemStack.getDurability());
                jsonWriter.name("tag");
                if (item != null && item.getTag() != null) {
                    jsonWriter.value(GsonFactory.nbtToString(item.getTag()));
                } else {
                    jsonWriter.value("");
                }
                jsonWriter.endObject();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public ItemStack read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                return null;
            }
            jsonReader.beginObject();
            jsonReader.nextName();
            Material type = Material.getMaterial((String) jsonReader.nextString());
            jsonReader.nextName();
            int amount = jsonReader.nextInt();
            jsonReader.nextName();
            int data = jsonReader.nextInt();
            net.minecraft.server.v1_8_R3.ItemStack item = new net.minecraft.server.v1_8_R3.ItemStack(CraftMagicNumbers.getItem(type), amount, data);
            jsonReader.nextName();
            String next = jsonReader.nextString();
            if (next.startsWith("{")) {
                NBTTagCompound compound = null;
                try {
                    compound = MojangsonParser.parse(ChatColor.translateAlternateColorCodes('&', next));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.setTag(compound);
            }
            jsonReader.endObject();
            return CraftItemStack.asBukkitCopy(item);
        }
    }

    private static class ExposeExlusion
            implements ExclusionStrategy {
        private ExposeExlusion() {
        }

        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            Ignore ignore = (Ignore) fieldAttributes.getAnnotation(Ignore.class);
            if (ignore != null) {
                return true;
            }
            Expose expose = (Expose) fieldAttributes.getAnnotation(Expose.class);
            return expose != null && (!expose.serialize() || !expose.deserialize());
        }

        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }

    @Retention(value = RetentionPolicy.RUNTIME)
    @Target(value = {ElementType.FIELD})
    public @interface Ignore {
    }
}


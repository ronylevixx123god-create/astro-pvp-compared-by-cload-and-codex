package me.ifamasssxd.cosmicskycustoms.struct;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.ItemPersistentData;
import org.bukkit.inventory.ItemStack;

public abstract class CosmicPersistentItem
extends CosmicItem {
    private static Map<String, ItemPersistentData> registeredItems = new HashMap<String, ItemPersistentData>();
    protected NBTWrapper wrapper;
    protected ItemStack item;

    public CosmicPersistentItem(String internalName) {
        super(internalName);
    }

    public CosmicPersistentItem(ItemStack item, String internalName) {
        this(item, new NBTWrapper(item), internalName);
    }

    public CosmicPersistentItem(ItemStack item, NBTWrapper wrapper, String internalName) {
        this(internalName);
        this.wrapper = wrapper;
        this.item = item;
    }

    public abstract void loadNBT();

    public abstract ItemStack build();

    public static <T extends CosmicPersistentItem> T constructItem(ItemStack item, Class<T> expected) {
        if (item == null) {
            return null;
        }
        return CosmicPersistentItem.constructItem(item, new NBTWrapper(item), expected);
    }

    public static <T extends CosmicPersistentItem> T constructItem(ItemStack original, NBTWrapper wrapper, Class<T> expected) {
        ItemPersistentData data;
        if (wrapper == null || original == null) {
            return null;
        }
        String persistentName = wrapper.getString("cosmicType");
        if (persistentName != null && !persistentName.isEmpty() && (data = registeredItems.get(persistentName)) != null) {
            try {
                if (data.getPersistentItem().isAssignableFrom(expected)) {
                    return (T)((CosmicPersistentItem)expected.cast(data.createInstance(original, wrapper)));
                }
            }
            catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                CosmicSkyCustomItems.get().log("Unable to create PersistentItem: " + persistentName);
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ItemPersistentData registerPersistentItem(String name, Class<? extends CosmicPersistentItem> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ItemPersistentData data = new ItemPersistentData(name, clazz);
        registeredItems.put(name, data);
        return data;
    }

    public static CosmicPersistentItem getSampleVersion(String name) {
        ItemPersistentData data = registeredItems.get(name);
        return data != null ? data.getSampleVersion() : null;
    }

    public static boolean unregisterPersistentItem(String name) {
        return registeredItems.remove(name) != null;
    }
}


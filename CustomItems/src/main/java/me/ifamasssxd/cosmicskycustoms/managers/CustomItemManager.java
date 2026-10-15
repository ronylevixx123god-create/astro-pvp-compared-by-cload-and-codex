package me.ifamasssxd.cosmicskycustoms.managers;

import com.cosmicpvp.cosmicutils.utils.CC;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItems;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicPersistentItem;
import me.ifamasssxd.cosmicskycustoms.struct.ItemPersistentData;

public class CustomItemManager {
    private Map<String, CosmicItem> customRegisteredItems = new HashMap<String, CosmicItem>();
    private CosmicSkyCustomItems plugin;

    public CustomItemManager(CosmicSkyCustomItems plugin) {
        this.plugin = plugin;
    }

    public void onEnable(CosmicSkyCustomItems plugin) {
        for (CosmicItems item : CosmicItems.values()) {
            this.registerCustomItem(item.getItem());
        }
    }

    public CosmicItem getCustomItem(String internalName) {
        return this.customRegisteredItems.get(internalName);
    }

    public void registerCustomItem(CosmicItem instance) {
        this.customRegisteredItems.put(instance.getInternalName(), instance);
        this.plugin.log(CC.GreenB + "Registering CustomItem: " + instance + " Class: " + instance.getClass().getName());
    }

    public <T extends CosmicPersistentItem> T registerPersistentItem(String name, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        this.plugin.log(CC.GreenB + "Registering PersistenCustomItem name: " + name + " Class: " + clazz.getSimpleName());
        ItemPersistentData data = CosmicPersistentItem.registerPersistentItem(name, clazz);
        this.customRegisteredItems.put(data.getInternalName(), data.getSampleVersion());
        return (T)data.getSampleVersion();
    }

    public void onDisable(CosmicSkyCustomItems plugin) {
    }

    public Map<String, CosmicItem> getCustomRegisteredItems() {
        return this.customRegisteredItems;
    }
}


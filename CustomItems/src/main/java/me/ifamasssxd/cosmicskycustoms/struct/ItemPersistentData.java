package me.ifamasssxd.cosmicskycustoms.struct;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicPersistentItem;
import org.bukkit.inventory.ItemStack;

public class ItemPersistentData {
    private Constructor<CosmicPersistentItem> constructor;
    private Constructor<CosmicPersistentItem> simpleConstructor;
    private Class<? extends CosmicPersistentItem> persistentItem;
    private CosmicPersistentItem sampleVersion;
    private String internalName;

    ItemPersistentData(String internal, Class<? extends CosmicPersistentItem> clazz) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        this(clazz.getConstructor(ItemStack.class, NBTWrapper.class, String.class), clazz.getConstructor(String.class), clazz, null, internal);
        this.sampleVersion = this.createSampleInstance();
    }

    public CosmicPersistentItem createInstance(ItemStack itemStack, NBTWrapper wrapper) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return this.constructor.newInstance(new Object[]{itemStack, wrapper, this.internalName});
    }

    public CosmicPersistentItem createSampleInstance() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return this.simpleConstructor.newInstance(this.internalName);
    }

    public ItemPersistentData(Constructor<? extends CosmicPersistentItem> constructor, Constructor<? extends CosmicPersistentItem> simpleConstructor, Class<? extends CosmicPersistentItem> persistentItem, CosmicPersistentItem sampleVersion, String internalName) {
        this.constructor = (Constructor<CosmicPersistentItem>) constructor;
        this.simpleConstructor = (Constructor<CosmicPersistentItem>) simpleConstructor;
        this.persistentItem = persistentItem;
        this.sampleVersion = sampleVersion;
        this.internalName = internalName;
    }

    public Constructor<CosmicPersistentItem> getConstructor() {
        return this.constructor;
    }

    public Constructor<CosmicPersistentItem> getSimpleConstructor() {
        return this.simpleConstructor;
    }

    public Class<? extends CosmicPersistentItem> getPersistentItem() {
        return this.persistentItem;
    }

    public CosmicPersistentItem getSampleVersion() {
        return this.sampleVersion;
    }

    public String getInternalName() {
        return this.internalName;
    }
}


/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  com.google.common.collect.BiMap
 *  com.google.common.collect.HashBiMap
 *  com.google.common.collect.Lists
 *  com.google.common.collect.MapMaker
 *  com.google.common.io.Closeables
 *  com.google.common.io.InputSupplier
 *  com.google.common.io.OutputSupplier
 *  com.google.common.primitives.Primitives
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.inventory.ItemStack
 */
package me.recolance.globalutils.attribute;

import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.io.Closeables;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import com.google.common.primitives.Primitives;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NbtFactory {
    private static final BiMap<Integer, Class<?>> NBT_CLASS = HashBiMap.create();
    private static final BiMap<Integer, NbtType> NBT_ENUM = HashBiMap.create();
    private static NbtFactory INSTANCE;
    private final Field[] DATA_FIELD = new Field[12];
    private Class<?> BASE_CLASS;
    private Class<?> COMPOUND_CLASS;
    private Class<?> STREAM_TOOLS;
    private Class<?> READ_LIMITER_CLASS;
    private Method NBT_CREATE_TAG;
    private Method NBT_GET_TYPE;
    private Field NBT_LIST_TYPE;
    private Class<?> CRAFT_STACK;
    private Field CRAFT_HANDLE;
    private Field STACK_TAG;
    private LoadCompoundMethod LOAD_COMPOUND;
    private Method SAVE_COMPOUND;

    private NbtFactory() {
        if (this.BASE_CLASS == null) {
            try {
                ClassLoader loader = NbtFactory.class.getClassLoader();
                String packageName = this.getPackageName();
                Class<?> offlinePlayer = loader.loadClass(packageName + ".CraftOfflinePlayer");
                this.COMPOUND_CLASS = NbtFactory.getMethod(0, 8, offlinePlayer, "getData", new Class[0]).getReturnType();
                this.BASE_CLASS = this.COMPOUND_CLASS.getSuperclass();
                this.NBT_GET_TYPE = NbtFactory.getMethod(0, 8, this.BASE_CLASS, "getTypeId", new Class[0]);
                this.NBT_CREATE_TAG = NbtFactory.getMethod(8, 0, this.BASE_CLASS, "createTag", Byte.TYPE);
                this.CRAFT_STACK = loader.loadClass(packageName + ".inventory.CraftItemStack");
                this.CRAFT_HANDLE = NbtFactory.getField(null, this.CRAFT_STACK, "handle");
                this.STACK_TAG = NbtFactory.getField(null, this.CRAFT_HANDLE.getType(), "tag");
                String nmsPackage = this.BASE_CLASS.getPackage().getName();
                this.initializeNMS(loader, nmsPackage);
                this.LOAD_COMPOUND = this.READ_LIMITER_CLASS != null ? new LoadMethodSkinUpdate(this.STREAM_TOOLS, this.READ_LIMITER_CLASS) : new LoadMethodWorldUpdate(this.STREAM_TOOLS);
                this.SAVE_COMPOUND = NbtFactory.getMethod(8, 0, this.STREAM_TOOLS, null, this.BASE_CLASS, DataOutput.class);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Unable to find offline player.", e);
            }
        }
    }

    private static NbtFactory get() {
        if (INSTANCE == null) {
            INSTANCE = new NbtFactory();
        }
        return INSTANCE;
    }

    public static NbtList createList(Object... content) {
        return NbtFactory.createList(Arrays.asList(content));
    }

    public static NbtList createList(Iterable<? extends Object> iterable) {
        NbtFactory nbtFactory = NbtFactory.get();
        nbtFactory.getClass();
        NbtList list = nbtFactory.new NbtList(INSTANCE.createNbtTag(NbtType.TAG_LIST, null));
        for (Object object : iterable) {
            list.add(object);
        }
        return list;
    }

    public static NbtCompound createCompound() {
        NbtFactory nbtFactory = NbtFactory.get();
        nbtFactory.getClass();
        return nbtFactory.new NbtCompound(INSTANCE.createNbtTag(NbtType.TAG_COMPOUND, null));
    }

    public static NbtList fromList(Object nmsList) {
        NbtFactory nbtFactory = NbtFactory.get();
        nbtFactory.getClass();
        return nbtFactory.new NbtList(nmsList);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static NbtCompound fromStream(InputSupplier<? extends InputStream> stream, StreamOptions option) throws IOException {
        NbtCompound nbtCompound;
        block7:
        {
            boolean suppress;
            InputStream input;
            block6:
            {
                input = null;
                DataInputStream data = null;
                suppress = true;
                try {
                    input = (InputStream) stream.getInput();
                    data = new DataInputStream(new BufferedInputStream(option == StreamOptions.GZIP_COMPRESSION ? new GZIPInputStream(input) : input));
                    NbtCompound result = NbtFactory.fromCompound(NbtFactory.get().LOAD_COMPOUND.loadNbt(data));
                    suppress = false;
                    nbtCompound = result;
                    if (data == null) break block6;
                } catch (Throwable throwable) {
                    if (data != null) {
                        Closeables.close(data, (boolean) suppress);
                    } else if (input != null) {
                        Closeables.close((Closeable) input, (boolean) suppress);
                    }
                    throw throwable;
                }
                Closeables.close((Closeable) data, (boolean) suppress);
                break block7;
            }
            if (input != null) {
                Closeables.close((Closeable) input, (boolean) suppress);
            }
        }
        return nbtCompound;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void saveStream(NbtCompound source, OutputSupplier<? extends OutputStream> stream, StreamOptions option) throws IOException {
        block7:
        {
            boolean suppress;
            OutputStream output;
            block6:
            {
                output = null;
                DataOutputStream data = null;
                suppress = true;
                try {
                    output = (OutputStream) stream.getOutput();
                    data = new DataOutputStream(option == StreamOptions.GZIP_COMPRESSION ? new GZIPOutputStream(output) : output);
                    NbtFactory.invokeMethod(NbtFactory.get().SAVE_COMPOUND, null, source.getHandle(), data);
                    suppress = false;
                    if (data == null) break block6;
                } catch (Throwable throwable) {
                    if (data != null) {
                        Closeables.close(data, (boolean) suppress);
                    } else if (output != null) {
                        Closeables.close((Closeable) output, (boolean) suppress);
                    }
                    throw throwable;
                }
                Closeables.close((Closeable) data, (boolean) suppress);
                break block7;
            }
            if (output != null) {
                Closeables.close((Closeable) output, (boolean) suppress);
            }
        }
    }

    public static NbtCompound fromCompound(Object nmsCompound) {
        NbtFactory nbtFactory = NbtFactory.get();
        nbtFactory.getClass();
        return nbtFactory.new NbtCompound(nmsCompound);
    }

    public static void setItemTag(ItemStack stack, NbtCompound compound) {
        NbtFactory.checkItemStack(stack);
        Object nms = NbtFactory.getFieldValue(NbtFactory.get().CRAFT_HANDLE, (Object) stack);
        NbtFactory.setFieldValue(NbtFactory.get().STACK_TAG, nms, compound.getHandle());
    }

    public static NbtCompound fromItemTag(ItemStack stack) {
        NbtFactory.checkItemStack(stack);
        Object nms = NbtFactory.getFieldValue(NbtFactory.get().CRAFT_HANDLE, (Object) stack);
        Object tag = NbtFactory.getFieldValue(NbtFactory.get().STACK_TAG, nms);
        if (tag == null) {
            NbtCompound compound = NbtFactory.createCompound();
            NbtFactory.setItemTag(stack, compound);
            return compound;
        }
        return NbtFactory.fromCompound(tag);
    }

    public static ItemStack getCraftItemStack(ItemStack stack) {
        if (stack == null || NbtFactory.get().CRAFT_STACK.isAssignableFrom(stack.getClass())) {
            return stack;
        }
        try {
            Constructor<?> caller = NbtFactory.INSTANCE.CRAFT_STACK.getDeclaredConstructor(ItemStack.class);
            caller.setAccessible(true);
            return (ItemStack) caller.newInstance(new Object[]{stack});
        } catch (Exception e) {
            throw new IllegalStateException("Unable to convert " + (Object) stack + " + to a CraftItemStack.");
        }
    }

    private static void checkItemStack(ItemStack stack) {
        if (stack == null) {
            throw new IllegalArgumentException("Stack cannot be NULL.");
        }
        if (!NbtFactory.get().CRAFT_STACK.isAssignableFrom(stack.getClass())) {
            throw new IllegalArgumentException("Stack must be a CraftItemStack.");
        }
        if (stack.getType() == Material.AIR) {
            throw new IllegalArgumentException("ItemStacks representing air cannot store NMS information.");
        }
    }

    private static Object invokeMethod(Method method, Object target, Object... params) {
        try {
            return method.invoke(target, params);
        } catch (Exception e) {
            throw new RuntimeException("Unable to invoke method " + method + " for " + target, e);
        }
    }

    private static void setFieldValue(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Unable to set " + field + " for " + target, e);
        }
    }

    private static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve " + field + " for " + target, e);
        }
    }

    private static Method getMethod(int requireMod, int bannedMod, Class<?> clazz, String methodName, Class<?>... params) {
        for (Method method : clazz.getDeclaredMethods()) {
            if ((method.getModifiers() & requireMod) != requireMod || (method.getModifiers() & bannedMod) != 0 || methodName != null && !method.getName().equals(methodName) || !Arrays.equals(method.getParameterTypes(), params))
                continue;
            method.setAccessible(true);
            return method;
        }
        if (clazz.getSuperclass() != null) {
            return NbtFactory.getMethod(requireMod, bannedMod, clazz.getSuperclass(), methodName, params);
        }
        throw new IllegalStateException(String.format("Unable to find method %s (%s).", methodName, Arrays.asList(params)));
    }

    private static Field getField(Object instance, Class<?> clazz, String fieldName) {
        if (clazz == null) {
            clazz = instance.getClass();
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.getName().equals(fieldName)) continue;
            field.setAccessible(true);
            return field;
        }
        if (clazz.getSuperclass() != null) {
            return NbtFactory.getField(instance, clazz.getSuperclass(), fieldName);
        }
        throw new IllegalStateException("Unable to find field " + fieldName + " in " + instance);
    }

    private void initializeNMS(ClassLoader loader, String nmsPackage) {
        try {
            this.STREAM_TOOLS = loader.loadClass(nmsPackage + ".NBTCompressedStreamTools");
            this.READ_LIMITER_CLASS = loader.loadClass(nmsPackage + ".NBTReadLimiter");
        } catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
    }

    private String getPackageName() {
        String name;
        Server server = Bukkit.getServer();
        String string = name = server != null ? server.getClass().getPackage().getName() : null;
        if (name != null && name.contains("craftbukkit")) {
            return name;
        }
        return "org.bukkit.craftbukkit.v1_7_R3";
    }

    private Map<String, Object> getDataMap(Object handle) {
        return (Map) NbtFactory.getFieldValue(this.getDataField(NbtType.TAG_COMPOUND, handle), handle);
    }

    private List<Object> getDataList(Object handle) {
        return (List) NbtFactory.getFieldValue(this.getDataField(NbtType.TAG_LIST, handle), handle);
    }

    private Object unwrapValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Wrapper) {
            return ((Wrapper) value).getHandle();
        }
        if (value instanceof List) {
            throw new IllegalArgumentException("Can only insert a WrappedList.");
        }
        if (value instanceof Map) {
            throw new IllegalArgumentException("Can only insert a WrappedCompound.");
        }
        return this.createNbtTag(this.getPrimitiveType(value), value);
    }

    private Object wrapNative(Object nms) {
        if (nms == null) {
            return null;
        }
        if (this.BASE_CLASS.isAssignableFrom(nms.getClass())) {
            NbtType type = this.getNbtType(nms);
            switch (type) {
                case TAG_COMPOUND: {
                    return new NbtCompound(nms);
                }
                case TAG_LIST: {
                    return new NbtList(nms);
                }
            }
            return NbtFactory.getFieldValue(this.getDataField(type, nms), nms);
        }
        throw new IllegalArgumentException("Unexpected type: " + nms);
    }

    private Object createNbtTag(NbtType type, Object value) {
        Object tag = NbtFactory.invokeMethod(this.NBT_CREATE_TAG, null, (byte) type.id);
        if (value != null) {
            NbtFactory.setFieldValue(this.getDataField(type, tag), tag, value);
        }
        return tag;
    }

    private Field getDataField(NbtType type, Object nms) {
        if (this.DATA_FIELD[type.id] == null) {
            this.DATA_FIELD[type.id] = NbtFactory.getField(nms, null, type.getFieldName());
        }
        return this.DATA_FIELD[type.id];
    }

    private NbtType getNbtType(Object nms) {
        byte type = (Byte) NbtFactory.invokeMethod(this.NBT_GET_TYPE, nms, new Object[0]);
        return (NbtType) ((Object) NBT_ENUM.get((Object) type));
    }

    private NbtType getPrimitiveType(Object primitive) {
        NbtType type = (NbtType) ((Object) NBT_ENUM.get(NBT_CLASS.inverse().get((Object) Primitives.unwrap(primitive.getClass()))));
        if (type == null) {
            throw new IllegalArgumentException(String.format("Illegal type: %s (%s)", primitive.getClass(), primitive));
        }
        return type;
    }

    private static enum NbtType {
        TAG_END(0, Void.class),
        TAG_BYTE(1, Byte.TYPE),
        TAG_SHORT(2, Short.TYPE),
        TAG_INT(3, Integer.TYPE),
        TAG_LONG(4, Long.TYPE),
        TAG_FLOAT(5, Float.TYPE),
        TAG_DOUBLE(6, Double.TYPE),
        TAG_BYTE_ARRAY(7, byte[].class),
        TAG_INT_ARRAY(11, int[].class),
        TAG_STRING(8, String.class),
        TAG_LIST(9, List.class),
        TAG_COMPOUND(10, Map.class);

        public final int id;

        private NbtType(int id, Class<?> type) {
            this.id = id;
            NBT_CLASS.put(id, type);
            NBT_ENUM.put(id, this);
        }

        private String getFieldName() {
            if (this == TAG_COMPOUND) {
                return "map";
            }
            if (this == TAG_LIST) {
                return "list";
            }
            return "data";
        }
    }

    public static enum StreamOptions {
        NO_COMPRESSION,
        GZIP_COMPRESSION;

    }

    public static interface Wrapper {
        public Object getHandle();
    }

    private static class LoadMethodSkinUpdate
            extends LoadCompoundMethod {
        private Object readLimiter;

        public LoadMethodSkinUpdate(Class<?> streamClass, Class<?> readLimiterClass) {
            this.setMethod(NbtFactory.getMethod(8, 0, streamClass, null, new Class[]{DataInput.class, readLimiterClass}));
            for (Field field : readLimiterClass.getDeclaredFields()) {
                if (!readLimiterClass.isAssignableFrom(field.getType())) continue;
                try {
                    this.readLimiter = field.get(null);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot retrieve read limiter.", e);
                }
            }
        }

        @Override
        public Object loadNbt(DataInput input) {
            return NbtFactory.invokeMethod(this.staticMethod, null, new Object[]{input, this.readLimiter});
        }
    }

    private static class LoadMethodWorldUpdate
            extends LoadCompoundMethod {
        public LoadMethodWorldUpdate(Class<?> streamClass) {
            this.setMethod(NbtFactory.getMethod(8, 0, streamClass, null, new Class[]{DataInput.class}));
        }

        @Override
        public Object loadNbt(DataInput input) {
            return NbtFactory.invokeMethod(this.staticMethod, null, new Object[]{input});
        }
    }

    private static abstract class LoadCompoundMethod {
        protected Method staticMethod;

        private LoadCompoundMethod() {
        }

        protected void setMethod(Method method) {
            this.staticMethod = method;
            this.staticMethod.setAccessible(true);
        }

        public abstract Object loadNbt(DataInput var1);
    }

    private class ConvertedList
            extends AbstractList<Object>
            implements Wrapper {
        private final Object handle;
        private final List<Object> original;
        private final CachedNativeWrapper cache;

        public ConvertedList(Object handle, List<Object> original) {
            this.cache = new CachedNativeWrapper();
            if (NbtFactory.this.NBT_LIST_TYPE == null) {
                NbtFactory.this.NBT_LIST_TYPE = NbtFactory.getField(handle, null, "type");
            }
            this.handle = handle;
            this.original = original;
        }

        protected Object wrapOutgoing(Object value) {
            return this.cache.wrap(value);
        }

        protected Object unwrapIncoming(Object wrapped) {
            return NbtFactory.this.unwrapValue(wrapped);
        }

        @Override
        public Object get(int index) {
            return this.wrapOutgoing(this.original.get(index));
        }

        @Override
        public int size() {
            return this.original.size();
        }

        @Override
        public Object set(int index, Object element) {
            return this.wrapOutgoing(this.original.set(index, this.unwrapIncoming(element)));
        }

        @Override
        public void add(int index, Object element) {
            Object nbt = this.unwrapIncoming(element);
            if (this.size() == 0) {
                NbtFactory.setFieldValue(NbtFactory.this.NBT_LIST_TYPE, this.handle, (byte) ((NbtFactory) NbtFactory.this).getNbtType((Object) nbt).id);
            }
            this.original.add(index, nbt);
        }

        @Override
        public Object remove(int index) {
            return this.wrapOutgoing(this.original.remove(index));
        }

        @Override
        public boolean remove(Object o) {
            return this.original.remove(this.unwrapIncoming(o));
        }

        @Override
        public Object getHandle() {
            return this.handle;
        }
    }

    private class ConvertedMap
            extends AbstractMap<String, Object>
            implements Wrapper {
        private final Object handle;
        private final Map<String, Object> original;
        private final CachedNativeWrapper cache;

        public ConvertedMap(Object handle, Map<String, Object> original) {
            this.cache = new CachedNativeWrapper();
            this.handle = handle;
            this.original = original;
        }

        protected Object wrapOutgoing(Object value) {
            return this.cache.wrap(value);
        }

        protected Object unwrapIncoming(Object wrapped) {
            return NbtFactory.this.unwrapValue(wrapped);
        }

        @Override
        public Object put(String key, Object value) {
            return this.wrapOutgoing(this.original.put(key, this.unwrapIncoming(value)));
        }

        @Override
        public Object get(Object key) {
            return this.wrapOutgoing(this.original.get(key));
        }

        @Override
        public Object remove(Object key) {
            return this.wrapOutgoing(this.original.remove(key));
        }

        @Override
        public boolean containsKey(Object key) {
            return this.original.containsKey(key);
        }

        @Override
        public Set<Map.Entry<String, Object>> entrySet() {
            return new AbstractSet<Map.Entry<String, Object>>() {

                @Override
                public boolean add(Map.Entry<String, Object> e) {
                    String key = e.getKey();
                    Object value = e.getValue();
                    ConvertedMap.this.original.put(key, ConvertedMap.this.unwrapIncoming(value));
                    return true;
                }

                @Override
                public int size() {
                    return ConvertedMap.this.original.size();
                }

                @Override
                public Iterator<Map.Entry<String, Object>> iterator() {
                    return ConvertedMap.this.iterator();
                }
            };
        }

        private Iterator<Map.Entry<String, Object>> iterator() {
            final Iterator<Map.Entry<String, Object>> proxy = this.original.entrySet().iterator();
            return new Iterator<Map.Entry<String, Object>>() {

                @Override
                public boolean hasNext() {
                    return proxy.hasNext();
                }

                @Override
                public Map.Entry<String, Object> next() {
                    Map.Entry entry = (Map.Entry) proxy.next();
                    return new AbstractMap.SimpleEntry<String, Object>((String) entry.getKey(), ConvertedMap.this.wrapOutgoing(entry.getValue()));
                }

                @Override
                public void remove() {
                    proxy.remove();
                }
            };
        }

        @Override
        public Object getHandle() {
            return this.handle;
        }
    }

    private final class CachedNativeWrapper {
        private final ConcurrentMap<Object, Object> cache = new MapMaker().weakKeys().makeMap();

        private CachedNativeWrapper() {
        }

        public Object wrap(Object value) {
            Object current = this.cache.get(value);
            if (current == null && ((current = NbtFactory.this.wrapNative(value)) instanceof ConvertedMap || current instanceof ConvertedList)) {
                this.cache.put(value, current);
            }
            return current;
        }
    }

    public final class NbtList
            extends ConvertedList {
        private NbtList(Object handle) {
            super(handle, NbtFactory.this.getDataList(handle));
        }
    }

    public final class NbtCompound
            extends ConvertedMap {
        private NbtCompound(Object handle) {
            super(handle, NbtFactory.this.getDataMap(handle));
        }

        public Byte getByte(String key, Byte defaultValue) {
            return this.containsKey(key) ? (Byte) this.get(key) : defaultValue;
        }

        public Short getShort(String key, Short defaultValue) {
            return this.containsKey(key) ? (Short) this.get(key) : defaultValue;
        }

        public Integer getInteger(String key, Integer defaultValue) {
            return this.containsKey(key) ? (Integer) this.get(key) : defaultValue;
        }

        public Long getLong(String key, Long defaultValue) {
            return this.containsKey(key) ? (Long) this.get(key) : defaultValue;
        }

        public Float getFloat(String key, Float defaultValue) {
            return this.containsKey(key) ? (Float) this.get(key) : defaultValue;
        }

        public Double getDouble(String key, Double defaultValue) {
            return this.containsKey(key) ? (Double) this.get(key) : defaultValue;
        }

        public String getString(String key, String defaultValue) {
            return this.containsKey(key) ? (String) this.get(key) : defaultValue;
        }

        public byte[] getByteArray(String key, byte[] defaultValue) {
            return this.containsKey(key) ? (byte[]) this.get(key) : defaultValue;
        }

        public int[] getIntegerArray(String key, int[] defaultValue) {
            return this.containsKey(key) ? (int[]) this.get(key) : defaultValue;
        }

        public NbtList getList(String key, boolean createNew) {
            NbtList list = (NbtList) this.get(key);
            if (list == null && createNew) {
                list = NbtFactory.createList(new Object[0]);
                this.put(key, (Object) list);
            }
            return list;
        }

        public NbtCompound getMap(String key, boolean createNew) {
            return this.getMap(Arrays.asList(key), createNew);
        }

        public NbtCompound putPath(String path, Object value) {
            List<String> entries = this.getPathElements(path);
            NbtCompound map = this.getMap(entries.subList(0, entries.size() - 1), true);
            map.put(entries.get(entries.size() - 1), value);
            return this;
        }

        public <T> T getPath(String path) {
            List<String> entries = this.getPathElements(path);
            NbtCompound map = this.getMap(entries.subList(0, entries.size() - 1), false);
            if (map != null) {
                return (T) map.get(entries.get(entries.size() - 1));
            }
            return null;
        }

        public void saveTo(OutputSupplier<? extends OutputStream> stream, StreamOptions option) throws IOException {
            NbtFactory.saveStream(this, stream, option);
        }

        private NbtCompound getMap(Iterable<String> path, boolean createNew) {
            NbtCompound current = this;
            for (String entry : path) {
                NbtCompound child = (NbtCompound) current.get(entry);
                if (child == null) {
                    if (!createNew) {
                        return null;
                    }
                    child = NbtFactory.createCompound();
                    current.put(entry, (Object) child);
                }
                current = child;
            }
            return current;
        }

        private List<String> getPathElements(String path) {
            return Lists.newArrayList((Iterable) Splitter.on((String) ".").omitEmptyStrings().split((CharSequence) path));
        }
    }
}


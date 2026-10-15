/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  lombok.NonNull
 */
package com.cosmicpvp.cosmicutils.utils;

import com.cosmicpvp.cosmicutils.compat.ParamBuilder;
import com.cosmicpvp.cosmicutils.compat.RParam;
import lombok.NonNull;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
    private static Map<String, Field> cachedFields = new HashMap<String, Field>();
    private static Map<String, Method> cachedMethods = new HashMap<String, Method>();
    private static String nmsVersion;
    private static Class<?> entityLivingClass;
    private static Class<?> genericAttributesClass;
    private static Class<?> iattributeInstanceClass;
    private static Class<?> attributeInstanceClass;

    static {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        nmsVersion = name.substring(name.lastIndexOf(46) + 1) + ".";
        Bukkit.getLogger().info("[CosmicUtils] Registered NMS version: " + nmsVersion);
        entityLivingClass = ReflectionUtils.getNMSClass("EntityLiving");
        attributeInstanceClass = ReflectionUtils.getNMSClass("AttributeInstance");
        genericAttributesClass = ReflectionUtils.getNMSClass("GenericAttributes");
        iattributeInstanceClass = ReflectionUtils.getNMSClass("IAttribute");
    }

    public static Field getField(Class<?> clazz, String field) throws NoSuchFieldException {
        Field cached = cachedFields.get(clazz.getName() + ":" + field);
        if (cached != null) {
            return cached;
        }
        Field f = clazz.getDeclaredField(field);
        f.setAccessible(true);
        cachedFields.put(clazz.getName() + ":" + field, f);
        return f;
    }

    public static Method getMethod(Class<?> clazz, String field) throws NoSuchMethodException {
        Method cached = cachedMethods.get(clazz.getName() + ":" + field);
        if (cached != null) {
            return cached;
        }
        Method f = clazz.getDeclaredMethod(field, new Class[0]);
        f.setAccessible(true);
        cachedMethods.put(clazz.getName() + ":" + field, f);
        return f;
    }

    public static Object invokeMethod(Method method, Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(Method method, Object instance) {
        try {
            return method.invoke(instance, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(Class<?> clazz, String field, Class<?>... paramaters) throws NoSuchMethodException {
        Method cached = cachedMethods.get(clazz.getName() + ":" + field);
        if (cached != null) {
            return cached;
        }
        Method f = clazz.getDeclaredMethod(field, paramaters);
        f.setAccessible(true);
        cachedMethods.put(clazz.getName() + ":" + field, f);
        return f;
    }

    public static void setObject(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = ReflectionUtils.getField(object.getClass(), fieldName);
        ReflectionUtils.setObject(object, field, value);
    }

    public static void setObject(Object object, Field field, Object value) throws IllegalAccessException {
        if (field != null) {
            field.set(object, value);
        }
    }

    public static Object getObject(Class<?> clazz, Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = ReflectionUtils.getField(clazz, fieldName);
        return f.get(instance);
    }

    public static Object callReflectionMethod(Object instance, Class<?> clazz, String method) {
        return ReflectionUtils.callReflectionMethod(instance, clazz, method, null);
    }

    public static Object callReflectionMethod(@NonNull Object instance, String method) {
        if (instance == null) {
            throw new NullPointerException("instance");
        }
        return ReflectionUtils.callReflectionMethod(instance, instance.getClass(), method, null);
    }

    public static Object callReflectionMethod(@NonNull Object instance, String method, ParamBuilder builder) {
        if (instance == null) {
            throw new NullPointerException("instance");
        }
        return ReflectionUtils.callReflectionMethod(instance, instance.getClass(), method, builder);
    }

    public static Object callReflectionMethod(Object instance, Class<?> clazz, String method, ParamBuilder builder) {
        try {
            if (builder != null) {
                Class[] params = new Class[builder.getParameters().size()];
                Object[] objcs = new Object[builder.getValidObjectCount()];
                int index = 0;
                for (RParam param : builder.getParameters()) {
                    int i = index++;
                    params[i] = param.getMethodClass();
                    objcs[i] = param.getInstance();
                }
                Method m = ReflectionUtils.getMethod(clazz, method, params);
                return m.invoke(instance, objcs);
            }
            Method m = ReflectionUtils.getMethod(clazz, method);
            return m.invoke(instance, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNMSVersion() {
        return nmsVersion;
    }

    public static Class<?> getNMSClass(String className) {
        String fullName = "net.minecraft.server." + ReflectionUtils.getNMSVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> getOBCClass(String className) {
        String fullName = "org.bukkit.craftbukkit." + ReflectionUtils.getNMSVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Object getNMSHandle(Object obj) {
        try {
            return ReflectionUtils.getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getEntityLivingClass() {
        return entityLivingClass;
    }

    public static Class<?> getGenericAttributesClass() {
        return genericAttributesClass;
    }

    public static Class<?> getIattributeInstanceClass() {
        return iattributeInstanceClass;
    }

    public static Class<?> getAttributeInstanceClass() {
        return attributeInstanceClass;
    }
}


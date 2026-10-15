package me.ifamasssxd.cosmicbosses.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static void setNull(final Object object, final String fieldName) {
        try {
            final Field f = object.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            System.out.println("Value Before: " + f.get(object));
            f.set(object, null);
            System.out.println("Value: " + f.get(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setObject(final Class<?> clazz, final Object instance, final Object objectToSet, final String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(instance, objectToSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getObject(final Class<?> clazz, final Object instance, final String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getObject(final Class<?> clazz, final String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

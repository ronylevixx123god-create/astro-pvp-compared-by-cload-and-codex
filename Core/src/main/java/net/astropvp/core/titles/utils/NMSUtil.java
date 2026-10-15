package net.astropvp.core.titles.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class NMSUtil {
    public static String getVersion(){
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1) + ".";
    }

    public static int getVersionNumber(){
        String name = getVersion().substring(3);
        return Integer.parseInt(name.substring(0, name.length() - 4));
    }

    public static int getVersionReleaseNumber(){
        String NMSVersion = getVersion();
        return Integer.parseInt(NMSVersion.substring(NMSVersion.length() - 2).replace(".", ""));
    }

    public static Class<?> getNMSClass(String className){
        try {
            String fullName = "net.minecraft.server." + getVersion() + className;
            Class<?> clazz = Class.forName(fullName);
            return clazz;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCraftClass(String className){
        try {
            String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
            Class<?> clazz = Class.forName(fullName);
            return clazz;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Field getField(Class<?> clazz, String name, boolean declared){
        try {
            Field field;
            if(declared) {
                field = clazz.getDeclaredField(name);
            } else{
                field = clazz.getField(name);
            }
            field.setAccessible(true);
            return field;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.SimpleCommandMap
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.utils;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CommandUtils {
    private static Object getPrivateField(Object object, String field) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

    public static void unRegisterBukkitCommand(Command cmd) {
        try {
            Object result = CommandUtils.getPrivateField(Arkkit.get().getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = CommandUtils.getPrivateField(commandMap, "knownCommands");
            HashMap knownCommands = (HashMap) map;
            knownCommands.remove(cmd.getName());
            for (String alias : cmd.getAliases()) {
                if (!knownCommands.containsKey(alias) || !knownCommands.get(alias).toString().contains(Arkkit.get().getName()))
                    continue;
                knownCommands.remove(alias);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unRegisterBukkitCommand(Plugin p, Command cmd) {
        try {
            Object result = CommandUtils.getPrivateField(p.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = CommandUtils.getPrivateField(commandMap, "knownCommands");
            HashMap knownCommands = (HashMap) map;
            knownCommands.remove(cmd.getName());
            for (String alias : cmd.getAliases()) {
                if (!knownCommands.containsKey(alias) || !knownCommands.get(alias).toString().contains(p.getName()))
                    continue;
                knownCommands.remove(alias);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


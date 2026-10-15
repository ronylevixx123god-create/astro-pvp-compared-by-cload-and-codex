package com.cosmicpvp.cosmicutils.utils;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.consumer.Queue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.Method;

public class CoreProtectUtils {
    private static boolean coreProtectEnabled = false;
    private static Object coreProtectAPI;
    private static Method queuePlayerCommandMethod;

    static {
        coreProtectEnabled = new File("plugins/CoreProtect.jar").exists();
    }

    public static Object getAPI() {
        if (!coreProtectEnabled) {
            return null;
        }
        if (coreProtectAPI == null) {
            CoreProtect protect = (CoreProtect) Bukkit.getPluginManager().getPlugin("CoreProtect");
            coreProtectAPI = protect.getAPI();
        }
        return coreProtectAPI;
    }

    public static boolean logPlacement(String user, Location location, int id, byte data) {
        if (!coreProtectEnabled) {
            return false;
        }
        CoreProtectAPI api = (CoreProtectAPI) CoreProtectUtils.getAPI();
        if (api == null) {
            return false;
        }
        api.logPlacement(user, location, id, data);
        return true;
    }

    public static boolean logCommand(Player player, String msg, long time) throws NoSuchMethodException {
        if (!coreProtectEnabled) {
            return false;
        }
        if (queuePlayerCommandMethod == null) {
            queuePlayerCommandMethod = ReflectionUtils.getMethod(Queue.class, "queuePlayerCommand", Player.class, String.class, Integer.TYPE);
        }
        if (queuePlayerCommandMethod == null) {
            return false;
        }
        try {
            queuePlayerCommandMethod.invoke(null, player, msg, (int) time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean logBreak(String user, Location location, int id, byte data) {
        if (!coreProtectEnabled) {
            return false;
        }
        try {
            CoreProtectAPI api = (CoreProtectAPI) CoreProtectUtils.getAPI();
            if (api == null) {
                return false;
            }
            api.logRemoval(user, location, id, data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean logInteraction(String user, Location location) {
        if (!coreProtectEnabled) {
            return false;
        }
        CoreProtectAPI api = (CoreProtectAPI) CoreProtectUtils.getAPI();
        if (api == null) {
            return false;
        }
        api.logInteraction(user, location);
        return true;
    }
}


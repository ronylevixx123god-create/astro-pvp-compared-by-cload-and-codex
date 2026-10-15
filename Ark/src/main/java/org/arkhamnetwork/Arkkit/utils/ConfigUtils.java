/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package org.arkhamnetwork.Arkkit.utils;

import org.bukkit.Bukkit;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigUtils {
    public static LinkedHashMap<String, String> getConfigurationMap(File f) throws IOException {
        if (!f.exists()) {
            return null;
        }
        LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (!line.contains("=")) continue;
            try {
                data.put(line.split("=")[0], line.split("=")[1]);
            } catch (Exception err) {
                Bukkit.getLogger().warning("Failed to parse configuration data for line: " + line);
                err.printStackTrace();
            }
        }
        reader.close();
        return data;
    }

    public static boolean saveConfigurationMap(HashMap<String, String> data_map, File f) {
        String all_data = "";
        for (Map.Entry<String, String> data : data_map.entrySet()) {
            all_data = all_data + data.getKey() + "=" + data.getValue() + "\r\n";
        }
        if (all_data.length() > 1) {
            try {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(f, false));
                dos.writeBytes(all_data + "\n");
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}


package com.rit.sucy.service;

import java.util.ArrayList;
import java.util.regex.Pattern;
import org.bukkit.Material;

public class MaterialsParser {
    public static Material[] toMaterial(String[] stringList) {
        ArrayList<Material> materials = new ArrayList<Material>();
        for (String blockString : stringList) {
            Pattern whitespace = Pattern.compile("\\s");
            if (whitespace.matcher(blockString).find()) {
                blockString = whitespace.matcher(blockString).replaceAll("");
            }
            Pattern onlyNumbers = Pattern.compile("[^0-9]");
            Material material = Material.matchMaterial((String)blockString);
            if (material == null) {
                String tempId = onlyNumbers.matcher(blockString).replaceAll("");
                if (!tempId.isEmpty()) {
                    material = Material.getMaterial((String)tempId);
                }
                if (material == null) {
                    Pattern onlyLetters = Pattern.compile("[^a-zA-Z_]");
                    material = Material.matchMaterial((String)onlyLetters.matcher(blockString).replaceAll(""));
                }
            }
            if (material == null) continue;
            materials.add(material);
        }
        return materials.<Material>toArray(new Material[materials.size()]);
    }

    public static String[] toStringArray(Material[] materials) {
        ArrayList<String> items = new ArrayList<String>();
        for (Material mat : materials) {
            items.add(mat.name());
        }
        return items.toArray(new String[items.size()]);
    }
}


package me.astropvp.factionlogs.utils;

import org.bukkit.entity.EntityType;

public class EntityUtils {
    public static String getEntityName(EntityType type) {
        if (type == null) return "null";
        if (type == EntityType.IRON_GOLEM) {
            return "Iron Golem";
        }
        if (type == EntityType.MUSHROOM_COW) {
            return "Mushroom Cow";
        }
        return type.getName();
    }
}

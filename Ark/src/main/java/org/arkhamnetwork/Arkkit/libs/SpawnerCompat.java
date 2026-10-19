package org.arkhamnetwork.Arkkit.libs;

import org.bukkit.block.CreatureSpawner;

import java.lang.reflect.Method;

/**
 * Reads a spawner's stack size on any of the server forks Astro has run on.
 * WineSpigot exposes getSpawnerAmount(), BananaSpigot exposes getSpawnerCount(),
 * and plain Spigot/TacoSpigot have no stacking (stack size is always 1).
 */
public final class SpawnerCompat {
    private static final Method STACK_SIZE_GETTER = findGetter();

    private SpawnerCompat() {
    }

    private static Method findGetter() {
        for (String name : new String[]{"getSpawnerAmount", "getSpawnerCount"}) {
            try {
                return CreatureSpawner.class.getMethod(name);
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }

    public static int getStackSize(CreatureSpawner spawner) {
        if (STACK_SIZE_GETTER == null) {
            return 1;
        }
        try {
            return ((Number) STACK_SIZE_GETTER.invoke(spawner)).intValue();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to read spawner stack size via " + STACK_SIZE_GETTER.getName(), e);
        }
    }
}
